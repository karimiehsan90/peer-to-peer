package ir.ac.sbu.net.conf;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import ir.ac.sbu.net.common.exception.ConfigurationException;

public class Conf {
    private int registerPort;
    private int searchPort;
    private String bindIp;
    private int backlog;
    private int expire;
    private String redisHost;
    private int redisPort;

    public static Conf load() {
        try {
            InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("master.properties");
            Properties properties = new Properties();
            properties.load(resourceAsStream);
            Conf conf = new Conf();
            conf.registerPort = Integer.parseInt(getProperty(properties, "port.register", "REGISTER_PORT", "2000"));
            conf.searchPort = Integer.parseInt(getProperty(properties, "port.search", "SEARCH_PORT", "2001"));
            conf.bindIp = getProperty(properties, "bind", "BIND_IP", "127.0.0.1");
            conf.redisHost = getProperty(properties, "redis.host", "REDIS_HOST", "127.0.0.1");
            conf.redisPort = Integer.parseInt(getProperty(properties, "redis.port", "REDIS_PORT", "6379"));
            conf.backlog = Integer.parseInt(getProperty(properties, "backlog", "SOCKET_BACKLOG", "5"));
            conf.expire = Integer.parseInt(getProperty(properties, "expire", "EXPIRE_TIME", "90"));
            return conf;
        } catch (IOException e) {
            throw new ConfigurationException(e);
        }
    }

    private static String getProperty(Properties properties, String propertiesKey, String envKey, String defaultValue) {
        return getByPrefer(System.getenv(envKey), properties.getProperty(propertiesKey), defaultValue);
    }

    private static String getByPrefer(String ... args) {
        for (String value : args) {
            if (value != null && !value.isEmpty()) {
                return value;
            }
        }
        return null;
    }

    public String getRedisHost() {
        return redisHost;
    }

    public int getRedisPort() {
        return redisPort;
    }

    public int getExpire() {
        return expire;
    }

    public int getBacklog() {
        return backlog;
    }

    public int getRegisterPort() {
        return registerPort;
    }

    public int getSearchPort() {
        return searchPort;
    }

    public String getBindIp() {
        return bindIp;
    }
}
