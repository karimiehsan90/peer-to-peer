package ir.ac.sbu.net.dao;

import ir.ac.sbu.net.common.entity.Slave;
import ir.ac.sbu.net.conf.Conf;
import redis.clients.jedis.JedisCluster;

import java.util.Set;
import java.util.stream.Collectors;

public class DAO {
    private Conf conf;
    private JedisCluster jedisCluster;

    public DAO(Conf conf, JedisCluster jedisCluster) {
        this.conf = conf;
        this.jedisCluster = jedisCluster;
    }

    public void registerSlave(Slave slave) {
        jedisCluster.set("slave:" + slave.getIp() + ":" + slave.getPort(), "");
        jedisCluster.expire("slave:" + slave.getIp() + ":" + slave.getPort(), conf.getExpire());
        for (String file : slave.getFiles()) {
            jedisCluster.sadd("file:" + file, slave.getIp() + ":" + slave.getPort());
        }
    }

    public Set<Slave> search(String query) {
        return jedisCluster.smembers("file:" + query).stream().filter(s -> {
            boolean con = jedisCluster.exists("slave:" + s);
            if (!con) {
                jedisCluster.del("slave:" + s);
            }
            return con;
        }).map(s -> {
            String[] split = s.split(":");
            return new Slave(split[0], Integer.parseInt(split[1]), null);
        }).collect(Collectors.toSet());
    }
}
