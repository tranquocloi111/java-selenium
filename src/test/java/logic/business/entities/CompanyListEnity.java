package logic.business.entities;

import logic.utils.Parser;
import logic.utils.TimeStamp;
import org.apache.commons.compress.archivers.zip.X5455_ExtendedTimestamp;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CompanyListEnity {

    public static HashMap<String, String> companyListEnity(String companyName, String adminCompany) {
        HashMap<String, String> enity = new HashMap<>();
        enity.put("Company Name", companyName);
        enity.put("Admin Company", adminCompany);
        enity.put("Updated Date",TimeStamp.getCurrentDate());
        return enity;
    }
}
