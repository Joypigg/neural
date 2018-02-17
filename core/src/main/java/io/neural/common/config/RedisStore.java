package io.neural.common.config;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.fastjson.JSON;
import io.neural.URL;
import io.neural.extension.Extension;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;

/**
 * The Store by Redis
 *
 * @author lry
 **/
@Extension("redis")
public class RedisStore implements IStore {

    private JedisPool jedisPool = null;
    private final Map<SubscribeListener, Jedis> jedisMap = new ConcurrentHashMap<>();
    private final Map<SubscribeListener, JedisPubSub> subscribed = new ConcurrentHashMap<>();

    @Override
    public void start(URL url) {
        String parametersJSON = JSON.toJSONString(url.getParameters());
        JedisPoolConfig jedisPoolConfig = JSON.parseObject(parametersJSON, JedisPoolConfig.class);
        this.jedisPool = new JedisPool(jedisPoolConfig, url.getHost(), url.getPort());
    }

    @Override
    public void batchUpOrAdd(long expire, Map<String, Long> data) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            for (Map.Entry<String, Long> entry : data.entrySet()) {
                jedis.incrBy(entry.getKey(), entry.getValue());
                jedis.pexpire(entry.getKey(), expire);
            }
        } finally {
            jedis.close();
        }
    }

    @Override
    public void add(String key, Map<String, String> data) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            for (Map.Entry<String, String> entry : data.entrySet()) {
                jedis.hset(key, entry.getKey(), entry.getValue());
            }
        } finally {
            jedis.close();
        }
    }

    @Override
    public Set<String> searchKeys(String keyword) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.keys(keyword);
        } finally {
            jedis.close();
        }
    }

    @Override
    public Map<String, String> query(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.hgetAll(key);
        } finally {
            jedis.close();
        }
    }

    @Override
    public synchronized void publish(String channel, String message) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.publish(channel, message);
        } finally {
            jedis.close();
        }
    }

    @Override
    public synchronized void subscribe(String[] subscribeKeys, SubscribeListener listener) {
        Jedis jedis = jedisPool.getResource();
        JedisPubSub jedisPubSub = new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
                listener.notify(channel, message);
            }
        };

        jedisMap.put(listener, jedis);
        subscribed.put(listener, jedisPubSub);
        jedis.subscribe(jedisPubSub, subscribeKeys);
    }

    @Override
    public synchronized void unSubscribe(SubscribeListener listener) {
        JedisPubSub jedisPubSub = subscribed.get(listener);
        if (jedisPubSub != null) {
            jedisPubSub.unsubscribe();
            subscribed.remove(listener);
        }

        Jedis jedis = jedisMap.get(listener);
        if (jedis != null) {
            jedis.close();
            jedisMap.remove(listener);
        }
    }

    @Override
    public void destroy() {
        if (null != jedisPool) {
            jedisPool.close();
        }
    }

}
