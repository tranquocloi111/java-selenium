package logic.pages.Admin;

import logic.pages.BasePage;
import logic.pages.TableControlBase;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class TrainerPage extends BasePage {

    private static TrainerPage instance;

    private TrainerPage() {
    }

    public static TrainerPage getInstance() {
        return new TrainerPage();
    }

    public void clickCreateNewBtn() {
        super.clickCreateNewBtn();
    }

    /////////////////////////////////////////////////////
    /* Create company*/
    public static class CreateTrainerPage extends TrainerPage {


        private static CreateTrainerPage instance;

        private CreateTrainerPage() {
        }

        public static CreateTrainerPage getInstance() {
            return new CreateTrainerPage();
        }

        @FindBy(xpath = "//label[normalize-space(text())='Full name']//following-sibling::div/input")
        WebElement fullnameInput;

        @FindBy(xpath = "//label[normalize-space(text())='Email']//following-sibling::div/input")
        WebElement emailInput;

        public void createTrainer(String fullnameInput, String trainerName) {
            enterValueByLabel(fullnameInput, fullnameInput);
            enterValueByLabel(emailInput, trainerName);
            clickSubmitBtn();
        }

    }
    /////////////////////////////////////////////////////
    /* Company list*/
    @FindBy(xpath = "//table[@id='company-table']")
    WebElement companyTable;
    TableControlBase tableControlBase = new TableControlBase(companyTable);

    public void getAllData() {
        tableControlBase.getAllRows();
    }



}
