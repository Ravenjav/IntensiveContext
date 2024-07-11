package com.aston.context.service;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

public class IntensiveComponentSearchService implements SearchService{

    @Override
    public Set<Class<?>> findClassesWithAnnotation(String basePackage, Class<? extends Annotation> annotation) {
        Set<Class<?>> classes = new HashSet<>();
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            String path = basePackage.replace('.', '/');
            Enumeration<URL> resources = classLoader.getResources(path);

            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                if (resource.getProtocol().equals("file")) {
                    File directory = new File(resource.getFile());
                    classes.addAll(findClassesInDirectory(directory, basePackage, annotation));
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return classes;
    }

    private Set<Class<?>> findClassesInDirectory(File directory, String packageName, Class<? extends Annotation> annotation)
            throws ClassNotFoundException {
        Set<Class<?>> classes = new HashSet<>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        if (files == null) {
            return classes;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                classes.addAll(findClassesInDirectory(file, packageName + "." + file.getName(), annotation));
            } else if (file.getName().endsWith(".class")) {
                String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                Class<?> clazz = Class.forName(className);
                if (clazz.isAnnotationPresent(annotation)) {
                    classes.add(clazz);
                }
            }
        }
        return classes;
    }
}
