package pArtapibeta;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

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
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
     * This class consists exclusively of void methods, that operate on making
     * requests and initializing specific field.
     *
     * <p>Some Getters of this class  throw a <tt>NullPointerException</tt>
     * if the collections or class objects provided to them are null.
     *
     * <p>This class is a member of the
     * <a href="www.picsart.com">
     * </a>.
     *
     * @author  Arman Andreasyan
     */



public class PhotoController {
    private Context ctx;
    private RequestListener listener;
    private String token;
    private volatile Photo photo;
    private static volatile Comment _comment;
    private ArrayList<Comment> commentsLists;
    private ArrayList<User> photoLikedUsers;
    private static ArrayList<RequestListener> st_listeners_all = new ArrayList<>();


    // Getters and Setters for all fields //

    public ArrayList<Comment> getCommentsLists() throws NullPointerException {

        return commentsLists;
    }

    public void setCommentsLists(ArrayList<Comment> commentList) {
        if (commentsLists == null) commentsLists = new ArrayList<>(commentList);
        else this.commentsLists = commentList;
    }

    public ArrayList<User> getPhotoLikedUsers() throws NullPointerException{
        return photoLikedUsers;
    }

    public void setPhotoLikedUsers(ArrayList<User> phLikeddUsers) {
        if (photoLikedUsers == null) phLikeddUsers = new ArrayList<>(phLikeddUsers);
        else this.photoLikedUsers = phLikeddUsers;
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
        PhotoController.st_listeners_all = st_listeners_all;
    }

    public Photo getPhoto() {
        return photo;
    }

    // End of Getters and Setters //



    /**
 * @param photo  photo objects to be uploaded
 *               if Success 101 code will be called in static listener
 * */
    public static synchronized void uploadPhoto(Photo... photo) {

        new ImageUploadTask().execute(photo);

    }

