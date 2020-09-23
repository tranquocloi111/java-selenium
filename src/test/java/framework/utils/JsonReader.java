package framework.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.mongodb.util.JSON;
import com.sun.tools.javac.util.List;
import logic.utils.Common;
import org.json.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.testng.Assert;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.stream.Collectors;

public class JsonReader {

    public static JSONObject readJson(String filename) {
        try {
            FileReader reader = new FileReader(filename);
            String object = Common.readFile(filename);
            JSONParser jsonParser = new JSONParser();
            return (JSONObject) jsonParser.parse(object);
        } catch (Exception ex) {
            Log.error(ex.getMessage());
        }
        return null;
    }

    public static boolean compareTwoJsonFormat(String expectedResult, String actualResult) {
        ObjectMapper mapper = new ObjectMapper();
        boolean flag = false;
        try {
            Assert.assertEquals(mapper.readTree(expectedResult), mapper.readTree(actualResult));
            flag = true;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return flag;
    }

    public static boolean compareTwoJsonFormatByJsonNode(String expectedResult, String actualResult) {
        boolean flag = false;
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode expected = mapper.readTree(expectedResult);
            JsonNode actual = mapper.readTree(actualResult);
            Assert.assertEquals(mapper.readTree(expectedResult), mapper.readTree(actualResult));
            flag = true;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return flag;
    }

    public static boolean compareTwoJsonObject(String expectedFormatFile, String actualObject) {
        String expectedObject = Common.readFile(expectedFormatFile);
        assert expectedObject != null;
        return compareTwoJsonFormat(expectedObject, actualObject);
    }

    public void setJsonValue(String body, String key, String value) {
        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(body);
//            jsonObject.keySet(key,value)
        } catch (Exception ex) {
            Log.info(ex.getMessage());
        }

    }

    public static void compareValueNode(String body,String node, String expectedValue){
        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(body);
            Object jsonArray =jsonObject.get("data");
            Assert.assertTrue(((JSONObject) jsonArray).containsValue(expectedValue));
//            jsonObject.keySet(key,value)
        } catch (Exception ex) {
            Log.info(ex.getMessage());
        }
    }
    public static boolean verifyNodeValue(String body,String node , String expectedResult,String parent) {
        ObjectMapper mapper = new ObjectMapper();
        boolean flag = false;
        try {
            Assert.assertEquals(mapper.readTree(body).findPath(parent).findPath(node).toString(),expectedResult);
            flag = true;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return flag;
    }
    public static boolean verifyNodeValueContainsExpectValue(String body,String node , String expectedResult,String parent) {
        ObjectMapper mapper = new ObjectMapper();
        boolean flag = false;
        try {
            String actualResult=mapper.readTree(body).findPath(parent).findPath(node).toString();
            Assert.assertEquals(actualResult.substring(1,actualResult.indexOf("=")+1),expectedResult);
            flag = true;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return flag;
    }



    public static void main(String[] args) throws Exception {

        JSONObject jsonObject1 = null;
        jsonObject1 = readJson("src/test/resources/Json/UserProfile");

        JSONObject jsonObject2 = null;
        jsonObject2 = readJson("src/test/resources/Json/UserProfile");
//        compareTwoJsonObject(jsonObject1,jsonObject2);


    }


}
