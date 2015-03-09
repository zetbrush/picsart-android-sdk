package pArtapibeta;

import org.json.JSONObject;

import java.net.URL;
import java.util.Date;

import test.api.picsart.com.picsart_api_test.PicsArtConst;

/**
 * Created by Arman on 3/9/15.
 */
public class PhotoFactory {


    public static Photo parseFrom(JSONObject jobj){
        try {
            String id = jobj.getString(PicsArtConst.paramsPhotoInfo[0]);
            URL url = (URL) jobj.get(PicsArtConst.paramsPhotoInfo[1]);
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
            String ownerid = (String) jobj.get(PicsArtConst.paramsPhotoInfo[13]);
            Location location = (Location)jobj.get(PicsArtConst.paramsPhotoInfo[21]);

            return new Photo(id, url, title, tags, created,isMature, width, height, likesCount, viewsCount, commentsCount, repostsCount, isLiked, isReposted, ownerid, location);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Photo parseFrom(Object o) throws ClassCastException{
        JSONObject oo = (JSONObject) o;
        return parseFrom(oo);
    }





}
