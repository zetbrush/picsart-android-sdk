package pArtapibeta;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import test.api.picsart.com.picsart_api_test.PicsArtConst;

public class UserController {


    public static final String MY_LOGS = "My_Logs";
    public static final int MAX_LIMIT = Integer.MAX_VALUE;

    private Context ctx;
    private pArtapibeta.RequestListener listener;

    private User user;
    private ArrayList<Photo> userPhotos;
    private ArrayList<String> userFollowing;
    private ArrayList<String> userFollowers;
    private ArrayList<Photo> userLikedPhotos;
    private ArrayList<Tag> userTags;
    private ArrayList<Place> userPlaces;
    private ArrayList<String> blockedUsers;


    public UserController(Context ctx) {
        this.ctx = ctx;
    }

    public void setListener(RequestListener listener) {
        this.listener = listener;
    }


    public User getUser() {
        return user;
    }

    public ArrayList<Photo> getPhotoUrl() {
        return userPhotos;
    }

    public ArrayList<String> getUserFollowing() {
        return userFollowing;
    }

    public ArrayList<String> getUserFollowers() {
        return userFollowers;
    }

    public ArrayList<Photo> getUserLikedPhotos() {
        return userLikedPhotos;
    }

    public ArrayList<Tag> getUserTags() {
        return userTags;
    }

    public ArrayList<Place> getUserPlaces() {
        return userPlaces;
    }

    public ArrayList<String> getBlockedUsers() {
        return blockedUsers;
    }


