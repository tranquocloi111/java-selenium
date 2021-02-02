package logic.pages;

import framework.wdm.DriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class DateTimePicker extends BasePage {
    WebDriver driver;
    WebElement dateTimePickerTHead;
    WebElement dateTimePickerTBody;

    public DateTimePicker(WebElement webElement){
        driver = DriverFactory.getInstance().getDriver(By.xpath("//select[@name='quantity']"));
        dateTimePickerTHead= webElement.findElement(By.xpath("//div[@class='datepicker-days']//thead"));
        dateTimePickerTBody=webElement.findElement(By.xpath("//div[@class='datepicker-days']//tbody"));
    }


    public void nextMonth(int time){
        for(int i=0;i<time;i++) {
          click( dateTimePickerTHead.findElement(By.xpath(".//th[@class='next']")));
        }
    }
    public void selectDate(String day){
        int date = Integer.parseInt(day);
        String xpath = String.format("//td[text()='%s']",String.valueOf(date));
        click(dateTimePickerTBody.findElement(By.xpath(xpath)));
    }
}
