package pArtapibeta;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.octo.android.robospice.persistence.springandroid.json.gson.GsonObjectPersister;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;

import test.api.picsart.com.picsart_api_test.PicsArtConst;


public class MainActivity extends Activity {


    private static Context context;
    WebView web;
    Button auth;
    SharedPreferences pref;
    TextView Access;
    ProgressDialog pDialog;
    Button testcallBtt;
    static String token;


    public static Context getAppContext() {
        return MainActivity.context;
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
        MainActivity.context = getApplicationContext();
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


                            new TokenGet().execute();

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


    private class SomWebViewDefClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url,
                                  Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            view.getSettings().setJavaScriptEnabled(true);
            view.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            view.canGoBackOrForward(1);

        }
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            return true;
        }
        @Override
        public void onLoadResource (WebView view, String url){
            if(url.contains("http://stage.i.picsart.com/api/oauth2/localhost?code=")){
                view.stopLoading();
                //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                //startActivity(intent);
                view.canGoBack();
            }
        }
    }

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





    public void onTestCallClick(View v){

        //// Volley Get request ///
       /* JsonObjectRequest  req= new UserProfile().makeRequest();
        RequestQueue que = Volley.newRequestQueue(this);
        que.add(req);*/


         TextView jj = (TextView)findViewById(R.id.Access);
        User aaa = new User();
      if (aaa.available)
        aaa.testPrint();


       // jj.setText();



        ////// END of Volley Test Request ///



       // new GetUser().execute(PicsArtConst.USER_PROFILE_URL,PicsArtConst.TOKEN_URL_PREFIX+token, PicsArtConst.CLIENT_ID);
    }

   /* private class GetUser extends AsyncTask<String,String, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected JSONObject doInBackground(String... args) {
            GetAccessToken jParser = new GetAccessToken();
            JSONObject json = jParser.getUserObject("http://stage.i.picsart.com/api/users/show/me.json?token=", token, CLIENT_ID);
            return json;

        }

        @Override
        protected void onPostExecute(JSONObject json) {
            if (json != null) {
                try {

                    String name = json.getString("name");
                    String username = json.getString("username");
                    String photo = json.getString("photo");
                    Toast.makeText(getApplicationContext(), " " + name + "\n" + username + "\n" + photo, Toast.LENGTH_LONG).show();

                } catch (JSONException e) {

                }


            }
        }
    }*/


    private class JsonPErsister extends JSONObject{
        JSONObject myobj;

        public JsonPErsister(JSONObject obj){myobj=obj;}
    }
}





