package com.esoterik.client.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileUtil {

    public static boolean appendTextFile(String data, String file) {
        try {
            Path path = Paths.get(file, new String[0]);
            Files.write(path, Collections.singletonList(data), StandardCharsets.UTF_8, Files.exists(path, new LinkOption[0]) ? StandardOpenOption.APPEND : StandardOpenOption.CREATE);
        }
        catch (IOException e) {
            System.out.println("WARNING: Unable to write file: " + file);
            return false;
        }
        return true;
    }

    public static List<String> readTextFileAllLines(String file) {
        try {
            Path path = Paths.get(file, new String[0]);
            return Files.readAllLines(path, StandardCharsets.UTF_8);
        }
        catch (IOException e) {
            System.out.println("WARNING: Unable to read file, creating new file: " + file);
            FileUtil.appendTextFile("", file);
            return Collections.emptyList();
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static List<File> getFiles(String dir) {
        try (Stream<Path> paths = Files.walk(Paths.get(dir, new String[0]), new FileVisitOption[0]);){
            List<File> list = paths.filter(x$0 -> Files.isRegularFile(x$0, new LinkOption[0])).map(Path::toFile).collect(Collectors.toList());
            return list;
        }
        catch (Exception exception) {
            return new ArrayList<File>();
        }
    }

    public static Optional<File> getFile(String name) {
        return Optional.of(new File(name));
    }

    public static String randomString() {
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvxyz";
        StringBuilder sb = new StringBuilder(20);
        for (int i = 0; i < 20; ++i) {
            int index = (int)((double)AlphaNumericString.length() * Math.random());
            sb.append(AlphaNumericString.charAt(index));
        }
        return sb.toString();
    }
}

