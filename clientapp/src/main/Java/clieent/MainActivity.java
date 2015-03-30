package clieent;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
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

import java.util.ArrayList;
import java.util.Arrays;

import Utils_Image.ImageLoader;
import picsartapi.AccessToken;
import picsartapi.Location;
import picsartapi.Photo;
import picsartapi.PhotoController;
import picsartapi.PicsArtConst;
import picsartapi.RequestListener;
import picsartapi.UserController;

public class MainActivity extends Activity {


    private static Context context;
    ProgressDialog pDialog;
    SharedPreferences pref;

    WebView web;
    PhotoController pctr;
    Button auth;
    TextView Access;
    Button testcallBtt;

    static String token;
    static int[] counter = new int[1];

    public static Context getAppContext() {
        return context;
    }

    public static String getAccessToken() {
        return token;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PicsArtConst.CLIENT_ID="armanClientdT762iHtILCtf7sl";
        PicsArtConst.CLIENT_SECRET="IBjgv1OKPADXX8b9KRJfJloc5n1AzDMb";

        setContentView(R.layout.activity_main);
        testcallBtt = (Button) findViewById(R.id.testCall);
        Access = (TextView) findViewById(R.id.Access);
        auth = (Button) findViewById(R.id.auth);
        MainActivity.context = this.getApplicationContext();
        try {
            pref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
            token = pref.getString("access_token", "");
            //Toast.makeText(getApplicationContext(), "Access Token " + token, Toast.LENGTH_LONG).show();
        } catch (Exception c) {
            Log.i("ERROR Loading Token ", ": no token is persist ");
        }



    }

    @Override
    public void onResume() {
        super.onResume();
        initing();

    }

