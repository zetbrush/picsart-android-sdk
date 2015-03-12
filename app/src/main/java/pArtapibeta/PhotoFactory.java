package pArtapibeta;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;



/**
 * Created by Arman on 3/9/15.
 */
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

    public static Photo parseFrom(Object o) throws ClassCastException {
        JSONObject oo = (JSONObject) o;
        return parseFrom(oo);
    }



}
