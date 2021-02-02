package suite.regression.UI;


import logic.pages.HomePage;
import logic.pages.SearchPage;
import org.testng.annotations.Test;

import java.util.HashMap;

public class ShoppingOnAmazoneTC extends BaseTest {

    @Test(enabled = true, description = "Test Amazon shopping")
    public void amazoneShoppingTCs() {

        test.get().info("Step 1: click today detail label");
        HomePage.getInstance().clickTodayDeal();

        test.get().info("Step 2: select High to low option");
        SearchPage.getInstance().triggerSortByText("Featured");
        SearchPage.getInstance().selectHighToLowOption();

        test.get().info("Step 3: click second item");
        SearchPage.getInstance().clickItemByIndex(1);

        test.get().info("Step 4: add 2 items in cart");
        SearchPage.getInstance().backToPreviousPage();
        SearchPage.getInstance().clickAddToCartByIndex(1);
        SearchPage.getInstance().clickAddToCartByIndex(1);

        test.get().info("Step 5: Go to to product cart ");
        SearchPage.getInstance().goToCart();

//
    }

}
