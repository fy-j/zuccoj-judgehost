package top.kealine.judgehost.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Properties;

public class ConfigUtil {
    private final static String FILE = "judgehost.properties";
    private final static Properties PROPERTIES = new Properties();

    static{
        try {
            PROPERTIES.load(new InputStreamReader(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream(FILE)), StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String get(String key){
        return PROPERTIES.getProperty(key).trim();
    }

    public static String get(String key,String defaultValue){
        return PROPERTIES.getProperty(key, defaultValue);
    }
}
