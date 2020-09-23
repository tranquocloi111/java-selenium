package logic.business.helper;


import framework.utils.JsonReader;
import org.json.simple.JSONObject;;import java.util.Objects;

public class JsonHelper extends JsonReader {

    public static JsonHelper getInstance() {
        return new JsonHelper();
    }

    public static String readJsonFile(String path) {
        return Objects.requireNonNull(JsonReader.readJson(path)).toJSONString();
    }

    public static boolean compareTwoJsonFiles(String object1, String object2) {
        return JsonReader.compareTwoJsonObject(object1, object2);
    }

    public static void verifyValueOfNode(String body, String node, String expectedValue){
        JsonReader.compareValueNode(body,node,expectedValue);
    }

    public static void verifyValueOfNode(String body, String node, String expectedValue,String parentNode){
        JsonReader.verifyNodeValue(body,node,expectedValue,parentNode);
    }
    public static void verifyValueOfNodeContainsExpectValue(String body, String node, String expectedValue,String parentNode){
        JsonReader.verifyNodeValueContainsExpectValue(body,node,expectedValue,parentNode);
    }
}
