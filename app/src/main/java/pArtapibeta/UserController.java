package pArtapibeta;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


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

    static ArrayList<RequestListener> st_listeners_all = new ArrayList<>();


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



    /**
     * Request User Profile
     *
     *           onResponse 205 code will be called in listener
     *           onErrorResponse 305 code will be called in listener
     */
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

    /**
     * Request User Profile with ID
     *
     * @param id ID of the User
     *
     *           onResponse 202 code will be called in listener
     *           onErrorResponse 302 code will be called in listener
     */
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



    /**
     * Requests Followers of the User
     *
     * @param user user(to examine)
     * @param offset starting point
     * @param limit limit of users
     *
     *           onResponse 208 code will be called in listener
     *           onErrorResponse 308 code will be called in listener
     */
    public synchronized void requestUserFollowers(User user, final int offset, final int limit) {
        requestUserFollowers(user.getId().toString(), offset, limit);
    }

    /**
     * Requests Followers of the User
     *
     * @param userId  ID of the User
     * @param offset starting point
     * @param limit limit of users
     *
     *           onResponse 208 code will be called in listener
     *           onErrorResponse 308 code will be called in listener
     */
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



    /**
     * Requests Following of the User
     *
     * @param user user(to examine)
     * @param offset starting point
     * @param limit limit of users
     *
     *           onResponse 209 code will be called in listener
     *           onErrorResponse 309 code will be called in listener
     */
    public synchronized void requestUserFollowing(User user, final int offset, final int limit) {
        requestUserFollowing(user.getId().toString(), offset, limit);
    }

    /**
     * Requests Following of the User
     *
     * @param userId  ID of the User
     * @param offset starting point
     * @param limit limit of users
     *
     *           onResponse 209 code will be called in listener
     *           onErrorResponse 309 code will be called in listener
     */
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



    /**
     * RequestsLiked Photos of the User
     *
     * @param user  user(to examine)
     * @param offset starting point
     * @param limit limit of users
     *
     *           onResponse  210 code will be called in listener
     *           onErrorResponse 310 code will be called in listener
     */
    public synchronized void requestLikedPhotos(User user, final int offset, final int limit) {
        requestLikedPhotos(user.getId().toString(), offset, limit);
    }

    /**
     * Requests Liked Photos of the User
     *
     * @param userId  ID of the User
     * @param offset starting point
     * @param limit limit of users
     *
     *           onResponse 210 code will be called in listener
     *           onErrorResponse 310 code will be called in listener
     */
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


    /**
     * Requests Blocked Users of the User
     *
     * @param user  user(to examine)
     * @param offset starting point
     * @param limit limit of users
     *
     *           onResponse  204 code will be called in listener
     *           onErrorResponse 304 code will be called in listener
     */
    public synchronized void requestBlockedUsers(User user, final int offset, final int limit) {
        requestBlockedUsers(user.getId().toString(), offset, limit);
    }

    /**
     * Requests Blocked Users of the User
     *
     * @param userId  ID of the User
     * @param offset starting point
     * @param limit limit of users
     *
     *           onResponse  204 code will be called in listener
     *           onErrorResponse 304 code will be called in listener
     */
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

                blockedUsers = UserFactory.parseFromAsArray(response, offset, limit);
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


    /**
     * Requests Places of the User
     *
     * @param user  user(to examine)
     * @param offset starting point
     * @param limit limit of users
     *
     *           onResponse  205 code will be called in listener
     *           onErrorResponse 305 code will be called in listener
     */
    public synchronized void requestPlaces(User user, final int offset, final int limit) {
        requestPlaces(user.getId().toString(), offset, limit);
    }

    /**
     * Requests Places of the User
     *
     * @param userId ID of the User
     * @param offset starting point
     * @param limit limit of users
     *
     *           onResponse 205 code will be called in listener
     *           onErrorResponse 305 code will be called in listener
     *
     */
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


    /**
     * Requests Tags of the User
     *
     * @param user user(to examine)
     * @param offset starting point
     * @param limit limit of users
     *
     *           onResponse 206 code will be called in listener
     *           onErrorResponse 306 code will be called in listener
     */
    public synchronized void requestTags(User user, final int offset, final int limit) {
        requestTags(user.getId().toString(), offset, limit);
    }

    /**
     * Requests Tags of the User
     *
     * @param userId ID of the User
     * @param offset starting point
     * @param limit limit of users
     *
     *           onResponse 206 code will be called in listener
     *           onErrorResponse 306 code will be called in listener
     */
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


    /**
     * Requests Photos of the User
     *
     * @param user user(to examine)
     * @param offset starting point
     * @param limit limit of users
     *
     *           onResponse 207 code will be called in listener
     *           onErrorResponse 307 code will be called in listener
     */
    public synchronized void requestUserPhotos(User user, final int offset, final int limit) {
        requestUserPhotos(user.getId().toString(), offset, limit);
    }

    /**
     * Requests Photos of the User
     *
     * @param userId ID of the User
     * @param offset starting point
     * @param limit limit of users
     *
     *           onResponse 207 code will be called in listener
     *           onErrorResponse 307 code will be called in listener
     */
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


    /**
     * Requests for Blocking User with ID
     *
     * @param blockingId ID of Blocking User
     *
     *           onResponse 211 code will be called in listener
     *           onErrorResponse 311 code will be called in listener
     */
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

    /**
     * Requests for Unblocking User with ID
     *
     * @param unblockingId ID of Unblocking User
     *
     *           onResponse 212 code will be called in listener
     *           onErrorResponse 312 code will be called in listener
     */
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

    /**
     * Requests for Following User with ID
     *
     * @param followingId ID of Following User
     *
     *           onResponse 217 code will be called in listener
     *           onErrorResponse 317 code will be called in listener
     */
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
                UserController.this.listener.onRequestReady(317, error.toString());

            }

            @Override
            public void onResponse(Object response) {

                Log.d(MY_LOGS, response.toString());
                UserController.this.listener.onRequestReady(217, response.toString());
            }
        });
        //new FollowUserAsyncTask().execute(id);
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
