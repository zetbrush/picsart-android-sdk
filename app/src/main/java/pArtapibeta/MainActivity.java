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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

import imageDLUtils.ImageLoader;
import test.pArtapibeta.testPhoto;


public class MainActivity extends Activity  {

static LinkedList<Photo> photoList = null;
    private static Context context;
    WebView web;
    Button auth;
    SharedPreferences pref;
    TextView Access;
    ProgressDialog pDialog;
    Button testcallBtt;
    static String token;
    static int[] counter =new int[1];


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
                            AccessToken.setListener(new RequestListener(1) {
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

         final PhotoController pc = new PhotoController(getAppContext(),token);




        ///Example  uploading photo to account/////


       Photo toUpload = new Photo(Photo.IS.GENERAL);
        toUpload.setLocation(new Location("poxoooc","Qaxaaaq","Plac@@@","State@@","Zipcod@@","Armenia",new ArrayList<>(Arrays.asList(45,37))));
        toUpload.setTitle("nkariii__anun3");
        toUpload.setTags(new ArrayList<>(Arrays.asList("ntag1", "nag2", "ntag3")));
        toUpload.setPath("/storage/removable/sdcard1/DCIM/100ANDRO/DSC_0008.jpg");
       // PhotoController.uploadPhoto(toUpload);


        //////////// Getting given comments Count//////

      //  PhotoController.getComments("163773067002202",0,50);




        counter[0]=10;
        final ArrayList<Photo> tmpPh = new ArrayList<>();


        /////////////////////////// Static Listener for Photo Stuff //////////////////////

        PhotoController.setSt_listener(new RequestListener(0) {
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

                if (requmber == 0002) {    ////PhotoInfo  Test Case
                    Log.i("TEST|Ph.Contr|: ", msg);
                }

                if (requmber == 44444) {    ////PhotoInfo  Upload Case
                    Log.i("Photo is uploaded: ", msg);
                }

                if(requmber ==555) {
                    for (Comment com : PhotoController.getComments()) {
                        Log.d("Comments ", com.getCreated() + " " + com.getText());
                    }
                }


        }});


        PhotoController.setSt_listener(new RequestListener(4) {


            @Override
            public void onRequestReady(int requmber, String message) {
                Log.d("imageDNLD ", String.valueOf(requmber));
                if (requmber ==13){
                    ImageView im1 = (ImageView)findViewById(R.id.img1);
                    ImageView im2 = (ImageView)findViewById(R.id.img2);
                    ImageView im3 = (ImageView)findViewById(R.id.img3);
                    ImageLoader imLdr = new ImageLoader(getAppContext());
                    imLdr.DisplayImage(tmpPh.get(0).getUrl(),R.drawable.ic_launcher,im1);
                    imLdr.DisplayImage(tmpPh.get(1).getUrl(),R.drawable.ic_launcher,im2);
                    imLdr.DisplayImage(tmpPh.get(2).getUrl(),R.drawable.ic_launcher,im3);

                }
            }
        });
           pc.setListener(new RequestListener(4) {
            @Override
            public void onRequestReady(int requmber, String message) {
               // Log.d("Comment resp", message);
               // pc.comment("163773067002202", message.substring(0,20));
                if(requmber==102){
                    tmpPh.add(pc.getPhoto());
                    counter[0] += 1;
                    Log.d("COUNTERR: ", String.valueOf(counter[0]));

                    PhotoController.getSt_listener(4).onRequestReady(counter[0], "");

                }

            }
        });

        // pc.comment("163773067002202","blabla  comment 2");
        String[] phids = {"163086538001202","163773067002202","163858526001202"};


        for(int i =0 ; i<3;i++){
            pc.requestPhoto(phids[i]);
        }











        Photo phh = new Photo(Photo.IS.GENERAL);
        phh.setLocation(new Location("nor poxoc", "nor Qaxaaaq", "nor Plac@@@", "nor State@@", "nor Zipcod@@", "Armenia", new ArrayList<Integer>(Arrays.asList(40,36))));
        phh.setTitle("nor nkariii anun 2");
        phh.setTags(new ArrayList<>(Arrays.asList("nor tag1", "nor tag2", "nor tag3")));
        phh.setId("163086538001202");
       // PhotoController.updatePhotoData(phh);


      //  testPhoto.testGetPhotoInfo("163086538001202", token);
      //  testPhoto.testLike("163086538001202", token);
       // testPhoto.testUnLike("163086538001202",token);
       // testPhoto.testComment("163086538001202","blaaaa",token);








    }







    @Override
    public void onStop(){

    }

}





