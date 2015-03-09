package pArtapibeta;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.VolleyError;

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
import java.util.HashMap;
import java.util.Map;

import test.api.picsart.com.picsart_api_test.PicsArtConst;

public class PhotoController {
    Context ctx;
    RequestListener listener;
    static RequestListener st_listener;
    String token;
    volatile Photo photo;
    static volatile Comment[][] comm = new Comment[1][];


    public static Comment[] getComments() {
        return comm[0];
    }


    public Photo getPhoto() {
        return photo;
    }

    public static synchronized void uploadPhoto(Photo... photo) {
        new ImageUploadTask().execute(photo);

    }

    public void setListener(RequestListener listener) {
        this.listener = listener;
    }

    public static void setSt_Listener(RequestListener listener) {
        PhotoController.st_listener = listener;
    }

    public synchronized void requestPhoto(String id) {
        assert this.listener != null;
        String url = PicsArtConst.Get_PHOTO_URL + id + PicsArtConst.TOKEN_PREFIX + token;
        PARequest request = new PARequest(Request.Method.GET, url, null, null);
        SingletoneRequestQue.getInstance(ctx).addToRequestQueue(request);
        request.setRequestListener(new PARequest.PARequestListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", error.toString());
            }

            @Override
            public void onResponse(Object response) {
                Log.d("Response 9", response.toString());
                photo = new Photo(Photo.IS.GENERAL);
                photo.parseFrom(response);
                listener.onRequestReady(9);
            }
        });


    }


    public PhotoController(Context ctx, String token) {
        this.ctx = ctx;
        this.token = token;
    }


    /**
     * @param photoId id of photo
     * @param limit   max limit to show
     * @param offset  starting index to count
     */

    public static synchronized void getComments(String photoId, final int limit, final int offset) {

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
                        String txt = val.getString("text");
                        // Date crtd = new Date(val.getString("created"));
                        String cmid = val.getString("_id");
                        comment[i] = (new Comment(txt, null, cmid));
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
                    comm[0] = tmp;
                    st_listener.onRequestReady(555);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }


    public Comment getCommentByid(String id) {
        //TODO
        return new Comment(null, null, null);
    }

    public void removeComment(String id) {

        //TODO
    }
    /*public User[] getLikes(){
        //TODO
    }*/

    public void updateData() {
        //TODO
    }

    /**
     *
     * */
    public static synchronized void comment(String photoID, final String comment) {

        String url = PicsArtConst.PHOTO_ADD_COMMENT_URL + photoID + ".json" + PicsArtConst.API_PREFX + PicsArtConst.APIKEY;
        PARequest req = new PARequest(Request.Method.POST, url, null, new PARequest.PARequestListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

            @Override
            public void onResponse(Object response) {
                Log.d("Response ", response.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("is_social", "1");
                params.put("text", comment);
                return params;
            }

            ;

            // params.put("Content-Type", "fmultipart/form-data");

        };

        SingletoneRequestQue.getInstance(MainActivity.getAppContext()).addToRequestQueue(req);
        req.setRequestListener(new PARequest.PARequestListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

            @Override
            public void onResponse(Object response) {
                //  Log.d("Response ", response.toString());
                st_listener.onRequestReady(444);
            }
        });

/*
        Map<String, String> hedr = req.getHeaders();
            hedr= new HashMap<>();
            hedr.put("Content-Type", "multipart/form-data");*/

          /*  String bdtype =req.getBodyContentType();
            bdtype ="multipart/form-data";*/

        //params.put("is_social","1");


    }


    public boolean like() {
        //TODO
        return false;
    }

    public boolean unLike() {
        //TODO
        return false;
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
                        for (String str : ph.getTags().getTagValues()) {
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
