package suite.regression.selfcarews.getinvoicehistory;

import framework.utils.Xml;
import logic.business.db.billing.CommonActions;
import logic.business.ws.ows.OWSActions;
import logic.business.ws.sws.SWSActions;
import logic.utils.Common;
import logic.utils.TimeStamp;
import logic.utils.XmlUtils;
import org.testng.Assert;
import org.testng.annotations.Test;
import suite.BaseTest;
import suite.regression.care.CareTestBase;

import java.sql.Date;

/**
 * User: Nhi Dinh
 * Date: 10/10/2019
 */
public class TC32298_Self_Care_WS_Basic_Path_Get_Invoice_History_Customer_with_Draft_Invoice extends BaseTest {
    private Date newStartDate = TimeStamp.TodayMinus20Days();
    private String customerNumber;

    @Test(enabled = true, description = "TC32298_Self_Care_WS_Basic_Path_Get_Invoice_History_Customer_with_Draft_Invoice", groups = "SelfCareWS.GetInvoiceHistory")
    public void TC32298_Self_Care_WS_Basic_Path_Get_Invoice_History_Customer_with_Draft_Invoice() {
        test.get().info("1. Create an online CC customer with FC 1 bundle of SB and simonly");
        OWSActions owsActions = new OWSActions();
        owsActions.createAnOnlineCCCustomerWithFC1BundleOfSBAndSimonly();
        customerNumber = owsActions.customerNo;

        test.get().info("2. Create the new billing group");
        BaseTest.createNewBillingGroup();

        test.get().info("3. Update bill group payment collection date to 10 days later");
        BaseTest.updateBillGroupPaymentCollectionDateTo10DaysLater();

        test.get().info("4. Set bill group for customer");
        BaseTest.setBillGroupForCustomer(customerNumber);

        test.get().info("5. Update the start date of customer");
        CommonActions.updateCustomerStartDate(customerNumber, newStartDate);
        //===========================================================================
        test.get().info("6. Submit do refill bc job");
        submitDoRefillBCJob();

        test.get().info("7. Submit do refill nc job");
        submitDoRefillNCJob();

        test.get().info("8. Submit do bundle renew job");
        submitDoBundleRenewJob();

        test.get().info("9. Submit draft bill run");
        submitDraftBillRun();

        test.get().info("11. Load customer in hub net");
        CareTestBase.page().loadCustomerInHubNet(customerNumber);

        test.get().info("12. Verify customer has 1 draft invoice generated");
        CareTestBase.page().verifyCustomerHas1DraftInvoiceGenerated();

        test.get().info("13. Submit Get invoice history request");
        SWSActions swsActions = new SWSActions();
        Xml response = swsActions.submitGetInvoiceHistoryByAccountNoRequest(customerNumber);

        test.get().info("14. Verify get invoice history response is correct");
        String tempFile = "src\\test\\resources\\xml\\sws\\getinvoicehistory\\TC32298_Response.xml";
        verifyGetInvoiceHistoryResponse(tempFile, response);
    }

    private void verifyGetInvoiceHistoryResponse(String tempFile, Xml response) {
        String xmlValue = Common.readFile(tempFile).replace("$accountNumber$", customerNumber);
        String expectedResponse = Common.saveXmlFile(customerNumber + "_ExpectedResponse.txt", XmlUtils.prettyFormat(XmlUtils.toCanonicalXml(xmlValue)));
        String actualFile = Common.saveXmlFile(customerNumber + "_ActualResponse.txt", XmlUtils.prettyFormat(XmlUtils.toCanonicalXml(response.toString())));

        Assert.assertEquals(1, Common.compareFile(expectedResponse, actualFile).size());

    }
}
