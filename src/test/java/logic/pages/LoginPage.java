package logic.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends BasePage {


    private static LoginPage instance = new LoginPage();

    public static LoginPage getInstance() {
        return new LoginPage();
    }

    WebElement getUserTxtBox() { return findInputByLabel("Email"); }

    WebElement getPasswordTxtBox() {
        return findInputByLabel("Mật khẩu");
    }

    public void login(String userName, String passWord) {
        enterValueByLabel(getUserTxtBox(), userName);
        enterValueByLabel(getPasswordTxtBox(), passWord);
        click(findButtonBy("Đăng nhập"));
    }

}
