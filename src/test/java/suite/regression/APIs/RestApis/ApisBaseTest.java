package suite.regression.APIs.RestApis;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import framework.config.Config;
import framework.report.elasticsearch.ExecutionListener;
import framework.utils.Log;
import framework.wdm.DriverFactory;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import logic.business.db.MySQLDb;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.net.MalformedURLException;

//import com.aventstack.extentreports.reporter.KlovReporter;
//import com.aventstack.extentreports.reporter.configuration.ChartLocation;

@Listeners(ExecutionListener.class)
public class ApisBaseTest {
    public static String accessToken = null;

    //region Hooks
    @BeforeSuite
    public void beforeSuite() {
        setUpReport();
        Config.loadEnvInfoToQueue();
        accessToken = getAccessToken();
    }

    @AfterSuite
    public void afterSuite() {

        extent.flush();
    }

    @BeforeMethod
    public void beforeMethod(Method m) throws MalformedURLException {
        test.set(extent.createTest(m.getName()));
        Config.loadEnvInfoToQueue();
    }

    @AfterMethod
    public void afterMethod(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            test.get().fail(MarkupHelper.createLabel("Test Case : " + result.getName().split("_")[0] + " FAILED ", ExtentColor.RED));
            test.get().fail(result.getThrowable());
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            test.get().pass(MarkupHelper.createLabel("Test Case : " + result.getName().split("_")[0] + " PASSED ", ExtentColor.GREEN));
        } else {
            test.get().skip(MarkupHelper.createLabel(result.getName() + " SKIPPED ", ExtentColor.ORANGE));
            test.get().skip(result.getThrowable());
        }

        Config.returnProp();
//        WdManager.dismissWD();
        DriverFactory.getInstance().removeDriver();
    }


    //endregion

    //region Report
    protected static ExtentReports extent;
    protected static ThreadLocal<ExtentTest> test = new ThreadLocal();

    private void setUpReport() {
        //HTML
        ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter("test-output//VLB_Automation_Report_APIS.html");
//        htmlReporter.config().setTestViewChartLocation(ChartLocation.BOTTOM);
//        htmlReporter.config().setChartVisibilityOnOpen(true);
        htmlReporter.config().setTheme(Theme.DARK);
        htmlReporter.config().setDocumentTitle("VietNamwork Learning - HTML Test Report");
        htmlReporter.config().setEncoding("utf-8");
        htmlReporter.config().setReportName("VLB - HTML Test Report");

        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
    }

    public String getAccessToken() {

        //define param
        Response response =
                RestAssured.given().auth().preemptive().basic(Config.getProp("clientId"), Config.getProp("clientSecret"))
                        .formParam("grant_type", "password")
                        .formParam("username", Config.getProp("apiUserName"))
                        .formParam("password", Config.getProp("apiPassword"))
                        .when()
                        .post(Config.getProp("apiUrl") + Config.getProp("loginEndPoint"));

        //assert response
        Log.info(response.getBody().asString());
        Assert.assertEquals(response.getStatusCode(), 200);
        JSONObject jsonObject = new JSONObject(response.getBody().asString());
        accessToken = jsonObject.get("access_token").toString();
        Assert.assertFalse(accessToken.isEmpty());
        return accessToken;

    }


    protected String getInviteId() {
        String email = Config.getProp("apiUserName");
        String sql = String.format("select inv.id from tbl_user as us, tbl_course_invitation as inv where us.username='%s' and us.id=inv.employee_id and inv.status <> '0' order by inv.id desc", email);
        return MySQLDb.getMySqlInstance().executeQueryReturnListString(sql).get(0)
                .replace("id", "")
                .replace("{", "")
                .replace("}", "")
                .replace("=", "");
    }
    //endregion

}
