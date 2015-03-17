package pArtapibeta;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class UserFactory {


    public static User parseFrom(Object object) {

        // try {

        JSONObject jsonObject = (JSONObject) object;

        Gson gson = new Gson();
        User phh = null;

        try {
            phh = gson.fromJson(jsonObject.toString(), User.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Location location = gson.fromJson((jsonObject.get("location")).toString(), Location.class);
            phh.setLocation(location);

        } catch (Exception e) {
            //e.printStackTrace();
        }

        return phh;

}

    public static ArrayList<User> parseFromAsArray(Object o, int offset, int limit) {

        ArrayList<User> userArrayList = new ArrayList<>();
        User nwUs = null;
        Location loc;
        Gson gson;
        int max_limit;

        try {

            JSONArray jsonArray = ((JSONObject) o).getJSONArray("response");
            max_limit = limit >= jsonArray.length() ? jsonArray.length() - 1 : limit;

            for (int i = offset; i <= max_limit; i++) {

                JSONObject jsonObject = jsonArray.getJSONObject(i);

                gson = new Gson();
                nwUs = gson.fromJson(jsonObject.toString(), User.class);
                userArrayList.add(nwUs);
                //Log.d("gagagagagag", "foll id:  " + nwUs.getUsername());
            }
            return userArrayList;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
