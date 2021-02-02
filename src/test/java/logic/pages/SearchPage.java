package logic.pages;

import framework.wdm.DriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class SearchPage extends BasePage{

    private SearchPage() {
    }
    public static SearchPage getInstance(){
        return new SearchPage();
    }

    public WebElement getHighToLowOption(){
        return getDriver().findElement(By.xpath("//a[normalize-space(text())='Discount - High to Low']"));
    }
    public void selectHighToLowOption(){
        click(getHighToLowOption());
        waitUntilSpecificTime(4);
    }

    public void triggerSortByText(String text){
        String xpath = String.format("//span[normalize-space(text())='%s']/ancestor::span[@role='button']",text);
        click(getDriver().findElement(By.xpath(xpath)));
    }
    public  void clickItemByIndex(int index) {
        click(getAllAElementsById("dealImage").get(index));
    }

    public  void clickAddToCartByIndex(int index) {
        click(getAllButtonsByText("Add To Cart").get(index));
    }

    public void goToCart(){
       click(DriverFactory.getInstance().getDriver(By.xpath("//select[@name='quantity']")).findElement(By.xpath("//span[normalize-space(text())='Cart']//ancestor::a[1]")));
    }

    public  void backToPreviousPage() {
        super.backToPreviousPage();
    }
}
