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

public class GetEmployeeCourse extends ApisBaseTest {



    /*
    The quanity of date is descrease per day -> unmatch with expected
    * object */
    @Test(enabled = true, description = "Test get employee course Apis")
    public void verifyGetEmployeeCourse() {

        //define param
        Response response =
                RestAssured.given().auth().oauth2(accessToken)
                        .when()
                        .get(Config.getProp("apiUrl")+Config.getProp("getEmployeeCourse"));

        //assert response
        Log.info(response.getBody().asString());
        Assert.assertEquals(response.getStatusCode(), 200);
//        Assert.assertTrue(JsonHelper.compareTwoJsonFiles(Config.getProp("employeeCourse"), response.getBody().asString()));
        JsonHelper.verifyValueOfNode(response.getBody().asString(),"title","\"Kỹ năng công việc\"","1");
        JsonHelper.verifyValueOfNode(response.getBody().asString(),"title","\"Kĩ năng lãnh đạo\"","2");
        JsonHelper.verifyValueOfNode(response.getBody().asString(),"title","\"Phát triển cá nhân\"","3");
        JsonHelper.verifyValueOfNode(response.getBody().asString(),"title","\"Giao tiếp-Thuyết trình\"","4");

        Assert.assertEquals(response.path("data.1.data[2].id").toString(),"1040");
        Assert.assertEquals(response.path("data.1.data[2].name"),"30 bí quyết quản lý thời gian hiệu quả");
        Assert.assertEquals(response.path("data.1.data[2].author"),"Huỳnh Ngọc Minh");
        Assert.assertTrue(response.path("data.1.data[2].image_url").toString().contains("course/2020/03/20/5e7496d964c52.jpg?"));
        Assert.assertEquals(response.path("data.1.data[2].start_date"),"03/04/2020 11:30 AM");

    }


    @Test(enabled = true, description = "Test get employee course  Apis with missing invalid access token")
    public void verifyGetEmployeeCourseInvalidAccessToken() {

        //define param
        Response response =
                RestAssured.given().auth().oauth2("accessToken")
                        .when()
                        .get(Config.getProp("apiUrl")+Config.getProp("getEmployeeCourse"));

        //assert response
        Log.info(response.getBody().asString());
        Assert.assertEquals(response.getStatusCode(), 401);
        JSONObject jsonObject = new JSONObject(response.getBody().asString());
        String error = jsonObject.get("error").toString();
        Assert.assertEquals(error,"invalid_grant");
        String errorMssg = jsonObject.get("error_description").toString();
        Assert.assertEquals(errorMssg,"The access token provided is invalid.");
    }


    @Test(enabled = true, description = "Test get employee course Apis with missing access token")
    public void verifyGetEmployeeCourseMissingAccessToken() {

        //define param
        Response response =
                RestAssured.given().auth().oauth2("")
                        .when()
                        .get(Config.getProp("apiUrl")+Config.getProp("getEmployeeCourse"));

        //assert response
        Log.info(response.getBody().asString());
        Assert.assertEquals(response.getStatusCode(), 401);
        JSONObject jsonObject = new JSONObject(response.getBody().asString());
        String error = jsonObject.get("error").toString();
        Assert.assertEquals(error,"access_denied");
        String errorMssg = jsonObject.get("error_description").toString();
        Assert.assertEquals(errorMssg,"OAuth2 authentication required");
    }

    @Test(enabled = true, description = "Test get employee course Apis with expired access token")
    public void verifyGetEmployeeCourseExpiredAccessToken() {

        //define param
        Response response =
                RestAssured.given().auth().oauth2(Config.getProp("expireAccesstoken"))
                        .when()
                        .get(Config.getProp("apiUrl")+Config.getProp("getEmployeeCourse"));

        //assert response
        Log.info(response.getBody().asString());
        Assert.assertEquals(response.getStatusCode(), 401);
        JSONObject jsonObject = new JSONObject(response.getBody().asString());
        String error = jsonObject.get("error").toString();
        Assert.assertEquals(error,"invalid_grant");
        String errorMssg = jsonObject.get("error_description").toString();
        Assert.assertEquals(errorMssg,"The access token provided has expired.");
    }
}
