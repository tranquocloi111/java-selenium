package logic.pages.Admin;

import logic.pages.BasePage;
import logic.pages.TableControlBase;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

import java.util.HashMap;
import java.util.List;

public class CompanyPage extends BasePage {

    private static CompanyPage instance;

    private CompanyPage() {
    }

    public static CompanyPage getInstance() {
        return new CompanyPage();
    }

    public void clickCreateNewBtn() {
        super.clickCreateNewBtn();
    }

    /////////////////////////////////////////////////////
    /* Create company*/
    public static class CreateCompanyPage extends CompanyPage {


        private static CreateCompanyPage instance;

        private CreateCompanyPage() {
        }

        public static CreateCompanyPage getInstance() {
            return new CreateCompanyPage();
        }

        @FindBy(xpath = "//label[normalize-space(text())='Company name']//following-sibling::div/input")
        WebElement companyNameInput;

        @FindBy(xpath = "//label[normalize-space(text())='Admin company']//following-sibling::div/input")
        WebElement adminCompanyInput;

        public void createCompany(String companyName, String adminCompany) {
            enterValueByLabel(companyNameInput, companyName);
            enterValueByLabel(adminCompanyInput, adminCompany);
            clickSubmitBtn();
        }

    }
    /////////////////////////////////////////////////////
    /* Company list*/

    public static class CompanyList extends CompanyPage {

        private CompanyList() {
        }

        public static CompanyList getInstance() {
            return new CompanyList();
        }

        @FindBy(xpath = "//table[@id='company-table']")
        WebElement companyTable;
        TableControlBase tableControlBase = new TableControlBase(companyTable);


        public void checkTheRowIsExist(HashMap<String, String> row) {
            waitUntilSpecificTime(3);
            Assert.assertEquals(1, tableControlBase.findRowsByColumns(row).size());
        }
    }


}
