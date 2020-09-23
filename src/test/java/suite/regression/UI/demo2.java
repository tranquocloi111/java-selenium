package suite.regression.UI;


import logic.business.entities.AdminUser;
import logic.pages.Admin.SideBar;
import org.testng.annotations.Test;


/**
 * User:Loi Tran
 * Date: 31/07/2019
 */
public class demo2 extends BaseTest {


        @Test(enabled = true, description = "demo2")
        public void demo3() {

            test.get().info("Step 1: login with admin role");
            AdminUser.getInstance().login();

            test.get().info("Step 2: click company tab");
            SideBar.getInstance().clickCompanyTab();

        }
}