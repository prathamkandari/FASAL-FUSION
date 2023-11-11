package com.example;

import java.io.File;

public class ResourcePathExample {
    public static void main(String[] args) {
        ResourcePathExample resourcePathExample = new ResourcePathExample();
        String resourcePath = resourcePathExample.getResourcePath("trained_model.scr");
        System.out.println("Path of trained_model.scr: " + resourcePath);
    }

    public String getResourcePath(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());

        if (file.exists()) {
            return file.getAbsolutePath();
        } else {
            return "File not found";
        }
    }
}
