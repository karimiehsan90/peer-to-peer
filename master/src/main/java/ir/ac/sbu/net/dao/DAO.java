package ir.ac.sbu.net.dao;

import com.github.benmanes.caffeine.cache.Cache;
import ir.ac.sbu.net.entity.Slave;

import java.util.*;

public class DAO {
    private Map<String, Set<Slave>> filesMap;
    private Set<Slave> slaves;
    private Cache<Slave, Long> cache;

    public DAO() {
        this.filesMap = new HashMap<>();
        this.slaves = new HashSet<>();
    }

    public void registerSlave(Slave slave) {
        cache.put(slave, System.currentTimeMillis());
        slaves.add(slave);
        for (String file : slave.getFiles()) {
            filesMap.putIfAbsent(file, new HashSet<>());
            filesMap.get(file).add(slave);
        }
    }

    public Set<Slave> search(String query) {
        return filesMap.get(query);
    }

    public void removeSlave(Slave slave) {
        for (String file : slave.getFiles()) {
            filesMap.putIfAbsent(file, new HashSet<>());
            filesMap.get(file).remove(slave);
        }
        slaves.remove(slave);
    }

    public void setCache(Cache<Slave, Long> cache) {
        this.cache = cache;
    }
}
