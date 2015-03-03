package pArtapibeta;

import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import test.api.picsart.com.picsart_api_test.PicsArtConst;

/**
 * Created by Arman on 2/23/15.
 */
public  class PhotoController  {
    Context ctx;
    RequestListener listener;
    static  RequestListener st_listener;
    String token;
    volatile Photo photo;
    volatile Comment[] comments;




    public Photo getPhoto() {
        return photo;
    }


     public static synchronized  void uploadPhoto(Photo... photo) {
           new ImageUploadTask().execute(photo);

    }

    public void setListener(RequestListener listener){
        this.listener=listener;
    }

    public static void setSt_Listener(RequestListener listener){
        PhotoController.st_listener=listener;
    }
     public synchronized void requestPhoto(String id) {
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



    public static synchronized  Comment[] getComments(String id,int limit, int offset){
        final Comment[][] comm = new Comment[1][];
        String url = PicsArtConst.PHOTO_COMMENT_URL+id+".json"+PicsArtConst.API_PREFX + PicsArtConst.APIKEY;

        PARequest req = new PARequest(Request.Method.GET,url,null,null);
        SingletoneRequestQue.getInstance(MainActivity.getAppContext()).addToRequestQueue(req);
        req.setRequestListener(new PARequest.PARequestListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
            @Override
            public void onResponse(Object response)  {

                try {
                    JSONArray phots = ((JSONObject)response).getJSONArray("comments");
                    Comment[] comment= new Comment[phots.length()];
                    for (int i = 0; i < phots.length(); i++) {
                        JSONObject val = phots.getJSONObject(i);
                        String txt = val.getString("text");
                        Date crtd = new Date(val.getString("created"));
                        String cmid = val.getString("id");
                        comment[i]=(new Comment(txt,crtd,cmid));

                    }
                    st_listener.onRequestReady(555);
                    comm[0] = comment;
                } catch ( Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return comm[0];
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



    private static class ImageUploadTask extends AsyncTask<Photo, Integer, JSONObject> {

        InputStream is = null;
        volatile JSONObject jObj = null;
        String json="";

        @Override
        protected JSONObject doInBackground(Photo... phot) {
            int iter =0;
            for(Photo ph: phot) {
                try {
                    Looper.getMainLooper();
                    Looper.prepare();
                    File file = new File(phot[0].getPath());
                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost((PicsArtConst.PHOTO_UPLOAD_URL) + PicsArtConst.API_PREFX + PicsArtConst.APIKEY);
                    MultipartEntity entity = new MultipartEntity(
                            HttpMultipartMode.BROWSER_COMPATIBLE);

                    //Location loc = new Location("poxoc","Qaxaq","Plac@","State","Zipcod@","Armenia",new Coordiantes("40.00","36.00"));
                    BasicNameValuePair[] tmp = phot[0].getLocation().getLocationPair();
                    for (int i = 0; i < tmp.length; i++) {
                        entity.addPart(tmp[i].getName(), new StringBody(tmp[i].getValue()));
                    }
                    entity.addPart("file", new FileBody(file));

                    for (String str : phot[0].getTags().getTagValues()) {
                        entity.addPart("tags[]", new StringBody(str));
                    }
                    entity.addPart("title", new StringBody(phot[0].getTitle()));
                    httpPost.setEntity(entity);
                    HttpResponse response = httpClient.execute(httpPost);
                    HttpEntity httpEntity = response.getEntity();
                    is = httpEntity.getContent();

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(
                            is, "iso-8859-1"), 8);
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    is.close();

                    json = sb.toString();
                    Log.e("JSONStr", json);
                } catch (Exception e) {
                    e.getMessage();
                    Log.e("Buffer Error", "Error converting result " + e.toString());
                }
                // Parse the String to a JSON Object
                try {
                    jObj = new JSONObject(json);
                } catch (JSONException e) {
                    Log.e("JSON Parser", "Error parsing data " + e.toString());
                }
                iter++;
                publishProgress(iter);

            }
                // Return JSON String
                return jObj;

        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            Log.d("Uploaded", "photo #"+progress);
        }

        @Override
        protected void onPostExecute(JSONObject sResponse) {

            if (sResponse != null) {
                try {
                    Log.d("response Upload", sResponse.toString());

                } catch (Exception e) {
                    Log.e(e.getClass().getName(), e.getMessage(), e);
                }
            }
        }




        public String encodeTobase64(Bitmap image) {
            Bitmap immagex = image;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            immagex.compress(Bitmap.CompressFormat.JPEG, 75, baos);
            byte[] b = baos.toByteArray();
            String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

            Log.e("LOOK", imageEncoded);
            return imageEncoded;
        }

        public Bitmap decodeBase64(String input) {
            byte[] decodedByte = Base64.decode(input, 0);
            return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
        }

    }


}