    public void initing() {

        auth.setOnClickListener(new View.OnClickListener() {
            Dialog authDialog;

            @Override
            public void onClick(View view) {
                authDialog = new Dialog(MainActivity.this);
                authDialog.setContentView(R.layout.auth_dialog);
                // alert.setContentView(R.layout.auth_dialog);

                web = (WebView) authDialog.findViewById(R.id.webv);
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

                            String authCode = pref.getString("Code", "");
                            AccessToken.setListener(new RequestListener(1) {
                                @Override
                                public void onRequestReady(int requmber, String mmsg) {
                                    MainActivity.token = AccessToken.getAccessToken();
                                    MainActivity.token = mmsg;
                                    PhotoController.setAccessToken(mmsg);

                                    Log.d("Token is ready: ", MainActivity.token + "::::: as message " + mmsg);
                                    SharedPreferences.Editor edit = pref.edit();
                                    edit.putString("access_token", mmsg);
                                    edit.commit();
                                }
                            });
                            AccessToken.requestAccessToken(PicsArtConst.TOKEN_URL, authCode, PicsArtConst.CLIENT_ID,
                                    PicsArtConst.CLIENT_SECRET, PicsArtConst.REDIRECT_URI, PicsArtConst.GRANT_TYPE,getAppContext());


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

    //////listener for picking

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == 88) {    //Picking path of photo
            if (resultCode == RESULT_OK) {

            }
        }
    }


    public void onTestCallClick(View v) {


        final ImageView im1 = (ImageView) findViewById(R.id.img1);
        final ImageView im2 = (ImageView) findViewById(R.id.img2);
        final ImageView im3 = (ImageView) findViewById(R.id.img3);

        counter[0] = 10;
        final ArrayList<Photo> tmpPh = new ArrayList<>();


        RequestListener listenerOne = new RequestListener(3333) {
            @Override
            public void onRequestReady(int requmber, String message) {
                if (requmber == 444) {
                    Log.d("Commented", " comment");
                }
                if (requmber == 2222) {

                    Log.d("Photo is updated", " is updated");
                }

                if (requmber == 44444) {    ////PhotoInfo  Upload Case
                    Log.i("Photo is uploaded: ", message);
                }


            }
        };

        RequestListener listenerTwo = new RequestListener(3335) {
            @Override
            public void onRequestReady(int requmber, String message) {

                Log.d("imageDNLD ", String.valueOf(requmber));
                if (requmber == 13) {
                    ImageLoader imLdr = new ImageLoader(getAppContext());
                    imLdr.DisplayImage(tmpPh.get(0).getUrl(), R.drawable.preloader, im1);
                    imLdr.DisplayImage(tmpPh.get(1).getUrl(), R.drawable.preloader, im2);
                    imLdr.DisplayImage(tmpPh.get(2).getUrl(), R.drawable.preloader, im3);
                }
            }
        };


        Log.d("Access token", getAccessToken());
        final PhotoController pc = new PhotoController(getAppContext(), getAccessToken());


        ///Example  uploading photo to account/////
        Photo toUpload = new Photo(Photo.IS.GENERAL);
        toUpload.setLocation(new Location("poxoooc", "Qaxaaaq", "Plac@@@", "State@@", "Zipcod@@", "Armenia", new ArrayList<>(Arrays.asList(45.0, 37.0))));
        toUpload.setTitle("picsarttt");
        toUpload.setTags(new ArrayList<>(Arrays.asList("ntag1", "nag2", "ntag3")));
        toUpload.setPath("/storage/removable/sdcard1/DCIM/100ANDRO/DSC_0009.jpg");
        toUpload.setId("164458028001202");
        toUpload.setMature(false);
        toUpload.set_public(true);
         PhotoController.uploadPhoto(toUpload);
        // toUpload.setIsFor(Photo.IS.COVER);
        // PhotoController.uploadPhoto(toUpload);


        //////////// Getting given comments Count//////



        /////////////////////////// Static Listener for Photo Stuff //////////////////////

        PhotoController.resgisterListener(listenerOne);
        PhotoController.resgisterListener(listenerTwo);


        pc.setListener(new RequestListener(0) {
            @Override
            public void onRequestReady(int requmber, String message) {
                // Log.d("Comment resp", message);
                // pc.addComment("163773067002202", message.substring(0,20));
                if (requmber == 601) {

                    Log.d("Data updated: ", message);

                }

                if (requmber == 603) {

                    Log.d("Data not updated: ", message);

                }

                if (requmber == 201) {
                    tmpPh.add(pc.getPhoto());
                    counter[0] += 1;
                    Log.d("COUNTERR: ", "   :::: " + pc.getPhoto().getUrl() + " " + pc.getPhoto().getTitle());
                    PhotoController.notifyListener(3335, counter[0], "");

                }

                if (requmber == 301) {
                    // eventManager.fire(new PAEvent(301,message));

                    Log.d("commEENT: ", pc.getCommentsLists().toString());

                }
                if (requmber == 501) {
                    Log.d("removed Comment: ", message);
                }

                if (requmber == 701) {
                    Log.d("Liked Photo: ", message);
                }

                if (requmber == 401) {
                    Log.d("Comment photo: ", message);
                }

                if (requmber == 1001) {
                    Log.d("Liked Users is here ", message + "  :::USERS::: " + pc.getPhotoLikedUsers().toString());


                }
            }
        });


        // pc.addComment("163773067002202","blabla  comment 2");
        String[] phids = {"164548899000202", "164294945000202", "147971743000201"};


        //++ pc.requestCommentByid("164458028001202","550abcd81fa703694b0000e5");
        //++ pc.requestPhoto("164458028001202");
        //++ pc.requestComments("163086538001202", 0, 4);
        //++ pc.requestLikedUsers("165074146000202",0,Integer.MAX_VALUE);
        //++ pc.addComment("164458028001202",new Comment("blaaaFinalP",true));
        //++ pc.like("164458028001202");
        //++ pc.unLike("164458028001202");

        //++ pc.deleteComment("164458028001202", "550abcd4556768804b00016e");

        //+ pc.uploadPhoto(toUpload);
         pc.updatePhotoData(toUpload);
        /*for (int i = 0; i < 3; i++) {
            pc.requestPhoto(phids[i]);
        }*/


        Photo phh = new Photo(Photo.IS.AVATAR);
        phh.setLocation(new Location("nor poxoc", "nor Qaxaaaq", "nor Plac@@@", "nor State@@", "nor Zipcod@@", "Armenia", new ArrayList<>(Arrays.asList(40.0, 36.0))));
        phh.setTitle("nor nkariii anun 2");
        phh.setTags(new ArrayList<>(Arrays.asList("nor tag1", "nor tag2", "nor tag3")));
        phh.setId("164458028001202");
        phh.setPath("/storage/removable/sdcard1/DCIM/100ANDRO/DSC_0014.jpg");


        final UserController uc = new UserController(token, getAppContext());
        uc.setListener(new RequestListener(0) {
            @Override
            public void onRequestReady(int requmber, String message) {
                if (requmber == 207) {
                    for (int i = 0; i < uc.getPhoto().size(); i++) {

                        Log.d("PhotoResponse", uc.getPhoto().get(i).getId());
                    }
                }

            }
        });

        //uc.requestLikedPhotos("me",2,1);
        // uc.requestUserPhotos("me",0,60);

        Photo ph2 = new Photo(Photo.IS.GENERAL);
        //ph2.setTitle("blaTitle");
        ph2.set_public(Boolean.TRUE);
        ph2.setPath("/storage/removable/sdcard1/DCIM/100ANDRO/DSC_0015.jpg");


        // PhotoControllerTests.testDeleteComment("164458028001202", "550abcce556768804b00016d",token);
        // PhotoControllerTests.testUploadImage(token, ph2);
        // PhotoControllerTests.testUpdatePhotoData(token, toUpload);
        // PhotoControllerTests.testRequestPhoto("163086538001202", token);
        // PhotoControllerTests.testLike("163086538001202", token);
        // PhotoControllerTests.testUnLike("163086538001202",token);
        // PhotoControllerTests.testAddComment("163086538001202","blaaaa",token);

        final PhotoController pc3 = new PhotoController(getAppContext(), token);
        // pc3.requestLikedUsers("163086538001202",0,50);
        pc3.setListener(new RequestListener(0) {
            @Override
            public void onRequestReady(int requmber, String message) {
                if (requmber == 1001) {
                    Log.d("liked Users", pc3.getPhotoLikedUsers().toString());
                }
            }
        });


    }


    @Override
    public void onStop() {
        super.onStop();
    }

}





