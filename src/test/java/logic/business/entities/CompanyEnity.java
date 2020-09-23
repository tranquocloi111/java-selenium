package logic.business.entities;

import org.codehaus.groovy.runtime.StringGroovyMethods;

public class CompanyEnity {


    private static String adminCompany;
    private static String companyName;



    public static String getAdminCompany() {
        return adminCompany;
    }

    public static void setAdminCompany(String companyAdmin) {
        CompanyEnity.adminCompany = companyAdmin;
    }

    public static String getCompanyName() {
        return companyName;
    }

    public static void setCompanyName(String companyName) {
        CompanyEnity.companyName = companyName;
    }

}
