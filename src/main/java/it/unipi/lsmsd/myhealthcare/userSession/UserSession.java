package it.unipi.lsmsd.myhealthcare.userSession;

import it.unipi.lsmsd.myhealthcare.databaseConnection.RedisConnectionManager;
import it.unipi.lsmsd.myhealthcare.model.User;
import com.google.gson.Gson;
import it.unipi.lsmsd.myhealthcare.utility.PropertiesManager;

public class UserSession {
    private static String host = PropertiesManager.redisHost;
    private static String replicaHost = PropertiesManager.redisReplicaHost;

    public UserSession(){}

    public static void login(User user){
        RedisConnectionManager redis = new RedisConnectionManager(user.getId(), "user", false);
        redis.addItemByKey(user.getId() + ":userObject", user.toJSONString());
        redis.closeConnection();
    }

    public static void logout(String userId){
        RedisConnectionManager redis = new RedisConnectionManager(userId, "user", false);
        redis.removeItemsByUserId(userId);
        redis.closeConnection();
    }

    public static User getUser(String userId, String cryptedPassword){
        RedisConnectionManager redis = new RedisConnectionManager(userId, "user", true);
        User user = new Gson().fromJson(
                redis.getItemByKey(userId + ":userObject"), User.class);
        redis.closeConnection();
        if(user.getPassword().equals(cryptedPassword))
            return user;
        else
            return null;
    }
}