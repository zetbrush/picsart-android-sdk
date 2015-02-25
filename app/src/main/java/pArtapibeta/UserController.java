package pArtapibeta;

import android.app.DownloadManager;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import test.api.picsart.com.picsart_api_test.PicsArtConst;

/**
 * Created by Arman on 2/25/15.
 */
public class UserController {
    Context ctx;
    RequestListener listener;

    public String[] getPhotoUrl() {
        return photoUrl;
    }

    String[] photoUrl;



    public UserController(Context ctx){
        this.ctx=ctx;

    }

    public void setListener(RequestListener listener){
        this.listener = listener;
    }

    public void getUserPhotos(User user, int limit, int offset ){
        getUserPhotos(user.getId(),limit,offset);

    }

     public void getUserPhotos(String userId , int limit, int offset){

         photoUrl = new String[limit];



     assert this.listener !=null;
        String url = PicsArtConst.GET_USER_PHOTOS_LIST+userId+"/photos/?token="+MainActivity.getAccessToken(); //+ PicsArtConst.GET_PHOTO_FILTER
        PARequest req = new PARequest(Request.Method.GET,url,null,null);
        SingletoneRequestQue.getInstance(ctx).addToRequestQueue(req);
        req.setRequestListener(new PARequest.PARequestListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
            @Override
            public void onResponse(Object response)  {
                JSONArray keyArray = null;
                try {
                    keyArray = ((JSONObject)response).getJSONArray("url");

                for(int i = 0; i < keyArray.length(); i++) {
                    photoUrl[i]= keyArray.getString(i);
                    UserController.this.listener.onRequestReady(7);
                }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

}
