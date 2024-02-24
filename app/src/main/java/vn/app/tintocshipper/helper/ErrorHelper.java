package vn.app.tintocshipper.helper;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Admin on 8/17/2017.
 */

public class ErrorHelper {
    public static JSONArray errorData;

    public static String getValueByKey(int key) {
        String retValue = "";
        try {
            if (errorData == null) {
                return "";
            }

            for(int i = 0; i < errorData.length(); i++){
                JSONObject jsonObject = errorData.getJSONObject(i);
                if(jsonObject.getInt("id") == key){
                    retValue = jsonObject.getString("description");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return retValue;
    }
}
