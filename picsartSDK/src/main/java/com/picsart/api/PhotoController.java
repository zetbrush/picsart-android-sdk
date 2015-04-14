package com.picsart.api;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Looper;
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
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * This class consists exclusively of void methods, that operate on making
 * requests and initializing specific field.
 *
 * Some Getters of this class  throw a <tt>NullPointerException</tt>
 * if the collections or class objects provided to them are null.
 *
 * This class is a member of th www.picsart.com.
 *
 * @author Arman Andreasyan
 */


public class PhotoController {


    private static Context ctx;
    private RequestListener listener;

    private static String token;
    private volatile Photo photo;
    private static volatile Comment _comment;
    private ArrayList<Comment> commentsLists;
    private ArrayList<User> photoLikedUsers;
    private static ArrayList<RequestListener> stListeners = new ArrayList<>();


    // Getters and Setters for all fields //

    public static String getAccessToken() {
        return token;
    }

    public static void setAccessToken(String token) {
        PhotoController.token = token;
    }

    public ArrayList<Comment> getCommentsLists() throws NullPointerException {

        return commentsLists;
    }


    public void setCommentsLists(ArrayList<Comment> commentList) {
        if (commentsLists == null) commentsLists = new ArrayList<>(commentList);
        else this.commentsLists = commentList;
    }

    public ArrayList<User> getPhotoLikedUsers() throws NullPointerException {
        return photoLikedUsers;
    }

    public void setPhotoLikedUsers(ArrayList<User> phLikeddUsers) {
        if (photoLikedUsers == null) phLikeddUsers = new ArrayList<>(phLikeddUsers);
        else this.photoLikedUsers = phLikeddUsers;
    }

    public static RequestListener getRegisteredListener(int indexNumb) {
        int indx;
        for (RequestListener listener : stListeners)
            if (listener.getIndexOfListener() == indexNumb) {
                indx = stListeners.indexOf(listener);
                return stListeners.get(indx);
            }
        return null;
    }

    public static void resgisterListener(RequestListener st_listener) {


        if (stListeners.size() == 0) {
            st_listener.setIndexInList(0);
            stListeners.add(st_listener);

        } else {
            int index = st_listener.getIndexOfListener();
            if (stListeners.contains(st_listener) && st_listener.getIndexOfListener() == index) {
                int indToChange = (stListeners.indexOf(st_listener));
                st_listener.setIndexInList(indToChange);
                stListeners.set(indToChange, st_listener);
            } else if (!((stListeners.contains(st_listener)))) {
                int indxToput = stListeners.size();
                st_listener.setIndexInList(indxToput);
                stListeners.add(st_listener);

            }

        }

    }

    public static ArrayList<RequestListener> getListeners() {
        return stListeners;
    }

    public static void registerListeners(ArrayList<RequestListener> st_listeners_all) {
        PhotoController.stListeners = st_listeners_all;
    }

    public Photo getPhoto() {
        return photo;
    }

    public Comment getComment() {
        return _comment;
    }

    // End of Getters and Setters //


    /**
     * @param photo photo objects to be uploaded
     *              if Success 101 code will be called in static listener
     */
    public static synchronized void uploadPhoto(ProgressListener progrs, Photo... photo) {

        new ImageUploadTask(progrs).execute(photo);

    }

