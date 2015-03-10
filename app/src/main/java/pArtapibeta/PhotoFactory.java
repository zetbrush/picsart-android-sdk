package pArtapibeta;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONObject;

import pArtapibeta.pojo.*;
import pArtapibeta.pojo.PojoUser;

/**
 * Created by Arman on 3/9/15.
 */
public class PhotoFactory {


    public static Photo parseFrom(JSONObject jobj) {
        try {
            Gson gson = new Gson();
            PojoPhoto phh = gson.fromJson(jobj.toString(), PojoPhoto.class);


          /*  String id = jobj.getString(PicsArtConst.paramsPhotoInfo[0]);
            String url = (String) jobj.get(PicsArtConst.paramsPhotoInfo[1]);
            String title = (String) jobj.get(PicsArtConst.paramsPhotoInfo[2]);
            Tag tags = new Tag( jobj.getJSONArray(PicsArtConst.paramsPhotoInfo[22]));
           Date created = (Date) jobj.get(PicsArtConst.paramsPhotoInfo[3]);
            boolean isMature = (boolean) jobj.get(PicsArtConst.paramsPhotoInfo[4]);
            int width = (int) jobj.get(PicsArtConst.paramsPhotoInfo[5]);
            int height = (int) jobj.get(PicsArtConst.paramsPhotoInfo[6]);
            int likesCount = (int) jobj.get(PicsArtConst.paramsPhotoInfo[7]);
            int viewsCount = (int) jobj.get(PicsArtConst.paramsPhotoInfo[8]);
            int commentsCount = (int) jobj.get(PicsArtConst.paramsPhotoInfo[9]);
            int repostsCount = (int) jobj.get(PicsArtConst.paramsPhotoInfo[10]);
            boolean isLiked = (boolean) jobj.get(PicsArtConst.paramsPhotoInfo[11]);
            boolean isReposted = (boolean) jobj.get(PicsArtConst.paramsPhotoInfo[12]);
            String ownerid = null;
             String ownerid = (String) jobj.get(PicsArtConst.paramsPhotoInfo[13]);
          Location location = (Location)jobj.get(PicsArtConst.paramsPhotoInfo[21]);*/

            return parseFrom(phh, jobj);
            //return new Photo(id, url, title, tags, null,isMature, width, height, likesCount, viewsCount, commentsCount, repostsCount, isLiked, isReposted, ownerid, location);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Photo parseFrom(Object o) throws ClassCastException {
        JSONObject oo = (JSONObject) o;
        return parseFrom(oo);
    }


    public static Photo parseFrom(PojoPhoto ph, JSONObject json) {
        Gson gson = new Gson();

        try {
            Log.d("PojPhoto gson: ", ph.getTitle() + " " + ph.getId() + " " + ph.getCreated() + " " + ph.getUrl());
            PojoUser pus = gson.fromJson(ph.toString(), PojoUser.class);

            Log.d("PojUser gson: ", pus.getName() + " " + pus.getUsername() + " " + pus.getId() + " " + pus.getPhoto());
            gson = new Gson();
            PojoLocation ploc = gson.fromJson(ph.toString(), PojoLocation.class);
            Log.d("PojLocation gson: ", ploc.getCity() + " " + ploc.getCountry() + " " + ploc.getPlace() + " " + ploc.getStreet());
            return new Photo(ph.getId(), ph.getUrl(), ph.getTitle(), new Tag(ph.getTags()), ph.getCreated(), ph.getMature(), ph.getWidth(), ph.getHeight(), ph.getLikesCount(),
                    ph.getViewsCount(), ph.getCommentsCount(), ph.getRepostsCount(), ph.getIsLiked(), ph.getIsReposted(), pus.getId(), new Location(ploc));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }


}
