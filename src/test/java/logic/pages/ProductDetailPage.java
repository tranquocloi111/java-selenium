package logic.pages;

import framework.wdm.DriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class ProductDetailPage extends BasePage{

    private ProductDetailPage() {
    }
    public static ProductDetailPage getInstance(){
        return new ProductDetailPage();
    }

    public WebElement getQuantitySelect(int index){
        Select quanitySelect=new Select (DriverFactory.getInstance().getDriver().findElement(By.xpath("//select[@name='quantity']")));
        quanitySelect.selectByVisibleText(String.valueOf(index));
    }

    public  void backToPreviousPage() {
        super.backToPreviousPage();
    }
}
