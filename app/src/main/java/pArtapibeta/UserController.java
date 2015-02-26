package pArtapibeta;

import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import test.api.picsart.com.picsart_api_test.PicsArtConst;

/**
 * Created by Arman on 2/25/15.
 */
public class UserController {

    public static final String MY_LOGS = "My_Logs";

    Context ctx;
    RequestListener listener;

    private String[] photoUrl;
    private ArrayList<String> userFollowing;
    private ArrayList<String> userFollowers;
    private ArrayList<String> userLikedPhotos;
    private ArrayList<Tag> userTags;
    private ArrayList<Place> userPlaces;
    private ArrayList<User> blockedUsers;


    public UserController(Context ctx) {
        this.ctx = ctx;
    }

    public void setListener(RequestListener listener) {
        this.listener = listener;
    }



    public String[] getPhotoUrl() {
        return photoUrl;
    }






    public void requestBlockedUsers(User user, int limit, int offset) {
        requestBlockedUsers(user.getId(), limit, offset);
    }

    public void requestBlockedUsers(String userId, int limit, int offset) {

        blockedUsers = new ArrayList<>();

        assert this.listener != null;
        String url = PicsArtConst.SHOW_BLOCKED_USERS + userId + "/blocks?token=" + MainActivity.getAccessToken();
        PARequest req = new PARequest(Request.Method.GET, url, null, null);
        SingletoneRequestQue.getInstance(ctx).addToRequestQueue(req);
        req.setRequestListener(new PARequest.PARequestListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

            @Override
            public void onResponse(Object response) {
                Log.d(MY_LOGS, response.toString());
                UserController.this.listener.onRequestReady(6);
            }
        });

    }


    public void requestPlaces(User user, int limit, int offset) {
        requestPlaces(user.getId(), limit, offset);
    }

    public void requestPlaces(String userId, int limit, int offset) {

        userPlaces = new ArrayList<>();

        assert this.listener != null;
        String url = PicsArtConst.SHOW_USER_PLACES + userId + "/places?token=" + MainActivity.getAccessToken();
        PARequest req = new PARequest(Request.Method.GET, url, null, null);
        SingletoneRequestQue.getInstance(ctx).addToRequestQueue(req);
        req.setRequestListener(new PARequest.PARequestListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

            @Override
            public void onResponse(Object response) {
                Log.d(MY_LOGS, response.toString());
                UserController.this.listener.onRequestReady(6);
            }
        });
    }


    public void requestTags(User user, int limit, int offset) {
        requestTags(user.getId(), limit, offset);
    }

    public void requestTags(String userId, int limit, int offset) {

        userTags = new ArrayList<>();


        assert this.listener != null;
        String url = PicsArtConst.SHOW_USER_TAGS + userId + "/tags?token=" + MainActivity.getAccessToken(); //+ PicsArtConst.GET_PHOTO_FILTER
        PARequest req = new PARequest(Request.Method.GET, url, null, null);
        SingletoneRequestQue.getInstance(ctx).addToRequestQueue(req);
        req.setRequestListener(new PARequest.PARequestListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

            @Override
            public void onResponse(Object response) {
                Log.d(MY_LOGS, response.toString());
                UserController.this.listener.onRequestReady(6);
            }
        });
    }


    public void requestUserPhotos(User user, int limit, int offset) {
        requestUserPhotos(user.getId(), limit, offset);

    }

    public void requestUserPhotos(String userId, int limit, int offset) {

        photoUrl = new String[limit];


        assert this.listener != null;
        String url = PicsArtConst.GET_USER_PHOTOS_LIST + userId + "/photos/?token=" + MainActivity.getAccessToken(); //+ PicsArtConst.GET_PHOTO_FILTER
        PARequest req = new PARequest(Request.Method.GET, url, null, null);
        SingletoneRequestQue.getInstance(ctx).addToRequestQueue(req);
        req.setRequestListener(new PARequest.PARequestListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

            @Override
            public void onResponse(Object response) {
                JSONArray keyArray = null;
                try {
                    keyArray = ((JSONObject) response).getJSONArray("url");

                    for (int i = 0; i < keyArray.length(); i++) {
                        photoUrl[i] = keyArray.getString(i);
                        UserController.this.listener.onRequestReady(7);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    public void requestUserFollowers(User user, int limit, int offset) {
        requestUserFollowers(user.getId(), limit, offset);
    }

    public void requestUserFollowers(String userId, int limit, int offset) {

        userFollowers = new ArrayList<>();

        assert this.listener != null;
        String url = PicsArtConst.SHOW_USER_FOLLOWERS + userId + ".json?token=" + MainActivity.getAccessToken(); //+ PicsArtConst.GET_PHOTO_FILTER
        PARequest req = new PARequest(Request.Method.GET, url, null, null);
        SingletoneRequestQue.getInstance(ctx).addToRequestQueue(req);
        req.setRequestListener(new PARequest.PARequestListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

            @Override
            public void onResponse(Object response) {
                try {
                    JSONArray jsonArray = ((JSONObject) response).getJSONArray("response");
                    //Log.d("gagagagagaga", jsonArray.toString());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        userFollowers.add(jsonObject.getString("id"));
                        Log.d(MY_LOGS, "follower id:  " + jsonObject.getString("id"));
                        UserController.this.listener.onRequestReady(8);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public void requestUserFollowing(User user, int limit, int offset) {
        requestUserFollowing(user.getId(), limit, offset);
    }

    public void requestUserFollowing(String userId, int limit, int offset) {

        userFollowing = new ArrayList<>();

        assert this.listener != null;
        String url = PicsArtConst.SHOW_USER_FOLLOWING + userId + ".json?token=" + MainActivity.getAccessToken(); //+ PicsArtConst.GET_PHOTO_FILTER
        PARequest req = new PARequest(Request.Method.GET, url, null, null);
        SingletoneRequestQue.getInstance(ctx).addToRequestQueue(req);
        req.setRequestListener(new PARequest.PARequestListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

            @Override
            public void onResponse(Object response) {
                try {
                    JSONArray jsonArray = ((JSONObject) response).getJSONArray("response");
                    //Log.d("gagagagagaga", jsonArray.toString());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        userFollowing.add(jsonObject.getString("id"));
                        Log.d(MY_LOGS, "following id:  " + jsonObject.getString("id"));
                        UserController.this.listener.onRequestReady(9);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public void requestLikedPhotos(User user, int limit, int offset) {
        requestLikedPhotos(user.getId(), limit, offset);
    }

    public void requestLikedPhotos(String userId, int limit, int offset) {

        userLikedPhotos = new ArrayList<>();

        assert this.listener != null;
        String url = PicsArtConst.SHOW_USER_LIKED_PHOTOS + userId + ".json?token=" + MainActivity.getAccessToken(); //+ PicsArtConst.GET_PHOTO_FILTER
        PARequest req = new PARequest(Request.Method.GET, url, null, null);
        SingletoneRequestQue.getInstance(ctx).addToRequestQueue(req);
        req.setRequestListener(new PARequest.PARequestListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

            @Override
            public void onResponse(Object response) {
                Log.d(MY_LOGS, "likedphotos:  " + response.toString());   //  {"response":[],"status":"success"}
                UserController.this.listener.onRequestReady(10);
            }
        });
    }


    public void blockUserWithID(String userId, String blockingId) {

        assert this.listener != null;
        String url = PicsArtConst.BLOCK_USER_WITH_ID + "blocks?token=" + MainActivity.getAccessToken();
        PARequest req = new PARequest(Request.Method.POST, url, null, null);
        try {
            req.getHeaders().put("block_id", "161263489000102");
        } catch (AuthFailureError authFailureError) {
            authFailureError.printStackTrace();
        }
        SingletoneRequestQue.getInstance(ctx).addToRequestQueue(req);
        req.setRequestListener(new PARequest.PARequestListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

            @Override
            public void onResponse(Object response) {
                Log.d(MY_LOGS, response.toString());

                UserController.this.listener.onRequestReady(11);
            }
        });

        //160573178000102
        //161263489000102
        //new BlockUserAsyncTask().execute();

        //gagaoooooo
    }

    /*class BlockUserAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... text) {

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

            nameValuePairs.add(new BasicNameValuePair("block_id", "161263489000102"));

            try {

                httpClient = new DefaultHttpClient();
                httpPost = new HttpPost(BLOCK_USER_WITH_ID + ACCESS_TOKEN);
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();

                Log.d(MY_LOGS, "" + httpResponse.toString());

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
            //postData();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.d("gagagagagagagagag", "json send:   " + json);
        }
    }*/


    /*public void followUserWithID(int id) {

        new FollowUserAsyncTask().execute();
    }

    class FollowUserAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... text) {

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

            nameValuePairs.add(new BasicNameValuePair("following_id", "160573178000102"));

            try {

                httpClient = new DefaultHttpClient();
                httpPost = new HttpPost(FOLLOW_USER_WITH_ID + ACCESS_TOKEN);
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();

                Log.d(MY_LOGS, "" + httpResponse.toString());

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
            //postData();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.d("gagagagagagagagag", "json send:   " + json);
        }
    }*/


    /*public void unblockUserWithID() {

        new UnblockUserAsyncTask().execute();
    }

    class UnblockUserAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... text) {

            try {

                httpClient = new DefaultHttpClient();
                HttpDelete httpDelete = new HttpDelete(UNBLOCK_USER_WITH_ID + ACCESS_TOKEN);
                HttpResponse httpResponse = httpClient.execute(httpDelete);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();

                Log.d(MY_LOGS, "" + httpResponse.toString());

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
            //postData();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.d(MY_LOGS, "json send:   " + json);
        }
    }*/


    /*public void uploadUserCover() {

        new UploadCoverAsyncTask().execute();

    }

    class UploadCoverAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... text) {

           *//**//* Bitmap bitmap = Bitmap.createBitmap(6,6,Bitmap.Config.RGB_565);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream); //compress to which format you want.
            byte [] byte_arr = stream.toByteArray();
            String image_str = String.valueOf(Base64.encode(byte_arr, Base64.NO_WRAP));
            ArrayList<NameValuePair> nameValuePairs = new  ArrayList<NameValuePair>();

            nameValuePairs.add(new BasicNameValuePair("image",image_str));

            try{
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://stage.i.picsart.com/api/users/cover/add.json?token=tMQdLWML0kspa8qQDvWyzm235Id2Cv9ypicsart11VNuSx6BNk4cMX7VH");
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                String the_string_response = convertResponseToString(response);
                Looper.loop();
                Log.d("gagagagagagaggg", "Response " + the_string_response);
            }catch(Exception e){
                  Log.d("gagagagagagaGGA","ERROR " + e.getMessage());
                  System.out.println("Error in http connection "+e.toString());
            }*//**//*


            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Bitmap compare = Bitmap.createBitmap(6, 6, Bitmap.Config.RGB_565);
            compare.eraseColor(Color.GRAY);
            compare.compress(Bitmap.CompressFormat.JPEG, 50, baos);
            byte[] imageBytes = baos.toByteArray();

            be = new ByteArrayEntity(imageBytes);

            try {

                httpClient = new DefaultHttpClient();
                httpPost = new HttpPost("http://stage.i.picsart.com/api/users/cover/add.json?token=tMQdLWML0kspa8qQDvWyzm235Id2Cv9ypicsart11VNuSx6BNk4cMX7VH");
                httpPost.setEntity(be);
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
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
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.d("gagagagagagagagag", "json send:   " + json);
        }
    }




    public void uploadUserAvatar() {

    }*/
}
