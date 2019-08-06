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
 * Date: 2/08/2019
 */
public class TC32132_Basic_Path_Active_Account_with_Current_Draft_Invoice extends BaseTest {
    private String customerNumber;
    private Date newStartDate = TimeStamp.TodayMinus10Days();
    private String latestSubscriptionNumber;
    private String clubCardNumber;

    @Test
    public void TC32132_Basic_Path_Active_Account_with_Current_Draft_Invoice() {
        test.get().info("Step 1 : Create a CC Customer with FC 1 bundle and NK2720");
        OWSActions owsActions = new OWSActions();
        owsActions.createAnOnlinesCCCustomerWithFC1BundleAndNK2720();
        customerNumber = owsActions.customerNo;

        test.get().info("Create new billing group start from today minus 15 days");
        createNewBillingGroup();

        test.get().info("Update bill group payment collection date to 10 days later");
        updateBillGroupPaymentCollectionDateTo10DaysLater();

        test.get().info("Set bill group for customer");
        setBillGroupForCustomer(customerNumber);

        test.get().info("Update Customer Start Date");
        CommonActions.updateCustomerStartDate(customerNumber, newStartDate);
        //=============================================================================
        test.get().info("Submit do refill BC batch job");
        submitDoRefillBCJob();

        test.get().info("Submit do refill NC batch job");
        submitDoRefillNCJob();

        test.get().info("Submit do bundle renew batch job");
        submitDoBundleRenewJob();

        test.get().info("Submit draft bill run");
        submitDraftBillRun();

        //=============================================================================

        test.get().info("Login to HUBNet then search Customer by customer number");
        CareTestBase.page().loadCustomerInHubNet(customerNumber);

        test.get().info("Record latest subsciption number for customer");
        latestSubscriptionNumber = CareTestBase.page().recordLatestSubscriptionNumberForCustomer();

        test.get().info("Record account name and club card number");
        clubCardNumber = CareTestBase.page().recordAccountNameAndClubCardNumber();
        //=============================================================================
        test.get().info("Verify Customer has 1 draft invoice generated");
        SWSActions swsActions = new SWSActions();
        swsActions.verifyCustomerHas1DraftInvoiceGenerated();
        //=============================================================================
        test.get().info("Submit Get Account Summary Request To SelfCare WebService");
        Xml response = swsActions.submitGetAccountSummaryRequestToSelfCareWS(customerNumber);

        test.get().info("Build Expected Account Summary Response Data");
        String sampleResponseFile = "src\\test\\resources\\xml\\sws\\getaccount\\TC32132_response.xml";
        Xml expectedResponse = swsActions.buildSimpleAccountSummaryResponseData(sampleResponseFile, newStartDate, customerNumber, latestSubscriptionNumber);

        test.get().info("Verify Get Account Summary Response");
        SelfCareWSTestBase selfCareWSTestBase = new SelfCareWSTestBase();
        selfCareWSTestBase.verifyGetAccountSummaryResponse(customerNumber, expectedResponse, response);
    }
}
