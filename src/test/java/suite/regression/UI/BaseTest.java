package suite.regression.UI;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
//import com.aventstack.extentreports.reporter.KlovReporter;
//import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;
import framework.config.Config;
import framework.report.elasticsearch.ExecutionListener;
import framework.wdm.DriverFactory;
import framework.wdm.WDFactory;
import framework.wdm.WdManager;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Listeners(ExecutionListener.class)
public class BaseTest {

    //region Hooks
    @BeforeSuite
    public void beforeSuite() {
        setUpReport();
    }

    @AfterSuite
    public void afterSuite() {
        extent.flush();
    }

    @BeforeMethod
    public  void beforeMethod(Method m) throws MalformedURLException {
        test.set(extent.createTest(m.getName()));
        Config.loadEnvInfoToQueue();
//        switch (Config.getProp("browser")){
//            case "gc":
//                WdManager.set(WDFactory.remote(new URL("http://10.50.172.189:4444/wd/hub"), DesiredCapabilities.chrome()));
//                break;
//            case "ff":
//                WdManager.set(WDFactory.remote(new URL("http://localhost:4444/wd/hub"), DesiredCapabilities.firefox()));
//                break;
//            case "ie" :
//                WdManager.set(WDFactory.remote(new URL("http://localhost:4444/wd/hub"), DesiredCapabilities.internetExplorer()));
//                break;
//        }
//        WDFactory.getConfig().setDriverVersion("81");
//        WdManager.set(WDFactory.initBrowser(Config.getProp("browser")));
//        WdManager.get().manage().window().maximize();
//        WdManager.get().get(Config.getProp("appUrl"));
        DriverFactory.getInstance().getDriver().manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        DriverFactory.getInstance().getDriver().manage().window().maximize();
        DriverFactory.getInstance().getDriver().get(Config.getProp("appUrl"));

    }

    @AfterMethod
    public void afterMethod(ITestResult result) {
        if(result.getStatus() == ITestResult.FAILURE) {
            test.get().fail(MarkupHelper.createLabel("Test Case : " + result.getName().split("_")[0] + " FAILED ", ExtentColor.RED));
            test.get().fail(result.getThrowable());
        }
        else if(result.getStatus() == ITestResult.SUCCESS) {
            test.get().pass(MarkupHelper.createLabel("Test Case : " + result.getName().split("_")[0]+ " PASSED ", ExtentColor.GREEN));
        }
        else {
            test.get().skip(MarkupHelper.createLabel(result.getName()+" SKIPPED ", ExtentColor.ORANGE));
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
        ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter("test-output//VLB_Automation_Report.html");
//        htmlReporter.config().setTestViewChartLocation(ChartLocation.BOTTOM);
//        htmlReporter.config().setChartVisibilityOnOpen(true);
        htmlReporter.config().setTheme(Theme.DARK);
        htmlReporter.config().setDocumentTitle("VietNamwork Learning - HTML Test Report");
        htmlReporter.config().setEncoding("utf-8");
        htmlReporter.config().setReportName("VLB - HTML Test Report");

        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);


    }
    //endregion

    //Common Function

    protected void loadPageByUrl(String url)
    {
        DriverFactory.getInstance().getDriver().get(url);
    }
    

    //end region

}
