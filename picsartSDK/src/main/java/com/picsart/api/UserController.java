package com.picsart.api;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * This class consists exclusively of void methods, that operate on making
 * requests and initializing specific field.
 * <p/>
 * Some Getters of this class  throw a <tt>NullPointerException</tt>
 * if the collections or class objects provided to them are null.
 * <p/>
 * This class is a member of the
 * www.picsart.com
 */
public class UserController {


    public static final String MY_LOGS = "My_Logs";
    public static final String ERROR = "Error";
    public static final String NULL_ID_ERROR = "Error : Null ID";
    public static final String EMPTY_ID = "";
    public static final String RESPONSE = "response";
    public static final String LIKES = "likes";
    public static final String BLOCKS = "blocks";
    public static final String PLACES = "places";
    public static final String PHOTOS = "photos";
    public static final String TAGS = "tags";
    public static final String TAG = "tag";

    public static final int MAX_LIMIT = Integer.MAX_VALUE;

    private Context ctx;
    private RequestListener listener;

    public static void setAccessToken(String accessToken) {
        UserController.accessToken = accessToken;
    }

    public static String getAccessToken() {
        return accessToken;
    }

    private static String accessToken;

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

    public UserController(Context ctx) {
        this.ctx = ctx;

    }

    public void setListener(RequestListener listener) {
        this.listener = listener;
    }


    public static RequestListener getListener(int indexNumb) {
        int indx;
        for (RequestListener listener : st_listeners_all)
            if (listener.getIndexOfListener() == indexNumb) {
                indx = st_listeners_all.indexOf(listener);
                return st_listeners_all.get(indx);
            }
        return null;
    }

