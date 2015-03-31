package com.picsart.api;

import android.util.Log;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;


/**
 * This class consists exclusively of parsing methods, that trying make Photo/ collection of Photo
 * using gson reflection on class.
 *
 This class is a member of the  www.com.picsart.com
 *
 * @author  Arman Andreasyan 3/9/15
 */

public class PhotoFactory {

    /**
     * @param jobj  JSONObject
     * @return Photo
     *
     *  Tries to Parse JSONObject to Photo
     * */
    public static Photo parseFrom(JSONObject jobj) {
        Photo nwPh=null;
        try {
            Gson gson = new Gson();

            nwPh = gson.fromJson(jobj.toString(), Photo.class);
            Log.d("User gson: ", nwPh.getTitle() + " " + nwPh.getCreated() + " " + nwPh.getId() + " " + nwPh.getUrl());
        }catch (Exception e){
            //e.printStackTrace();

        }

        try {
            Gson gson = new Gson();
            User nwUs = gson.fromJson(jobj.get("user").toString(), User.class);
            Log.d("User gson: ", nwUs.getName() + " " + nwUs.getUsername() + " " + nwUs.getId() + " " + nwUs.getPhoto());
            nwPh.setOwner(nwUs);
        }catch (Exception e){
            //e.printStackTrace();
            }

        try {
            Gson gson = new Gson();
            Location lccc = gson.fromJson(jobj.get("location").toString(), Location.class);
            nwPh.setLocation(lccc);
        }catch (Exception e) {

        }

            return nwPh;


    }

    /**
     * @param obj  Object
     * @param offset starting point (from)
     * @param limit  limit of outcome
     * @return ArrayList of Photo
     *
     *  Tries to Parse Object to ArrayList of Photo instances
     * */
    public static ArrayList<Photo> parseFromAsArray(Object obj, int offset, int limit, String keyword) {
        ArrayList<Photo> tmpPh = new ArrayList<>();
        Photo nwPh;
        User nwUs;
        Location loc;
        if (offset < 0) offset = 0;
        if (limit < 1) limit = 1;

        try {
            JSONObject jsonObj = (JSONObject) obj;
            Gson gson = new Gson();
            JSONArray jarr=null;
            if(keyword!=null|| keyword!=""){
                try{
                    jarr = new JSONArray(jsonObj.get(keyword).toString());
                } catch (Exception e){ };
            }
            else  jarr = new JSONArray(jsonObj.toString());
            for (int i = offset; i < jarr.length() && limit > 0; i++, limit--) {

                nwPh = gson.fromJson(jarr.get(i).toString(), Photo.class);

                try {
                    gson = new Gson();
                    JSONObject jooobj = (JSONObject) jarr.get(i);
                    nwUs = gson.fromJson(jooobj.get("user").toString(), User.class);
                    nwPh.setOwner(nwUs);
                } catch (Exception e) {
                }

                try {
                    gson = new Gson();
                    JSONObject jooobj = (JSONObject) jarr.get(i);
                    loc = gson.fromJson(jooobj.get("location").toString(), Location.class);
                    nwPh.setLocation(loc);
                } catch (Exception e) {
                }

                tmpPh.add(nwPh);

            }

            return tmpPh;

        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;
    }


    /**
     * @param o  Object
     * @return Photo
     *
     *  Tries to Parse Object to Photo
     * */
    public static Photo parseFrom(Object o) throws ClassCastException {
        JSONObject oo = (JSONObject) o;
        return parseFrom(oo);
    }


}
