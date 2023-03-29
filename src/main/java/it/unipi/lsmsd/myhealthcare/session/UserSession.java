package it.unipi.lsmsd.myhealthcare.session;

import it.unipi.lsmsd.myhealthcare.databaseConnection.RedisConnectionManager;
import it.unipi.lsmsd.myhealthcare.model.User;
import com.google.gson.Gson;
import it.unipi.lsmsd.myhealthcare.service.PropertiesManager;

public class UserSession {
    private static final Integer expirationTime = PropertiesManager.redisUserExpirationTime;
    public UserSession(){}

    public static void login(User user){
        RedisConnectionManager redis = new RedisConnectionManager(user.getId(), "user", expirationTime);
        redis.setItemByKey(user.getId() + ":userObject", user.toJSONString());
        redis.closeConnection();
    }

    public static void logout(String userId){
        RedisConnectionManager redis = new RedisConnectionManager(userId, "user", 0);
        redis.removeItemsByUserId(userId);
        redis.closeConnection();
    }

    public static void refreshUser(User user){
        RedisConnectionManager redis = new RedisConnectionManager(user.getId(), "user", expirationTime);
        redis.setItemByKey(user.getId() + ":userObject", user.toJSONString());
        redis.closeConnection();
    }

    public static User getUser(String userId){
        RedisConnectionManager redis = new RedisConnectionManager(userId, "user", expirationTime);
        User user = new Gson().fromJson(
                redis.getItemByKey(userId + ":userObject"), User.class);
        redis.closeConnection();
        return user;
    }
}