    /**
 * @param id ID of Photo to request
 *
 *           onResponse       201 code will be called in listener
 *           onErrorResponse  203 code will be called in listener
 * */
    public synchronized void requestPhoto(String id) {
        assert this.listener != null;
        String url = PicsArtConst.Get_PHOTO_URL + id + PicsArtConst.TOKEN_PREFIX + token;
        url = PicsArtConst.Get_PHOTO_URL_PUB + id + ".json" + PicsArtConst.API_PREFX + PicsArtConst.APIKEY;
        PARequest request = new PARequest(Request.Method.GET, url, null, null);
        SingletoneRequestQue.getInstance(ctx).addToRequestQueue(request);
        request.setRequestListener(new PARequest.PARequestListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", error.toString());
                listener.onRequestReady(203, error.toString());

            }

            @Override
            public void onResponse(Object response) {
                Log.d("Response 9", response.toString());
                //photo = new Photo(Photo.IS.GENERAL);
                photo = PhotoFactory.parseFrom(response);
                listener.onRequestReady(201, response.toString());
            }
        });


    }

    /**
     * @param photoId id of photo
     * @param limit   max limit to show
     * @param offset  starting index to count
     *
     *  The comments will be ordered first-to-last.
     *                 onResponse       301 code will be called in listener
     *                 onErrorResponse  303 code will be called in listener
     */
    public synchronized void requestComments(String photoId, final int offset, final int limit) {

        String url = PicsArtConst.PHOTO_COMMENTS_URL + photoId + ".json" + PicsArtConst.API_PREFX + PicsArtConst.APIKEY;
        PARequest req = new PARequest(Request.Method.GET, url, null, null);
        SingletoneRequestQue.getInstance(MainActivity.getAppContext()).addToRequestQueue(req);
        req.setRequestListener(new PARequest.PARequestListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onRequestReady(303, error.toString());
            }
            @Override
            public void onResponse(Object response) {

                try {
                    JSONArray _comArr = ((JSONObject) response).getJSONArray("response");
                    ArrayList<Comment> comment = new ArrayList<Comment>();
                    for (int i = 0; i < _comArr.length(); i++) {
                        JSONObject val = _comArr.getJSONObject(i);
                        Gson gson = new Gson();
                        comment.add(i, gson.fromJson(val.toString(), Comment.class));
                    }
                    int nwOffset = 0;
                    int nwlimit = 0;
                    if (offset > comment.size()) {
                        nwOffset = comment.size();
                    } else if (offset < 0) {
                        nwOffset = 0;
                    } else {
                        nwOffset = offset;
                    }
                    if (limit > comment.size()) {
                        nwlimit = comment.size();
                    } else if (limit < 0) {
                        nwlimit = 0;
                    } else {
                        nwlimit = limit;
                    }
                    if (nwlimit - nwOffset < 0) nwlimit = nwOffset;
                    ArrayList<Comment> tmp = new ArrayList<Comment>();
                    for (int i = nwOffset, j = 0; i < nwlimit; i++, j++) {
                        tmp.add(j, comment.get(i));
                    }
                    commentsLists = new ArrayList<Comment>(tmp);
                    listener.onRequestReady(301, "Comments is HERE");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }


    /**
     * @param photoId id of photo
     * @param limit   max limit to show
     * @param offset  starting index to count
     *
     *  The comments will be ordered last-to-first.
     *                 onResponse       301 code will be called in listener
     *                 onErrorResponse  303 code will be called in listener
     */
    public synchronized void requestCommentsOffLim(String photoId, final int offset, final int limit) {
        String url = PicsArtConst.PHOTO_COMMENTS_URL + photoId + ".json" + PicsArtConst.API_PREFX + PicsArtConst.APIKEY + PicsArtConst.OFFSET + offset + PicsArtConst.LIMIT + limit;
        PARequest req = new PARequest(Request.Method.GET, url, null, null);
        SingletoneRequestQue.getInstance(MainActivity.getAppContext()).addToRequestQueue(req);
        req.setRequestListener(new PARequest.PARequestListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onRequestReady(303, error.toString());
            }

            @Override
            public void onResponse(Object response) {

                try {
                    JSONArray _comArr = ((JSONObject) response).getJSONArray("response");
                    ArrayList<Comment> comment = new ArrayList<Comment>();
                    for (int i = 0; i < _comArr.length(); i++) {
                        JSONObject val = _comArr.getJSONObject(i);
                        Gson gson = new Gson();
                        comment.add(i, (gson.fromJson(val.toString(), Comment.class)));

                    }
                    commentsLists = new ArrayList<Comment>(comment);
                    listener.onRequestReady(301, "Comments ready");
                    //notifyListeners(555, "getComments");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }
    /**
 * @param photoID ID of photo
 * @param comment text of comment
 *
 *       Adds Comment to the Photo
 *          onResponse          401 code will be called in listener
 *          onErrorResponse     403 code will be called in listener
 *
 *
 * */
    public synchronized void addComment(String photoID, final String comment) {

        String url = PicsArtConst.PHOTO_ADD_COMMENT_URL + photoID + ".json" + PicsArtConst.API_PREFX + PicsArtConst.APIKEY;
        StringRequest req = new StringRequest(Request.Method.POST, url,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response inner", response);
                        listener.onRequestReady(401, response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onRequestReady(403, error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("is_social", "1");
                params.put("text", comment);
                return params;

            }
        };

        SingletoneRequestQue.getInstance(MainActivity.getAppContext()).addToRequestQueue(req);


    }

    /**
     * @param photoId ID of photo
     * @param commentId ID of comment
     *          onResponse          501 code will be called in  listener
     *          onErrorResponse     503 code will be called in  listener
     * */
    public synchronized void removeComment(String photoId, final String commentId) {
        String url = PicsArtConst.PHOTO_REMOVE_COMMENT_URL + photoId + ".json" + PicsArtConst.API_PREFX + PicsArtConst.APIKEY;
        StringRequest req = new StringRequest(Request.Method.POST, url,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Log.d("RemoveComment ", response);
                        listener.onRequestReady(501, response);
                        //notifyListeners(666, "");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onRequestReady(503, error.toString());
                    }

                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("comment_id", commentId);
                return params;

            }
        };

        SingletoneRequestQue.getInstance(MainActivity.getAppContext()).addToRequestQueue(req);

    }


    /**
     * @param photo new photo info to apply
     *              Initialize new photo with updated fields to apply,
     *              then call this method to update data
     *          onResponse          601 code will be called in static listeners
     *          onErrorResponse     603 code will be called in static listeners
     */
    public static synchronized void updatePhotoData(final Photo photo) {
        JSONObject jobj = new JSONObject();
        final ArrayList<String> tgss = new ArrayList<>();
        for (String tgs : photo.getTags()) {
            tgss.add(tgs);

        }
        JSONArray tgarr;
        tgarr = new JSONArray();
        tgarr.put(tgss);

        try {
            jobj.put("tags", tgarr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        BasicNameValuePair[] tmp = photo.getLocation().getLocationPair();
        for (BasicNameValuePair pair : tmp) {
            try {
                jobj.put(pair.getName(), pair.getValue());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        try {
            jobj.put("title", photo.getTitle());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        String url = PicsArtConst.PHOTO_UPDATE_INFO_URL + photo.getId() +  PicsArtConst.API_TEST_PREF + PicsArtConst.APIKEY;
        PARequest req = new PARequest(Request.Method.POST, url, jobj, null);

        req.setRequestListener(new PARequest.PARequestListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                notifyListeners(603, error.toString());
            }

            @Override
            public void onResponse(Object response) {
                Log.d("UpdatedonLisData ", response.toString());
                notifyListeners(601, response.toString());
            }
        });

        SingletoneRequestQue.getInstance(MainActivity.getAppContext()).addToRequestQueue(req);

    }


    /**
     * @param photoId ID of the Photo
     *
     *                Likes the Photo with given ID
     *
     *          onResponse          701 code will be called in listener
     *          onErrorResponse     703 code will be called in listener
     */
    public synchronized void like(String photoId) {
        String url = PicsArtConst.PHOTO_LIKE_URL + photoId + ".json" + PicsArtConst.API_PREFX + PicsArtConst.APIKEY;
        StringRequest req = new StringRequest(Request.Method.POST, url,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("PhotoLike ", response);
                        listener.onRequestReady(701, response);
                        // st_listener.onRequestReady(999,response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("PhotoLikeError ", error.toString());
                        listener.onRequestReady(703, error.toString());
                        // st_listener.onRequestReady(999,"");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("is_social", "1");
                return params;

            }
        };

        SingletoneRequestQue.getInstance(MainActivity.getAppContext()).addToRequestQueue(req);


    }


    /**
     * @param photoId ID of Photo
     *                UnLikes Photo with given ID
     *
     *          onResponse          801 code will be called in listener
     *          onErrorResponse     803 code will be called in listener
     */
    public synchronized void unLike(String photoId) {
        String url = PicsArtConst.PHOTO_UNLIKE_URL + photoId + ".json" + PicsArtConst.API_PREFX + PicsArtConst.APIKEY;
        StringRequest req = new StringRequest(Request.Method.POST, url,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("PhotoUnlike ", response);
                        listener.onRequestReady(801, response);
                        //st_listener.onRequestReady(1111,"");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("PhotoUnlike ", error.toString());
                        listener.onRequestReady(803, error.toString());
                        //st_listener.onRequestReady(1111,"");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                //  params.put("is_social", "1");
                return params;

            }
        };

        SingletoneRequestQue.getInstance(MainActivity.getAppContext()).addToRequestQueue(req);
    }


    /**
     *     @param ctx       Context of application
     *     @param token     OAuth 2.0 token to make calls
     *
     *
     */
    public PhotoController(Context ctx, String token) {
        this.ctx = ctx;
        this.token = token;
    }


    /**
     *     @param listener     Listener objec
     *
     */
    public void setListener(RequestListener listener) {
        this.listener = listener;
    }


    /**
     *     @param id     ID of particular Comment
     *
     *          Requests for particular Comment with given ID
     *
     *          onResponse          901 code will be called in listener
     *          onErrorResponse     903 code will be called in listener
     */
    public synchronized void getCommentByid(String id) {

        String url = PicsArtConst.PHOTO_LIKED_USERS_URL + id + PicsArtConst.API_TEST_PREF + PicsArtConst.APIKEY;

        PARequest request = new PARequest(Request.Method.GET, url, null, null);
        SingletoneRequestQue.getInstance(ctx).addToRequestQueue(request);
        request.setRequestListener(new PARequest.PARequestListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", error.toString());
                listener.onRequestReady(903, error.toString());

            }

            @Override
            public void onResponse(Object response) {
                Log.d("Response 9", response.toString());
                //photo = new Photo(Photo.IS.GENERAL);
                Gson gson = new Gson();
                _comment = gson.fromJson(response.toString(), Comment.class);
                listener.onRequestReady(901, response.toString());
            }
        });


    }

    /**
     *     @param photoId     ID of the Photo
     *     @param offset      start point (from)
     *     @param limit       limit of outcome
     *
     *          Requests for Users who liked the Photo with given ID
     *
     *          onResponse          1001 code will be called in listener
     *          onErrorResponse     1003 code will be called in listener
     *
     */
    public synchronized void getLikedUsers(String photoId, int offset, int limit) {

        String url = PicsArtConst.PHOTO_LIKED_USERS_URL + photoId + PicsArtConst.API_TEST_PREF + PicsArtConst.APIKEY + PicsArtConst.OFFSET + offset + PicsArtConst.LIMIT + limit;
        // url = PicsArtConst.Get_PHOTO_URL_PUB + id + ".json" + PicsArtConst.API_PREFX + PicsArtConst.APIKEY;
        PARequest request = new PARequest(Request.Method.GET, url, null, null);
        SingletoneRequestQue.getInstance(ctx).addToRequestQueue(request);
        request.setRequestListener(new PARequest.PARequestListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", error.toString());
                listener.onRequestReady(1003, error.toString());

            }

            @Override
            public void onResponse(Object response) {
                Log.d("Response 9", response.toString());
                photoLikedUsers = UserFactory.parseFromAsArray(response,0,Integer.MAX_VALUE);
                listener.onRequestReady(1001, response.toString());
            }
        });

    }


    /**
     *     @param reqnumber   code to send
     *     @param msg          message
     *
     *         Notifies all static listeners with given code and message
     */
    public static void notifyListeners(int reqnumber, String msg) {
        for (RequestListener listeners : getSt_listeners_all()) {
            listeners.onRequestReady(reqnumber, msg);

        }
    }

    /**
     *     @param listenerNumb listener nuber(ID) to notify
     *     @param reqNumb       code to send
     *     @param msg          message
     *
     *         Notifies specified static listener with given code and message
     */
    public static void notifyListener(int listenerNumb, int reqNumb, String msg) {
        try {
            getSt_listener(listenerNumb).onRequestReady(reqNumb, msg);
        } catch (NullPointerException e) {
            Log.e("Listener Error: ", "Non Existing Listener with index " + listenerNumb);
        }
    }



    /**
     *  Uploads images
     * */
    private static class ImageUploadTask extends AsyncTask<Photo, Integer, JSONObject> {

        InputStream is = null;
        volatile JSONObject jObj = null;
        String json = "";

        @Override
        protected JSONObject doInBackground(Photo... phot) {
            int iter = 0;
            for (Photo ph : phot) {
                try {
                    Looper.getMainLooper();
                    Looper.prepare();
                    File file = new File(ph.getPath());
                    HttpClient httpClient = new DefaultHttpClient();
                    String url = "";
                    MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                    entity.addPart("file", new FileBody(file));
                    if (ph.isFor == Photo.IS.AVATAR) {
                        url = PicsArtConst.PHOTO_AVATAR_URL + PicsArtConst.API_PREFX + PicsArtConst.APIKEY;
                    } else if (ph.isFor == Photo.IS.COVER) {
                        url = PicsArtConst.PHOTO_COVER_URL + PicsArtConst.API_PREFX + PicsArtConst.APIKEY;
                    } else {
                        url = PicsArtConst.PHOTO_UPLOAD_URL + PicsArtConst.API_PREFX + PicsArtConst.APIKEY;
                        BasicNameValuePair[] tmp = ph.getLocation().getLocationPair();
                        for (int i = 0; i < tmp.length; i++) {
                            entity.addPart(tmp[i].getName(), new StringBody(tmp[i].getValue()));
                        }
                        for (String str : ph.getTags()) {
                            entity.addPart("tags[]", new StringBody(str));
                        }
                        entity.addPart("title", new StringBody(ph.getTitle()));
                    }
                    HttpPost httpPost = new HttpPost(url);
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
            Log.d("Uploaded", "photo #" + progress);
        }

        @Override
        protected void onPostExecute(JSONObject sResponse) {

            if (sResponse != null) {
                try {
                    Log.d("response Upload", sResponse.toString());

                    notifyListeners(101, "Image(es) is/are uploaded!!");

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
