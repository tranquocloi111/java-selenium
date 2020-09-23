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

public class GetLessionDetail extends ApisBaseTest {

    @Test(enabled = true, description = "Test get course detail Apis")
    public void getLessionDetail() {

        Response response =
                RestAssured.given().config(RestAssured.config().sslConfig(new SSLConfig().allowAllHostnames()))
                        .auth().preemptive().basic(Config.getProp("kynabizUsername"),Config.getProp("kynabizPassword"))
                        .formParam("expand","sections,documents")
                        .when()
                        .get(Config.getProp("kynabizApis") + Config.getProp("getLessionDetailEndPoint"));

        //assert response
        Log.info(response.getBody().asString());
        Assert.assertEquals(response.getStatusCode(), 200);
        JsonHelper.compareTwoJsonObject(Config.getProp("lessionDetail"),response.getBody().asString());

    }
}
