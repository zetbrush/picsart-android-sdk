package pArtapibeta;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;




public class PhotoFactory {


    public static Photo parseFrom(JSONObject jobj) {
        try {
            Gson gson = new Gson();

            Photo nwPh = gson.fromJson(jobj.toString(),Photo.class);
            Log.d("User gson: ", nwPh.getTitle() + " " + nwPh.getCreated() + " " + nwPh.getId() + " " + nwPh.getUrl());
            gson = new Gson();
            User nwUs = gson.fromJson(jobj.get("user").toString(),User.class);
            Log.d("User gson: ", nwUs.getName() + " " + nwUs.getUsername() + " " + nwUs.getId() + " " + nwUs.getPhoto());
            Location lccc = gson.fromJson(jobj.get("location").toString(), Location.class);
            nwPh.setLocation(lccc);
            nwPh.setOwner(nwUs);

            return nwPh;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static ArrayList<Photo> parseFromAsArray(Object obj) {
        ArrayList<Photo> tmpPh = new ArrayList<>();
        Photo nwPh;
        User nwUs;
        Location loc;
        try {
            JSONObject jsonObj = (JSONObject)obj;
            Gson gson = new Gson();
            JSONArray jarr = new JSONArray(jsonObj.toString());
            for(int i =0; i<jarr.length(); i++) {

                 nwPh = gson.fromJson(jarr.get(i).toString(), Photo.class);

               try{
                  gson = new Gson();
                  JSONObject jooobj = (JSONObject)jarr.get(i);
                  nwUs = gson.fromJson(jooobj.get("user").toString(),User.class);
                    nwPh.setOwner(nwUs);
                  }catch (Exception e){
               }

                try{
                    gson = new Gson();
                    JSONObject jooobj = (JSONObject)jarr.get(i);
                    loc = gson.fromJson(jooobj.get("location").toString(), Location.class);
                    nwPh.setLocation(loc);
                }catch (Exception e){
                }

                tmpPh.add(nwPh);

            }

            return tmpPh;

        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;
    }

    public static Photo parseFrom(Object o) throws ClassCastException {
        JSONObject oo = (JSONObject) o;
        return parseFrom(oo);
    }



}
