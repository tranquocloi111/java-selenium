package suite.regression.selfcare.modifyaccount;


import framework.config.Config;
import framework.utils.Log;
import logic.business.db.OracleDB;
import logic.business.db.billing.CommonActions;
import logic.business.entities.EventEntity;
import logic.business.helper.FTPHelper;
import logic.business.helper.RemoteJobHelper;
import logic.business.ws.ows.OWSActions;
import logic.pages.care.MenuPage;
import logic.pages.care.find.DetailsContentPage;
import logic.pages.care.find.SelfCareSettingContentPage;
import logic.business.helper.EmailHelper;
import logic.pages.care.find.ServiceOrdersContentPage;
import logic.pages.care.main.TasksContentPage;
import logic.pages.selfcare.MyPasswordPage;
import logic.utils.Common;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import suite.BaseTest;
import suite.regression.care.CareTestBase;
import suite.regression.selfcare.SelfCareTestBase;
import java.io.File;
import java.util.List;

public class TC30223_Change_Self_Care_Password extends BaseTest {
/*
add email information in properties file
Change expected email URL base on environment
Note: Mel environment can not run FTP.
 */

    @Test(enabled = true, description = "TC30223 Change password by selfcare", groups = "SelfCare")
    public void TC30223_Change_Self_Care_Password() {

        test.get().info("Step 1 : delete all old data");
        String expectedFile = "src\\test\\resources\\txt\\TC30223_expectedEmail.txt";
        String actualFile = "src\\test\\resources\\txt\\TC30223_actualEmail.txt";
        Common.deleteFile(actualFile);
        EmailHelper.getInstance().deleteAllEmailByFolderNameAndEmailSubject("TescoMobilePayMonthly", "Password reset");
        EmailHelper.getInstance().deleteAllEmailByFolderNameAndEmailSubject("TescoMobilePayMonthly", "Have you logged");
        EmailHelper.getInstance().deleteAllEmailByFolderNameAndEmailSubject("TescoMobilePayMonthly", "Change of your Pay Monthly Price Plans");

        test.get().info("Step 2 : Create a CC customer with 3 subscriptions");
        String path = "src\\test\\resources\\xml\\SelfCare\\viewaccount\\TC30222_CreateOrder";
        OWSActions owsActions = new OWSActions();
        owsActions.createGeneralCustomerOrder(path);

        test.get().info("Step 3 : load user in the hub net");
        CareTestBase.page().loadCustomerInHubNet(owsActions.customerNo);
        String subscriptionNo1 = BaseTest.getSubscriptionNumberBySubscriptionNumber("Mobile NC 1");

        test.get().info("Step 4: Access detail screen");
        MenuPage.LeftMenuPage.getInstance().clickDetailsLink();

        test.get().info("Step 5: Change Email and bill notification");
        String email = Config.getProp("emailUsername");
        CareTestBase.page().clickEditBtnBySection("Address Information");
        DetailsContentPage.AddressInformationPage.getInstance().changeEmail(email);
        CareTestBase.page().clickApplyBtn();

        CareTestBase.page().clickEditBtnBySection("Billing Information");
        DetailsContentPage.BillingInformationSectionPage.getInstance().changeBillNotification("Email");
        CareTestBase.page().clickApplyBtn();

        test.get().info("Step 6: go to self care setting  ");
        MenuPage.LeftMenuPage.getInstance().clickSelfCareSetting();
        SelfCareSettingContentPage.SelfCareSettingSection.getInstance().clickFirstRow();

        test.get().info("Step 7: Click reset password ");
        SelfCareSettingContentPage.SelfCareSettingSection.getInstance().clickResetPasswordBtn();
        SelfCareSettingContentPage.SelfCareSettingSection.getInstance().acceptComfirmDialog();

        test.get().info("Step 8 : get temporary password ");
        EmailHelper.getInstance().waitEmailByFolderNameAndEmailSubject("TescoMobilePayMonthly", "Password reset", 120);
        String temporaryPassEmail = EmailHelper.getInstance().extractPasswordEmailByFolderNameAndEmailSubject("TescoMobilePayMonthly", "Password reset");
        String link = EmailHelper.getInstance().extractLinkEmailByFolderNameAndEmailSubject("TescoMobilePayMonthly", "Password reset");

        test.get().info("Step 9: login selfcare via the link and password in email");
        SelfCareTestBase.page().LoginIntoSelfCarePageByChangePasswordLink(owsActions.username, temporaryPassEmail, owsActions.customerNo, link);

        test.get().info("Step 10: verify no sms is created in the hitransactioneven table");
        List<String> smsList = CommonActions.getNumberSMSCreatedInHitransactionEventTable(subscriptionNo1);
        Assert.assertEquals(smsList.size(), 0);

        test.get().info("Step 11 : delete all email 'Have you logged in to My Account?' in folder TescoMobilePayMonthly");
        EmailHelper.getInstance().deleteAllEmailByFolderNameAndEmailSubject("TescoMobilePayMonthly", "Have you logged");
        EmailHelper.getInstance().deleteAllEmailByFolderNameAndEmailSubject("TescoMobilePayMonthly", "Change of your Pay Monthly Price Plans");

        test.get().info("Step 12 : set up the new password for user");
        MyPasswordPage.getInstance().updateNewPassword(temporaryPassEmail, "password1");
        SelfCareTestBase.page().verifyMyPersonalInformationPageIsDisplayed();

        test.get().info("Step 13 : Verify the second email format ");
        EmailHelper.getInstance().waitEmailByFolderNameAndEmailSubject("TescoMobilePayMonthly", "Password reset", 120);
        String secondEmailBody = EmailHelper.getInstance().extractPasswordEmailByFolderNameAndEmailSubject("TescoMobilePayMonthly", "Password reset");
        verifySecondEmail(expectedFile, actualFile);

        test.get().info("Step 14 : load user in hub net and go to service order ");
        CareTestBase.page().loadCustomerInHubNet(owsActions.customerNo);
        MenuPage.LeftMenuPage.getInstance().clickServiceOrdersLink();

        test.get().info("Step 15: Verify expected SO is generated for customer");
        MenuPage.RightMenuPage.getInstance().clickRefreshLink();
        MenuPage.LeftMenuPage.getInstance().clickServiceOrdersLink();
        ServiceOrdersContentPage.getInstance().clickServiceOrderByType("Ad-hoc SMS Messages");
        String description = String.format("Password Change (%s)", subscriptionNo1);
        int event = TasksContentPage.TaskPage.EventsGridSectionPage.getInstance().getNumberOfEventsByEvent(EventEntity.dataForEventChangePassword(description, "Created", "Batch"));
        Assert.assertEquals(event, 1);

        test.get().info("Step 16: verify  sms is created in the hitransactioneven table");
        smsList = CommonActions.getNumberSMSCreatedInHitransactionEventTable(subscriptionNo1);
        Assert.assertEquals(smsList.size(), 1);
        Assert.assertTrue(smsList.get(0).contains("Tesco Mobile: Your password for your Pay monthly online account has been changed successfully"));

        test.get().info("Step 17: Run request job");
        RemoteJobHelper.getInstance().runSMSRequestJob();

        test.get().info("Step 18:Download GRG SMS file");
        String pathFile = downloadGRGSMSFile();

        test.get().info("Step 19: Verify GRG temporary_password_is_not_recorded");
        verifyGRGTemporaryPasswordIsNotRecorded(pathFile);
    }

