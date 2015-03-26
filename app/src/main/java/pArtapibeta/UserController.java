package pArtapibeta;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class UserController {


    public static final String MY_LOGS = "My_Logs";
    public static final int MAX_LIMIT = Integer.MAX_VALUE;

    private Context ctx;
    private pArtapibeta.RequestListener listener;
    private String accessToken;

    private User user;
    private ArrayList<Photo> userPhotos;
    private ArrayList<User> userFollowing;
    private ArrayList<User> userFollowers;
    private ArrayList<Photo> userLikedPhotos;
    private ArrayList<String> userTags;
    private ArrayList<Photo> userPlaces;
    private ArrayList<User> blockedUsers;

    private static RequestListener st_listener;
    private static ArrayList<RequestListener> st_listeners_all = new ArrayList<>();


    public UserController(String token, Context ctx) {
        this.ctx = ctx;
        this.accessToken = token;
    }


    public void setListener(RequestListener listener) {
        this.listener = listener;
    }


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


    public User getUser() {
        return user;
    }

    public ArrayList<Photo> getPhoto() {
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

    public ArrayList<Photo> getUserPlaces() {
        return userPlaces;
    }

    public ArrayList<User> getBlockedUsers() {
        return blockedUsers;
    }


    /**
     * Request User Profile
     * <p/>
     * onResponse 205 code will be called in listener
     * onErrorResponse 305 code will be called in listener
     */
    public synchronized void requestUser() {

        if(listener==null){
            return;
        }

        String url = PicsArtConst.SHOW_USER + "me" + PicsArtConst.TOKEN_PREFIX + accessToken;
        PARequest req = new PARequest(Request.Method.GET, url, null, null);
        SingletoneRequestQue.getInstance(ctx).addToRequestQueue(req);
        req.setRequestListener(new PARequest.PARequestListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onRequestReady(301, error.toString());
            }

            @Override
            public void onResponse(Object response) {
                Log.d(MY_LOGS, response.toString());
                user = UserFactory.parseFrom(response);
                UserController.this.listener.onRequestReady(201, response.toString());
            }
        });
    }

    /**
     * Request User Profile with ID
     *
     * @param id ID of the User
     *           <p/>
     *           onResponse 202 code will be called in listener
     *           onErrorResponse 302 code will be called in listener
     */
    public synchronized void requestUser(String id) {

        if(listener==null){
            Log.e("Error","null listener error");
            return;
        }
        if(id==null){
            Log.e("Error","null id");
            return;
        }
        String url = PicsArtConst.SHOW_USER + id + PicsArtConst.TOKEN_PREFIX + accessToken;
        PARequest req = new PARequest(Request.Method.GET, url, null, null);
        SingletoneRequestQue.getInstance(ctx).addToRequestQueue(req);
        req.setRequestListener(new PARequest.PARequestListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                listener.onRequestReady(302, error.toString());
            }

            @Override
            public void onResponse(Object response) {

                Log.d(MY_LOGS, response.toString());
                user = UserFactory.parseFrom(response);
                listener.onRequestReady(202, response.toString());
            }
        });
    }


    /**
     * Requests Followers of the User
     *
     * @param user   user(to examine)
     * @param offset starting point
     * @param limit  limit of users
     *               <p/>
     *               onResponse 208 code will be called in listener
     *               onErrorResponse 308 code will be called in listener
     */
    public synchronized void requestUserFollowers(User user, final int offset, final int limit) {
        requestUserFollowers(user.getId().toString(), offset, limit);
    }

    /**
     * Requests Followers of the User
     *
     * @param userId ID of the User
     * @param offset starting point
     * @param limit  limit of users
     *               <p/>
     *               onResponse 208 code will be called in listener
     *               onErrorResponse 308 code will be called in listener
     */
    public synchronized void requestUserFollowers(String userId, final int offset, final int limit) {

        if(listener==null){
            Log.e("Error","null listener error");
            return;
        }
        if(userId==null){
            Log.e("Error","null id");
            return;
        }
        userFollowers = new ArrayList<>();

        String url = PicsArtConst.SHOW_USER + userId + PicsArtConst.FOLLOWERS_PREFIX + PicsArtConst.TOKEN_PREFIX + accessToken;
        PARequest req = new PARequest(Request.Method.GET, url, null, null);
        SingletoneRequestQue.getInstance(ctx).addToRequestQueue(req);
        req.setRequestListener(new PARequest.PARequestListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                UserController.this.listener.onRequestReady(308, error.toString());
            }

            @Override
            public void onResponse(Object response) {

                userFollowers = UserFactory.parseFromAsArray(response, offset, limit, "response");
                UserController.this.listener.onRequestReady(208, response.toString());

            }
        });
    }


    /**
     * Requests Following of the User
     *
     * @param user   user(to examine)
     * @param offset starting point
     * @param limit  limit of users
     *               <p/>
     *               onResponse 209 code will be called in listener
     *               onErrorResponse 309 code will be called in listener
     */
    public synchronized void requestUserFollowing(User user, final int offset, final int limit) {
        requestUserFollowing(user.getId().toString(), offset, limit);
    }

    /**
     * Requests Following of the User
     *
     * @param userId ID of the User
     * @param offset starting point
     * @param limit  limit of users
     *               <p/>
     *               onResponse 209 code will be called in listener
     *               onErrorResponse 309 code will be called in listener
     */
    public synchronized void requestUserFollowing(String userId, final int offset, final int limit) {

        if(listener==null){
            Log.e("Error","null listener error");
            return;
        }
        if(userId==null){
            Log.e("Error","null id");
            return;
        }
        userFollowing = new ArrayList<>();

        String url = PicsArtConst.SHOW_USER + userId + PicsArtConst.FOLLOWING_PREFIX + PicsArtConst.TOKEN_PREFIX + accessToken;
        PARequest req = new PARequest(Request.Method.GET, url, null, null);
        SingletoneRequestQue.getInstance(ctx).addToRequestQueue(req);
        req.setRequestListener(new PARequest.PARequestListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                UserController.this.listener.onRequestReady(309, error.toString());
            }

            @Override
            public void onResponse(Object response) {

                userFollowing = UserFactory.parseFromAsArray(response, offset, limit, "response");
                listener.onRequestReady(209, response.toString());
            }
        });
    }


    /**
     * RequestsLiked Photos of the User
     *
     * @param user   user(to examine)
     * @param offset starting point
     * @param limit  limit of users
     *               <p/>
     *               onResponse  210 code will be called in listener
     *               onErrorResponse 310 code will be called in listener
     */
    public synchronized void requestLikedPhotos(User user, final int offset, final int limit) {
        requestLikedPhotos(user.getId().toString(), offset, limit);
    }

    /**
     * Requests Liked Photos of the User
     *
     * @param userId ID of the User
     * @param offset starting point
     * @param limit  limit of users
     *               <p/>
     *               onResponse 210 code will be called in listener
     *               onErrorResponse 310 code will be called in listener
     */
    public synchronized void requestLikedPhotos(String userId, final int offset, final int limit) {

        if(listener==null){
            Log.e("Error","null listener error");
            return;
        }
        if(userId==null){
            Log.e("Error","null id");
            return;
        }
        userLikedPhotos = new ArrayList<>();

        String url = PicsArtConst.SHOW_USER + userId + PicsArtConst.LIKED_PHOTOS_PREFIX + PicsArtConst.TOKEN_PREFIX + accessToken;
        PARequest req = new PARequest(Request.Method.GET, url, null, null);
        SingletoneRequestQue.getInstance(ctx).addToRequestQueue(req);
        req.setRequestListener(new PARequest.PARequestListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                UserController.this.listener.onRequestReady(310, error.toString());

            }

            @Override
            public void onResponse(Object response) {

                userLikedPhotos = PhotoFactory.parseFromAsArray(response, offset, limit, "likes");
                UserController.this.listener.onRequestReady(210, response.toString());

            }
        });
    }


    /**
     * Requests Blocked Users of the User
     *
     * @param user   user(to examine)
     * @param offset starting point
     * @param limit  limit of users
     *               <p/>
     *               onResponse  204 code will be called in listener
     *               onErrorResponse 304 code will be called in listener
     */
    public synchronized void requestBlockedUsers(User user, final int offset, final int limit) {
        requestBlockedUsers(user.getId().toString(), offset, limit);
    }

    /**
     * Requests Blocked Users of the User
     *
     * @param userId ID of the User
     * @param offset starting point
     * @param limit  limit of users
     *               <p/>
     *               onResponse  204 code will be called in listener
     *               onErrorResponse 304 code will be called in listener
     */
    public synchronized void requestBlockedUsers(String userId, final int offset, final int limit) {

        if(listener==null){
            Log.e("Error","null listener error");
            return;
        }
        if(userId==null){
            Log.e("Error","null id");
            return;
        }
        blockedUsers = new ArrayList<>();

        String url = PicsArtConst.SHOW_USER + userId + PicsArtConst.BLOCKED_PREFIX + PicsArtConst.TOKEN_PREFIX + accessToken;
        PARequest req = new PARequest(Request.Method.GET, url, null, null);
        SingletoneRequestQue.getInstance(ctx).addToRequestQueue(req);
        req.setRequestListener(new PARequest.PARequestListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                UserController.this.listener.onRequestReady(304, error.toString());

            }

            @Override
            public void onResponse(Object response) {

                //Log.d(MY_LOGS, response.toString());
                blockedUsers = UserFactory.parseFromAsArray(response, offset, limit, "blocks");
                UserController.this.listener.onRequestReady(204, response.toString());

            }
        });

    }


    /**
     * Requests Places of the User
     *
     * @param user   user(to examine)
     * @param offset starting point
     * @param limit  limit of users
     *               <p/>
     *               onResponse  205 code will be called in listener
     *               onErrorResponse 305 code will be called in listener
     */
    public synchronized void requestPlaces(User user, final int offset, final int limit) {
        requestPlaces(user.getId().toString(), offset, limit);
    }

    /**
     * Requests Places of the User
     *
     * @param userId ID of the User
     * @param offset starting point
     * @param limit  limit of users
     *               <p/>
     *               onResponse 205 code will be called in listener
     *               onErrorResponse 305 code will be called in listener
     */
    public synchronized void requestPlaces(String userId, final int offset, final int limit) {    //  5

        if(userId==null || userId==""){
            Log.e("Error","null id");
            return;
        }
        userPlaces = new ArrayList<>();

        String url = PicsArtConst.SHOW_USER + userId + PicsArtConst.PLACES_PREFIX + PicsArtConst.TOKEN_PREFIX + accessToken;
        PARequest req = new PARequest(Request.Method.GET, url, null, null);
        SingletoneRequestQue.getInstance(ctx).addToRequestQueue(req);
        req.setRequestListener(new PARequest.PARequestListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                UserController.this.listener.onRequestReady(305, error.toString());

            }

            @Override
            public void onResponse(Object response) {

                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(((JSONObject) response).get("places").toString());
                    //Log.d(MY_LOGS, "" + jsonArray.get(0).toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject jsonObject1 = new JSONObject(jsonArray.get(i).toString());
                        JSONArray jsonArray1 = new JSONArray(jsonObject1.get("photos").toString());
                        JSONObject jsonObject2 = jsonArray1.getJSONObject(0);
                        userLikedPhotos.add(PhotoFactory.parseFrom(jsonObject2));
                        //Log.d(MY_LOGS, "" + PhotoFactory.parseFrom(jsonObject2).getLocation().getPlace());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                UserController.this.listener.onRequestReady(205, response.toString());

            }
        });
    }


    /**
     * Requests Tags of the User
     *
     * @param user   user(to examine)
     * @param offset starting point
     * @param limit  limit of users
     *               <p/>
     *               onResponse 206 code will be called in listener
     *               onErrorResponse 306 code will be called in listener
     */
    public synchronized void requestTags(User user, final int offset, final int limit) {
        requestTags(user.getId().toString(), offset, limit);
    }

    /**
     * Requests Tags of the User
     *
     * @param userId ID of the User
     * @param offset starting point
     * @param limit  limit of users
     *               <p/>
     *               onResponse 206 code will be called in listener
     *               onErrorResponse 306 code will be called in listener
     */
    public synchronized void requestTags(String userId, final int offset, final int limit) {    // 6

        if(listener==null){
            Log.e("Error","null listener error");
            return;
        }
        if(userId==null){
            Log.e("Error","null id");
            return;
        }
        userTags = new ArrayList<>();

        String url = PicsArtConst.SHOW_USER + userId + PicsArtConst.TAGS_PREFIX + PicsArtConst.TOKEN_PREFIX + accessToken;
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

                    JSONArray jsonArray = ((JSONObject) response).getJSONArray("tags");
                    max_limit = limit >= jsonArray.length() ? jsonArray.length() - 1 : limit;

                    for (int i = offset; i <= max_limit; i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        userTags.add(jsonObject.getString("tag"));
                        Log.d(MY_LOGS, jsonObject.getString("tag"));

                    }
                    UserController.this.listener.onRequestReady(206, response.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }


    /**
     * Requests Photos of the User
     *
     * @param user   user(to examine)
     * @param offset starting point
     * @param limit  limit of users
     *               <p/>
     *               onResponse 207 code will be called in listener
     *               onErrorResponse 307 code will be called in listener
     */
    public synchronized void requestUserPhotos(User user, final int offset, final int limit) {
        requestUserPhotos(user.getId().toString(), offset, limit);
    }

    /**
     * Requests Photos of the User
     *
     * @param userId ID of the User
     * @param offset starting point
     * @param limit  limit of users
     *               <p/>
     *               onResponse 207 code will be called in listener
     *               onErrorResponse 307 code will be called in listener
     */
    public synchronized void requestUserPhotos(String userId, final int offset, final int limit) {    //  7

        if(listener==null){
            Log.e("Error","null listener error");
            return;
        }
        if(userId==null){
            Log.e("Error","null id");
            return;
        }
        userPhotos = new ArrayList<>();

        String url = PicsArtConst.SHOW_USER + userId + PicsArtConst.PHOTOS_PREFIX + PicsArtConst.TOKEN_PREFIX + accessToken;
        PARequest req = new PARequest(Request.Method.GET, url, null, null);
        SingletoneRequestQue.getInstance(ctx).addToRequestQueue(req);
        req.setRequestListener(new PARequest.PARequestListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                UserController.this.listener.onRequestReady(307, error.toString());

            }

            @Override
            public void onResponse(Object response) {

                userPhotos = PhotoFactory.parseFromAsArray(response, offset, limit, "photos");
                UserController.this.listener.onRequestReady(207, response.toString());
            }
        });
    }


    /**
     * Requests for Blocking User with ID
     *
     * @param blockingId ID of Blocking User
     *                   <p/>
     *                   onResponse 211 code will be called in listener
     *                   onErrorResponse 311 code will be called in listener
     */
    public void blockUserWithID(final String blockingId) {

        if(listener==null){
            Log.e("Error","null listener error");
            return;
        }
        if(blockingId==null){
            Log.e("Error","null id");
            return;
        }
        String url = PicsArtConst.SHOW_USER + "me" + PicsArtConst.BLOCKED_PREFIX + PicsArtConst.TOKEN_PREFIX + accessToken;
        StringRequest req = new StringRequest(Request.Method.POST, url,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(MY_LOGS, response.toString());
                        listener.onRequestReady(211, response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onRequestReady(311, error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("block_id", blockingId);
                return params;

            }
        };

        SingletoneRequestQue.getInstance(MainActivity.getAppContext()).addToRequestQueue(req);

        //153741055000102
        //155035207000102

    }

    /**
     * Requests for Unblocking User with ID
     *
     * @param unblockingId ID of Unblocking User
     *                     <p/>
     *                     onResponse 212 code will be called in listener
     *                     onErrorResponse 312 code will be called in listener
     */
    public void unblockUserWithID(final String unblockingId) {

        if(listener==null){
            Log.e("Error","null listener error");
            return;
        }
        if(unblockingId==null){
            Log.e("Error","null id");
            return;
        }
        String url = PicsArtConst.SHOW_USER + "me" + PicsArtConst.BLOCKED_PREFIX + "/" + unblockingId + PicsArtConst.TOKEN_PREFIX + accessToken;

        PARequest req = new PARequest(Request.Method.DELETE, url, null, null) {
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

    /**
     * Requests for Following User with ID
     *
     * @param followingId ID of Following User
     *                    <p/>
     *                    onResponse 217 code will be called in listener
     *                    onErrorResponse 317 code will be called in listener
     */
    public void followUserWithID(final String followingId) {

        if(listener==null){
            Log.e("Error","null listener error");
            return;
        }
        if(followingId==null){
            Log.e("Error","null id");
            return;
        }
        String url = PicsArtConst.SHOW_USER + "me" + PicsArtConst.FOLLOWING_PREFIX + PicsArtConst.TOKEN_PREFIX + accessToken;
        StringRequest req = new StringRequest(Request.Method.POST, url,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(MY_LOGS, response.toString());
                        listener.onRequestReady(217, response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onRequestReady(317, error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("following_id", followingId);
                return params;

            }
        };

        SingletoneRequestQue.getInstance(MainActivity.getAppContext()).addToRequestQueue(req);

    }


    public static void notifyListeners(int reqnumber, String msg) {
        for (RequestListener listeners : getSt_listeners_all()) {
            listeners.onRequestReady(reqnumber, msg);

        }
    }

    public static void notifyListener(int listenerNumb, int reqNumb, String msg) {
        try {
            getSt_listener(listenerNumb).onRequestReady(reqNumb, msg);
        } catch (NullPointerException e) {
            Log.e("Listener Error: ", "Non Existing Listener with index " + listenerNumb);
        }
    }


}
