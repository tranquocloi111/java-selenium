package suite.regression.tropicana;

import logic.business.db.billing.CommonActions;
import logic.business.entities.ServiceOrderEntity;
import logic.business.helper.RemoteJobHelper;
import logic.business.ws.ows.OWSActions;
import logic.business.ws.sws.SWSActions;
import logic.pages.care.MenuPage;
import logic.pages.care.find.CommonContentPage;
import logic.pages.care.find.ServiceOrdersContentPage;
import logic.utils.TimeStamp;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import suite.BaseTest;
import suite.regression.care.CareTestBase;

import java.sql.Date;
import java.util.List;

public class TC4613_TC001_SCWS_Validation_Of_Offer_Element_In_The_Get_Subscription_Response extends BaseTest {
    private String customerNumber = "15758";
    private Date newStartDate;
    String subscription1;
    String subscription2;
    String serviceOrderId;

    @Test(enabled = true, description = "SCWS-Validation of Offer element in the Get Subscription response", groups = "Tropicana")
    public void TC4613_SCWS_Validation_Of_Offer_Element_In_The_Get_Subscription_Response(){
        test.get().info("Step 1 : Create a account having 2 subscriptions offered Bonus bundle and 2 subscriptions not offered Bonus bundle");
        OWSActions owsActions = new OWSActions();
        String path = "\\src\\test\\resources\\xml\\tropicana\\TC4613_TC001_request.xml";
        owsActions.createGeneralCustomerOrder(path);

        test.get().info("Step 2 : Create New Billing Group");
        BaseTest.createNewBillingGroup();

        test.get().info("Step 3 : Update Bill Group Payment Collection Date To 10 Days Later");
        BaseTest.updateBillGroupPaymentCollectionDateTo10DaysLater();

        test.get().info("Step 4 : Set bill group for customer");
        customerNumber = owsActions.customerNo;
        BaseTest.setBillGroupForCustomer(customerNumber);

        test.get().info("Step 4 : Update Customer Start Date");
        newStartDate = TimeStamp.TodayMinus15Days();
        CommonActions.updateCustomerStartDate(customerNumber, newStartDate);

        test.get().info("Step 5 : Get Subscription Number");
        CareTestBase.page().loadCustomerInHubNet(customerNumber);
        MenuPage.LeftMenuPage.getInstance().clickSubscriptionsLink();
        subscription1 = CommonContentPage.SubscriptionsGirdSectionPage.getInstance().getSubscriptionNumberValue("Mobile Ref 1");
        subscription2 = CommonContentPage.SubscriptionsGirdSectionPage.getInstance().getSubscriptionNumberValue("Mobile Ref 2");

        test.get().info("Step 6 : Add Bonus Bundle to Subscription");
        SWSActions swsActions = new SWSActions();
        String selfCarePath = "src\\test\\resources\\xml\\sws\\maintainbundle\\TC4682_request.xml";
        swsActions.submitMaintainBundleRequest(selfCarePath, customerNumber, subscription1);

        test.get().info("Step 7 : Submit Provision Wait");
        MenuPage.RightMenuPage.getInstance().clickRefreshLink();
        MenuPage.LeftMenuPage.getInstance().clickServiceOrdersLink();
        List<WebElement> serviceOrder = ServiceOrdersContentPage.getInstance().getServiceOrders(ServiceOrderEntity.dataServiceOrderBySubAndType(subscription1, "Change Bundle", "Provision Wait"));
        serviceOrderId = ServiceOrdersContentPage.getInstance().getServiceOrderIdByElementServiceOrders(serviceOrder);
        BaseTest.updateThePDateAndBillDateForSO(serviceOrderId);
        RemoteJobHelper.getInstance().runProvisionSevicesJob();

        test.get().info("Step 8 : Add Bonus Bundle to Subscription");
        swsActions.submitMaintainBundleRequest(selfCarePath, customerNumber, subscription2);

        test.get().info("Step 9 : Submit Provision Wait");
        MenuPage.RightMenuPage.getInstance().clickRefreshLink();
        MenuPage.LeftMenuPage.getInstance().clickServiceOrdersLink();
        serviceOrder = ServiceOrdersContentPage.getInstance().getServiceOrders(ServiceOrderEntity.dataServiceOrderBySubAndType(subscription2, "Change Bundle", "Provision Wait"));
        serviceOrderId = ServiceOrdersContentPage.getInstance().getServiceOrderIdByElementServiceOrders(serviceOrder);
        BaseTest.updateThePDateAndBillDateForSO(serviceOrderId);
        RemoteJobHelper.getInstance().runProvisionSevicesJob();

        test.get().info("Step 10 : Submit a 'Get Subscription Summary' SCWS request including Inactive Subscription Flag = TRUE and accountNumber.");
        swsActions.submitGetSubscriptionSummaryRequestByCusNumber(customerNumber, true);

        test.get().info("Step 11 : Verify ");

        test.get().info("Step 12 : Submit a 'Get Subscription Summary' SCWS request including Inactive Subscription Flag = TRUE and subscriptionNumber.");
        swsActions.submitGetSubscriptionSummaryRequestBySubNumber(subscription1,true);

        test.get().info("Step 13 : Verify ");
    }
}