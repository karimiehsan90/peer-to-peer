package ir.ac.sbu.net.conf;

import ir.ac.sbu.net.common.exception.ConfigurationException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class Conf {
    private int port;
    private int aliveTime;
    private String bind;
    private String ip;
    private int backlog;
    private String masterIp;
    private int masterRegisterPort;
    private int masterSearchPort;
    private String filesPath;
    private List<String> files;
    private int bufferSize;
    public static Conf load() {
        InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("slave.properties");
        Properties properties = new Properties();
        try {
            Conf conf = new Conf();
            properties.load(resourceAsStream);
            conf.bind = properties.getProperty("bind");
            conf.ip = properties.getProperty("ip");
            conf.port = Integer.parseInt(properties.getProperty("port"));
            conf.backlog = Integer.parseInt(properties.getProperty("backlog"));
            conf.aliveTime = Integer.parseInt(properties.getProperty("alive"));
            conf.masterIp = properties.getProperty("master.ip");
            conf.masterRegisterPort = Integer.parseInt(properties.getProperty("master.port.register"));
            conf.masterSearchPort = Integer.parseInt(properties.getProperty("master.port.search"));
            conf.bufferSize = Integer.parseInt(properties.getProperty("buffer.size"));
            conf.filesPath = properties.getProperty("files");
            conf.files = Files
                    .list(Paths.get(conf.filesPath))
                    .filter(Files::isRegularFile)
                    .map(path -> new File(path.toUri().getPath()).getName())
                    .collect(Collectors.toList());
            return conf;
        } catch (IOException e) {
            throw new ConfigurationException(e);
        }
    }

    public String getFilesPath() {
        return filesPath;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public List<String> getFiles() {
        return files;
    }

    public String getMasterIp() {
        return masterIp;
    }

    public int getMasterRegisterPort() {
        return masterRegisterPort;
    }

    public int getMasterSearchPort() {
        return masterSearchPort;
    }

    public int getPort() {
        return port;
    }

    public int getAliveTime() {
        return aliveTime;
    }

    public String getBind() {
        return bind;
    }

    public String getIp() {
        return ip;
    }

    public int getBacklog() {
        return backlog;
    }
}
