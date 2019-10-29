package suite.regression.selfcarews.maintainpayment;

import framework.utils.Xml;
import logic.business.db.billing.CommonActions;
import logic.business.entities.FinancialTransactionEnity;
import logic.business.entities.ServiceOrderEntity;
import logic.business.entities.selfcare.MaintainPaymentResponseData;
import logic.business.ws.ows.OWSActions;
import logic.business.ws.sws.SWSActions;
import logic.business.ws.sws.SelfCareWSTestBase;
import logic.pages.care.MenuPage;
import logic.pages.care.find.FinancialTransactionPage;
import logic.pages.care.find.PaymentDetailPage;
import logic.pages.care.find.ServiceOrdersContentPage;
import logic.pages.care.main.TasksContentPage;
import logic.utils.Parser;
import logic.utils.TimeStamp;
import org.testng.Assert;
import org.testng.annotations.Test;
import suite.BaseTest;
import suite.regression.care.CareTestBase;

import java.sql.Date;
import java.util.HashMap;

public class TC32666_Basic_Path_Adhoc_Payment_Existing_Card_All_Card_Details_provided extends BaseTest {
    String customerNumber;
    String financialTransactionContentRef;

    @Test(enabled = true, description = "TC32666 basic path adhoc payment existing card all card details provided", groups = "SelfCare")
    public void TC32666_Basic_Path_Adhoc_Payment_Existing_Card_All_Card_Details_provided() {
        //-----------------------------------------
        String path = "src\\test\\resources\\xml\\commonrequest\\onlines_CC_customer_with_FC_1_bundle_of_SB_and_sim_only";
        test.get().info("Step 1 : Create a customer ");
        OWSActions owsActions = new OWSActions();
        owsActions.createGeneralCustomerOrder(path);
        customerNumber = owsActions.customerNo;
        owsActions.getSubscription(owsActions.orderIdNo, "Mobile FC");
        String sub = owsActions.serviceRef;

        test.get().info("Step 2 : Create the new billing group");
        BaseTest.createNewBillingGroup();

        test.get().info("Step 3: Update the payment collection date is 10");
        BaseTest.updateBillGroupPaymentCollectionDateTo10DaysLater();

        test.get().info("Step 4: set bill group for customer");
        BaseTest.setBillGroupForCustomer(customerNumber);

        test.get().info("Step 5: Update the start date of customer");
        Date newStartDate = TimeStamp.TodayMinus20Days();
        CommonActions.updateCustomerStartDate(customerNumber, newStartDate);

        test.get().info("Step 2 : load customer in hub net ");
        CareTestBase.page().loadCustomerInHubNet(customerNumber);

        test.get().info("Step 3 : Build maintain payment detail request ");
        //Here we give cvv number as 222 which means this card is not authorised.
        path = "src\\test\\resources\\xml\\sws\\maintaincontact\\TC32663_request";
        SWSActions swsActions = new SWSActions();
        swsActions.buildPaymentDetailRequestWithSubscriptionNumber(sub, customerNumber, path);

        test.get().info("Step 4  submit the request to webservice");
        Xml response = swsActions.submitTheRequest();

        test.get().info("Step 5  verify maintain payment response");
        MaintainPaymentResponseData maintainPaymentResponseData =new MaintainPaymentResponseData();
        maintainPaymentResponseData.setAccountNumber(customerNumber);
        maintainPaymentResponseData.setAction("ADHOC_PAYMENT");
        maintainPaymentResponseData.setResponseCode("Payment was successful");
        maintainPaymentResponseData.setReference("True");
        maintainPaymentResponseData.setReference(Parser.parseDateFormate(TimeStamp.Today(),TimeStamp.DATE_FORMAT_XML));

        SelfCareWSTestBase.verifyMaintainPaymentResponse(maintainPaymentResponseData,response);

        test.get().info("Step 6  refresh current customer data in hub net");
        MenuPage.RightMenuPage.getInstance().clickRefreshLink();


        test.get().info("Step 7  access financial transaction content for customer");
        MenuPage.LeftMenuPage.getInstance().clickFinancialTransactionLink();

        test.get().info("Step 8: verify 1 ad hoc payment generation");
        HashMap<String, String> financialTransaction = FinancialTransactionEnity.dataFinancialTransactionForMakeAOneOffPayment("Ad Hoc Payment", "£20.00");
        Assert.assertEquals(FinancialTransactionPage.FinancialTransactionGrid.getInstance().getNumberOfFinancialTransaction(financialTransaction), 1);


        test.get().info("Step 8: verify the adhoc payment transaction detail");
        financialTransactionContentRef = FinancialTransactionPage.FinancialTransactionGrid.getInstance().getRefNumberByDetail("Ad Hoc Payment");
        FinancialTransactionPage.FinancialTransactionGrid.getInstance().clickFinancialTransactionByDetail("Ad Hoc Payment");
        verifyAdHocPaymentTransactionDetail();

        test.get().info("Step 9 :Open the service order content for customer");
        MenuPage.LeftMenuPage.getInstance().clickServiceOrdersLink();

        HashMap<String, String> serviceOrder = ServiceOrderEntity.dataServiceOrderFinancialTransaction();
        Assert.assertEquals(ServiceOrdersContentPage.getInstance().getNumberOfServiceOrders(serviceOrder), 1);



        test.get().info("Step 10 :verify the service order detail content for customer");
        ServiceOrdersContentPage.getInstance().clickServiceOrderByType("Ad-hoc Payment");

        Assert.assertEquals(TasksContentPage.TaskSummarySectionPage.getInstance().getDescription(), "Ad-hoc Payment");
        Assert.assertEquals(TasksContentPage.TaskSummarySectionPage.getInstance().getStatus(), "Completed Task");
        Assert.assertEquals("MasterCard", TasksContentPage.TaskPage.DetailsPage.getInstance().getCardType());
        Assert.assertEquals("****************5100", TasksContentPage.TaskPage.DetailsPage.getInstance().getCardNumber());
        Assert.assertEquals("2030", TasksContentPage.TaskPage.DetailsPage.getInstance().getCreditCardExpiryYear());
        Assert.assertEquals("20", TasksContentPage.TaskPage.DetailsPage.getInstance().getAmountToBeDebited());
        Assert.assertEquals("12", TasksContentPage.TaskPage.DetailsPage.getInstance().getCreditCardExpiryMonth());


    }

    public void verifyAdHocPaymentTransactionDetail() {
        Assert.assertEquals(PaymentDetailPage.ReceiptDetail.getInstance().getReceiptType(), "Ad Hoc Payment");
        Assert.assertEquals(PaymentDetailPage.ReceiptDetail.getInstance().getReceiptStatus(), "Fully Allocated");
        Assert.assertEquals(PaymentDetailPage.ReceiptDetail.getInstance().getPaymentAmount(), "£20.00");
        Assert.assertEquals(PaymentDetailPage.ReceiptDetail.getInstance().getPaymentCurrency(), "Great Britain Pound");
        Assert.assertEquals(PaymentDetailPage.ReceiptDetail.getInstance().getCardType(), "MasterCard");
        Assert.assertEquals(PaymentDetailPage.ReceiptDetail.getInstance().getCardNumber(), "************5100");
    }

}




