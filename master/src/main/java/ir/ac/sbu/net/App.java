package ir.ac.sbu.net;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalListener;
import ir.ac.sbu.net.conf.Conf;
import ir.ac.sbu.net.dao.DAO;
import ir.ac.sbu.net.common.entity.Slave;
import ir.ac.sbu.net.thread.ServerAcceptThread;
import ir.ac.sbu.net.thread.ServerSearchThread;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.concurrent.TimeUnit;

public class App {
    public static void main(String[] args) throws IOException {
        Conf conf = Conf.load();
        DAO dao = new DAO();
        Cache<Slave, Long> cache = Caffeine.newBuilder()
                .expireAfterWrite(conf.getExpire(), TimeUnit.SECONDS)
                .removalListener((RemovalListener<Slave, Long>) (slave, aLong, removalCause) -> dao.removeSlave(slave))
                .build();
        dao.setCache(cache);
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
