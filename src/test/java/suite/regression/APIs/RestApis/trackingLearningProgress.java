package suite.regression.APIs.RestApis;

import framework.config.Config;
import framework.utils.Log;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import logic.business.helper.JsonHelper;
import logic.utils.Common;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import suite.regression.APIs.RestApis.ApisBaseTest;

public class trackingLearningProgress extends ApisBaseTest {


    @Test(enabled = true, description = "Test get employee course Apis")
    public void gettrackingLearningProgress() {
        String body = "{\n" +
                "        \"invite_id\": 1040,\n" +
                "        \"current_time\": 1,\n" +
                "        \"lesson_id\": 1\n" +
                "}";

        //define param
        Response response =
                RestAssured.given().auth().oauth2(accessToken)
                        .body(body)
                        .when()
                        .post(Config.getProp("apiUrl") + Config.getProp("getTrackingLearningProgress"));

        //assert response
        Log.info(response.getBody().asString());
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(JsonHelper.compareTwoJsonFiles(Config.getProp("trackingLearningProgress"), response.getBody().asString()));
    }


    @Test(enabled = true, description = "verify error message when the course is completed")
    public void verifyErrorMessageWhenTheCoureIsCompleted() {
        String body = "{\n" +
                "        \"invite_id\": 151,\n" +
                "        \"current_time\": 1,\n" +
                "        \"lesson_id\": 90\n" +
                "}";
        //define param
        Response response =
                RestAssured.given().auth().oauth2(accessToken)
                        .body(body)
                        .when()
                        .post(Config.getProp("apiUrl") + Config.getProp("getTrackingLearningProgress"));


        //assert response
        Log.info(response.getBody().asString());
        Assert.assertEquals(response.getStatusCode(), 400);
        Assert.assertTrue(JsonHelper.compareTwoJsonFiles(Config.getProp("theCourseIsCompleted"), response.getBody().asString()));
    }

    @Test(enabled = true, description = "verify error message when missing access token")
    public void verifyTrackingMissingAccessToken() {
        String body = Common.readFile("src/test/resources/Json/Production/TrackingLearningProgress");
        //define param
        Response response =
                RestAssured.given().auth().oauth2("")
                        .body(body)
                        .when()
                        .post(Config.getProp("apiUrl") + Config.getProp("getTrackingLearningProgress"));


        //assert response
        Log.info(response.getBody().asString());
        Assert.assertEquals(response.getStatusCode(), 401);
        JSONObject jsonObject = new JSONObject(response.getBody().asString());
        String error = jsonObject.get("error").toString();
        Assert.assertEquals(error, "access_denied");
        String errorMssg = jsonObject.get("error_description").toString();
        Assert.assertEquals(errorMssg, "OAuth2 authentication required");
    }


}
