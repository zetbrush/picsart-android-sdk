package pArtapibeta;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.VolleyError;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import test.api.picsart.com.picsart_api_test.PicsArtConst;

public class UserController {


    public static final String MY_LOGS = "My_Logs";

    private Context ctx;
    private pArtapibeta.RequestListener listener;

    private User user;
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


    public User getUser() {
        return user;
    }

    public String[] getPhotoUrl() {
        return photoUrl;
    }

    public ArrayList<String> getUserFollowing() {
        return userFollowing;
    }

    public ArrayList<String> getUserFollowers() {
        return userFollowers;
    }

    public ArrayList<String> getUserLikedPhotos() {
        return userLikedPhotos;
    }

    public ArrayList<Tag> getUserTags() {
        return userTags;
    }

    public ArrayList<Place> getUserPlaces() {
        return userPlaces;
    }

    public ArrayList<User> getBlockedUsers() {
        return blockedUsers;
    }


    public void requestUser() {

        assert this.listener != null;
        String url = PicsArtConst.SHOW_USER_URL + "me?token=" + MainActivity.getAccessToken();
        PARequest req = new PARequest(Request.Method.GET, url, null, null);
        SingletoneRequestQue.getInstance(ctx).addToRequestQueue(req);
        req.setRequestListener(new PARequest.PARequestListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

            @Override
            public void onResponse(Object response) {
                Log.d(MY_LOGS, response.toString());
                user = new User();
                user.parseFrom(response);
                UserController.this.listener.onRequestReady(5);
            }
        });

    }

    public void requestUser(String id) {     //    3

        assert this.listener != null;
        String url = PicsArtConst.SHOW_USER_URL + id + "?token=" + MainActivity.getAccessToken();
        PARequest req = new PARequest(Request.Method.GET, url, null, null);
        SingletoneRequestQue.getInstance(ctx).addToRequestQueue(req);
        req.setRequestListener(new PARequest.PARequestListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

            @Override
            public void onResponse(Object response) {
                Log.d(MY_LOGS, response.toString());
                user = new User();
                user.parseFrom(response);
                UserController.this.listener.onRequestReady(3);
            }
        });
    }


    public void requestBlockedUsers(User user, int limit, int offset) {
        requestBlockedUsers(user.getId(), limit, offset);
    }

    public void requestBlockedUsers(String userId, int limit, int offset) {    //   4

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
                UserController.this.listener.onRequestReady(4);

            }
        });

    }


    public void requestPlaces(User user, int limit, int offset) {
        requestPlaces(user.getId(), limit, offset);
    }

    public void requestPlaces(String userId, int limit, int offset) {    //  5

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
                UserController.this.listener.onRequestReady(5);
            }
        });
    }


    public void requestTags(User user, int limit, int offset) {
        requestTags(user.getId(), limit, offset);
    }

    public void requestTags(String userId, int limit, int offset) {    // 6

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

    public void requestUserPhotos(String userId, int limit, int offset) {    //  7

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

    public void requestUserFollowers(String userId, int limit, int offset) {    //   8

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

    public void requestUserFollowing(String userId, int limit, int offset) {    //   9

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

    public void requestLikedPhotos(String userId, int limit, int offset) {    //   10

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


    public void blockUserWithID(String id) {

        /*assert this.listener != null;
        String url = PicsArtConst.BLOCK_USER_WITH_ID + "blocks?token=" + MainActivity.getAccessToken();
        PARequest req = new PARequest(Request.Method.POST, url, null, null) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("block_id", "161263489000102");
                return params;
            }


        };

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
        });*/

        //160573178000102
        //161263489000102

        new BlockUserAsyncTask().execute(id);
    }

    class BlockUserAsyncTask extends AsyncTask<String, Void, Void> {

        InputStream is;
        JSONObject jObj;
        String json;

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(String... id) {

            //161263489000102

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

            nameValuePairs.add(new BasicNameValuePair("block_id", id[0]));

            try {

                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(PicsArtConst.BLOCK_USER_WITH_ID + "blocks?token=" + MainActivity.getAccessToken());
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
            UserController.this.listener.onRequestReady(12);

        }
    }


    public void followUserWithID(String id) {

        new FollowUserAsyncTask().execute(id);
    }

    class FollowUserAsyncTask extends AsyncTask<String, Void, Void> {

        InputStream is;
        JSONObject jObj;
        String json;

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(String... id) {

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

            nameValuePairs.add(new BasicNameValuePair("following_id", id[0]));

            try {

                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(PicsArtConst.FOLLOW_USER_WITH_ID + MainActivity.getAccessToken());
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
    }


    public void unblockUserWithID(String id) {

        new UnblockUserAsyncTask().execute(id);
    }

    class UnblockUserAsyncTask extends AsyncTask<String, Void, Void> {

        InputStream is;
        JSONObject jObj;
        String json;

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(String... id) {

            try {


                //161263489000102

                HttpClient httpClient = new DefaultHttpClient();
                HttpDelete httpDelete = new HttpDelete(PicsArtConst.UNBLOCK_USER_WITH_ID + id[0] + "?token=" + MainActivity.getAccessToken());
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
    }


    public void uploadUserCover() {

        //API Key    cd371244-3887-405b-82d1-7aadcb2617b9
        new UploadCoverAsyncTask().execute();

    }

    class UploadCoverAsyncTask extends AsyncTask<Void, Void, Void> {

        InputStream is;
        JSONObject jObj;
        String json;

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... text) {

            Bitmap bm = Bitmap.createBitmap(10, 10, Bitmap.Config.RGB_565);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 40, baos);
            byte[] byteImage_photo = baos.toByteArray();
            String encodedImage = Base64.encodeToString(byteImage_photo, Base64.DEFAULT);

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("file", encodedImage));

            try {

                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("https://api.picsart.com/users/cover/add.json?key=cd371244-3887-405b-82d1-7aadcb2617b9");
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
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

            /*String url = "https://api.picsart.com/users/cover/add.json?key=cd371244-3887-405b-82d1-7aadcb2617b9";
            File file = new File("storage/emulated/0/DCIM/Camera/",
                    "aaa.jpeg");
            try {
                HttpClient httpclient = new DefaultHttpClient();

                HttpPost httppost = new HttpPost(url);

                InputStreamEntity reqEntity = new InputStreamEntity(
                        new FileInputStream(file), -1);
                reqEntity.setContentType("binary/octet-stream");
                reqEntity.setChunked(true); // Send in multiple parts if needed
                httppost.setEntity(reqEntity);
                HttpResponse response = httpclient.execute(httppost);
                //Do something with response...

                Log.d(MY_LOGS,response.toString());
            } catch (Exception e) {
                // show error
            }*/
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.d(MY_LOGS, "json send:   " + json);
        }
    }
}
