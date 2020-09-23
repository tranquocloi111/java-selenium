package suite.regression.APIs.RestApis;

import framework.config.Config;
import framework.utils.Log;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import logic.business.helper.JsonHelper;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import suite.regression.APIs.RestApis.ApisBaseTest;

public class GetUserProfile extends ApisBaseTest {


    @Test(enabled = true, description = "Test get employee course Apis")
    public void verifyGetUserProfile() {

        //define param
        Response response =
                RestAssured.given().auth().oauth2(accessToken)
                        .when()
                        .get(Config.getProp("apiUrl") + Config.getProp("getUserProfile"));

        //assert response
        Log.info(response.getBody().asString());
        Assert.assertEquals(response.getStatusCode(), 200);
//        Assert.assertTrue(JsonHelper.compareTwoJsonFiles(Config.getProp("userProfileJson"), response.getBody().asString()));
        JsonHelper.verifyValueOfNode(response.getBody().asString(),"full_name","\"Trần Quốc Lợi\"","data");
        JsonHelper.verifyValueOfNodeContainsExpectValue(response.getBody().asString(),"avatar","http://staging.vietnamworkslearning.com/build/images/vnwlLogo.png?timestamp=","data");
        JsonHelper.verifyValueOfNode(response.getBody().asString(),"course_complete","\"03\"","data");
        JsonHelper.verifyValueOfNode(response.getBody().asString(),"course_not_learn","\"01\"","data");
        JsonHelper.verifyValueOfNode(response.getBody().asString(),"roles","[\"ROLE_EMPLOYEE\"]","data");

    }


    @Test(enabled = true, description = "Test get employee course  Apis with missing invalid access token")
    public void verifyGetUserProfileInvalidAccessToken() {

        //define param
        Response response =
                RestAssured.given().auth().oauth2("accessToken")
                        .when()
                        .get(Config.getProp("apiUrl") + Config.getProp("getUserProfile"));

        //assert response
        Log.info(response.getBody().asString());
        Assert.assertEquals(response.getStatusCode(), 401);
        JSONObject jsonObject = new JSONObject(response.getBody().asString());
        String error = jsonObject.get("error").toString();
        Assert.assertEquals(error, "invalid_grant");
        String errorMssg = jsonObject.get("error_description").toString();
        Assert.assertEquals(errorMssg, "The access token provided is invalid.");
    }


    @Test(enabled = true, description = "Test get employee course Apis with missing access token")
    public void verifyGetUserProfileMissingAccessToken() {

        //define param
        Response response =
                RestAssured.given().auth().oauth2("")
                        .when()
                        .get(Config.getProp("apiUrl") + Config.getProp("getUserProfile"));

        //assert response
        Log.info(response.getBody().asString());
        Assert.assertEquals(response.getStatusCode(), 401);
        JSONObject jsonObject = new JSONObject(response.getBody().asString());
        String error = jsonObject.get("error").toString();
        Assert.assertEquals(error, "access_denied");
        String errorMssg = jsonObject.get("error_description").toString();
        Assert.assertEquals(errorMssg, "OAuth2 authentication required");
    }

    @Test(enabled = true, description = "Test get employee course Apis with expired access token")
    public void verifyGetUserProfileExpiredAccessToken() {

        //define param
        Response response =
                RestAssured.given().auth().oauth2(Config.getProp("expireAccesstoken"))
                        .when()
                        .get(Config.getProp("apiUrl") + Config.getProp("getUserProfile"));

        //assert response
        Log.info(response.getBody().asString());
        Assert.assertEquals(response.getStatusCode(), 401);
        JSONObject jsonObject = new JSONObject(response.getBody().asString());
        String error = jsonObject.get("error").toString();
        Assert.assertEquals(error, "invalid_grant");
        String errorMssg = jsonObject.get("error_description").toString();
        Assert.assertEquals(errorMssg, "The access token provided has expired.");
    }
}
