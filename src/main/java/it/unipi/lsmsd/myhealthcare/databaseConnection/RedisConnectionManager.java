package it.unipi.lsmsd.myhealthcare.databaseConnection;

import it.unipi.lsmsd.myhealthcare.service.PropertiesManager;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.Map;

public class RedisConnectionManager {
    private JedisPool pool;
    private Jedis jedis;
    private final String NAMESPACE = PropertiesManager.redisNamespace;
    private String mainKey;

    public RedisConnectionManager(String userId, String type, Integer time){
        pool = new JedisPool(PropertiesManager.redisHost, PropertiesManager.redisPort);
        if(pool == null)
            pool = new JedisPool(PropertiesManager.redisReplicaHost, PropertiesManager.redisPort);
        jedis = pool.getResource();
        if(type != null) {
            mainKey = getMainKey(userId, type);
            if(time != null) {
                jedis.set(mainKey, String.valueOf(System.currentTimeMillis()));
                setExpirationTime(mainKey, time, type);
            }
        }
        System.out.println("connected to Redis");
    }

    private String getMainKey(String userId, String type) {
        return NAMESPACE + ":" + userId + ":" + type;
    }

    public void setExpirationTime(String key, Integer time, String type){
        if(!type.equals("user")) {
            jedis.expire(key, time);
            System.out.println("key " + key + " will expire in " + jedis.ttl(key) + " seconds...");
        }
    }

    public void addItem(String userId, String item, String type, String keyToExpire, Integer time){
        if(!isExpired()) {
            String key = NAMESPACE + ":" + userId + ":" + type;
            jedis.set(key, item);
            setExpirationTime(keyToExpire, time, type);
            for(Map.Entry<String, String> map: getValues(userId, type).entrySet())
                setExpirationTime(map.getKey(), time, type);
            System.out.println(key + "            - " + item + " added to redis");
        } else
            closeConnection();
    }

    public Map<String, String> getValues(String userId, String type){
        Map<String, String> values = new HashMap<String, String>();
        if(!isExpired()) {
            for (String key : jedis.keys(NAMESPACE + ":" + userId + ":" + type + "*"))
                values.put(key, jedis.get(key));
        } else
            closeConnection();
        return values;
    }

    public long remainingTime(){
        return jedis.ttl(mainKey);
    }

    public boolean isExpired(){
        return remainingTime() <= 0;
    }

    public void setItemByKey(String key, String item){
        jedis.set(NAMESPACE + ":" + key, item);
    }

    public String getItemByKey(String key){
        return jedis.get(NAMESPACE + ":" + key);
    }

    public boolean existsKey(String key){
        return getItemByKey(key) != null;
    }

    public void removeItemByKey(String key){
        jedis.del(key);
    }

    public void removeItemsByUserId(String userId){
        if(!isExpired()) {
            for (String key : jedis.keys("*"))
                if (jedis.get(key).contains(userId)) {
                    jedis.del(key);
                    System.out.println(userId + " removed from redis");
                    return;
                }
        } else
            closeConnection();
    }

    public void closeConnection(){
        if (jedis.isConnected())
            jedis.close();
        if (!pool.isClosed())
            pool.close();
        System.out.println("Redis connection closed");
    }
}
