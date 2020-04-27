package model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class ConfigurationReader {

    private static final Logger logger = LogManager.getLogger(ConfigurationReader.class);
    String fileName;
    Properties props;

    ConfigurationReader() { }

    public static Configuration getConfiguration(String fileName) throws ConfigurationReaderException {

        ConfigurationReader configReader = new ConfigurationReader();
        configReader.fileName = fileName;
        configReader.props = new Properties();
        FileInputStream is = null;
        File configFile = configReader.getPropertyFile();
        try {
            is = new FileInputStream(configFile);
            configReader.props.load(new InputStreamReader(is, Charset.forName("UTF-8")));
            return configReader.parseProperies(configReader.props);
        } catch (FileNotFoundException e) {
            throw new ConfigurationReaderException(configFile);
        } catch (IOException e) {
            throw new ConfigurationReaderException(configFile);
        }
    }

    File getPropertyFile() {
        String rootPath = System.getProperty("user.dir");
        Path filePath = Paths.get(rootPath, this.fileName);
        return filePath.toFile();
    }

    Configuration parseProperies(Properties props) {
        Configuration configuration = new Configuration();
        parseClassProperties("", configuration, props);

        for(int i = 1; ; i++) {
            if(props.getProperty("master-" + i + "-name") != null) {
                MasterInfo masterInfo = new MasterInfo();
                parseClassProperties("master-" + i + "-", masterInfo, props);
                configuration.addMasterConfiguration(masterInfo);
            } else {
                break;
            }
        }

        return configuration;
    }

    private void parseClassProperties(String prefix, Object configInst, Properties props) {
        Field[] fields = configInst.getClass().getDeclaredFields();
        for(Field field: fields) {
            ConfigProperty configPropertyAnnotation = field.getAnnotation(ConfigProperty.class);
            if(configPropertyAnnotation != null) {
                try {
                    setProperty(prefix, configInst, props, field, configPropertyAnnotation);
                } catch (IllegalAccessException e) {
                    logger.error("Cannot read property (illegal access): " + field.getName());
                } catch (InvocationTargetException e) {
                    logger.error("Cannot read property (invocation target exception): " + field.getName());
                }
            }
        }
    }

    private void setProperty(String prefix, Object configInst, Properties props, Field field, ConfigProperty configPropertyAnnotation) throws IllegalAccessException, InvocationTargetException {

        String propertyInFile = null;
        if(configPropertyAnnotation.nameInFile().isEmpty()) {
            propertyInFile = prefix + field.getName();
        } else {
            propertyInFile = prefix + configPropertyAnnotation.nameInFile();
        }

        String propertyValue = props.getProperty(propertyInFile);
        if(propertyValue != null) {

            Method stringMethod = findStringSetterMethod(configInst, field.getName());
            if(stringMethod != null) {
                stringMethod.invoke(configInst, propertyValue);
            } else {
                logger.error("Cannot set property as string setter method does not exist: " + field.getName() );
            }
        }

    }

    private Method findStringSetterMethod(Object inst, String name) {
        String methodName = "set" + capitalizeFirst(name);
        if(methodName != null) {
            try {
                return inst.getClass().getMethod(methodName, String.class);
            } catch (NoSuchMethodException e) {
            }
        }
        return null;
    }

    private String capitalizeFirst(final String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }


}
