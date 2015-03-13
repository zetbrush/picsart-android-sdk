package pArtapibeta;

import com.google.gson.Gson;

import org.json.JSONObject;


public class UserFactory {

    public static User parseFrom(Object object) {

        try {

            JSONObject jsonObject = (JSONObject) object;
            Gson gson = new Gson();
            User phh = gson.fromJson(jsonObject.toString(), User.class);
            return phh;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }




}
