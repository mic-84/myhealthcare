package it.unipi.lsmsd.myhealthcare.databaseConnection;

import it.unipi.lsmsd.myhealthcare.utility.PropertiesManager;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.Map;

public class RedisConnectionManager {
    private JedisPool pool;
    private Jedis jedis;
    private final String NAMESPACE = PropertiesManager.redisNamespace;
    private String userId, type, mainKey;

    public RedisConnectionManager(String userId, String type, boolean replica){
        this.userId = userId;
        if(!replica)
            pool = new JedisPool(PropertiesManager.redisHost, PropertiesManager.redisPort);
        else
            pool = new JedisPool(PropertiesManager.redisReplicaHost, PropertiesManager.redisPort);
        jedis = pool.getResource();
        mainKey = getMainKey(userId, type);
        if(!replica)
            jedis.set(mainKey, String.valueOf(System.currentTimeMillis()));
        this.type = type;
        System.out.println("connected to Redis");
        setExpirationCountdown(mainKey);
    }

    private String getMainKey(String userId, String type) {
        return NAMESPACE + ":" + userId + ":" + type;
    }

    public void setExpirationCountdown(String key){
        if(!type.equals("user")) {
            jedis.expire(key, PropertiesManager.redisBookingExpirationTime);
            System.out.println("key " + key + " will expire in " + jedis.ttl(key) + " seconds...");
        }
    }

    public void addItem(String item, String type){
        if(!isExpired()) {
            String key = NAMESPACE + ":" + userId + ":" + type + ":" + countItems(type);
            jedis.set(key, item);
            setExpirationCountdown(mainKey);
            for(Map.Entry<String, String> map: getValues(type).entrySet())
                setExpirationCountdown(map.getKey());
            System.out.println(key + "            - " + item + " added to redis");
        } else
            closeConnection();
    }

    public Map<String, String> getValues(String type){
        Map<String, String> values = new HashMap<String, String>();
        if(!isExpired()) {
            for (String key : jedis.keys(NAMESPACE + ":" + userId + ":" + type + "*"))
                values.put(key, jedis.get(key));
        } else
            closeConnection();
        return values;
    }

    public int countItems(String type){
        return getValues(type).size();
    }

    public long remainingTime(){
        return jedis.ttl(mainKey);
    }

    public boolean isExpired(){
        return remainingTime() <= 0;
    }

    public void addItemByKey(String key, String item){
        jedis.set(NAMESPACE + ":" + key, item);
    }

    public String getItemByKey(String key){
        return jedis.get(NAMESPACE + ":" + key);
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
