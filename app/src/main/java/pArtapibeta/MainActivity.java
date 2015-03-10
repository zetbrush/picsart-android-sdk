package pArtapibeta;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

import test.pArtapibeta.testPhoto;


public class MainActivity extends Activity implements RequestListener {

static LinkedList<Photo> photoList = null;
    private static Context context;
    WebView web;
    Button auth;
    SharedPreferences pref;
    TextView Access;
    ProgressDialog pDialog;
    Button testcallBtt;
    static String token;


    public static Context getAppContext() {
        return context;
    }

    public static String getAccessToken(){
        return token;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        testcallBtt = (Button)findViewById(R.id.testCall);

        Access = (TextView) findViewById(R.id.Access);
        auth = (Button) findViewById(R.id.auth);
        MainActivity.context = this.getApplicationContext();
        try{
            pref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
             token = pref.getString("access_Token","");
            //Toast.makeText(getApplicationContext(), "Access Token " + token, Toast.LENGTH_LONG).show();
        } catch (Exception c){
          Log.i("ERROR Loading Token ", ": no token is persist ");
        }

        auth.setOnClickListener(new View.OnClickListener() {
            Dialog authDialog;
            @Override
            public void onClick(View view) {
                authDialog = new Dialog(MainActivity.this);
                authDialog.setContentView(R.layout.auth_dialog);
               // alert.setContentView(R.layout.auth_dialog);

                web = (WebView)authDialog.findViewById(R.id.webv);
                web.getSettings().setJavaScriptEnabled(true);
                web.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
                web.getSettings().setSupportMultipleWindows(true);
                //web.setWebViewClient(new SomWebViewDefClient());
                web.setWebViewClient(new WebViewClient() {
                    boolean authComplete = false;
                    Intent resultIntent = new Intent();

                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        Log.i("ShouldOvver", " rideUrlLoading" + url);


                        return false;
                    }
                    @Override
                    public void onPageStarted(WebView view, String url,
                                              Bitmap favicon) {
                        super.onPageStarted(view, url, favicon);
                       // view.setWebChromeClient(new MyWebChromeClient());
                        authComplete = false;
                        pDialog = ProgressDialog.show(view.getContext(), "",
                                "Connecting to " + " server", false);
                        pDialog.dismiss();

                    }

                    String authCode;
                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        pDialog.dismiss();

                        Log.i("COOODEE", "LINE 82 : " + authCode);
                        if (url.contains("?code=") && authComplete != true) {

                            Uri uri = Uri.parse(url);
                            authCode = uri.getQueryParameter("code");
                            Log.i("COOODEE", "CODE : " + authCode);
                            authComplete = true;
                            resultIntent.putExtra("code", authCode);
                            MainActivity.this.setResult(Activity.RESULT_OK,
                                    resultIntent);
                            setResult(Activity.RESULT_CANCELED, resultIntent);

                            SharedPreferences.Editor edit = pref.edit();
                            edit.putString("Code", authCode);
                            edit.commit();


                          // new TokenGet().execute();

                            String Code = pref.getString("Code", "");
                            AccessToken.setListener(new RequestListener() {
                                @Override
                                public void onRequestReady(int requmber,String mmsg) {
                                    MainActivity.token = AccessToken.getAccessToken();
                                    Log.d("Token is ready: ", MainActivity.token);
                                }
                            });
                            AccessToken.requestAccessToken(PicsArtConst.TOKEN_URL, Code, PicsArtConst.CLIENT_ID,
                                    PicsArtConst.CLIENT_SECRET, PicsArtConst.REDIRECT_URI, PicsArtConst.GRANT_TYPE);


                            Toast.makeText(getApplicationContext(),
                                    "Authorization Code is: " + authCode,
                                    Toast.LENGTH_LONG).show();
                            authDialog.dismiss();
                        } else if (url.contains("error=access_denied")) {
                            Log.i("", "ACCESS_DENIED_HERE");
                            resultIntent.putExtra("code", authCode);
                            authComplete = true;
                            setResult(Activity.RESULT_CANCELED, resultIntent);
                            Toast.makeText(getApplicationContext(),
                                    "Error Occured", Toast.LENGTH_SHORT).show();



                        }

                    }

                });

                String authURL = PicsArtConst.OAUTH_URL + "?redirect_uri=" + PicsArtConst.REDIRECT_URI + "&response_type=code&client_id=" + PicsArtConst.CLIENT_ID;
                String tokenURL = PicsArtConst.TOKEN_URL + "?client_id=" + PicsArtConst.CLIENT_ID + "&client_secret=" + PicsArtConst.CLIENT_SECRET + "&redirect_uri=" + PicsArtConst.REDIRECT_URI + "&grant_type=authorization_code";

                web.loadUrl(authURL);
               // web.loadUrl("http://stage.i.picsart.com/api/oauth2/authorize?redirect_uri=localhost&response_type=code&client_id=armantestclient1nHhXPI9ZqwQA03XI");
                authDialog.show();
                authDialog.setTitle("Authorize PicsArt");
                authDialog.setCancelable(true);
            }

        });

    }
    final Context myApp = this;



    private class TokenGet extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;
        String Code;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Contacting ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            Code = pref.getString("Code", "");
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            GetAccessToken jParser = new GetAccessToken();
            JSONObject json = jParser.gettoken(PicsArtConst.TOKEN_URL, Code, PicsArtConst.CLIENT_ID,
                    PicsArtConst.CLIENT_SECRET, PicsArtConst.REDIRECT_URI, PicsArtConst.GRANT_TYPE);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {

            if (json != null) {

                try {

                    String tok = json.getString("access_token");
                    // String expire = json.getString("expires_in");
                    // String refresh = json.getString("refresh_token");
                    Log.d("Token Access", tok);
                    SharedPreferences prrefs = PreferenceManager.getDefaultSharedPreferences(
                            MainActivity.this);
                    prrefs.edit().putString("access_Token",tok).commit();

                    pDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Access Token: " + tok, Toast.LENGTH_LONG).show();
                    token=tok;
                    auth.setText("Authenticated");

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(getApplicationContext(), "Network Error",
                        Toast.LENGTH_SHORT).show();
                pDialog.dismiss();
            }


        }

    }


    @Override
    public void onRequestReady(int reqnumber,String mmsg) {

        if(reqnumber ==1)
        {
            Log.d("MainListener","reqnumber= "+reqnumber);
        }

    }


        //////listener for picking

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == 88) {    //Picking path of photo
            if (resultCode == RESULT_OK) {


            }
        }
    }

    public void onTestCallClick(View v) {


        PARequest.PARequestListener<JSONObject> listn = null;
        TextView jj = (TextView) findViewById(R.id.Access);




        ///Example  uploading photo to account/////
/*

       Photo toUpload = new Photo(Photo.IS.COVER);
        toUpload.setLocation(new Location("poxoooc","Qaxaaaq","Plac@@@","State@@","Zipcod@@","Armenia",new Coordiantes("40.00","36.00")));
        toUpload.setTitle("nkariii anun 2");
        toUpload.setTags(new Tag("tag1", "tag2", "tag3"));
        toUpload.setPath("/storage/removable/sdcard1/DCIM/100ANDRO/DSC_0025.jpg");
        PhotoController.uploadPhoto(toUpload);
*/

        //////////// Getting given comments Count//////
     /*   PhotoController.getComments("163086538001202",3,5);
        PhotoController.setSt_Listener(new RequestListener() {
            @Override
            public void onRequestReady(int requmber) {
                for(Comment com : PhotoController.getComments()) {
                    Log.d("Comments ",com.toString());
                }
            }
        });
       */


        /////////////////////////// Static Listener for Photo Request Stuff //////////////////////

        PhotoController.setSt_Listener(new RequestListener() {
            @Override
            public void onRequestReady(int requmber, String msg) {
                if (requmber == 444) {
                    Log.d("Commented", " comment");
                }
                if (requmber == 2222) {

                    Log.d("Photo is updated", " is updated");
                }

                if (requmber == 0001) {    ////PhotoInfo Get Test Case
                    Log.i("TEST|Ph.Contr|: ", msg);
                }

                if (requmber == 0002) {    ////PhotoInfo Get Test Case
                    Log.i("TEST|Ph.Contr|: ", msg);
                }

            }


        });

        // PhotoController.comment("163086538001202","blabla codeee comment");

        Photo phh = new Photo(Photo.IS.GENERAL);
        phh.setLocation(new Location("nor poxoc", "nor Qaxaaaq", "nor Plac@@@", "nor State@@", "nor Zipcod@@", "Armenia", new Coordiantes("nor 40.00", "nor 36.00")));
        phh.setTitle("nor nkariii anun 2");
        phh.setTags(new Tag("nor tag1", "nor tag2", "nor tag3"));
        phh.setId("163086538001202");
        PhotoController.updatePhotoData(phh);



        testPhoto.testGetPhotoInfo("163086538001202", token);
        testPhoto.testLike("163086538001202",token);



    }







    @Override
    public void onStop(){
        super.onStop();
        SingletoneRequestQue.getInstance(getAppContext()).getRequestQueue().stop();
    }

}





