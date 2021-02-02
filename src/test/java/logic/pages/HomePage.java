package logic.pages;

import org.openqa.selenium.WebElement;

public class HomePage extends BasePage{

    private HomePage() {
    }
    public static HomePage getInstance(){
        return new HomePage();
    }

    public WebElement getTodayDeal(){
        return getAElementByText("Today's Deals");
    }
    public void clickTodayDeal(){
        click(getTodayDeal());
    }
}
