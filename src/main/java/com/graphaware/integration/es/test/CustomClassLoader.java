package com.graphaware.integration.es.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom class loader. The ultimate purpose of this class is to avoid clashes between Neo4j and Elasticsearch
 * Lucene versions.
 */
public class CustomClassLoader {
    private static final Logger LOG = LoggerFactory.getLogger(CustomClassLoader.class);

    private final ClassLoader classloader;

    public CustomClassLoader(String libPath, ClassLoader parent) throws MalformedURLException {
        List<URL> urls = new ArrayList<>();
        File directory = new File(libPath);
        if (!directory.exists()) {
            throw new RuntimeException("Path : " + libPath + " doesn't exist");
        }
        for (File f : directory.listFiles()) {
            final URL toURL = f.toURI().toURL();
            urls.add(toURL);
            LOG.info(toURL.getPath());
        }
        this.classloader = new URLClassLoader(urls.toArray(new URL[urls.size()]), parent);
    }

    public CustomClassLoader(String libPath) throws MalformedURLException {
        this(libPath, ClassLoader.getSystemClassLoader().getParent());
    }

    public Class<?> loadClass(String classname) throws ClassNotFoundException {
        return classloader.loadClass(classname);
    }
}
