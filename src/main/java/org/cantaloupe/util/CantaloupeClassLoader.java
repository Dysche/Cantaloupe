package org.cantaloupe.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class CantaloupeClassLoader {
    private static final Class<?>[] parameters = new Class[] {
            URL.class
    };

    public static void addFile(String path) throws IOException {
        addFile(new File(path));
    }

    public static void addFile(File file) throws IOException {
        addURL(file.toURI().toURL());
    }

    public static void addURL(URL url) throws IOException {
        URLClassLoader sysloader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        Class<?> sysclass = URLClassLoader.class;

        try {
            Method method = sysclass.getDeclaredMethod("addURL", parameters);
            method.setAccessible(true);
            method.invoke(sysloader, new Object[] {
                    url
            });
        } catch (Throwable t) {
            t.printStackTrace();
            throw new IOException("Could not add URL to system ClassLoader.");
        }
    }
}
