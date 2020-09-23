package suite.regression.UI;

import logic.business.entities.AdminUser;
import logic.business.entities.CompanyEnity;
import logic.business.entities.ContractEnity;
import logic.pages.Admin.ContractPage;
import logic.pages.Admin.SideBar;
import logic.utils.TimeStamp;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CreatePackage extends BaseTest {

    @Test(enabled = true, description = "Create package")
    public void createPackage() {
        String companyName= CompanyEnity.getCompanyName();
        test.get().info("Step 1: login with admin role");
        AdminUser.getInstance().login();

        test.get().info("Step 2: click course contract tab");
        SideBar.getInstance().clickContractTab();
        SideBar.getInstance().clickCourseContract();

        test.get().info("Step 3: click Create new button");
        ContractPage.getInstance().clickCreateNewContractBtn();

    }
}
