package suite.regression.APIs.RestApis;

import framework.config.Config;
import framework.utils.Log;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import suite.regression.APIs.RestApis.ApisBaseTest;


public class Login extends ApisBaseTest {

    @Test(enabled = true, description = "Test login Apis")
    public void verifyGetAccessToken() {

        //define param
        Response response =
                RestAssured.given().auth().preemptive().basic(Config.getProp("clientId"), Config.getProp("clientSecret"))
                        .formParam("grant_type", "password")
                        .formParam("username", Config.getProp("apiUserName"))
                        .formParam("password", Config.getProp("apiPassword"))
                        .when()
                        .post(Config.getProp("apiUrl")+Config.getProp("loginEndPoint"));

        //assert response
        Log.info(response.getBody().asString());
        Assert.assertEquals(response.getStatusCode(), 200);
        JSONObject jsonObject = new JSONObject(response.getBody().asString());
        accessToken = jsonObject.get("access_token").toString();
        Assert.assertFalse(accessToken.isEmpty());
        String tokenType = jsonObject.get("token_type").toString();
        Assert.assertFalse(tokenType.isEmpty());

    }

    @Test(enabled = true, description = "Test login Apis missing username")
    public void verifyGetAccessTokenMissingUserName() {

        //define param
        Response response =
                RestAssured.given().auth().preemptive().basic(Config.getProp("clientId"), Config.getProp("clientSecret"))
                        .formParam("grant_type", "password")
                        .formParam("password", Config.getProp("apiPassword"))
                        .when()
                        .post(Config.getProp("apiUrl")+Config.getProp("loginEndPoint"));

        //assert response
        Log.info(response.getBody().asString());
        Assert.assertEquals(response.getStatusCode(), 400);
        JSONObject jsonObject = new JSONObject(response.getBody().asString());

        String error = jsonObject.get("error").toString();
        Assert.assertEquals(error,"invalid_request");
        String errorMssg = jsonObject.get("error_description").toString();
        Assert.assertEquals(errorMssg,"Missing parameters. \"username\" and \"password\" required");
    }

    @Test(enabled = true, description = "Test login Apis missing password")
    public void verifyGetAccessTokenMissingPassword() {

        //define param
        Response response =
                RestAssured.given().auth().preemptive().basic(Config.getProp("clientId"), Config.getProp("clientSecret"))
                        .formParam("grant_type", "password")
                        .formParam("username", Config.getProp("apiUserName"))
                        .when()
                        .post(Config.getProp("apiUrl")+Config.getProp("loginEndPoint"));

        //assert response
        Log.info(response.getBody().asString());
        Assert.assertEquals(response.getStatusCode(), 400);
        JSONObject jsonObject = new JSONObject(response.getBody().asString());

        String error = jsonObject.get("error").toString();
        Assert.assertEquals(error,"invalid_request");
        String errorMssg = jsonObject.get("error_description").toString();
        Assert.assertEquals(errorMssg,"Missing parameters. \"username\" and \"password\" required");
    }

    @Test(enabled = true, description = "Test login Apis missing password")
    public void verifyGetAccessTokenMissingGrantType() {

        //define param
        Response response =
                RestAssured.given().auth().preemptive().basic(Config.getProp("clientId"), Config.getProp("clientSecret"))
                        .formParam("password", Config.getProp("apiPassword"))
                        .formParam("username", Config.getProp("apiUserName"))
                        .when()
                        .post(Config.getProp("apiUrl")+Config.getProp("loginEndPoint"));

        //assert response
        Log.info(response.getBody().asString());
        Assert.assertEquals(response.getStatusCode(), 400);
        JSONObject jsonObject = new JSONObject(response.getBody().asString());

        String error = jsonObject.get("error").toString();
        Assert.assertEquals(error,"invalid_request");
        String errorMssg = jsonObject.get("error_description").toString();
        Assert.assertEquals(errorMssg,"Invalid grant_type parameter or parameter missing");
    }


    @Test(enabled = true, description = "Test login Apis missing client id")
    public void verifyGetAccessTokenMissingClientID() {

        //define param
        Response response =
                RestAssured.given().auth().preemptive().basic("", Config.getProp("clientSecret"))
                        .formParam("grant_type", "password")
                        .formParam("username", Config.getProp("apiUserName"))
                        .formParam("password", Config.getProp("apiPassword"))
                        .when()
                        .post(Config.getProp("apiUrl")+Config.getProp("loginEndPoint"));

        //assert response
        Log.info(response.getBody().asString());
        Assert.assertEquals(response.getStatusCode(), 400);
        JSONObject jsonObject = new JSONObject(response.getBody().asString());

        String error = jsonObject.get("error").toString();
        Assert.assertEquals(error,"invalid_client");
        String errorMssg = jsonObject.get("error_description").toString();
        Assert.assertEquals(errorMssg,"Client id was not found in the headers or body");

    }

    @Test(enabled = true, description = "Test login Apis missing client secret")
    public void verifyGetAccessTokenMissingClientSecret() {

        //define param
        Response response =
                RestAssured.given().auth().preemptive().basic(Config.getProp("clientId"), "")
                        .formParam("grant_type", "password")
                        .formParam("username", Config.getProp("apiUserName"))
                        .formParam("password", Config.getProp("apiPassword"))
                        .when()
                        .post(Config.getProp("apiUrl")+Config.getProp("loginEndPoint"));

        //assert response
        Log.info(response.getBody().asString());
        Assert.assertEquals(response.getStatusCode(), 400);
        JSONObject jsonObject = new JSONObject(response.getBody().asString());//
        String error = jsonObject.get("error").toString();
        Assert.assertEquals(error,"invalid_client");
        String errorMssg = jsonObject.get("error_description").toString();
        Assert.assertEquals(errorMssg,"The client credentials are invalid");

    }

}
