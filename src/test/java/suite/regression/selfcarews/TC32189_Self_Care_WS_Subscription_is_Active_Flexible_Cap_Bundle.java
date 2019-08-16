package suite.regression.selfcarews;

import framework.utils.Xml;
import logic.business.db.billing.CommonActions;
import logic.business.ws.ows.OWSActions;
import logic.business.ws.sws.SWSActions;
import logic.business.ws.sws.SelfCareWSTestBase;
import logic.utils.TimeStamp;
import org.testng.annotations.Test;
import suite.BaseTest;
import suite.regression.care.CareTestBase;

import java.sql.Date;

/**
 * User: Nhi Dinh
 * Date: 7/08/2019
 */
public class TC32189_Self_Care_WS_Subscription_is_Active_Flexible_Cap_Bundle extends BaseTest {
    private String customerNumber;
    private Date newStartDate = TimeStamp.TodayMinus20Days();
    private String subscriptionNumber;
    private String clubCardNumber;

    @Test(enabled = true, description = "TC32189 Self Care WS Subscription is Active Flexible Cap Bundle", groups = "SelfCareWS")
    public void TC32189_Self_Care_WS_Subscription_is_Active_Flexible_Cap_Bundle(){
        test.get().info("Step 1 : Create a Customer with flexible cab bundle subscription active");
        OWSActions owsActions = new OWSActions();
        owsActions.createAnOnlinesCCCustomerWithFC1BundleAndSimOnly();
        customerNumber = owsActions.customerNo;

        owsActions.getOrder(owsActions.orderIdNo);
        subscriptionNumber = owsActions.getOrderMpnByReference(1);

        test.get().info("Create new billing group");
        createNewBillingGroup();

        test.get().info("Update bill group payment collection date to 10 days later");
        updateBillGroupPaymentCollectionDateTo10DaysLater();

        test.get().info("Set bill group for customer");
        setBillGroupForCustomer(customerNumber);

        test.get().info("Update Customer Start Date");
        CommonActions.updateCustomerStartDate(customerNumber, newStartDate);
        //=============================================================================
        test.get().info("Login to HUBNet then search Customer by customer number");
        CareTestBase.page().loadCustomerInHubNet(customerNumber);

        test.get().info("Record discount bundle monthly refill SO id");
        String orderId = CareTestBase.recordDiscountBundleMonthlyRefillSOId(subscriptionNumber);

        test.get().info("Submit get bundle request to selfcare WS");
        SWSActions swsActions = new SWSActions();
        Xml response = swsActions.submitGetBundleRequest(customerNumber, subscriptionNumber);

        test.get().info("Verify get bundle response are correct");
        String sampleResponseFile = "src\\test\\resources\\xml\\sws\\getbundle\\TC32189_response.xml";
        SelfCareWSTestBase selfCareWSTestBase = new SelfCareWSTestBase();
        selfCareWSTestBase.verifyGetBundleResponseAreCorrect(sampleResponseFile, response, customerNumber, subscriptionNumber, orderId, newStartDate);

    }
}
