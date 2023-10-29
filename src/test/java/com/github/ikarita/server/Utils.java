package com.github.ikarita.server;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;

import static org.junit.jupiter.api.Assertions.fail;


public class Utils {

    public static File getResourceFile(String name) throws IOException, URISyntaxException {
        URL resource = Utils.class.getClassLoader().getResource(name);
        if (resource == null) {
            throw new IOException("Failed to locate resource template for project analytics");
        }

        return Paths.get(resource.toURI()).toFile();
    }

    public static String getFileContent(File f) throws IOException {
        return Files.readString(f.toPath());
    }

    public static String getFileContent(String path) throws IOException, URISyntaxException {
        return getFileContent(getResourceFile(path));
    }

    public static String getFileContentFail(String path) {
        try {
            return getFileContent(path);
        } catch (IOException | URISyntaxException e) {
            fail(String.format("Failed to load '%s': %s", path, e.getMessage()));
            return null;
        }
    }
}
