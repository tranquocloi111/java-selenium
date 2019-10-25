package suite.regression.selfcare.changebundle;

import logic.business.db.billing.BillingActions;
import logic.business.db.billing.CommonActions;
import logic.business.entities.DiscountBundleEntity;
import logic.business.entities.EventEntity;
import logic.business.entities.OtherProductEntiy;
import logic.business.entities.ServiceOrderEntity;
import logic.business.helper.DateTimeHelper;
import logic.business.ws.ows.OWSActions;
import logic.pages.care.MenuPage;
import logic.pages.care.find.CommonContentPage;
import logic.pages.care.find.ServiceOrdersContentPage;
import logic.pages.care.find.SubscriptionContentPage;
import logic.pages.care.main.TasksContentPage;
import logic.pages.selfcare.ChangeMySafetyBufferPage;
import logic.pages.selfcare.MonthlyBundlesAddChangeOrRemovePage;
import logic.pages.selfcare.MyPersonalInformationPage;
import logic.utils.Parser;
import logic.utils.TimeStamp;
import org.testng.Assert;
import org.testng.annotations.Test;
import suite.BaseTest;
import suite.regression.care.CareTestBase;
import suite.regression.selfcare.SelfCareTestBase;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;

public class TC31926_B11_Change_SB_to_HUL_Apply_the_change_From_my_next_bill_date extends BaseTest {

    String serviceRefOf1stSubscription;
    String serviceOrderID;
    Date newStartDate;
    String customerNumber;
    String discountGroupCodeOfMobileRef1;
    String serviceOrderIdSms;
    String provisionDate;

