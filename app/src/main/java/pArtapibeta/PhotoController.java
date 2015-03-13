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
 * Created by Arman on 2/23/15.
 */
public class PhotoController {
    Context ctx;
    RequestListener listener;
    static RequestListener st_listener;
    String token;
    volatile Photo photo;
    static volatile Comment _comment;
    static volatile Photo[] photos;
    static volatile Comment[][] commentList = new Comment[1][];
    ArrayList<User> photoLikedUsers = new ArrayList<>();



    public ArrayList<User> getPhotoLikedUsers() {
        return photoLikedUsers;
    }
    public void setPhotoLikedUsers(ArrayList<User> photoLikedUsers) {
        this.photoLikedUsers = photoLikedUsers;
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
        if (PhotoController.st_listener == null)
            PhotoController.st_listener = st_listener;

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
    static ArrayList<RequestListener> st_listeners_all = new ArrayList<>();





    public static Comment[] getComments() {
        return commentList[0];
    }

    public Photo getPhoto() {
        return photo;
    }

    public Photo[] getPhotos() {
        return photos;
    }

    public void setPhotos(Photo[] photos) {
        this.photos = photos;
    }


    public static synchronized void uploadPhoto(Photo... photo) {

        new ImageUploadTask().execute(photo);

    }

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
                listener.onRequestReady(101, error.toString());

            }

            @Override
            public void onResponse(Object response) {
                Log.d("Response 9", response.toString());
                //photo = new Photo(Photo.IS.GENERAL);
                photo = PhotoFactory.parseFrom(response);
                listener.onRequestReady(102, response.toString());
            }
        });


    }

    /**
     * @param photoId id of photo
     * @param limit   max limit to show
     * @param offset  starting index to count
     */
    public static synchronized void getComments(String photoId, final int offset, final int limit) {

        String url = PicsArtConst.PHOTO_COMMENTS_URL + photoId + ".json" + PicsArtConst.API_PREFX + PicsArtConst.APIKEY;
        PARequest req = new PARequest(Request.Method.GET, url, null, null);
        SingletoneRequestQue.getInstance(MainActivity.getAppContext()).addToRequestQueue(req);
        req.setRequestListener(new PARequest.PARequestListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }

            @Override
            public void onResponse(Object response) {

                try {
                    JSONArray _comArr = ((JSONObject) response).getJSONArray("response");
                    Comment[] comment = new Comment[_comArr.length()];
                    for (int i = 0; i < _comArr.length(); i++) {
                        JSONObject val = _comArr.getJSONObject(i);
                        Gson gson = new Gson();
                        comment[i] = (gson.fromJson(val.toString(), Comment.class));
                    }
                    int nwOffset = 0;
                    int nwlimit = 0;
                    if (offset > comment.length) {
                        nwOffset = comment.length;
                    } else if (offset < 0) {
                        nwOffset = 0;
                    } else {
                        nwOffset = offset;
                    }
                    if (limit > comment.length) {
                        nwlimit = comment.length;
                    } else if (limit < 0) {
                        nwlimit = 0;
                    } else {
                        nwlimit = limit;
                    }
                    if (nwlimit - nwOffset < 0) nwlimit = nwOffset;
                    Comment[] tmp = new Comment[nwlimit - nwOffset];
                    for (int i = nwOffset, j = 0; i < nwlimit; i++, j++) {
                        tmp[j] = comment[i];
                    }
                    commentList[0] = tmp;
                    notifyListeners(555, "getComments");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public synchronized void comment(String photoID, final String comment) {

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

    public static synchronized void removeComment(String photoId, final String commentId) {
        String url = PicsArtConst.PHOTO_REMOVE_COMMENT_URL + photoId + ".json" + PicsArtConst.API_PREFX + PicsArtConst.APIKEY;
        StringRequest req = new StringRequest(Request.Method.POST, url,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("RemoveComment ", response);
                        notifyListeners(666, "");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

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


        String url = PicsArtConst.PHOTO_UPDATE_INFO_URL + photo.getId() + ".json" + PicsArtConst.API_PREFX + PicsArtConst.APIKEY;
        PARequest req = new PARequest(Request.Method.POST, url, jobj, null);

        req.setRequestListener(new PARequest.PARequestListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

            @Override
            public void onResponse(Object response) {
                Log.d("UpdatedonLisData ", response.toString());
                notifyListeners(2222, response.toString());
            }
        });

        SingletoneRequestQue.getInstance(MainActivity.getAppContext()).addToRequestQueue(req);

    }

    public synchronized void like(String photoId) {
        String url = PicsArtConst.PHOTO_LIKE_URL + photoId + ".json" + PicsArtConst.API_PREFX + PicsArtConst.APIKEY;
        StringRequest req = new StringRequest(Request.Method.POST, url,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("PhotoLike ", response);
                        listener.onRequestReady(202, response);
                        // st_listener.onRequestReady(999,response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("PhotoLikeError ", error.toString());
                        listener.onRequestReady(203, error.toString());
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

    public synchronized void unLike(String photoId) {
        String url = PicsArtConst.PHOTO_UNLIKE_URL + photoId + ".json" + PicsArtConst.API_PREFX + PicsArtConst.APIKEY;
        StringRequest req = new StringRequest(Request.Method.POST, url,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("PhotoUnlike ", response);
                        listener.onRequestReady(301, response);
                        //st_listener.onRequestReady(1111,"");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("PhotoUnlike ", error.toString());
                        listener.onRequestReady(302, error.toString());
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


    public PhotoController(Context ctx, String token) {
        this.ctx = ctx;
        this.token = token;
    }

    public void setListener(RequestListener listener) {
        this.listener = listener;
    }


    /*public static void  setSt_Listener(RequestListener listener){
        if(st_listener==null)
        PhotoController.st_listener=listener;

        else  st_listener =listener;

    }*/


    public Comment getCommentByid(String id) {
        //TODO
        return new Comment(null, null, null);
    }


    public void getLikedUsers(String photoId, int offset, int limit){

        String url = PicsArtConst.PHOTO_LIKED_USERS_URL + photoId + PicsArtConst.API_TEST_PREF + PicsArtConst.APIKEY+PicsArtConst.OFFSET+offset+PicsArtConst.LIMIT+limit;
       // url = PicsArtConst.Get_PHOTO_URL_PUB + id + ".json" + PicsArtConst.API_PREFX + PicsArtConst.APIKEY;
        PARequest request = new PARequest(Request.Method.GET, url, null, null);
        SingletoneRequestQue.getInstance(ctx).addToRequestQueue(request);
        request.setRequestListener(new PARequest.PARequestListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", error.toString());
                listener.onRequestReady(101, error.toString());

            }

            @Override
            public void onResponse(Object response) {
                Log.d("Response 9", response.toString());
                //photo = new Photo(Photo.IS.GENERAL);
               // photoLikedUsers = UserFactory.parseFrom(response);
                listener.onRequestReady(102, response.toString());
            }
        });

    }

    public synchronized void searchPhotos() {

    }


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

                    notifyListeners(44444, "Image(es) is/are uploaded!!");

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
