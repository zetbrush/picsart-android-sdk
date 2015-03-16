package pArtapibeta;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Looper;
import android.provider.ContactsContract;
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
import java.util.Objects;


public class UserController {


    public static final String MY_LOGS = "My_Logs";
    public static final int MAX_LIMIT = Integer.MAX_VALUE;

    private Context ctx;
    private pArtapibeta.RequestListener listener;

    private User user;
    private ArrayList<Photo> userPhotos;
    private ArrayList<User> userFollowing;
    private ArrayList<User> userFollowers;
    private ArrayList<Photo> userLikedPhotos;
    private ArrayList<String> userTags;
    private ArrayList<String> userPlaces;
    private ArrayList<User> blockedUsers;

    private static RequestListener st_listener;

    public static RequestListener getSt_listener(int indexNumb) {
        int indx;
        for (RequestListener listener : st_listeners_all)
            if (listener.getIndexOfListener() == indexNumb) {
                indx = st_listeners_all.indexOf(listener);
                return st_listeners_all.get(indx);
            }
        return null;
    }

    public static void setSt_listener(RequestListener st_listener) {
        if (UserController.st_listener == null)
            UserController.st_listener = st_listener;

        if (st_listeners_all.size() == 0) {
            st_listener.setIndexInList(0);
            st_listeners_all.add(st_listener);

        } else {
            int index = st_listener.getIndexOfListener();
            if (st_listeners_all.contains(st_listener) && st_listener.getIndexOfListener() == index) {
                int indToChange = (st_listeners_all.indexOf(st_listener));
                st_listener.setIndexInList(indToChange);
                st_listeners_all.set(indToChange, st_listener);
            } else if (!((st_listeners_all.contains(st_listener)))) {
                int indxToput = st_listeners_all.size();
                st_listener.setIndexInList(indxToput);
                st_listeners_all.add(st_listener);
            }
        }
    }

    public static ArrayList<RequestListener> getSt_listeners_all() {
        return st_listeners_all;
    }

    public static void setSt_listeners_all(ArrayList<RequestListener> st_listeners_all) {
        UserController.st_listeners_all = st_listeners_all;
    }


    static ArrayList<RequestListener> st_listeners_all = new ArrayList<>();


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

    public ArrayList<User> getUserFollowing() {
        return userFollowing;
    }

    public ArrayList<User> getUserFollowers() {
        return userFollowers;
    }

    public ArrayList<Photo> getUserLikedPhotos() {
        return userLikedPhotos;
    }

    public ArrayList<String> getUserTags() {
        return userTags;
    }

    public ArrayList<String> getUserPlaces() {
        return userPlaces;
    }

    public ArrayList<User> getBlockedUsers() {
        return blockedUsers;
    }


