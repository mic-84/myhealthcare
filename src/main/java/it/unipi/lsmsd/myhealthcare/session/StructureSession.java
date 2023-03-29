package it.unipi.lsmsd.myhealthcare.session;

import com.google.gson.Gson;
import it.unipi.lsmsd.myhealthcare.dao.StructureDao;
import it.unipi.lsmsd.myhealthcare.databaseConnection.RedisConnectionManager;
import it.unipi.lsmsd.myhealthcare.model.Structure;
import it.unipi.lsmsd.myhealthcare.repository.StructureRepository;
import it.unipi.lsmsd.myhealthcare.service.PropertiesManager;

public class StructureSession {
    private static final Integer expirationTime = PropertiesManager.redisStructureExpirationTime;
    public StructureSession(){}

    public static void refreshStructure(Structure structure){
        RedisConnectionManager redis = new RedisConnectionManager(structure.getId(), "structure", expirationTime);
        redis.setItemByKey(structure.getId() + ":structureObject", structure.toJSONString());
        redis.closeConnection();
    }

    public static Structure getStructure(String structureId, StructureRepository structureRepository){
        RedisConnectionManager redis = new RedisConnectionManager(structureId, "structure", expirationTime);
        Structure structure = null;
        try {
            structure = new Gson().fromJson(
                    redis.getItemByKey(structureId + ":structureObject"), Structure.class);
            redis.closeConnection();
        } catch (Exception e){ }
        if (structure == null) {
            structure = StructureDao.fromDTO(
                    StructureDao.readById(structureId, structureRepository));
            refreshStructure(structure);
        }

        return structure;
    }
}