     @Test(enabled = true, description = "TC31926 SelfCare SB to HUL apply the change From my next bill date", groups = "SelfCare")
    public void TC31926_B11_Change_SB_to_HUL_Apply_the_change_From_my_next_bill_date() {
        String path = "src\\test\\resources\\xml\\commonrequest\\onlines_CC_customer_with_FC_1_bundle_and_NK2720";
        test.get().info("Step 1 : Create a customer with FC 1 bundle and NK270");
        OWSActions owsActions = new OWSActions();
        owsActions.createGeneralCustomerOrder(path);
        customerNumber = owsActions.customerNo;

        test.get().info("Step 2 : Create the new billing group");
        BaseTest.createNewBillingGroupToMinus15days();

        test.get().info("Step 3: Update the payment collection date is 10");
        BaseTest.updateBillGroupPaymentCollectionDateTo10DaysLater();

        test.get().info("Step 4: set bill group for customer");
        BaseTest.setBillGroupForCustomer(customerNumber);

        test.get().info("Step 5: Update the start date of customer");
        newStartDate = TimeStamp.TodayMinus10Days();
        CommonActions.updateCustomerStartDate(customerNumber, newStartDate);

        test.get().info("Step 6 : Load customer in hub net");
        CareTestBase.page().loadCustomerInHubNet(customerNumber);

        test.get().info("Step 7 : Verify customer data is updated ");
        CareTestBase.page().verifyCustomerStartDateAndBillingGroupAreUpdatedSuccessfully(newStartDate);

        test.get().info("Step 8 : Verify all discount bundle entries align with bill run calendar entries");
        MenuPage.LeftMenuPage.getInstance().clickSubscriptionsLink();
        CommonContentPage.SubscriptionsGridSectionPage.getInstance().clickSubscriptionNumberLinkByIndex(1);
        discountGroupCodeOfMobileRef1 = SubscriptionContentPage.SubscriptionDetailsPage.GeneralSectionPage.getInstance().getDiscountGroupCode();
        serviceRefOf1stSubscription = SubscriptionContentPage.SubscriptionDetailsPage.GeneralSectionPage.getInstance().getSubscriptionNumber();

        test.get().info("Step 9 : Login to Self Care");
        SelfCareTestBase.page().LoginIntoSelfCarePage(owsActions.username, owsActions.password, customerNumber);
        SelfCareTestBase.page().verifyMyPersonalInformationPageIsDisplayed();

        test.get().info("Step 10 : Click view or change my tariff detail links");
        MyPersonalInformationPage.MyTariffPage.getInstance().clickViewOrChangeMyTariffDetailsLink();
        SelfCareTestBase.page().verifyMyTariffDetailsPageIsDisplayed();


        test.get().info("Step 11: click change my safety buffer button");
        MyPersonalInformationPage.MyTariffPage.MyTariffDetailsPage.getInstance("Mobile Ref 1").clickChangeMySafetyBufferBtn();
        SelfCareTestBase.page().verifyChangeMySafetyBufferPage();

        test.get().info("Step 12: select 40 safety buffer and change it now but only until my next bill date");

        ChangeMySafetyBufferPage.getInstance().selectSafetyBuffer("No safety buffer");
        ChangeMySafetyBufferPage.getInstance().selectWhenWouldLikeToChangeMethod("Change it from my next bill date");
        ChangeMySafetyBufferPage.ComfirmationYourChanges.getInstance().tickAgreeCheckBox();

        test.get().info("Step 13 : verify confirming your changes displays correct result");
        Assert.assertEquals("£20.00", ChangeMySafetyBufferPage.getInstance().getPreviousSafetyBuffer());
        Assert.assertEquals("None", ChangeMySafetyBufferPage.getInstance().getNewSafetyBuffer());


        test.get().info("Step 14 : click save button");
        MonthlyBundlesAddChangeOrRemovePage.getInstance().clickSaveBtn();

        test.get().info("Step 15 :verify my tariff details page displayed with successful alert");
        List<String> alert = SelfCareTestBase.page().successfulMessageStack();
        Assert.assertEquals(2, alert.size());
        Assert.assertEquals("You’ve successfully removed your safety buffer.", alert.get(0));
        Assert.assertEquals("Your changes will take effect from " + Parser.parseDateFormate(TimeStamp.TodayPlus1MonthMinus15Day(), TimeStamp.DATE_FORMAT_IN_PDF), alert.get(1));
        Assert.assertEquals("£20 safety buffer    PENDING removal  as of  " + Parser.parseDateFormate(TimeStamp.TodayPlus1MonthMinus15Day(), TimeStamp.DATE_FORMAT_IN_PDF), MyPersonalInformationPage.MyTariffPage.MyTariffDetailsPage.getInstance("Mobile Ref 1").getSafetyBuffer());

        test.get().info("Step 16 : load user in hub net for customer");
        CareTestBase.page().loadCustomerInHubNet(customerNumber);
        MenuPage.RightMenuPage.getInstance().clickRefreshLink();


        test.get().info("Step 17 : Open service orders page in hub net for customer");
        MenuPage.LeftMenuPage.getInstance().clickServiceOrdersLink();

        test.get().info("Step 18 : Verify customer has 1 expected change bundle SO record");
        serviceOrderIdSms = ServiceOrdersContentPage.getInstance().getServiceOrderidByType("Ad-hoc SMS Messages");
        HashMap<String, String> temp = ServiceOrderEntity.dataServiceOrder(serviceRefOf1stSubscription, "Change Bundle", "Provision Wait");
        int size = ServiceOrdersContentPage.getInstance().getNumberOfServiceOrdersByOrderService(temp);
        Assert.assertEquals(1, size);
        serviceOrderID = ServiceOrdersContentPage.getInstance().getServiceOrderidByType("Change Bundle");

        test.get().info("Step 19 : Open details screen for change bundle SO");
        ServiceOrdersContentPage.getInstance().clickServiceOrderByType("Change Bundle");

        test.get().info("Step 20 : Verify SO details data are updated");
        Assert.assertEquals(serviceRefOf1stSubscription + " Mobile Ref 1", TasksContentPage.TaskPage.DetailsPage.getInstance().getSubscriptionNumber());
        Assert.assertEquals("FC12-1000-500SO £10 Tariff 12 Month Contract {£10.00}", TasksContentPage.TaskPage.DetailsPage.getInstance().getTariff());
        Assert.assertEquals(Parser.parseDateFormate(TimeStamp.TodayPlus1MonthMinus15Day(), TimeStamp.DATE_FORMAT), TasksContentPage.TaskPage.DetailsPage.getInstance().getProvisioningDate());
        Assert.assertEquals("Yes", TasksContentPage.TaskPage.DetailsPage.getInstance().getNotificationOfLowBalance());
        Assert.assertEquals("No", TasksContentPage.TaskPage.DetailsPage.getInstance().getTemporaryChangeFlag());
        Assert.assertEquals("£50 High usage limit;", TasksContentPage.TaskPage.DetailsPage.getInstance().getBundlesAdded());
        Assert.assertEquals("£20 safety buffer;", TasksContentPage.TaskPage.DetailsPage.getInstance().getBundlesRemoved());

        Assert.assertEquals(2, TasksContentPage.TaskPage.EventsGridSectionPage.getInstance().getRowNumberOfEventGird());
        Assert.assertEquals(1, TasksContentPage.TaskPage.EventsGridSectionPage.getInstance().getNumberOfEventsByEvent(EventEntity.dataForEventServiceOrder("Service Order set to Provision Wait", "Provision Wait")));
        Assert.assertEquals(1, TasksContentPage.TaskPage.EventsGridSectionPage.getInstance().getNumberOfEventsByEvent(EventEntity.dataForEventServiceOrder("SMS Request Completed", "Completed Task")));

        provisionDate = TasksContentPage.TaskPage.DetailsPage.getInstance().getProvisioningDate();
        java.util.Date date= DateTimeHelper.getInstance().parseStringToDate(provisionDate,TimeStamp.DATE_FORMAT);
        provisionDate= DateTimeHelper.getInstance().changeformatDate(date,TimeStamp.DATE_FORMAT6);

        test.get().info("Step 21 : verify a new sms message so create and details");
        verifyANewSMSMessageSOCreatedAndDetails();


    }

    private void verifyANewSMSMessageSOCreatedAndDetails() {
        Assert.assertTrue(CommonActions.getSMSIsSent(serviceOrderID, "") != null);
        String mess = String.format("<SMSGateway><sms:sendSms><smsRequest><type>OFCOM</type><mpn>%s</mpn><message>Tesco Mobile: You have removed your safety buffer. This takes effect from %s.</message>", serviceRefOf1stSubscription, provisionDate);
        List<String> str = CommonActions.getContextInfoOfSMSServiceOrderIsCorrectInDb(serviceOrderID, "");
        String result =str.get(1).substring(str.get(1).indexOf("<SMSGateway>"),str.get(1).indexOf("</smsRequest>"));
        Assert.assertEquals(mess,result);
    }


}
