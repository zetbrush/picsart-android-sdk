package pArtapibeta;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class UserFactory {


    /**
     * @param object Object
     * @return User
     *
     * Tries to Parse Object to User
     */
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

    /**
     * @param object Object
     * @param offset starting point
     * @param limit  limit of users
     * @return ArrayList<User>
     *
     * Tries to Parse Object to ArrayList of User instances
     */
    public static ArrayList<User> parseFromAsArray(Object object, int offset, int limit, String keyword) {

        ArrayList<User> userArrayList = new ArrayList<>();
        User nwUs = null;
        Location loc;
        Gson gson;
        int max_limit;
        JSONArray jarr=null;
        JSONObject jsonObj=(JSONObject)object;

        try {

            if(keyword!=null || keyword!=""){
                try{

                    jarr = new JSONArray(jsonObj.get(keyword).toString());
                } catch (Exception e){ }
            }
            else  jarr = new JSONArray(jsonObj.toString());

            max_limit = limit >= jarr.length() ? jarr.length() - 1 : limit;

            for (int i = offset; i <= max_limit; i++) {

                JSONObject jsonObject = jarr.getJSONObject(i);

                gson = new Gson();
                nwUs = gson.fromJson(jsonObject.toString(), User.class);
                userArrayList.add(nwUs);
                //Log.d("gagagagagag", "foll id:  " + nwUs.getId());
            }
            return userArrayList;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
