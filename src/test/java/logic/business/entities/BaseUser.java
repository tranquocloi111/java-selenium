package logic.business.entities;

import framework.wdm.DriverFactory;
import logic.pages.LoginPage;
import org.openqa.selenium.By;

public class BaseUser {

    protected String userName;

    protected String passWord;


    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void login() {
        LoginPage.getInstance().login(userName, passWord);
     
    }


}
