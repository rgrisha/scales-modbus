package model;

import java.io.File;

public class ConfigurationReaderException extends Exception {

    File configFile;

    public ConfigurationReaderException(File configFile) {
        this.configFile = configFile;
    }

    @Override
    public String toString() {
        return "Nepasiekiamas failas: " + configFile;
    }
}
