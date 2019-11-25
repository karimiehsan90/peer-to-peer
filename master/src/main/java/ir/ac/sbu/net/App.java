package ir.ac.sbu.net;

import ir.ac.sbu.net.conf.Conf;
import ir.ac.sbu.net.dao.DAO;
import ir.ac.sbu.net.thread.ServerAcceptThread;
import ir.ac.sbu.net.thread.ServerSearchThread;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.HashSet;
import java.util.Set;

public class App {
    public static void main(String[] args) throws IOException {
        Conf conf = Conf.load();
        Set<HostAndPort> hostAndPorts = new HashSet<>();
        hostAndPorts.add(new HostAndPort(conf.getRedisHost(), conf.getRedisPort()));
        JedisCluster jedisCluster = new JedisCluster(hostAndPorts);
        DAO dao = new DAO(conf, jedisCluster);
        InetAddress inetAddress = InetAddress.getByName(conf.getBindIp());
        ServerSocket registerSocket = new ServerSocket(conf.getRegisterPort(), conf.getBacklog(), inetAddress);
        ServerSocket searchSocket = new ServerSocket(conf.getSearchPort(), conf.getBacklog(), inetAddress);
        ServerAcceptThread acceptThread = new ServerAcceptThread(registerSocket, dao);
        Thread thread = new Thread(acceptThread, "accept");
        thread.start();
        ServerSearchThread searchThread = new ServerSearchThread(searchSocket, dao);
        thread = new Thread(searchThread, "search");
        thread.start();
    }
}
