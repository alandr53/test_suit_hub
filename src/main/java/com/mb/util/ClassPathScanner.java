package com.mb.util;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ClassPathScanner {
    public Set<Class<?>> findClassesImplementing(String packageName, Class<?> interfaceOrSuperclass) throws IOException, URISyntaxException {
        return findClasses(packageName).stream()
                .filter(clazz -> {
                    try {
                        return interfaceOrSuperclass.isAssignableFrom(clazz) && !clazz.equals(interfaceOrSuperclass);
                    } catch (Exception e) {
                        return false;
                    }
                })
                .collect(Collectors.toSet());
    }

    private Set<Class<?>> findClasses(String packageName) throws IOException, URISyntaxException {
        String path = packageName.replace('.', '/');
        Set<Class<?>> classes = new HashSet<>();
        Path myPath;
        URI uri = ClassLoader.getSystemResource(path).toURI();

        try (FileSystem fileSystem = (uri.getScheme().equals("jar")) ? FileSystems.newFileSystem(uri, Collections.emptyMap()) : null) {
            myPath = Paths.get(uri);
            Stream<Path> walk = Files.walk(myPath, 1);
            for (Iterator<Path> it = walk.iterator(); it.hasNext();){
                Path p = it.next();
                if (p.toString().endsWith(".class")) {
                    String className = p.getFileName().toString();
                    className = className.substring(0, className.length() - 6); // remove ".class"
                    className = packageName + '.' + className;
                    try {
                        classes.add(Class.forName(className));
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            walk.close();
        }
        return classes;
    }
}
