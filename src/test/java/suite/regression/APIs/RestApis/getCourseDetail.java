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

public class getCourseDetail extends ApisBaseTest {

    /*
    The access token for attachment file and course is dynamic-> lead to failed case
    * */

    @Test(enabled = true, description = "Test get employee course Apis")
    public void verifyGetCourseDetails() {

        String inviteId=getInviteId();
        Response response =
                RestAssured.given().auth().oauth2(accessToken).formParam("invite_id",inviteId)
                        .when()
                        .get(Config.getProp("apiUrl") + Config.getProp("getCourseDetail"));

        //assert response
        Log.info(response.getBody().asString());
        Assert.assertEquals(response.getStatusCode(), 200);
        JsonHelper.compareValueNode(response.getBody().asString(),"course_name","30 bí quyết quản lý thời gian hiệu quả");


        Assert.assertEquals(response.path("data.attach_file[0].file_name").toString(),"168 Planning sheet");
        Assert.assertEquals(response.path("data.attach_file[0].url").toString(),"https://media.kyna.vn/uploads/courses/784/docs/document-1504583302.pdf");
        Assert.assertEquals(response.path("data.chapter[0].chapter_name").toString(),"Giới Thiệu");
        Assert.assertEquals(response.path("data.chapter[1].chapter_name").toString(),"PHẦN 1: Hiểu đúng về quản trị thời gian");
        Assert.assertEquals(response.path("data.chapter[1].number_lesson").toString(),"6");

    }



    @Test(enabled = true, description = "Test get employee course Apis missing access token")
    public void verifyGetCourseDetailsMissingAccessToken() {

        String inviteId=getInviteId().replace("{","").replace("}","").substring(3);
        Response response =
                RestAssured.given().auth().oauth2("").formParam("invite_id",inviteId)
                        .when()
                        .get(Config.getProp("apiUrl") + Config.getProp("getCourseDetail"));

        //assert response
        Log.info(response.getBody().asString());
        Assert.assertEquals(response.getStatusCode(), 401);
        JSONObject jsonObject = new JSONObject(response.getBody().asString());
        String error = jsonObject.get("error").toString();
        Assert.assertEquals(error,"access_denied");
        String errorMssg = jsonObject.get("error_description").toString();
        Assert.assertEquals(errorMssg,"OAuth2 authentication required");
    }

    @Test(enabled = true, description = "Test get employee course Apis invalid access token")
    public void verifyGetCourseDetailsInvalidAccessToken() {

        String inviteId=getInviteId().replace("{","").replace("}","").substring(3);
        Response response =
                RestAssured.given().auth().oauth2("23342rnkbjbjbvwbrvw").formParam("invite_id",inviteId)
                        .when()
                        .get(Config.getProp("apiUrl") + Config.getProp("getCourseDetail"));

        //assert response
        Log.info(response.getBody().asString());
        Assert.assertEquals(response.getStatusCode(), 401);
        JSONObject jsonObject = new JSONObject(response.getBody().asString());
        String error = jsonObject.get("error").toString();
        Assert.assertEquals(error,"invalid_grant");
        String errorMssg = jsonObject.get("error_description").toString();
        Assert.assertEquals(errorMssg,"The access token provided is invalid.");
    }

    @Test(enabled = true, description = "Test get employee course Apis expired access token")
    public void verifyGetCourseDetailsExpiredAccessToken() {

        String inviteId=getInviteId().replace("{","").replace("}","").substring(3);
        Response response =
                RestAssured.given().auth().oauth2(Config.getProp("expireAccesstoken")).formParam("invite_id",inviteId)
                        .when()
                        .get(Config.getProp("apiUrl") + Config.getProp("getCourseDetail"));

        //assert response
        Log.info(response.getBody().asString());
        Assert.assertEquals(response.getStatusCode(), 401);
        JSONObject jsonObject = new JSONObject(response.getBody().asString());
        String error = jsonObject.get("error").toString();
        Assert.assertEquals(error,"invalid_grant");
        String errorMssg = jsonObject.get("error_description").toString();
        Assert.assertEquals(errorMssg,"The access token provided has expired.");
    }

    @Test(enabled = true, description = "Test get employee course Apis with missing invite id")
    public void verifyGetCourseDetailsWithMissingInviteID() {

        String inviteId=getInviteId().replace("{","").replace("}","").substring(3);
        Response response =
                RestAssured.given().auth().oauth2(accessToken)
                        .when()
                        .get(Config.getProp("apiUrl") + Config.getProp("getCourseDetail"));

        //assert response
        Log.info(response.getBody().asString());
        Log.info(response.getBody().asString());
        Assert.assertEquals(response.getStatusCode(), 400);
        Assert.assertTrue(JsonHelper.compareTwoJsonFiles(Config.getProp("missingInviteId"), response.getBody().asString()));
    }

    @Test(enabled = true, description = "Test get employee course Apis with Invalid invite id")
    public void verifyGetCourseDetailsWithInvalidInviteID() {

        String inviteId=getInviteId();
        Response response =
                RestAssured.given().auth().oauth2(accessToken).formParam("invite_id",33)
                        .when()
                        .get(Config.getProp("apiUrl") + Config.getProp("getCourseDetail"));

        //assert response
        Log.info(response.getBody().asString());
        Log.info(response.getBody().asString());
        Assert.assertEquals(response.getStatusCode(), 400);
        Assert.assertTrue(JsonHelper.compareTwoJsonFiles(Config.getProp("invalidInviteId"), response.getBody().asString()));
    }

}
