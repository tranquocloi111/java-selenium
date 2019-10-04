package logic.pages.selfcare;

import framework.utils.Log;
import logic.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class AddOrChangeAFamilyPerkPage extends BasePage {
    public static AddOrChangeAFamilyPerkPage getInstance() {
        return new AddOrChangeAFamilyPerkPage();
    }
    @FindBy(id = "header")
    WebElement header;
    public String getHeaderName(){
        return getTextOfElement(header);
    }

    public static class InfoPage extends AddOrChangeAFamilyPerkPage {
        private static InfoPage instance;
        @FindBy(xpath = "//div[@class='msg-box']//table[@class='body-content-tab']")
        WebElement infoTable;

        public static InfoPage getInstance() {
            if (instance == null)
                return new InfoPage();
            return instance;
        }

        public String getMobilePhoneNumber() {
            return getTextOfElement(findLabelCell(infoTable, "Mobile phone number").findElement(By.xpath(".//following-sibling::td[1]")));
        }

        public String getTariff() {
            return getTextOfElement(findLabelCell(infoTable, "Tariff").findElement(By.xpath(".//following-sibling::td[1]")));
        }

        public String getMonthlyAllowance() {
            return getTextOfElement(findLabelCell(infoTable, "Monthly allowance").findElement(By.xpath(".//following-sibling::td[1]")));
        }

        public String getMonthlyBundles() {
            return getTextOfElement(findLabelCell(infoTable, "Monthly bundles").findElement(By.xpath(".//following-sibling::td[1]")));
        }

        public String getMonthlySafetyBuffer() {
            return getTextOfElement(findLabelCell(infoTable, "Monthly safety buffer").findElement(By.xpath(".//following-sibling::td[1]")));
        }

        public String getThisMonthAllowanceExpiryDate() {
            return getTextOfElement(findLabelCell(infoTable, "This month’s allowance expiry date").findElement(By.xpath(".//following-sibling::td[1]")));
        }

    }

    public static class BundleAllowancePage extends AddOrChangeAFamilyPerkPage {
        private static BundleAllowancePage instance;
        @FindBy(xpath = "//div[@class='msg-box']/following-sibling::table[1]")
        WebElement bundleTable;

        @FindBy(id = "SaveBtn")
        WebElement saveBtn;

        public static BundleAllowancePage getInstance() {
            if (instance == null)
                return new BundleAllowancePage();
            return instance;
        }

        private WebElement allowanceTable() {
            return bundleTable.findElement(By.xpath(".//td[2]")).findElement(By.tagName("table"));
        }

        private WebElement warningMessageCtl() {
            return allowanceTable().findElement(By.xpath("following-sibling::span[1]"));
        }

        private WebElement chooseBundlesDiv() {
            return bundleTable.findElement(By.xpath(".//td[1]")).findElement(By.xpath(".//div[@class='msg-box']"));
        }

        private WebElement acceptTermsAndConditionsDiv(){
            return getDriver().findElement(By.xpath("//div[@class='msg-box']/following-sibling::div[@class='msg-box']"));
        }

        public String getWarningMessage(){
            return getTextOfElement(warningMessageCtl());
        }

        public void selectBundlesByName(String... names) {
            for (String name : names) {
                WebElement checkbox = findCheckBox(chooseBundlesDiv(), name);
                if (findCheckBox(chooseBundlesDiv(), name).getAttribute("checked") == null) {
                    click(checkbox);
                }
            }
        }

        public void unselectBundlesByName(String... names) {
            for(String name : names)
            {
                WebElement checkbox = findCheckBox(chooseBundlesDiv(), name);
                if (checkbox.getAttribute("checked").equalsIgnoreCase("true")) {
                    click(checkbox);
                }
            }
        }

        public String getTextsRow(String column, int index){
            WebElement tableRow = allowanceTable().findElements(By.tagName("tr")).get(index);
            if (column.equalsIgnoreCase("Current allowance")){
                return tableRow.findElement(By.xpath(".//td[2]")).getText().trim();
            }
            else if (column.equalsIgnoreCase("New allowance")){
                return tableRow.findElement(By.xpath(".//td[3]")).getText().trim();
            }
            else {
                Log.error("Can't find bundle allowance value");
            }
            return null;
        }

        public void tickBoxToAcceptTheFamilyPerkTermsAndConditions() {
                WebElement checkbox = findCheckBox(acceptTermsAndConditionsDiv(), "Tick the box to accept the Family Perks terms and conditions.");
                if (checkbox.getAttribute("checked") == null){
                    click(checkbox);
                }
        }

        public void clickSaveButton(){
            clickByJs(saveBtn);
        }

        public String getTermsAndConditions(){
            WebElement element = acceptTermsAndConditionsDiv().findElement(By.xpath(".//tr[1]//td"));
            return getTextOfElement(element);
        }

        public String getTickBoxToAccept(){
            WebElement element = acceptTermsAndConditionsDiv().findElement(By.xpath(".//tr[2]//td"));
            return getTextOfElement(element);
        }
    }


}