    /**
     * @param id ID of Photo to request
     *
     *           onResponse       201 code will be called in listener
     *           onErrorResponse  203 code will be called in listener
     */
    public synchronized void requestPhoto(String id) {
        assert this.listener != null;
        String url = PicsArtConst.Get_PHOTO_URL + id + PicsArtConst.TOKEN_PREFIX + token;
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
     *                The comments will be ordered last-to-first.
     *                onResponse       301 code will be called in listener
     *                onErrorResponse  303 code will be called in listener
     */
    public synchronized void requestComments(final String photoId, final int offset, final int limit) {
        String url = PicsArtConst.PHOTO_PRE_URL + photoId + PicsArtConst.PHOTO_ADD_COMMENT_URL + PicsArtConst.TOKEN_PREFIX + token;
        PARequest req = new PARequest(Request.Method.GET, url, null, null);

        SingletoneRequestQue.getInstance(ctx).addToRequestQueue(req);
        req.setRequestListener(new PARequest.PARequestListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onRequestReady(303, error.toString());
            }

            @Override
            public void onResponse(Object response) {
                try {
                    JSONArray _comArr = ((JSONObject) response).getJSONArray("comments");
                    ArrayList<Comment> comment = new ArrayList<Comment>();
                    for (int i = 0; i < _comArr.length(); i++) {
                        JSONObject val = _comArr.getJSONObject(i);
                        Gson gson = new Gson();
                        comment.add(i, (gson.fromJson(val.toString(), Comment.class)));
                        comment.get(i).setPotoID(photoId);

                    }
                    Collections.reverse(comment);
                    if (offset >= 0 && limit > 0 && offset < comment.size()) {
                        ArrayList<Comment> tmp = new ArrayList<>();
                        int lim = limit;
                        for (int i = offset; i < comment.size() && lim > 0; i++, lim--) {
                            tmp.add(comment.get(i));
                        }
                        comment = tmp;
                    }
                    commentsLists = new ArrayList<>(comment);

                    listener.onRequestReady(301, "Comments ready");

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
     *                Adds Comment to the Photo
     *                onResponse          401 code will be called in listener
     *                onErrorResponse     403 code will be called in listener
     */
    public synchronized void addComment(String photoID, final Comment comment) {

        String url = PicsArtConst.PHOTO_GENERAL_PREFIX + photoID + PicsArtConst.PHOTO_ADD_COMMENT_URL + PicsArtConst.TOKEN_PREFIX + token;
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
                params.put("is_social", comment.getIsSocial().toString());
                params.put("text", comment.getText());
                return params;

            }
        };

        SingletoneRequestQue.getInstance(ctx).addToRequestQueue(req);


    }

    /**
     * @param photoId   ID of photo
     * @param commentId ID of comment
     *                  onResponse          501 code will be called in  listener
     *                  onErrorResponse     503 code will be called in  listener
     */
    public synchronized void deleteComment(String photoId, final String commentId) {

        String url = PicsArtConst.PHOTO_PRE_URL + photoId + PicsArtConst.PHOTO_COMMENT_MIDLE + commentId + PicsArtConst.TOKEN_PREFIX + token;//+"&method=delete";
        StringRequest req = new StringRequest(Request.Method.DELETE, url,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Log.d("RemoveComment ", response);
                        listener.onRequestReady(501, response);

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
                //params.put("photo_id", commentId);
                //params.put("comment_id", commentId);
                return params;

            }
        };

        SingletoneRequestQue.getInstance(ctx).addToRequestQueue(req);

    }


    /**
     * @param photo new photo info to apply
     *              Initialize new photo with updated fields to apply,
     *              then call this method to update data
     *              onResponse          601 code will be called in static listeners
     *              onErrorResponse     603 code will be called in static listeners
     */
    public static synchronized void updatePhotoData(final Photo photo) {
        JSONObject jobj = new JSONObject();
        final ArrayList<String> tgss = new ArrayList<>();

        if (photo.getId() == null) {
            notifyListeners(603, "error : Photo specified has no ID");
            return;
        }

        if (photo.getTags() != null) {
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
        }

        if (photo.getLocation() != null && photo.getLocation().getLocationPair() != null) {
            BasicNameValuePair[] tmp = photo.getLocation().getLocationPair();
            for (BasicNameValuePair pair : tmp) {
                try {
                    jobj.put(pair.getName(), pair.getValue());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        if (photo.getLocation() != null && photo.getLocation().getCoordinates() != null && photo.getLocation().getCoordinates().size() >= 2) {

            JSONArray cordarr;
            cordarr = new JSONArray();
            cordarr.put(photo.getLocation().getCoordinates().get(0));
            cordarr.put(photo.getLocation().getCoordinates().get(1));
            try {
                jobj.put("location_coordinates", cordarr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        if (photo.getTitle() != null) {
            try {
                jobj.put("title", photo.getTitle());
            } catch (JSONException e) {
            }
        }


        String url = PicsArtConst.PHOTO_PRE_URL + photo.getId() + PicsArtConst.TOKEN_PREFIX + token;
        PARequest req = new PARequest(Request.Method.PUT, url, jobj, null) {
            @Override
            protected void deliverResponse(JSONObject jobj) {
                Log.d("deliveryResponse ", "At updatePhotoData" + jobj.toString());
                if (jobj.toString().contains("error")) notifyListeners(603, jobj.toString());

            }
        };

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

        SingletoneRequestQue.getInstance(ctx).addToRequestQueue(req);

    }


    /**
     * @param photoId ID of the Photo
     *
     *                Likes the Photo with given ID
     *
     *                onResponse          701 code will be called in listener
     *                onErrorResponse     703 code will be called in listener
     */
    public synchronized void like(String photoId) {
        String url = PicsArtConst.PHOTO_PRE_URL + photoId + PicsArtConst.PHOTO_LIKE_URL + PicsArtConst.TOKEN_PREFIX + token;
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
                //params.put("is_social", "1");
                return params;

            }
        };

        SingletoneRequestQue.getInstance(ctx).addToRequestQueue(req);


    }


    /**
     * @param photoId ID of Photo
     *                UnLikes Photo with given ID
     *
     *                onResponse          801 code will be called in listener
     *                onErrorResponse     803 code will be called in listener
     */
    public synchronized void unLike(String photoId) {
        String url = PicsArtConst.PHOTO_PRE_URL + photoId + PicsArtConst.PHOTO_LIKE_URL + PicsArtConst.TOKEN_PREFIX + token + "&method=delete";

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

        SingletoneRequestQue.getInstance(ctx).addToRequestQueue(req);
    }


    /**
     * @param ctx   Context of application
     * @param token OAuth 2.0 token to make calls
     */
    public PhotoController(Context ctx, String token) {
        this.ctx = ctx;
        PhotoController.token = token;
    }

    public PhotoController(Context ctx) {
        this.ctx = ctx;

    }

    /**
     * @param listener Listener objec
     */
    public void setListener(RequestListener listener) {
        this.listener = listener;
    }


    /**
     * @param id ID of particular Comment
     *
     *           Requests for particular Comment with given ID
     *
     *           onResponse          901 code will be called in listener
     *           onErrorResponse     903 code will be called in listener
     */
    public synchronized void requestCommentByid(final String photoId, String id) {


        String url = PicsArtConst.PHOTO_GENERAL_PREFIX + photoId + PicsArtConst.PHOTO_COMMENT_MIDLE + id + PicsArtConst.TOKEN_PREFIX + token;

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
                _comment.setPotoID(photoId);
                listener.onRequestReady(901, response.toString());
            }
        });


    }

    /**
     * @param photoId ID of the Photo
     * @param offset  start point (from)
     * @param limit   limit of outcome
     *
     *                Requests for Users who liked the Photo with given ID
     *
     *                onResponse          1001 code will be called in listener
     *                onErrorResponse     1003 code will be called in listener
     */
    public synchronized void requestLikedUsers(String photoId, final int offset, final int limit) {

        String url = PicsArtConst.PHOTO_PRE_URL + photoId + PicsArtConst.PHOTO_LIKE_URL + PicsArtConst.TOKEN_PREFIX + token;

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
                if(response.toString().contains("error")) {
                    listener.onRequestReady(1003, response.toString());
                    return;
                }
                else
                  photoLikedUsers = UserFactory.parseFromAsArray(response, offset, limit, "likes");
                listener.onRequestReady(1001, response.toString());
            }
        });

    }

    /**
     * @param reqnumber code to send
     * @param msg       message
     *
     *                 Notifies all static listeners with given code and message
     */
    public static void notifyListeners(int reqnumber, String msg) {
        for (RequestListener listeners : getListeners()) {
            listeners.onRequestReady(reqnumber, msg);

        }
    }

    /**
     * @param listenerNumb listener nuber(ID) to notify
     * @param reqNumb      code to send
     * @param msg          message
     *
     *                     Notifies specified static listener with given code and message
     */
    public static void notifyListener(int listenerNumb, int reqNumb, String msg) {
        try {
            getRegisteredListener(listenerNumb).onRequestReady(reqNumb, msg);
        } catch (NullPointerException e) {
            Log.e("Listener Error: ", "Non Existing Listener with index " + listenerNumb);
        }
    }

    /**
 * Setting flag for canceling upload, if any.
 * */
    public static void cancelUpload(){
        ImageUploadTask.setCancelFlag(true);
    }


    /**
     * Uploads images
     */
    private static class ImageUploadTask extends AsyncTask<Photo, Integer, JSONObject>  {

        InputStream is = null;
        static boolean cancelFlag = false;
        volatile JSONObject jObj = null;
        String json = "";
        ProgressListener progrs;

        public static void setCancelFlag(boolean cancelFlag) {
            ImageUploadTask.cancelFlag = cancelFlag;
        }

        public ImageUploadTask(ProgressListener progrs){
            this.progrs = progrs;
        }

        @Override
        protected synchronized JSONObject doInBackground(Photo... phot) {


            final int[] iter = new int[1];

            Looper.prepare();
            for (Photo ph : phot) {


                try {
                    final File file = new File(ph.getPath());
                    final long[] totalSize = new long[1];
                    totalSize[0] = file.length();
                    final HttpClient httpClient = new DefaultHttpClient();
                    String url = "";
                    //   MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                    MultiPartEntityMod multipartContent = null;
                    try {


                        multipartContent = new MultiPartEntityMod(new ProgressListener() {

                            @Override
                            public boolean doneFlag(boolean b)  {

                                return false;
                            }

                            @Override
                            public void transferred(long num) {
                                long checker=-1;
                                if(ImageUploadTask.this.isCancelled()){
                                    httpClient.getConnectionManager().shutdown();
                                }

                                if(num!=checker && num%2==0) {
                                    checker=num;
                                    publishProgress((int) ((num / (float) totalSize[0] * 100)), iter[0]);

                                }
                            }
                        });


                    } catch (Exception e) {
                        e.printStackTrace();
                    }



                    multipartContent.addPart("file", new FileBody(file));
                    totalSize[0] = multipartContent.getContentLength();
                    if (ph.isFor == Photo.IS.AVATAR) {
                        url = PicsArtConst.USER_URL_PATH + PicsArtConst.ME_PREFIX + PicsArtConst.PHOTO_AVATAR_ENDX + PicsArtConst.TOKEN_PREFIX + PhotoController.token;
                    } else if (ph.isFor == Photo.IS.COVER) {
                        url = PicsArtConst.USER_URL_PATH + PicsArtConst.ME_PREFIX + PicsArtConst.PHOTO_COVER_ENDX + PicsArtConst.TOKEN_PREFIX + PhotoController.token;
                    } else {
                        url = PicsArtConst.PHOTO_UPLOAD_URL + PicsArtConst.TOKEN_PREFIX + PhotoController.token;
                        BasicNameValuePair[] tmp = null;
                        if (ph.getLocation() != null && ph.getLocation().getLocationPair() != null) {
                            tmp = ph.getLocation().getLocationPair();

                            for (int i = 0; i < tmp.length; i++) {
                                multipartContent.addPart(tmp[i].getName(), new StringBody(tmp[i].getValue()));

                            }
                        }
                        if (ph.getTags() != null) {
                            for (String str : ph.getTags()) {
                                multipartContent.addPart("tags[]", new StringBody(str));
                            }
                        }
                        if (ph.getTitle() != null)
                            multipartContent.addPart("title", new StringBody(ph.getTitle()));
                        if (ph.getIsPublic() != null)
                            multipartContent.addPart("is_public", new StringBody(ph.getIsPublic().toString()));
                        if (ph.getMature() != null)
                            multipartContent.addPart("mature", new StringBody(ph.getMature().toString()));
                    }

                    HttpPost httpPost = new HttpPost(url);
                    HttpContext httpContext = new BasicHttpContext();
                    httpPost.setEntity(multipartContent);
                    HttpResponse response= null;
                    try {
                         response = httpClient.execute(httpPost, httpContext);
                    }catch(IllegalStateException e){
                        return null;
                    }
                    HttpEntity httpEntity = response.getEntity();
                    is = httpEntity.getContent();


                } catch (UnsupportedEncodingException e) {
                    notifyListeners(103, "error uploading photo");
                    // e.printStackTrace();
                } catch (ClientProtocolException e) {
                    notifyListeners(103, "error uploading photo");
                    // e.printStackTrace();
                } catch (IOException e) {
                    notifyListeners(103, "error IO");
                    // e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                    notifyListeners(103, "error ");
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
                } catch (Exception e) {
                    Log.e("JSON Parser", "Error parsing data " + e.toString());
                }
                iter[0]++;



            }
            // Return JSON String
            return jObj;

        }



        @Override
        protected void onPreExecute() {

            Log.d("Upload", " Uploading Picture...");

        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            progrs.transferred(progress[0]);

            if(cancelFlag) {
                ImageUploadTask.this.cancel(true);
                progrs.doneFlag(true);
                cancelFlag=false;
            }
            Log.d("Uploaded", "photo #" + progress[1] + " done " + progress[0] + "%");
        }

        @Override
        protected void onPostExecute(JSONObject sResponse) {
             progrs.doneFlag(true);
            if (sResponse != null) {
                try {
                    Log.d("response Upload", sResponse.toString());
                    notifyListeners(101, sResponse.toString());

                } catch (Exception e) {
                    Log.e(e.getClass().getName(), e.getMessage(), e);
                }
            }
        }

    }




    /**
     * This class serves as placeholder for mainly during image uploading.
     * @link ProgressListener is used here to monitore progress.
     *
     * */
    private static class MultiPartEntityMod extends MultipartEntity {

        private final ProgressListener listener;

        public MultiPartEntityMod(final ProgressListener listener) {
            super();
            this.listener = listener;
        }

        public MultiPartEntityMod(final HttpMultipartMode mode, final ProgressListener listener) {
            super(mode);
            this.listener = listener;
        }

        public MultiPartEntityMod(HttpMultipartMode mode, final String boundary, final Charset charset, final ProgressListener listener) {
            super(mode, boundary, charset);
            this.listener = listener;
        }

        @Override
        public void writeTo(final OutputStream outstream) throws IOException {
            super.writeTo(new CountingOutputStream(outstream, this.listener));
        }



        /**
         *  This class serves for counting progress of transferred streams.
         *
         * */
        private class CountingOutputStream extends FilterOutputStream {

            private final ProgressListener listener;
            private long transferred;

            public CountingOutputStream(final OutputStream out, final ProgressListener listener) {
                super(out);
                this.listener = listener;
                this.transferred = 0;
            }

            public void write(byte[] b, int off, int len) throws IOException {
                out.write(b, off, len);
                this.transferred += len;
                this.listener.transferred(this.transferred);
            }

            public void write(int b) throws IOException {
                out.write(b);
                this.transferred++;
                this.listener.transferred(this.transferred);
            }
        }
    }

    public interface ProgressListener {
        abstract boolean doneFlag(boolean b);
        void transferred(long num);
    }

}


