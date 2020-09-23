package suite.regression.UI;

import framework.utils.RandomCharacter;
import logic.business.entities.AdminUser;
import logic.business.entities.CompanyEnity;
import logic.business.entities.CompanyListEnity;
import logic.pages.Admin.CompanyPage;
import logic.pages.Admin.SideBar;
import org.testng.annotations.Test;

import java.util.HashMap;

public class CreateCompanyAdmin extends BaseTest {

    @Test(enabled = true, description = "Test create company")
    public void createCompanyAdmin() {
            String companyName= "Automation Test "+ RandomCharacter.getRandomNumericString(9);
            String adminCompany = "AutomationTest"+RandomCharacter.getRandomNumericString(3)+"@yopmail.com";

            CompanyEnity.setAdminCompany(adminCompany);
            CompanyEnity.setCompanyName(companyName);

            test.get().info("Step 1: login with admin role");
            AdminUser.getInstance().login();

            test.get().info("Step 2: click company tab");
            SideBar.getInstance().clickCompanyTab();

            test.get().info("Step 3: click Create new button");
            CompanyPage.getInstance().clickCreateNewBtn();

            test.get().info("Step 4: create company name ");
            CompanyPage.CreateCompanyPage.getInstance().createCompany(companyName,adminCompany);

            test.get().info("Step 5: Return company list ");
            CompanyPage.CreateCompanyPage.getInstance().clickBackToListBtn();

            test.get().info("Step 6: Get all company list ");
            HashMap<String,String> enity= CompanyListEnity.companyListEnity(companyName,adminCompany);

    }

}
