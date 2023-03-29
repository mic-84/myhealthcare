package it.unipi.lsmsd.myhealthcare.service;

import it.unipi.lsmsd.myhealthcare.MyHealthCareApplication;
import it.unipi.lsmsd.myhealthcare.model.Service;
import it.unipi.lsmsd.myhealthcare.model.Structure;

import java.util.ArrayList;
import java.util.List;

public class StructureUtility {
    public static List<Service> getAvailableServices(Structure structure){
        List<Service> availableServices = new ArrayList<Service>();
        for(Service service : MyHealthCareApplication.services) {
            boolean found = false;
            for (Service structureService : structure.getServices())
                if (structureService.getId().equals(service.getId())) {
                    found = true;
                    break;
                }
            if (!found)
                availableServices.add(service);
        }
        return availableServices;
    }
}
