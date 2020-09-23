package logic.pages.Admin;

import logic.pages.BasePage;
import logic.pages.TableControlBase;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

import java.util.HashMap;
import java.util.List;

public class ContractPage extends BasePage {

    private static ContractPage instance;

    private ContractPage() {
    }

    public static ContractPage getInstance() {
        return new ContractPage();
    }

    public void clickCreateNewContractBtn() {
        click(getAElementByText("New course's contract"));
    }

    /////////////////////////////////////////////////////
    /* Create company*/
    public static class CreateContractPage extends ContractPage {


        private static CreateContractPage instance;

        private CreateContractPage() {
        }

        public static CreateContractPage getInstance() {
            return new CreateContractPage();
        }

        public void selectCompanyName(String companyName) {
            selectDropDownListByVisibleTextAndLabel("Company", companyName);
        }

        @FindBy(xpath = "//div[@class='datepicker-days']")
        WebElement datePicker;

        public void selectStartDate(String date) {
            super.setDateByLabel(datePicker, "Start Date", 0, date);
        }

        public void selectEndDate(String date) {
            super.setDateByLabel(datePicker, "Expired Date", 3, date);
        }

        public void selectPackage(String pack) {
            super.searchAutoCompleteInputByLabel("Select a package of courses", pack);
        }

        public void fillInQuanity(String amount) {
            enterValueByLabel(findDivByLabel("Slot quantity").findElement(By.xpath(".//input")), amount);
        }

        public void selectStatus(String status) {
            click(findSelectByLabel("Status"));
            selectDropDownListByVisibleTextAndLabel("Status", status);
        }

        public void verifyIsCLPIsChecked(String label) {
            Assert.assertTrue(verifyCheckBoxIsCheckedByLabel(label));
        }

        public void clickAddPackage() {
            click(findButtonBy("Add another Package"));
        }

        public void verifyCoursesAreAdded(List<String> courseList,String label){
           List<WebElement> elementList= findSpansByLabel(label);
           for (int i=0;i<elementList.size();i++){
               Assert.assertEquals(elementList.get(i).getText(),courseList.get(i));
           }
        }

        public void verifyQuanity(String quantity,String label){
            List<WebElement> elementList= findInputsByLabel(label);
            for (int i=0;i<elementList.size();i++){
                Assert.assertEquals(elementList.get(i).getAttribute("value"),quantity);
            }
        }
    }
    /////////////////////////////////////////////////////
    /* Company list*/

    public static class ContractList extends ContractPage {

        private ContractList() {
        }

        public static ContractList getInstance() {
            return new ContractList();
        }

        @FindBy(xpath = "//table[@id='order-table']")
        WebElement contractTable;
        TableControlBase tableControlBase = new TableControlBase(contractTable);


        public void checkTheRowIsExist(HashMap<String, String> row) {
            waitUntilSpecificTime(3);
            Assert.assertEquals(1, tableControlBase.findARowByColumns(row).size());
        }
    }


}
