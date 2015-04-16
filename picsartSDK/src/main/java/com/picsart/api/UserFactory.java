package com.picsart.api;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * This class consists exclusively of parsing methods, that trying make User/ collection of User
 * using gson reflection on class.
 *
 * This class is a member of the
 * "www.picsart.com"
 *
 *
 */
public class UserFactory {

    /**
     * Tries to Parse Object to User
     *
     * @param object Object
     * @return User
     */
    public static User parseFrom(Object object) {


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
     * @param o  Object
     * @param offset starting point (from)
     * @param limit  limit of outcome
     *
     *
     *  Tries to Parse Object to ArrayList of User instances
     * */
    public static ArrayList<User> parseFromAsArray(Object o, int offset, int limit,String keyword) {

        ArrayList<User> userArrayList = new ArrayList<>();
        User nwUs = null;
        Location loc;
        Gson gson;

        try {
            JSONObject jsonObj = (JSONObject)o;
             gson = new Gson();
            JSONArray jarr=null;
            if(keyword!=null|| keyword!=""){
                try{
                    jarr = new JSONArray(jsonObj.get(keyword).toString());
                } catch (Exception e){ };
            }
            else  jarr = new JSONArray(jsonObj.toString());

            for (int i = offset; i < jarr.length() && limit>=0; i++,limit--) {
                JSONObject jsonObject = jarr.getJSONObject(i);
                gson = new Gson();
               try {
                   nwUs = gson.fromJson(jsonObject.toString(), User.class);

               }catch(Exception e){}

                try{
                    loc = gson.fromJson(jsonObject.toString(), Location.class);
                    nwUs.setLocation(loc);
                }catch(Exception e){}

                userArrayList.add(nwUs);

            }

            return userArrayList;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<User> parseFromArray(Object o, int offset, int limit) {

        ArrayList<User> userArrayList = new ArrayList<>();
        User nwUs = null;
        Location loc;
        Gson gson;

        try {

            JSONArray jarr = (JSONArray)o;

            for (int i = offset; i < jarr.length() && limit>=0; i++,limit--) {
                JSONObject jsonObject = jarr.getJSONObject(i);
                gson = new Gson();
                try {
                    nwUs = gson.fromJson(jsonObject.toString(), User.class);

                }catch(Exception e){}

                try{
                    loc = gson.fromJson(jsonObject.toString(), Location.class);
                    nwUs.setLocation(loc);
                }catch(Exception e){}

                userArrayList.add(nwUs);

            }

            return userArrayList;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
