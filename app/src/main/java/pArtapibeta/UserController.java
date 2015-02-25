package pArtapibeta;

import android.app.DownloadManager;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import test.api.picsart.com.picsart_api_test.PicsArtConst;

/**
 * Created by Arman on 2/25/15.
 */
public class UserController {
Context ctx;
    RequestListener listener;



    public UserController(Context ctx){
        this.ctx=ctx;
    }
    public void setListener(RequestListener listener){
        this.listener = listener;
    }

    public String[] getUserPhotos(User user){
       return getUserPhotos(user.getId());

    }

     public String[] getUserPhotos(String userId){
         final String[] result = null;
     assert this.listener !=null;
        String url = PicsArtConst.GET_USER_PHOTOS_LIST+userId+"/photos/"; //+ PicsArtConst.GET_PHOTO_FILTER
        PARequest req = new PARequest(Request.Method.GET,url,null,null);
        SingletoneRequestQue.getInstance(ctx).addToRequestQueue(req);
        req.setRequestListener(new PARequest.PARequestListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

            @Override
            public void onResponse(Object response) throws JSONException {
                JSONArray keyArray
                new String[keyArray.length()];
                for(int i = 0; i < keyArray.length(); i++) {
                    keyAttributes[i] = keyArray.getString(i);
                }
                result = ((JSONObject)response).getJSONArray("url").;
            }
        });


    }


}