    public void requestUser() {

        assert this.listener != null;
        String url = PicsArtConst.SHOW_USER_URL + "me" + PicsArtConst.API_TEST_PREF + PicsArtConst.APIKEY;
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
        String url = PicsArtConst.SHOW_USER_URL + id + PicsArtConst.API_TEST_PREF + PicsArtConst.APIKEY;
        PARequest req = new PARequest(Request.Method.GET, url, null, null);
        SingletoneRequestQue.getInstance(ctx).addToRequestQueue(req);
        req.setRequestListener(new PARequest.PARequestListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

            @Override
            public void onResponse(Object response) {
                /*Log.d(MY_LOGS, response.toString());
                user = new User();
                user.parseFrom(response);
                UserController.this.listener.onRequestReady(3);*/

                Gson gson = new Gson();

                pArtapibeta.pojo.User target = new pArtapibeta.pojo.User();
                String json = gson.toJson(response); // serializes target to Json
                Log.d(MY_LOGS, "json:  "+json);

                pArtapibeta.pojo.User user1=new pArtapibeta.pojo.User();
                user1=gson.fromJson(json, pArtapibeta.pojo.User.class);

                Log.d(MY_LOGS, "target:  "+user1.getUsername());
            }
        });
    }


    public void requestUserFollowers(User user, final int offset, final int limit) {
        requestUserFollowers(user.getId(), offset, limit);
    }

    public void requestUserFollowers(String userId, final int offset, final int limit) {    //   8

        /**
         * checking argument validation
         */

        if (offset < 0 || limit < 0 || offset > limit) {
            throw new IllegalArgumentException();
        }

        assert this.listener != null;
        userFollowers = new ArrayList<>();

        String url = PicsArtConst.SHOW_USER_FOLLOWERS + userId + PicsArtConst.API_TEST_PREF + PicsArtConst.APIKEY;
        PARequest req = new PARequest(Request.Method.GET, url, null, null);
        SingletoneRequestQue.getInstance(ctx).addToRequestQueue(req);
        req.setRequestListener(new PARequest.PARequestListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }

            @Override
            public void onResponse(Object response) {

                int max_limit;

                try {

                    JSONArray jsonArray = ((JSONObject) response).getJSONArray("response");
                    max_limit = limit >= jsonArray.length() ? jsonArray.length() - 1 : limit;

                    for (int i = offset; i <= max_limit; i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        userFollowers.add(jsonObject.getString("id"));
                        Log.d(MY_LOGS, "follower id:  " + jsonObject.getString("id"));

                    }
                    UserController.this.listener.onRequestReady(8);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public void requestUserFollowing(User user, final int offset, final int limit) {
        requestUserFollowing(user.getId(), offset, limit);
    }

    public void requestUserFollowing(String userId, final int offset, final int limit) {    //   9

        /**
         * checking argument validation
         */

        if (offset < 0 || limit < 0 || offset > limit) {
            throw new IllegalArgumentException();
        }

        assert this.listener != null;
        userFollowing = new ArrayList<>();

        String url = PicsArtConst.SHOW_USER_FOLLOWING + userId + PicsArtConst.API_TEST_PREF + PicsArtConst.APIKEY;
        PARequest req = new PARequest(Request.Method.GET, url, null, null);
        SingletoneRequestQue.getInstance(ctx).addToRequestQueue(req);
        req.setRequestListener(new PARequest.PARequestListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }

            @Override
            public void onResponse(Object response) {

                int max_limit;

                try {

                    JSONArray jsonArray = ((JSONObject) response).getJSONArray("response");
                    max_limit = limit >= jsonArray.length() ? jsonArray.length() - 1 : limit;

                    for (int i = offset; i <= max_limit; i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        userFollowing.add(jsonObject.getString("id"));
                        Log.d(MY_LOGS, "following id:  " + jsonObject.getString("id"));

                    }
                    UserController.this.listener.onRequestReady(9);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public void requestLikedPhotos(User user, final int offset, final int limit) {
        requestLikedPhotos(user.getId(), offset, limit);
    }

    public void requestLikedPhotos(String userId, final int offset, final int limit) {    //   10

        /**
         * checking argument validation
         */

        if (offset < 0 || limit < 0 || offset > limit) {
            throw new IllegalArgumentException();
        }

        assert this.listener != null;
        userLikedPhotos = new ArrayList<>();

        String url = PicsArtConst.SHOW_USER_LIKED_PHOTOS + userId + PicsArtConst.API_TEST_PREF + PicsArtConst.APIKEY;
        PARequest req = new PARequest(Request.Method.GET, url, null, null);
        SingletoneRequestQue.getInstance(ctx).addToRequestQueue(req);
        req.setRequestListener(new PARequest.PARequestListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

            @Override
            public void onResponse(Object response) {

                int max_limit;

                try {

                    JSONArray jsonArray = ((JSONObject) response).getJSONArray("response");
                    max_limit = limit >= jsonArray.length() ? jsonArray.length()-1 : limit;

                    for (int i = offset; i <= max_limit; i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Photo photo = new Photo(jsonObject.getString("id"), new URL(jsonObject.getString("url")), null, null, jsonObject.getJSONObject("user").getString("id"));
                        userLikedPhotos.add(photo);
                        Log.d(MY_LOGS, "liked photo id :  " + photo.getId());

                    }
                    UserController.this.listener.onRequestReady(10);

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

            }
        });
    }


    public void requestBlockedUsers(User user, final int offset, final int limit) {
        requestBlockedUsers(user.getId(), offset, limit);
    }

    public void requestBlockedUsers(String userId, final int offset, final int limit) {    //   4

        /**
         * checking argument validation
         */

        if (offset < 0 || limit < 0 || offset > limit) {
            throw new IllegalArgumentException();
        }

        assert this.listener != null;
        blockedUsers = new ArrayList<>();

        String url = PicsArtConst.SHOW_BLOCKED_USERS + userId + PicsArtConst.API_TEST_PREF + PicsArtConst.APIKEY;
        PARequest req = new PARequest(Request.Method.GET, url, null, null);
        SingletoneRequestQue.getInstance(ctx).addToRequestQueue(req);
        req.setRequestListener(new PARequest.PARequestListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

            @Override
            public void onResponse(Object response) {

                int max_limit;

                try {

                    JSONArray jsonArray = ((JSONObject) response).getJSONArray("response");
                    max_limit = limit >= jsonArray.length() ? jsonArray.length()-1 : limit;

                    for (int i = offset; i <= max_limit; i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        blockedUsers.add(jsonObject.getString("id"));
                        Log.d(MY_LOGS, "blocked user id :  " + jsonObject.getString("id"));

                    }
                    UserController.this.listener.onRequestReady(4);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

    }


    public void requestPlaces(User user, final int offset, final int limit) {
        requestPlaces(user.getId(), offset, limit);
    }

    public void requestPlaces(String userId, final int offset, final int limit) {    //  5

        /**
         * checking argument validation
         */

        if (offset < 0 || limit < 0 || offset > limit) {
            throw new IllegalArgumentException();
        }

        assert this.listener != null;
        userPlaces = new ArrayList<>();

        String url = PicsArtConst.SHOW_USER_PLACES + userId + PicsArtConst.API_TEST_PREF + PicsArtConst.APIKEY;
        PARequest req = new PARequest(Request.Method.GET, url, null, null);
        SingletoneRequestQue.getInstance(ctx).addToRequestQueue(req);
        req.setRequestListener(new PARequest.PARequestListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

            @Override
            public void onResponse(Object response) {
                int max_limit;

                try {

                    JSONArray jsonArray = ((JSONObject) response).getJSONArray("response");
                    max_limit = limit >= jsonArray.length() ? jsonArray.length()-1 : limit;

                    for (int i = offset; i <= max_limit; i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        userPlaces.add(new Place(jsonObject.getString("place")));
                        Log.d(MY_LOGS, jsonObject.getString("place"));

                    }
                    UserController.this.listener.onRequestReady(5);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public void requestTags(User user, final int offset, final int limit) {
        requestTags(user.getId(), offset, limit);
    }

    public void requestTags(String userId, final int offset, final int limit) {    // 6

        /**
         * checking argument validation
         */

        if (offset < 0 || limit < 0 || offset > limit) {
            throw new IllegalArgumentException();
        }

        assert this.listener != null;
        userTags = new ArrayList<>();

        String url = PicsArtConst.SHOW_USER_TAGS + userId + PicsArtConst.API_TEST_PREF + PicsArtConst.APIKEY;
        PARequest req = new PARequest(Request.Method.GET, url, null, null);
        SingletoneRequestQue.getInstance(ctx).addToRequestQueue(req);
        req.setRequestListener(new PARequest.PARequestListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

            @Override
            public void onResponse(Object response) {

                int max_limit;

                try {

                    JSONArray jsonArray = ((JSONObject) response).getJSONArray("response");
                    max_limit = limit >= jsonArray.length() ? jsonArray.length()-1 : limit;

                    for (int i = offset; i <= max_limit; i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        userTags.add(new Tag(jsonObject.getString("tag")));
                        Log.d(MY_LOGS, jsonObject.getString("tag"));

                    }
                    UserController.this.listener.onRequestReady(6);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }


    public void requestUserPhotos(User user, final int offset, final int limit) {
        requestUserPhotos(user.getId(), offset, limit);

    }

    public void requestUserPhotos(String userId, final int offset, final int limit) {    //  7

        /**
         * checking argument validation
         */

        if (offset < 0 || limit < 0 || offset > limit) {
            throw new IllegalArgumentException();
        }

        assert this.listener != null;
        userPhotos = new ArrayList<>();

        String url = PicsArtConst.SHOW_USER_PHOTOS_LIST + userId + PicsArtConst.API_TEST_PREF + PicsArtConst.APIKEY;
        PARequest req = new PARequest(Request.Method.GET, url, null, null);
        SingletoneRequestQue.getInstance(ctx).addToRequestQueue(req);
        req.setRequestListener(new PARequest.PARequestListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }

            @Override
            public void onResponse(Object response) {

                int max_limit;

                try {

                    JSONArray jsonArray = ((JSONObject) response).getJSONArray("response");
                    max_limit = limit >= jsonArray.length() ? jsonArray.length()-1 : limit;

                    for (int i = offset; i <= max_limit; i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Photo photo = new Photo(jsonObject.getString("id"), new URL(jsonObject.getString("url")), null, null, jsonObject.getJSONObject("user").getString("id"));
                        userPhotos.add(photo);
                        Log.d(MY_LOGS, photo.getId());

                    }
                    UserController.this.listener.onRequestReady(7);

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public void blockUserWithID(final String blockingId) {

        assert this.listener != null;
        String url = PicsArtConst.BLOCK_USER_WITH_ID + blockingId + PicsArtConst.API_TEST_PREF + PicsArtConst.APIKEY;

        PARequest req = new PARequest(Request.Method.POST, url, null, null) {

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
        });

        //153741055000102
        //155035207000102

    }

    public void unblockUserWithID(final String unblockingId) {

        assert this.listener != null;
        String url = PicsArtConst.UNBLOCK_USER_WITH_ID + unblockingId + PicsArtConst.API_TEST_PREF + PicsArtConst.APIKEY;

        PARequest req = new PARequest(Request.Method.POST, url, null, null) {

        };

        SingletoneRequestQue.getInstance(ctx).addToRequestQueue(req);
        req.setRequestListener(new PARequest.PARequestListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

            @Override
            public void onResponse(Object response) {

                Log.d(MY_LOGS, response.toString());
                UserController.this.listener.onRequestReady(12);
            }
        });

    }

    public void followUserWithID(final String followingId) {

        assert this.listener != null;
        String url = PicsArtConst.FOLLOW_USER_WITH_ID + followingId + PicsArtConst.API_TEST_PREF + PicsArtConst.APIKEY;

        PARequest req = new PARequest(Request.Method.POST, url, null, null) {

            /*@Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("is_social", followingId);
                return params;
            }*/
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
        });
        //new FollowUserAsyncTask().execute(id);
    }


    public void uploadUserPhoto(Photo photo) {
        new UploadPhotoAsyncTask().execute(photo);
    }

    class UploadPhotoAsyncTask extends AsyncTask<Photo, Void, Void> {

        InputStream is;
        JSONObject jObj;
        String json;
        String url;

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Photo... photo) {

            try {

                Looper.getMainLooper();
                Looper.prepare();

                if (photo[0].getIsFor() == Photo.IS.COVER) {
                    url = PicsArtConst.UPLOAD_COVER_PHOTO + PicsArtConst.API_PREFX + PicsArtConst.APIKEY;
                } else if (photo[0].getIsFor() == Photo.IS.AVATAR) {
                    url = PicsArtConst.UPLOAD_AVATAR_PHOTO + PicsArtConst.API_PREFX + PicsArtConst.APIKEY;
                } else {
                    url = PicsArtConst.UPLOAD_PROFILE_PHOTO + PicsArtConst.API_PREFX + PicsArtConst.APIKEY;
                }

                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                MultipartEntity entity = new MultipartEntity(
                        HttpMultipartMode.BROWSER_COMPATIBLE);
                entity.addPart("file", new FileBody(new File(photo[0].getPath())));
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

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.d(MY_LOGS, "json send:   " + json);
        }
    }

}
