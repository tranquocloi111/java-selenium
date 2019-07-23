package logic.pages.care.find;

import logic.pages.BasePage;
import logic.pages.TableControlBase;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.HashMap;
import java.util.List;

public class OtherChargesCreditsContent extends BasePage {

    private static OtherChargesCreditsContent instance = new OtherChargesCreditsContent();
    public static OtherChargesCreditsContent getInstance() {
        return new OtherChargesCreditsContent();
    }

    @FindBy(xpath = "//td[@class='informationBoxHeader' and contains(text(),'Other Charges/Credits')]/../../..//following-sibling::table")
    WebElement otherChargesCreditsContentTable;
    TableControlBase table = new TableControlBase(otherChargesCreditsContentTable);

    public List<WebElement> getChargeCredits(List<HashMap<String, String>> chargeCredit) {
        return table.findRowsByColumns(chargeCredit);
    }

    public List<WebElement> getChargeCredits(HashMap<String, String> chargeCredit) {
        return table.findRowsByColumns(chargeCredit);
    }

    public int getNumberOfChargeCredits(HashMap<String, String> chargeCredit) {
        return getChargeCredits(chargeCredit).size();
    }

    public int getRowNumberOfOtherChargesCreditsContentTable() {
        return table.getRowsCount();
    }
}
