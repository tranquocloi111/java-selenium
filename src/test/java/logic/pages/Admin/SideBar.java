package logic.pages.Admin;

import framework.wdm.WdManager;
import logic.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.concurrent.ExecutionException;

public class SideBar extends BasePage {


    private static SideBar instance;

    private SideBar() {
    }

    public static SideBar getInstance() {

        return new SideBar();
    }


    @FindBy(xpath = "//div[@id='sidebar']")
    WebElement sideElement;

    private WebElement companyTab() {
        return getAElementByText("Company");
    }
    private WebElement contractTab() {
        return getAElementByText("Contract");
    }
    private WebElement courseContractTab() {
        return getAElementByText("Course's Contract");
    }

    public void clickCompanyTab() {
            click(companyTab());
    }
    public void clickContractTab() {
        click(contractTab());
    }
    public void clickCourseContract() {
        click(courseContractTab());
    }

    private WebElement trainerTab() {
        return sideElement.findElement(By.xpath("//a[contains(@href, 'trainers')]"));
    }
    public void clickTrainerTab() {
        click(trainerTab());
    }

}
