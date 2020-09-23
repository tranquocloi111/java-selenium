package suite.regression.APIs.RestApis.KynaBizApis;

import framework.config.Config;
import framework.utils.Log;
import io.restassured.RestAssured;
import io.restassured.config.SSLConfig;
import io.restassured.response.Response;
import logic.business.helper.JsonHelper;
import org.testng.Assert;
import org.testng.annotations.Test;
import suite.regression.APIs.RestApis.ApisBaseTest;

public class GetCombos extends ApisBaseTest {

    @Test(enabled = true, description = "Test get quiz question Apis")
    public void getCombos() {

        Response response =
                RestAssured.given().config(RestAssured.config().sslConfig(new SSLConfig().allowAllHostnames()))
                        .auth().preemptive().basic(Config.getProp("kynabizUsername"),Config.getProp("kynabizPassword"))
                        .formParam("page","0")
                        .formParam("per-page","10")
                        .when()
                        .get(Config.getProp("kynabizApis") + Config.getProp("getComboEndPoint"));

        //assert response
        Log.info(response.getBody().asString());
        Assert.assertEquals(response.getStatusCode(), 200);
        JsonHelper.compareTwoJsonObject(Config.getProp("combos"),response.getBody().asString());

    }
}
