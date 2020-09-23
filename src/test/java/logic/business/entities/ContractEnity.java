package logic.business.entities;

import logic.utils.TimeStamp;

import java.util.HashMap;

public class ContractEnity {

    public static HashMap<String, String> contractEnity(String companyName, String type, String expiredDate, String status) {
        HashMap<String, String> enity = new HashMap<>();
        enity.put("Company", companyName);
        enity.put("Type", type);
//        enity.put("Expired Date", TimeStamp.getCurrentDate());
        enity.put("Updated Date", TimeStamp.getCurrentDate());
        enity.put("Status", status);
        return enity;
    }
}
