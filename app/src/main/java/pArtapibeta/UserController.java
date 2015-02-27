package pArtapibeta;

import android.app.DownloadManager;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import test.api.picsart.com.picsart_api_test.PicsArtConst;

/**
 * Created by Arman on 2/25/15.
 */
public class UserController {
    Context ctx;
    RequestListener listener;
    List<String> userphotosurls;
    String token;

    public User getUser() {
        return user;
    }

    User user;
    public List<String> getUserphotosurls() {
        return userphotosurls;
    }





    public UserController(Context ctx, String token){
        this.ctx=ctx;
        this.token = token;
    }

    public void setListener(RequestListener listener){
        this.listener = listener;
    }



    public void requestUserPhotos(User user, int limit, int offset ){
        requestUserPhotos(user.getId(), limit, offset);

    }
    public void requestUserPhotos(String userId , int limit, int offset){

        userphotosurls = new ArrayList<>();
        assert this.listener !=null;
        String url = PicsArtConst.GET_USER_PHOTOS_LIST+userId+"/photos/?token="+token; //+ PicsArtConst.GET_PHOTO_FILTER
        PARequest req = new PARequest(Request.Method.GET,url,null,null);

        SingletoneRequestQue.getInstance(ctx).addToRequestQueue(req);
        req.setRequestListener(new PARequest.PARequestListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
            @Override
            public void onResponse(Object response)  {

             try {
                    JSONArray phres = ((JSONObject)response).getJSONArray("photos");

                for (int i = 0; i < phres.length(); i++) {
                    JSONObject val = phres.getJSONObject(i);
                    String url = val.getString("url");
                    userphotosurls.add(url);
                }
                 UserController.this.listener.onRequestReady(7);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    public void requestUser(String id){
        assert this.listener !=null;
        String url = PicsArtConst.USE_PROFILE_URL+id+PicsArtConst.TOKEN_PREFIX+token;
        PARequest req = new PARequest(Request.Method.GET,url,null,null);
        SingletoneRequestQue.getInstance(ctx).addToRequestQueue(req);
        req.setRequestListener(new PARequest.PARequestListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
            @Override
            public void onResponse(Object response)  {

                try {
                     user = new User();
                     user.parseFrom(response);
                    UserController.this.listener.onRequestReady(7);
                } catch ( Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }
    public void requestUser(){
        assert this.listener !=null;
        String url = PicsArtConst.MY_PROFILE_URL+PicsArtConst.TOKEN_URL_PREFIX+token;
        PARequest req = new PARequest(Request.Method.GET,url,null,null);
        SingletoneRequestQue.getInstance(ctx).addToRequestQueue(req);
        req.setRequestListener(new PARequest.PARequestListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

            @Override
            public void onResponse(Object response)  {

                try {
                    user = new User();
                    user.parseFrom(response);
                    UserController.this.listener.onRequestReady(7);
                } catch ( Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }






}