    public synchronized void requestUser() {

        assert this.listener != null;
        String url = PicsArtConst.SHOW_USER_URL + "me" + PicsArtConst.API_TEST_PREF + PicsArtConst.APIKEY;
        PARequest req = new PARequest(Request.Method.GET, url, null, null);
        SingletoneRequestQue.getInstance(ctx).addToRequestQueue(req);
        req.setRequestListener(new PARequest.PARequestListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onRequestReady(305, error.toString());
            }

            @Override
            public void onResponse(Object response) {
                Log.d(MY_LOGS, response.toString());
                //user = new User();
                user = UserFactory.parseFrom(response);
                UserController.this.listener.onRequestReady(205, response.toString());
            }
        });
    }

    public synchronized void requestUser(String id) {     //    3

        assert this.listener != null;
        String url = PicsArtConst.SHOW_USER_URL + id + PicsArtConst.API_TEST_PREF + PicsArtConst.APIKEY;
        PARequest req = new PARequest(Request.Method.GET, url, null, null);
        SingletoneRequestQue.getInstance(ctx).addToRequestQueue(req);
        req.setRequestListener(new PARequest.PARequestListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                listener.onRequestReady(302, error.toString());
            }

            @Override
            public void onResponse(Object response) {
                /*Log.d(MY_LOGS, response.toString());
                user = new User();
                user.parseFrom(response);
                UserController.this.listener.onRequestReady(3);*/

                user = UserFactory.parseFrom(response);
                listener.onRequestReady(202, response.toString());
            }
        });
    }


    public synchronized void requestUserFollowers(User user, final int offset, final int limit) {
         requestUserFollowers(user.getId().toString(), offset, limit);
    }

    public synchronized void requestUserFollowers(String userId, final int offset, final int limit) {    //   8

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
                UserController.this.listener.onRequestReady(308, error.toString());
            }

            @Override
            public void onResponse(Object response) {

                /*int max_limit;


                JSONArray jsonArray = null;
                try {
                    jsonArray = ((JSONObject) response).getJSONArray("response");
                    Log.d(MY_LOGS,response.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                max_limit = limit >= jsonArray.length() ? jsonArray.length() - 1 : limit;

                    //userFollowers=UserFactory.parseFromAsArray(response);
                    for (int i = offset; i <= max_limit; i++) {

                        JSONObject jsonObject = null;
                        try {
                            jsonObject = jsonArray.getJSONObject(i);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Gson gson=new Gson();

                        userFollowers.add(gson.fromJson(jsonObject.toString(),User.class));
                        try {
                            Log.d(MY_LOGS, "follower id:  " + jsonObject.getString("id"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }*/
                userFollowers = UserFactory.parseFromAsArray(response, offset, limit);
                UserController.this.listener.onRequestReady(208, response.toString());

            }
        });
    }


    public synchronized void requestUserFollowing(User user, final int offset, final int limit) {
        requestUserFollowing(user.getId().toString(), offset, limit);
    }

    public synchronized void requestUserFollowing(String userId, final int offset, final int limit) {    //   9

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
                UserController.this.listener.onRequestReady(309, error.toString());
            }

            @Override
            public void onResponse(Object response) {

                /*int max_limit;

                try {

                    JSONArray jsonArray = ((JSONObject) response).getJSONArray("response");
                    max_limit = limit >= jsonArray.length() ? jsonArray.length() - 1 : limit;

                    for (int i = offset; i <= max_limit; i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        userFollowing.add(jsonObject.getString("id"));
                        //Log.d(MY_LOGS, "following id:  " + jsonObject.getString("id"));

                    }
                    UserController.this.listener.onRequestReady(209, response.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }*/
                userFollowing = UserFactory.parseFromAsArray(response, offset, limit);
                listener.onRequestReady(209, response.toString());
            }
        });
    }


    public synchronized void requestLikedPhotos(User user, final int offset, final int limit) {
        requestLikedPhotos(user.getId().toString(), offset, limit);
    }

    public synchronized void requestLikedPhotos(String userId, final int offset, final int limit) {    //   10

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
                UserController.this.listener.onRequestReady(310, error.toString());

            }

            @Override
            public void onResponse(Object response) {

                int max_limit;

                try {

                    JSONArray jsonArray = ((JSONObject) response).getJSONArray("response");
                    max_limit = limit >= jsonArray.length() ? jsonArray.length() - 1 : limit;

                    for (int i = offset; i <= max_limit; i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Photo photo = PhotoFactory.parseFrom(jsonObject);
                        //Photo photo = new Photo(jsonObject.getString("id"), new URL(jsonObject.getString("url")), null, null, jsonObject.getJSONObject("user").getString("id"));
                        userLikedPhotos.add(photo);
                        Log.d(MY_LOGS, "liked photo id :  " + photo.getId());

                    }
                    UserController.this.listener.onRequestReady(210, response.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }


    public synchronized void requestBlockedUsers(User user, final int offset, final int limit) {
        requestBlockedUsers(user.getId().toString(), offset, limit);
    }

    public synchronized void requestBlockedUsers(String userId, final int offset, final int limit) {    //   4

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
                UserController.this.listener.onRequestReady(304, error.toString());

            }

            @Override
            public void onResponse(Object response) {

                blockedUsers=UserFactory.parseFromAsArray(response,offset,limit);
                UserController.this.listener.onRequestReady(204, response.toString());

                /*int max_limit;

                try {

                    JSONArray jsonArray = ((JSONObject) response).getJSONArray("response");
                    max_limit = limit >= jsonArray.length() ? jsonArray.length() - 1 : limit;

                    for (int i = offset; i <= max_limit; i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        blockedUsers.add(jsonObject.getString("id"));
                        //Log.d(MY_LOGS, "blocked user id :  " + jsonObject.getString("id"));

                    }
                    UserController.this.listener.onRequestReady(204, response.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
*/

            }
        });

    }


    public synchronized void requestPlaces(User user, final int offset, final int limit) {
        requestPlaces(user.getId().toString(), offset, limit);
    }

    public synchronized void requestPlaces(String userId, final int offset, final int limit) {    //  5

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
                UserController.this.listener.onRequestReady(305, error.toString());

            }

            @Override
            public void onResponse(Object response) {
                int max_limit;

                try {

                    JSONArray jsonArray = ((JSONObject) response).getJSONArray("response");
                    max_limit = limit >= jsonArray.length() ? jsonArray.length() - 1 : limit;

                    for (int i = offset; i <= max_limit; i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        userPlaces.add(jsonObject.getString("place"));
                        //Log.d(MY_LOGS, jsonObject.getString("place"));

                    }
                    UserController.this.listener.onRequestReady(205, response.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public synchronized void requestTags(User user, final int offset, final int limit) {
        requestTags(user.getId().toString(), offset, limit);
    }

    public synchronized void requestTags(String userId, final int offset, final int limit) {    // 6

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
                UserController.this.listener.onRequestReady(306, error.toString());

            }

            @Override
            public void onResponse(Object response) {

                int max_limit;

                try {

                    JSONArray jsonArray = ((JSONObject) response).getJSONArray("response");
                    max_limit = limit >= jsonArray.length() ? jsonArray.length() - 1 : limit;

                    for (int i = offset; i <= max_limit; i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        userTags.add(jsonObject.getString("tag"));
                        //Log.d(MY_LOGS, jsonObject.getString("tag"));

                    }
                    UserController.this.listener.onRequestReady(206, response.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }


    public synchronized void requestUserPhotos(User user, final int offset, final int limit) {
        requestUserPhotos(user.getId().toString(), offset, limit);
    }

    public synchronized void requestUserPhotos(String userId, final int offset, final int limit) {    //  7

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
                UserController.this.listener.onRequestReady(307, error.toString());

            }

            @Override
            public void onResponse(Object response) {

                int max_limit;

                try {

                    JSONArray jsonArray = ((JSONObject) response).getJSONArray("response");
                    max_limit = limit >= jsonArray.length() ? jsonArray.length() - 1 : limit;

                    for (int i = offset; i <= max_limit; i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Photo photo = PhotoFactory.parseFrom(jsonObject);
                        //Photo photo = new Photo(jsonObject.getString("id"), new URL(jsonObject.getString("url")), null, null, jsonObject.getJSONObject("user").getString("id"));
                        userPhotos.add(photo);
                        Log.d(MY_LOGS, photo.getId());

                    }
                    UserController.this.listener.onRequestReady(207, response.toString());

                } catch (JSONException e) {
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
                UserController.this.listener.onRequestReady(311, error.toString());

            }

            @Override
            public void onResponse(Object response) {

                Log.d(MY_LOGS, response.toString());
                UserController.this.listener.onRequestReady(211, response.toString());
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
                UserController.this.listener.onRequestReady(312, error.toString());

            }

            @Override
            public void onResponse(Object response) {

                Log.d(MY_LOGS, response.toString());
                UserController.this.listener.onRequestReady(212, response.toString());
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
                UserController.this.listener.onRequestReady(311, error.toString());

            }

            @Override
            public void onResponse(Object response) {

                Log.d(MY_LOGS, response.toString());
                UserController.this.listener.onRequestReady(211, response.toString());
            }
        });
        //new FollowUserAsyncTask().execute(id);
    }


}
