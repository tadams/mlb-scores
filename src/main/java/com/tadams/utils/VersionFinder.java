package com.tadams.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URL;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

@Component
public class VersionFinder {

    private static final Logger logger = LoggerFactory.getLogger(VersionFinder.class);
    protected static final String NO_VERSION = "NO_VERSION";

    @PostConstruct
    public void findVersionOnStartUp() {
        logger.info("\n" +
                "            _ _                                       \n" +
                            "  _ __ ___ | | |__        ___  ___ ___  _ __ ___  ___ \n" +
                            " | '_ ` _ \\| | '_ \\ _____/ __|/ __/ _ \\| '__/ _ \\/ __|\n" +
                            " | | | | | | | |_) |_____\\__ \\ (_| (_) | | |  __/\\__ \\\n" +
                            " |_| |_| |_|_|_.__/      |___/\\___\\___/|_|  \\___||___/\n" +
                            "                                                      \n");
        logger.info("Version: " + getVersion() + "\n\n\n");
    }

    public String getVersion() {
        try {
            return readManifestForVersion();
        } catch (IOException e) {
            throw new RuntimeException("Cannot find mlb-scores manifest", e);
        }
    }

    protected String readManifestForVersion() throws IOException {

        Class clazz = this.getClass();
        String className = clazz.getSimpleName() + ".class";
        String classPath = clazz.getResource(className).toString();
        if (!classPath.startsWith("jar")) {
            return NO_VERSION;
        }

        String manifestPath = classPath.substring(0, classPath.lastIndexOf("!") + 1) +
                "/META-INF/MANIFEST.MF";
        Manifest manifest = new Manifest(new URL(manifestPath).openStream());

        Attributes attr = manifest.getMainAttributes();
        return attr.getValue("Implementation-Version");
    }
}