    public static void resgisterListener(RequestListener st_listener) {

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


    public static ArrayList<RequestListener> getRegisteredListeners() {
        return st_listeners_all;
    }

    public static void setListeners(ArrayList<RequestListener> st_listeners_all) {
        UserController.st_listeners_all = st_listeners_all;
    }


    public User getUser() {
        return user;
    }

    public ArrayList<Photo> getPhotos() {
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
     * onResponse 201 code will be called in listener
     * onErrorResponse 301 code will be called in listener
     */
    public synchronized void requestUser() {

        String url = PicsArtConst.SHOW_USER + PicsArtConst.ME_PREFIX + PicsArtConst.TOKEN_PREFIX + accessToken;
        PARequest req = new PARequest(Request.Method.GET, url, null, null);
        SingletoneRequestQue.getInstance(ctx).addToRequestQueue(req);
        req.setRequestListener(new PARequest.PARequestListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onRequestReady(PicsArtConst.REQ_BAD_USER, error.toString());
            }

            @Override
            public void onResponse(Object response) {
                //Log.d(MY_LOGS, response.toString());
                user = UserFactory.parseFrom(response);
                UserController.this.listener.onRequestReady(PicsArtConst.REQ_OK_USER, response.toString());
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

        if (id == null || id == EMPTY_ID) {
            Log.e(ERROR, NULL_ID_ERROR);
            return;
        }
        String url = PicsArtConst.SHOW_USER + id + PicsArtConst.TOKEN_PREFIX + accessToken;
        PARequest req = new PARequest(Request.Method.GET, url, null, null);
        SingletoneRequestQue.getInstance(ctx).addToRequestQueue(req);
        req.setRequestListener(new PARequest.PARequestListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                listener.onRequestReady(PicsArtConst.REQ_BAD_USER_ID, error.toString());

            }

            @Override
            public void onResponse(Object response) {

                //Log.d(MY_LOGS, response.toString());
                user = UserFactory.parseFrom(response);
                listener.onRequestReady(PicsArtConst.REQ_OK_USER_ID, response.toString());

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
     *               onResponse 203 code will be called in listener
     *               onErrorResponse 303 code will be called in listener
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
     *               onResponse 203 code will be called in listener
     *               onErrorResponse 303 code will be called in listener
     */
    public synchronized void requestUserFollowers(String userId, final int offset, final int limit) {

        if (userId == null || userId == EMPTY_ID) {
            Log.e(ERROR, NULL_ID_ERROR);
            return;
        }
        userFollowers = new ArrayList<>();

        String url = PicsArtConst.SHOW_USER + userId + PicsArtConst.FOLLOWERS_PREFIX + PicsArtConst.TOKEN_PREFIX + accessToken;
        PaArrayRequest req = new PaArrayRequest(url, null, null);
        SingletoneRequestQue.getInstance(ctx).addToRequestQueue(req);
        req.setRequestListener(new PaArrayRequest.PARequestListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                UserController.this.listener.onRequestReady(PicsArtConst.REQ_BAD_FOLLOWER, error.toString());
            }

            @Override
            public void onResponse(Object response) {

                userFollowers = UserFactory.parseFromArray(response, offset, limit);
                UserController.this.listener.onRequestReady(PicsArtConst.REQ_OK_FOLLOWER, response.toString());

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
     *               onResponse 204 code will be called in listener
     *               onErrorResponse 304 code will be called in listener
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
     *               onResponse 204 code will be called in listener
     *               onErrorResponse 304 code will be called in listener
     */
    public synchronized void requestUserFollowing(String userId, final int offset, final int limit) {

        if (userId == null || userId == EMPTY_ID) {
            Log.e(ERROR, NULL_ID_ERROR);
            return;
        }
        userFollowing = new ArrayList<>();

        String url = PicsArtConst.SHOW_USER + userId + PicsArtConst.FOLLOWING_PREFIX + PicsArtConst.TOKEN_PREFIX + accessToken;
        PaArrayRequest req = new PaArrayRequest(url, null, null);
        SingletoneRequestQue.getInstance(ctx).addToRequestQueue(req);
        req.setRequestListener(new PaArrayRequest.PARequestListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                UserController.this.listener.onRequestReady(PicsArtConst.REQ_BAD_FOLLOWING, error.toString());
            }

            @Override
            public void onResponse(Object response) {

                userFollowing = UserFactory.parseFromArray(response, offset, limit);
                listener.onRequestReady(PicsArtConst.REQ_OK_FOLLOWING, response.toString());
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
     *               onResponse  205 code will be called in listener
     *               onErrorResponse 305 code will be called in listener
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
     *               onResponse 205 code will be called in listener
     *               onErrorResponse 305 code will be called in listener
     */
    public synchronized void requestLikedPhotos(String userId, final int offset, final int limit) {

        if (userId == null || userId == EMPTY_ID) {
            Log.e(ERROR, NULL_ID_ERROR);
            return;
        }
        userLikedPhotos = new ArrayList<>();

        String url = PicsArtConst.SHOW_USER + userId + PicsArtConst.LIKED_PHOTOS_PREFIX + PicsArtConst.TOKEN_PREFIX + accessToken;
        PaArrayRequest req = new PaArrayRequest(url, null, null);
        SingletoneRequestQue.getInstance(ctx).addToRequestQueue(req);
        req.setRequestListener(new PaArrayRequest.PARequestListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                UserController.this.listener.onRequestReady(PicsArtConst.REQ_BAD_LIKED_PHOTO, error.toString());

            }

            @Override
            public void onResponse(Object response) {

                userLikedPhotos = PhotoFactory.parseFromArray(response, offset, limit);
                UserController.this.listener.onRequestReady(PicsArtConst.REQ_OK_LIKED_PHOTO, response.toString());

            }
        });
    }


    /**
     * Requests Blocked Users of the User
     *
     * @param offset starting point
     * @param limit  limit of users
     *               <p/>
     *               onResponse  206 code will be called in listener
     *               onErrorResponse 306 code will be called in listener
     */
    public synchronized void requestBlockedUsers(final int offset, final int limit) {

        blockedUsers = new ArrayList<>();

        String url = PicsArtConst.SHOW_USER + PicsArtConst.ME_PREFIX + PicsArtConst.BLOCKED_PREFIX + PicsArtConst.TOKEN_PREFIX + accessToken;
        PaArrayRequest req = new PaArrayRequest(url, null, null);
        SingletoneRequestQue.getInstance(ctx).addToRequestQueue(req);
        req.setRequestListener(new PaArrayRequest.PARequestListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                UserController.this.listener.onRequestReady(PicsArtConst.REQ_BAD_BLOCKED, error.toString());

            }

            @Override
            public void onResponse(Object response) {

                blockedUsers = UserFactory.parseFromArray(response, offset, limit);
                UserController.this.listener.onRequestReady(PicsArtConst.REQ_OK_BLOCKED, response.toString());

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
     *               onResponse  207 code will be called in listener
     *               onErrorResponse 307 code will be called in listener
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
     *               onResponse 207 code will be called in listener
     *               onErrorResponse 307 code will be called in listener
     */
    public synchronized void requestPlaces(String userId, final int offset, final int limit) {    //  5

        if (userId == null || userId == EMPTY_ID) {
            Log.e(ERROR, NULL_ID_ERROR);
            return;
        }
        userPlaces = new ArrayList<>();

        String url = PicsArtConst.SHOW_USER + userId + PicsArtConst.PLACES_PREFIX + PicsArtConst.TOKEN_PREFIX + accessToken;
        PaArrayRequest req = new PaArrayRequest(url, null, null);
        SingletoneRequestQue.getInstance(ctx).addToRequestQueue(req);
        req.setRequestListener(new PaArrayRequest.PARequestListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                UserController.this.listener.onRequestReady(PicsArtConst.REQ_BAD_PLACES, error.toString());

            }

            @Override
            public void onResponse(Object response) {

                JSONArray jsonArray = null;
                try {
                    jsonArray = (JSONArray) response;

                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {

                            JSONObject jsonObject1 = new JSONObject(jsonArray.get(i).toString());
                            JSONArray jsonArray1 = new JSONArray(jsonObject1.get(PHOTOS).toString());
                            JSONObject jsonObject2 = jsonArray1.getJSONObject(0);
                            userPlaces.add(PhotoFactory.parseFrom(jsonObject2));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    UserController.this.listener.onRequestReady(PicsArtConst.REQ_OK_PLACES, response.toString());

                } catch (Exception e) {
                    UserController.this.listener.onRequestReady(PicsArtConst.REQ_BAD_PLACES, e.toString());
                }


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
     *               onResponse 208 code will be called in listener
     *               onErrorResponse 308 code will be called in listener
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
     *               onResponse 208 code will be called in listener
     *               onErrorResponse 308 code will be called in listener
     */
    public synchronized void requestTags(String userId, final int offset, final int limit) {    // 6

        if (userId == null || userId == EMPTY_ID) {
            Log.e(ERROR, NULL_ID_ERROR);
            return;
        }
        userTags = new ArrayList<>();
        String url = PicsArtConst.SHOW_USER + userId + PicsArtConst.TAGS_PREFIX + PicsArtConst.TOKEN_PREFIX + accessToken;
        PaArrayRequest req = new PaArrayRequest(url, null, null);
        SingletoneRequestQue.getInstance(ctx).addToRequestQueue(req);
        req.setRequestListener(new PaArrayRequest.PARequestListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                UserController.this.listener.onRequestReady(PicsArtConst.REQ_BAD_TAGS, error.toString());

            }

            @Override
            public void onResponse(Object response) {

                JSONArray jsonArray = null;
                try {
                    jsonArray = (JSONArray) response;

                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {

                            JSONObject jsonObject1 = new JSONObject(jsonArray.get(i).toString());
                            userTags.add(jsonObject1.getString(TAG));
                    /*String key = jsonObject1.getString(TAG);
                    ArrayList<Photo> value = PhotoFactory.parseFromAsArray(jsonArray.get(i), 0, MAX_LIMIT, PHOTOS);
                    Log.d(MY_LOGS, key);
                    Log.d(MY_LOGS, PhotoFactory.parseFromAsArray(jsonArray.get(i), 0, MAX_LIMIT, PHOTOS).size() + "");
                    userTags.put(key, value);*/

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    UserController.this.listener.onRequestReady(PicsArtConst.REQ_OK_TAGS, response.toString());
                } catch (Exception e) {
                    UserController.this.listener.onRequestReady(PicsArtConst.REQ_BAD_TAGS, e.toString());
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
     *               onResponse 209 code will be called in listener
     *               onErrorResponse 309 code will be called in listener
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
     *               onResponse 209 code will be called in listener
     *               onErrorResponse 309 code will be called in listener
     */
    public synchronized void requestUserPhotos(String userId, final int offset, final int limit) {    //  7

        if (userId == null || userId == EMPTY_ID) {
            Log.e(ERROR, NULL_ID_ERROR);
            return;
        }
        userPhotos = new ArrayList<>();

        String url = PicsArtConst.SHOW_USER + userId + PicsArtConst.PHOTOS_PREFIX + PicsArtConst.TOKEN_PREFIX + accessToken;
        PaArrayRequest req = new PaArrayRequest(url, null, null);
        SingletoneRequestQue.getInstance(ctx).addToRequestQueue(req);
        req.setRequestListener(new PaArrayRequest.PARequestListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                UserController.this.listener.onRequestReady(PicsArtConst.REQ_BAD_USER_PHOTO, error.toString());
            }

            @Override
            public void onResponse(Object response) {
                userPhotos = PhotoFactory.parseFromArray(response, offset, limit);
                UserController.this.listener.onRequestReady(PicsArtConst.REQ_OK_USER_PHOTO, response.toString());
            }

        });
    }


    /**
     * Requests for Blocking User with ID
     *
     * @param blockingId ID of Blocking User
     *                   <p/>
     *                   onResponse 210 code will be called in listener
     *                   onErrorResponse 310 code will be called in listener
     */
    public void blockUserWithID(final String blockingId) {

        if (blockingId == null || blockingId == EMPTY_ID) {
            Log.e(ERROR, NULL_ID_ERROR);
            return;
        }
        String url = PicsArtConst.SHOW_USER + PicsArtConst.ME_PREFIX + PicsArtConst.BLOCKED_PREFIX + PicsArtConst.TOKEN_PREFIX + accessToken;
        StringRequest req = new StringRequest(Request.Method.POST, url,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        listener.onRequestReady(PicsArtConst.BLOCK_USER_OK_CODE, response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onRequestReady(PicsArtConst.BLOCK_USER_BAD_CODE, error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("block_id", blockingId);
                return params;

            }

            @Override
            public void deliverError(VolleyError error) {
                super.deliverError(error);
                listener.onRequestReady(PicsArtConst.BLOCK_USER_BAD_CODE, error.toString());
            }
        };

        SingletoneRequestQue.getInstance(ctx).addToRequestQueue(req);


    }

    /**
     * Requests for Unblocking User with ID
     *
     * @param unblockingId ID of Unblocking User
     *                     <p/>
     *                     onResponse 211 code will be called in listener
     *                     onErrorResponse 311 code will be called in listener
     */
    public void unblockUserWithID(final String unblockingId) {

        if (unblockingId == null || unblockingId == EMPTY_ID) {
            Log.e(ERROR, NULL_ID_ERROR);
            return;
        }
        String url = PicsArtConst.SHOW_USER + PicsArtConst.ME_PREFIX + PicsArtConst.BLOCKED_PREFIX + "/" + unblockingId + PicsArtConst.TOKEN_PREFIX + accessToken;

        PARequest req = new PARequest(Request.Method.DELETE, url, null, null);
        SingletoneRequestQue.getInstance(ctx).addToRequestQueue(req);
        req.setRequestListener(new PARequest.PARequestListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                UserController.this.listener.onRequestReady(PicsArtConst.UNBLOCK_USER_BAD_CODE, error.toString());
            }

            @Override
            public void onResponse(Object response) {
                UserController.this.listener.onRequestReady(PicsArtConst.UNBLOCK_USER_OK_CODE, response.toString());
            }
        });
    }

    /**
     * Requests for Following User with ID
     *
     * @param followingId ID of Following User
     *                    <p/>
     *                    onResponse 212 code will be called in listener
     *                    onErrorResponse 312 code will be called in listener
     */
    public void followUserWithID(final String followingId) {

        if (followingId == null || followingId == EMPTY_ID) {
            Log.e(ERROR, NULL_ID_ERROR);
            return;
        }
        String url = PicsArtConst.SHOW_USER + PicsArtConst.ME_PREFIX + PicsArtConst.FOLLOWING_PREFIX + PicsArtConst.TOKEN_PREFIX + accessToken;
        StringRequest req = new StringRequest(Request.Method.POST, url,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        listener.onRequestReady(PicsArtConst.FOLLOW_USER_OK_CODE, response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onRequestReady(PicsArtConst.FOLLOW_USER_BAD_CODE, error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("following_id", followingId);
                return params;

            }

            @Override
            public void deliverError(VolleyError error) {
                super.deliverError(error);
                listener.onRequestReady(PicsArtConst.FOLLOW_USER_BAD_CODE, error.toString());
            }
        };

        SingletoneRequestQue.getInstance(ctx).addToRequestQueue(req);

    }

    /**
     * Requests for Unfollowing User with ID
     *
     * @param unfollowingId ID of Unfollowing User
     *                      <p/>
     *                      onResponse 213 code will be called in listener
     *                      onErrorResponse 313 code will be called in listener
     */
    public void unfollowUserWithID(final String unfollowingId) {

        if (unfollowingId == null || unfollowingId == EMPTY_ID) {
            Log.e(ERROR, NULL_ID_ERROR);
            return;
        }

        String url = PicsArtConst.SHOW_USER + PicsArtConst.ME_PREFIX + PicsArtConst.FOLLOWING_PREFIX + "/" + unfollowingId + PicsArtConst.TOKEN_PREFIX + accessToken;
        StringRequest req = new StringRequest(Request.Method.DELETE, url,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        listener.onRequestReady(PicsArtConst.UNFOLLOW_USER_OK_CODE, response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onRequestReady(PicsArtConst.UNFOLLOW_USER_BAD_CODE, error.toString());
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("unfollowing_id", unfollowingId);
                return params;

            }

            @Override
            public void deliverError(VolleyError error) {
                super.deliverError(error);
                listener.onRequestReady(PicsArtConst.UNFOLLOW_USER_BAD_CODE, error.toString());
            }

        };

        SingletoneRequestQue.getInstance(ctx).addToRequestQueue(req);

    }


    /**
     * @param reqnumber code to send
     * @param msg       message
     *                  <p/>
     *                  Notifies all static listeners with given code and message
     */
    public static void notifyListeners(int reqnumber, String msg) {
        for (RequestListener listeners : getRegisteredListeners()) {
            listeners.onRequestReady(reqnumber, msg);

        }
    }

    /**
     * @param listenerNumb listener nuber(ID) to notify
     * @param reqNumb      code to send
     * @param msg          message
     *                     <p/>
     *                     Notifies specified static listener with given code and message
     */
    public static void notifyListener(int listenerNumb, int reqNumb, String msg) {
        try {
            getListener(listenerNumb).onRequestReady(reqNumb, msg);
        } catch (NullPointerException e) {
            Log.e("Listener Error: ", "Non Existing Listener with index " + listenerNumb);
        }
    }


}
