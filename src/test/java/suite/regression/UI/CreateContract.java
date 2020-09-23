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

public class CreateContract extends BaseTest {

    @Test(enabled = true, description = "Create contract")
    public void createContractTest() {
        String companyName= CompanyEnity.getCompanyName();
        test.get().info("Step 1: login with admin role");
        AdminUser.getInstance().login();

        test.get().info("Step 2: click course contract tab");
        SideBar.getInstance().clickContractTab();
        SideBar.getInstance().clickCourseContract();

        test.get().info("Step 3: click Create new button");
        ContractPage.getInstance().clickCreateNewContractBtn();

        test.get().info("Step 4: Select the company name ");
        ContractPage.CreateContractPage.getInstance().selectCompanyName(companyName);

        test.get().info("Step 5: select start date");
        List<String> today = new ArrayList<>();
        today = Arrays.asList(TimeStamp.Today().toString().split("-"));
        ContractPage.CreateContractPage.getInstance().selectStartDate(today.get(2));

        test.get().info("Step 6: Select end Date ");
        ContractPage.CreateContractPage.getInstance().selectEndDate(today.get(2));

        test.get().info("Step 7: Select status approve  ");
        ContractPage.CreateContractPage.getInstance().selectStatus("Approved");

        test.get().info("Step 8: verify IsCLP checkbox is checked");
        ContractPage.CreateContractPage.getInstance().verifyIsCLPIsChecked("Is CLP");

        test.get().info("Step 9: click add package");
        ContractPage.CreateContractPage.getInstance().clickAddPackage();

        test.get().info("Step 10: Select package ");
        ContractPage.CreateContractPage.getInstance().selectPackage("Bundle 5");

        test.get().info("Step 11: Fill in quantity ");
        ContractPage.CreateContractPage.getInstance().fillInQuanity("30");

        test.get().info("Step 12: click submit button ");
        ContractPage.CreateContractPage.getInstance().clickSubmitBtn();

        test.get().info("Step 13: verify courses are added in contract ");
        verifyCoursesAreAddedInContract();

        test.get().info("Step 14: verify the submit button is disappear");
        Assert.assertFalse(ContractPage.CreateContractPage.getInstance().isSubmitBtnDisplayed());

        test.get().info("Step 15: click back to list button ");
        ContractPage.CreateContractPage.getInstance().clickBackToListBtn();

        test.get().info("Step 16: verify new contract is displayed ");
        HashMap<String,String> enity =  ContractEnity.contractEnity(companyName,"Course's contract","","Approved");
        ContractPage.ContractList.getInstance().checkTheRowIsExist(enity);

    }

    private void verifyCoursesAreAddedInContract(){
        List<String> courseList=new ArrayList<>();
        courseList.add("Lập trình Java trong 4 tuần");
        courseList.add("JavaScript dành cho người mới bắt đầu");
        courseList.add("Javascript nâng cao ES6");
        courseList.add("Vượt qua buồn chán và lo âu");
        courseList.add("Lập trình Python cơ bản");

       ContractPage.CreateContractPage.getInstance().verifyCoursesAreAdded(courseList,"Course");
       ContractPage.CreateContractPage.getInstance().verifyQuanity("30","Slot quantity");
    }

}