    @DataProvider(name = "browsername")
    public Object[][] dataProviderMethod() {
        return new Object[][]{{"gc"}, {"ff"}, {"ie"}};
    }

    public void verifySecondEmail(String expectedFile, String actualFile) {
        Common.deleteFile(actualFile);
        Common.waitForFileDelete(120, actualFile);
        EmailHelper.getInstance().convertEmailToFile("TescoMobilePayMonthly", "Change of your Pay Monthly Price Plans", actualFile);
        Common.waitForFileExist(120, actualFile);
        List result = Common.compareFiles(expectedFile, actualFile, "Dear ");
        Assert.assertEquals(result.size(), 0);

    }


    public void verifyGRGTemporaryPasswordIsNotRecorded(String fileName) {
        File file = new File("fileName");
        String expectedResult = fileName.split("_")[4];
        String fileResult = Common.readFile(fileName);
        Assert.assertTrue(fileResult.contains(expectedResult));
        Assert.assertFalse(fileResult.contains("password1"));
    }

    public static String downloadGRGSMSFile() {
        String sql = "SELECT h.OrigTransactionIdent" +
                " FROM HITransactionDefinition d, HITransaction h" +
                " WHERE  d.TransactionKeyRef LIKE 'SMS%REQUEST' " +
                " AND    d.HITransactionDefinitionID = h.HITransactionDefinitionID" +
                " ORDER BY h.transactionDate DESC";
        List<String> GRGSMSFileName = OracleDB.SetToNonOEDatabase().executeQueryReturnListString(sql);
        String firstResult = GRGSMSFileName.get(0);
        String value = firstResult.substring(firstResult.indexOf("=") + 1).replace("}", "");
        String ftpFilePath = Config.getProp("cdrFolder");
        ftpFilePath = ftpFilePath.replace("Feed/a2aInterface/fileinbox", "ftp/tesgrg/fileoutbox");
        String localPath = Common.getFolderLogFilePath();
        FTPHelper.getInstance().downLoadFromDisk(ftpFilePath, value, localPath);
        Log.info("TM_HUB_SMSRQST file:" + localPath);
        return localPath + value;
    }
}
