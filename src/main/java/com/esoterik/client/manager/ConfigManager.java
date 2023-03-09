package com.esoterik.client.manager;

import com.esoterik.client.esohack;
import com.esoterik.client.features.Feature;
import com.esoterik.client.features.modules.Module;
import com.esoterik.client.features.setting.Bind;
import com.esoterik.client.features.setting.EnumConverter;
import com.esoterik.client.features.setting.Setting;
import com.esoterik.client.util.Util;
import com.google.gson.*;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.FileAttribute;
import java.util.*;
import java.util.stream.Collectors;

public class ConfigManager implements Util {

    public ArrayList<Feature> features = new ArrayList();
    public String config = "esohack/config/";

    public void loadConfig(String name) {
        final List<File> files = Arrays.stream(Objects.requireNonNull(new File("esohack").listFiles())).filter(File::isDirectory).collect(Collectors.toList());
        this.config = files.contains(new File("esohack/" + name + "/")) ? "esohack/" + name + "/" : "esohack/config/";
        esohack.friendManager.onLoad();
        for (Feature feature : this.features) {
            try {
                this.loadSettings(feature);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.saveCurrentConfig();
    }

    public void saveConfig(String name) {
        this.config = "esohack/" + name + "/";
        File path = new File(this.config);
        if (!path.exists()) {
            path.mkdir();
        }
        esohack.friendManager.saveFriends();
        for (Feature feature : this.features) {
            try {
                this.saveSettings(feature);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.saveCurrentConfig();
    }

    public void saveCurrentConfig() {
        File currentConfig = new File("esohack/currentconfig.txt");
        try {
            if (currentConfig.exists()) {
                FileWriter writer = new FileWriter(currentConfig);
                String tempConfig = this.config.replaceAll("/", "");
                writer.write(tempConfig.replaceAll("esohack", ""));
                writer.close();
            } else {
                currentConfig.createNewFile();
                FileWriter writer = new FileWriter(currentConfig);
                String tempConfig = this.config.replaceAll("/", "");
                writer.write(tempConfig.replaceAll("esohack", ""));
                writer.close();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String loadCurrentConfig() {
        File currentConfig = new File("esohack/currentconfig.txt");
        String name = "config";
        try {
            if (currentConfig.exists()) {
                Scanner reader = new Scanner(currentConfig);
                while (reader.hasNextLine()) {
                    name = reader.nextLine();
                }
                reader.close();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return name;
    }

    public void resetConfig(boolean saveConfig, String name) {
        for (Feature feature : this.features) {
            feature.reset();
        }
        if (saveConfig) {
            this.saveConfig(name);
        }
    }

    public void saveSettings(Feature feature) throws IOException {
        String featureName;
        Path outputFile;
        JsonObject object = new JsonObject();
        File directory = new File(this.config + this.getDirectory(feature));
        if (!directory.exists()) {
            directory.mkdir();
        }
        if (!Files.exists(outputFile = Paths.get(featureName = this.config + this.getDirectory(feature) + feature.getName() + ".json", new String[0]), new LinkOption[0])) {
            Files.createFile(outputFile, new FileAttribute[0]);
        }
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson((JsonElement)this.writeSettings(feature));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(outputFile, new OpenOption[0])));
        writer.write(json);
        writer.close();
    }

    public static void setValueFromJson(Feature feature, Setting setting, JsonElement element) {
        switch (setting.getType()) {
            case "Boolean": {
                setting.setValue(element.getAsBoolean());
                break;
            }
            case "Double": {
                setting.setValue(element.getAsDouble());
                break;
            }
            case "Float": {
                setting.setValue(Float.valueOf(element.getAsFloat()));
                break;
            }
            case "Integer": {
                setting.setValue(element.getAsInt());
                break;
            }
            case "String": {
                String str = element.getAsString();
                setting.setValue(str.replace("_", " "));
                break;
            }
            case "Bind": {
                setting.setValue(new Bind.BindConverter().doBackward(element));
                break;
            }
            case "Enum": {
                try {
                    EnumConverter converter = new EnumConverter(((Enum)setting.getValue()).getClass());
                    Enum value = converter.doBackward(element);
                    setting.setValue(value == null ? setting.getDefaultValue() : value);
                }
                catch (Exception e) {}
                break;
            }
            default: {
                esohack.LOGGER.error("Unknown Setting type for: " + feature.getName() + " : " + setting.getName());
            }
        }
    }

    public void init() {
        this.features.addAll(ModuleManager.modules);
        this.features.add(esohack.friendManager);
        String name = this.loadCurrentConfig();
        this.loadConfig(name);
        esohack.LOGGER.info("Config loaded.");
    }

    private void loadSettings(Feature feature) throws IOException {
        String featureName = this.config + this.getDirectory(feature) + feature.getName() + ".json";
        Path featurePath = Paths.get(featureName, new String[0]);
        if (!Files.exists(featurePath, new LinkOption[0])) {
            return;
        }
        this.loadPath(featurePath, feature);
    }

    private void loadPath(Path path, Feature feature) throws IOException {
        InputStream stream = Files.newInputStream(path, new OpenOption[0]);
        try {
            ConfigManager.loadFile(new JsonParser().parse((Reader)new InputStreamReader(stream)).getAsJsonObject(), feature);
        }
        catch (IllegalStateException e) {
            esohack.LOGGER.error("Bad Config File for: " + feature.getName() + ". Resetting...");
            ConfigManager.loadFile(new JsonObject(), feature);
        }
        stream.close();
    }

    private static void loadFile(JsonObject input, Feature feature) {
        for (Map.Entry entry : input.entrySet()) {
            String settingName = (String)entry.getKey();
            JsonElement element = (JsonElement)entry.getValue();
            if (feature instanceof FriendManager) {
                try {
                    esohack.friendManager.addFriend(new FriendManager.Friend(element.getAsString(), UUID.fromString(settingName)));
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                continue;
            }
            boolean settingFound = false;
            for (Setting setting : feature.getSettings()) {
                if (!settingName.equals(setting.getName())) continue;
                try {
                    ConfigManager.setValueFromJson(feature, setting, element);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                settingFound = true;
            }
            if (!settingFound) continue;
        }
    }

    public JsonObject writeSettings(Feature feature) {
        JsonObject object = new JsonObject();
        JsonParser jp = new JsonParser();
        for (Setting setting : feature.getSettings()) {
            if (setting.isEnumSetting()) {
                EnumConverter converter = new EnumConverter(((Enum)setting.getValue()).getClass());
                object.add(setting.getName(), converter.doForward((Enum)setting.getValue()));
                continue;
            }
            if (setting.isStringSetting()) {
                String str = (String)setting.getValue();
                setting.setValue(str.replace(" ", "_"));
            }
            try {
                object.add(setting.getName(), jp.parse(setting.getValueAsString()));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return object;
    }

    public String getDirectory(Feature feature) {
        String directory = "";
        if (feature instanceof Module) {
            directory = directory + ((Module)feature).getCategory().getName() + "/";
        }
        return directory;
    }
}

