package ir.ac.sbu.net.conf;

import ir.ac.sbu.net.exception.ConfigurationException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Conf {
    private int registerPort;
    private int searchPort;
    private String bindIp;
    private int backlog;
    private int expire;

    public static Conf load() {
        try {
            InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("master.properties");
            Properties properties = new Properties();
            properties.load(resourceAsStream);
            Conf conf = new Conf();
            conf.registerPort = Integer.parseInt(properties.getProperty("port.register"));
            conf.searchPort = Integer.parseInt(properties.getProperty("port.search"));
            conf.bindIp = properties.getProperty("bind");
            conf.backlog = Integer.parseInt(properties.getProperty("backlog"));
            conf.expire = Integer.parseInt(properties.getProperty("expire"));
            return conf;
        } catch (IOException e) {
            throw new ConfigurationException(e);
        }
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
