package logic.business.entities;

import framework.config.Config;

public class AdminUser extends BaseUser {

   static String adminEmail= Config.getProp("adminUsername");
   static String password =Config.getProp("adminPassword");;
    public static AdminUser getInstance() {
        AdminUser adminUser = new AdminUser();
        adminUser.setUserName(adminEmail);
        adminUser.setPassWord(password);
        return adminUser;
    }

}
