package suite.regression.tropicana;

import framework.utils.Xml;
import logic.business.db.billing.CommonActions;
import logic.business.entities.ServiceOrderEntity;
import logic.business.ws.ows.OWSActions;
import logic.business.ws.sws.SWSActions;
import logic.pages.care.MenuPage;
import logic.pages.care.find.CommonContentPage;
import logic.pages.care.find.ServiceOrdersContentPage;
import logic.utils.Common;
import logic.utils.Parser;
import logic.utils.TimeStamp;
import logic.utils.XmlUtils;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import suite.BaseTest;
import suite.regression.care.CareTestBase;

import java.sql.Date;
import java.util.List;

public class TC4617_TC006_WS_02_Customer_Has_1_Subscription_has_2_Permitted_Additional_Bundle_With_Same_Free_Type extends BaseTest {
    String subscriptionNumber;
    private String customerNumber;
    private Date newStartDate;
    private String serviceOrderId;

    @Test(enabled = true, description = "TC4617_TC006_WS_02_Customer_Has_1_Subscription_has_2_Permitted_Additional_Bundle_With_Same_Free_Type", groups = "Tropicana")
    public void TC4617_TC006_WS_02_Customer_Has_1_Subscription_has_2_Permitted_Additional_Bundle_With_Same_Free_Type() {
        test.get().info("Step 1 : Customer has 1 Subscription that has 2 Permitted/Additional Bundle with same Free Type");
        String path = "src\\test\\resources\\xml\\tropicana\\TC4617_TC006_request.xml";
        OWSActions owsActions = new OWSActions();
        owsActions.createGeneralCustomerOrder(path);

        test.get().info("Step 2 : Create New Billing Group");
        BaseTest.createNewBillingGroup();

        test.get().info("Step 3 : Update Bill Group Payment Collection Date To 10 Days Later");
        BaseTest.updateBillGroupPaymentCollectionDateTo10DaysLater();

        test.get().info("Step 4 : Set bill group for customer");
        customerNumber = owsActions.customerNo;
        BaseTest.setBillGroupForCustomer(customerNumber);

        test.get().info("Step 5 : Update Customer Start Date");
        newStartDate = TimeStamp.TodayMinus15Days();
        CommonActions.updateCustomerStartDate(customerNumber, newStartDate);

        test.get().info("Step 6 : Get Subscription Number");
        CareTestBase.page().loadCustomerInHubNet(customerNumber);
        MenuPage.LeftMenuPage.getInstance().clickSubscriptionsLink();
        subscriptionNumber = CommonContentPage.SubscriptionsGridSectionPage.getInstance().getSubscriptionNumberValue("Mobile Ref 1");
        MenuPage.LeftMenuPage.getInstance().clickServiceOrdersLink();
        List<WebElement> serviceOrder = ServiceOrdersContentPage.getInstance().getServiceOrders(ServiceOrderEntity.dataServiceOrderBySubAndType(subscriptionNumber, "Discount Bundle Monthly Refill", "In Progress"));
        serviceOrderId = ServiceOrdersContentPage.getInstance().getServiceOrderIdByElementServiceOrders(serviceOrder);

        test.get().info("Step 7 : Locate test customer/subscription in GetBundle and run request");
        SWSActions swsActions = new SWSActions();
        Xml xml = swsActions.submitGetBundleRequest(customerNumber, subscriptionNumber);

        test.get().info("Step 8 : Verify get bundle response are correct");
        verifyGetBundleResponseAreCorrect(xml);
    }

    private void verifyGetBundleResponseAreCorrect(Xml xml){
        String localTime = Common.getCurrentLocalTime();
        String actualFile = Common.saveXmlFile(customerNumber + localTime +"_ActualResponse.txt", XmlUtils.prettyFormat(XmlUtils.toCanonicalXml(xml.toString())));
        String file =  Common.readFile("src\\test\\resources\\xml\\tropicana\\TC4617_TC006_response.xml")
                .replace("$accountNumber$", customerNumber)
                .replace("$subscriptionNumber$", subscriptionNumber)
                .replace("$startDate$", Parser.parseDateFormate(newStartDate,"yyyy-MM-dd"))
                .replace("$nextScheduledRefill$", Parser.parseDateFormate(TimeStamp.TodayPlus1Month(),"yyyy-MM-dd"))
                .replace("$SOId$", serviceOrderId);;
        String expectedResponseFile = Common.saveXmlFile(customerNumber + localTime +"_ExpectedResponse.txt", XmlUtils.prettyFormat(XmlUtils.toCanonicalXml(file)));
        int size = Common.compareFile(actualFile, expectedResponseFile).size();
        Assert.assertEquals(1, size);
    }
}