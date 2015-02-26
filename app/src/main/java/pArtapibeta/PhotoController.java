package pArtapibeta;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import test.api.picsart.com.picsart_api_test.PicsArtConst;

/**
 * Created by Arman on 2/23/15.
 */
public  class PhotoController {
    Context ctx;
    RequestListener listener;
    String token;

    public Photo getPhoto() {
        return photo;
    }

    Photo photo;

    synchronized public static void uploadPhoto(Photo photo) {

        //TODO
    }

    public void setListener(RequestListener listener){
        this.listener=listener;
    }
     public void requestPhoto(String id) {
        assert this.listener !=null;
        String url = PicsArtConst.Get_PHOTO_URL + id + PicsArtConst.TOKEN_PREFIX+token;
        PARequest request = new PARequest(Request.Method.GET, url, null,null);
        SingletoneRequestQue.getInstance(ctx).addToRequestQueue(request);
        request.setRequestListener(new PARequest.PARequestListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", error.toString());
            }
            @Override
            public void onResponse(Object response) {
                Log.d("Response 9", response.toString());
                photo = new Photo();
                photo.parseFrom(response);
                listener.onRequestReady(9);

            }
        });




    }

    public PhotoController(Context ctx,String token){
    this.ctx = ctx;
    this.token = token;
    }

    public Comment[] getComments(){
        //TODO
        return new Comment[1];
    }

    public Comment getCommentByid(String id){
        //TODO
        return new Comment(null,null,null);
    }

    public void removeComment(String id){

        //TODO
    }
    /*public User[] getLikes(){
        //TODO

    }*/

    public void updateData(){
        //TODO
    }

    public void comment(String comment){
        //TODO
    }

    public boolean like(){
        //TODO
        return false;
    }

    public boolean unLike(){
        //TODO
        return false;
    }




}
