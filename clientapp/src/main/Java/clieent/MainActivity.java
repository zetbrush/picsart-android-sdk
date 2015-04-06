package clieent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.picsart.api.AccessToken;
import com.picsart.api.Comment;
import com.picsart.api.Location;
import com.picsart.api.Photo;
import com.picsart.api.PhotoController;
import com.picsart.api.PicsArtConst;
import com.picsart.api.RequestListener;
import com.picsart.api.UserController;

import java.util.ArrayList;
import java.util.Arrays;

import clieent.Utils_Image.ImageLoader;

public class MainActivity extends Activity {


    private static Context context;
    ProgressDialog pDialog;
    SharedPreferences pref;

    WebView web;
    Button auth;
    TextView Access;
    Button testcallBtt;
    Button following;
    Button follower;
    Button getPhotoinf;
    Button uploadbtn;
    ViewPager viewPager;
    static String token;
    ArrayList<String> imagePaths =null;
    final int IMAGE_PICK = 789654;
    public static String USER_ID="me";

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

        PicsArtConst.CLIENT_ID="ZetOmniaexo1SNtY52usPTry";
        PicsArtConst.CLIENT_SECRET="yY2fEJU8R9rFmuwtOZRQhm4ZK2Kdwqhk";

        setContentView(R.layout.activity_main);

        getPhotoinf = (Button)findViewById(R.id.photoinf);
        uploadbtn = (Button)findViewById(R.id.uploadph);
        testcallBtt = (Button) findViewById(R.id.testCall);
        viewPager = (ViewPager) findViewById(R.id.myviewpager);
        following = (Button)findViewById(R.id.followings);
        follower = (Button)findViewById(R.id.followers);


        auth = (Button) findViewById(R.id.auth);

        MainActivity.context = this.getApplicationContext();
        try {
            pref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
            token = pref.getString("access_token", "");
            PhotoController.setAccessToken(token);
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
        onPhotoGet(getPhotoinf);

        auth.setOnClickListener(new View.OnClickListener() {
            Dialog authDialog;

            @Override
            public void onClick(View view) {
                authDialog = new Dialog(MainActivity.this);
                authDialog.setContentView(R.layout.auth_dialog);
                web = (WebView) authDialog.findViewById(R.id.webv);
                web.getSettings().setJavaScriptEnabled(true);
                web.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
                web.getSettings().setSupportMultipleWindows(true);

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


                web.loadUrl(authURL);
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

        if(requestCode ==IMAGE_PICK && resultCode==RESULT_OK){
            Uri path = data.getData();

                Cursor cursor = null;
                try {
                    String[] proj = { MediaStore.Images.Media.DATA };
                    cursor = context.getContentResolver().query(path,  proj, null, null, null);
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    imagePaths=new ArrayList<>();
                    imagePaths.add(cursor.getString(column_index));
                    PhotoController.notifyListener(IMAGE_PICK,IMAGE_PICK,"");
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }

                }



        }
    }


    public void myPageClick(View v) {

        USER_ID="me";
        onPhotoGet(getPhotoinf);


        final ArrayList<Photo> tmpPh = new ArrayList<>();

        counter[0] = 10;
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

                }
            }
        };


        final PhotoController pc = new PhotoController(getAppContext(), getAccessToken());
        ///Example  uploading photo to account/////
        Photo toUpload = new Photo(Photo.IS.GENERAL);
        toUpload.setLocation(new Location("poxoooc", "Qaxaaaq", "Plac@@@", "State@@", "Zipcod@@", "Armenia", new ArrayList<>(Arrays.asList(45.0, 37.0))));
        toUpload.setTitle("picsarttt");
        toUpload.setTags(new ArrayList<>(Arrays.asList("nnnntag1", "naaaaaag2", "ntaaag3")));
        toUpload.setPath("/storage/removable/sdcard1/DCIM/100ANDRO/DSC_0009.jpg");
        toUpload.setId("164458028001202");
        toUpload.setMature(false);
        toUpload.setPublic(true);

        // PhotoController.uploadPhoto(toUpload);
        // toUpload.setIsFor(Photo.IS.COVER);
        // PhotoController.uploadPhoto(toUpload);



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


         //uc.requestLikedPhotos("me",2,1);
         // uc.requestUserPhotos("me",0,60);



        // PhotoControllerTests.testDeleteComment("164458028001202", "550abcce556768804b00016d",token);
        // PhotoControllerTests.testUploadImage(token, ph2);
        // PhotoControllerTests.testUpdatePhotoData(token, toUpload);
        // PhotoControllerTests.testRequestPhoto("163086538001202", token);
        // PhotoControllerTests.testLike("163086538001202", token);
        // PhotoControllerTests.testUnLike("163086538001202",token);
        // PhotoControllerTests.testAddComment("163086538001202", "blaaaa", token,getAppContext());
        // PhotoControllerTests.testGetCommentById("163086538001202","",token,getAppContext());
        // PhotoControllerTests.testGetLikedUsers("photoid",0,5,token,getAppContext());
        // PhotoControllerTests.testRequestComments("photoid",0,5,token,getAppContext());

        //++ pc.requestCommentByid("164458028001202","550abcd81fa703694b0000e5");
        //++ pc.requestPhoto("164458028001202");
        //++ pc.requestComments("163086538001202", 0, 4);
        //++ pc.requestLikedUsers("165074146000202",0,Integer.MAX_VALUE);
        //++ pc.addComment("164458028001202",new Comment("blaaaFinalP",true));
        //++ pc.like("164458028001202");
        //++ pc.unLike("164458028001202");
        //++ pc.deleteComment("164458028001202", "550abcd4556768804b00016e");



    }



    public void onPhotoGet(View v){

        final UserController uc = new UserController(getAccessToken(),getAppContext());

        uc.setListener(new RequestListener(0) {
            @Override
            public void onRequestReady(int requmber, String message) {


                ImagePagerAdapter adapter = new ImagePagerAdapter(uc.getPhotos(), getAppContext(), new ImagePagerAdapter.onDoneClick() {
                    @Override
                    public void onPagerVClick(View v, int position, Photo ph) {
                        switch (v.getId()) {

                            case R.id.likeic:
                                onLikeUnlike(v, position, ph);
                                return;

                            case R.id.commentic:
                                onCommentsClick(v, position, ph);
                                return;

                            case R.id.addcomm:
                                onAddCommentClick(v, position, ph);

                            default:
                                ;
                        }
                    }

                });
                viewPager.setAdapter(adapter);


            }
        });

            uc.requestUserPhotos(USER_ID, 0, Integer.MAX_VALUE);

    }

            public void onUpload(View v) {
                //registering listener for uploading task, which will be called if IMAGE_PICK  Listener will be notified

                PhotoController.resgisterListener(new RequestListener(IMAGE_PICK) {
                    @Override
                    public void onRequestReady(int requmber, String message) {
                        if (requmber == IMAGE_PICK) {
                            final ProgressDialog d = new ProgressDialog(MainActivity.this, AlertDialog.THEME_HOLO_DARK);
                            d.setCanceledOnTouchOutside(true);
                            String path = imagePaths.get(0);
                            Photo ph = new Photo(Photo.IS.GENERAL);
                            ph.setPath(path);
                            PhotoController.ProgressListener progrs = new PhotoController.ProgressListener() {
                                @Override
                                public boolean doneFlag(boolean b) {
                                    if (b) {
                                        d.dismiss();
                                        Toast.makeText(MainActivity.this,"Successfully uploaded",Toast.LENGTH_SHORT).show();
                                        onPhotoGet(getPhotoinf);
                                    }
                                    return b;
                                }

                                @Override
                                public void transferred(long num) {
                                    d.setProgress((int) num);
                                }
                            };


                            d.setTitle("Uploading Photo..");
                            d.setCancelable(true);

                            d.setProgressStyle(d.STYLE_HORIZONTAL);
                            d.show();
                            d.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    PhotoController.cancelUpload();
                                    dialog.dismiss();
                                }
                            });
                            PhotoController.uploadPhoto(progrs, ph);

                        }
                    }
                });

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, IMAGE_PICK);


            }

            public void onLikeUnlike(View v, int position, final Photo ph) {
                PhotoController pc = new PhotoController(MainActivity.this);
                pc.setListener(new RequestListener(0) {
                    @Override
                    public void onRequestReady(int requmber, String message) {

                        if (requmber == 701) {
                            Log.d("Like", message);
                            ph.setIsLiked(true);

                        }
                        if (requmber == 801) {
                            Log.d("Like", message);
                            ph.setIsLiked(false);
                        }
                    }
                });

                if (ph.getIsLiked() == null || ph.getIsLiked() == false) {
                    pc.like((ph.getId()));
                } else if (ph.getIsLiked() == true) {
                    pc.unLike(ph.getId());
                }


            }


            public void onCommentsClick(View v, int position, final Photo ph) {
                final PhotoController pc = new PhotoController(this);


                pc.setListener(new RequestListener(0) {
                    @Override
                    public void onRequestReady(int requmber, String message) {
                        if(requmber==301) {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

                            alertDialog.setTitle("Comments");

                            ListCommentAdapter adapter = new ListCommentAdapter(MainActivity.this, R.layout.item_list_view, pc.getCommentsLists());
                            alertDialog.setAdapter(adapter, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });


                            alertDialog.show();
                        }
                        else Toast.makeText(MainActivity.this,message.toString(),Toast.LENGTH_SHORT).show();

                    }
                });
                pc.requestComments(ph.getId(), ph.getCommentsCount()-50, Integer.MAX_VALUE);
            }


            public void onAddCommentClick(View v,int position, final Photo ph){
                final PhotoController pc = new PhotoController(this);
                final Dialog commentDialog = new Dialog(MainActivity.this);
                commentDialog.setTitle("New Comment");
                pc.setListener(new RequestListener(0) {
                    @Override
                    public void onRequestReady(int requmber, String message) {
                         if(requmber==401) {
                             commentDialog.dismiss();
                             Toast.makeText(MainActivity.this,"Successfully commented",Toast.LENGTH_SHORT).show();
                         }
                    }
                });


                commentDialog.setContentView(R.layout.add_comment);
                Button okBtn = (Button) commentDialog.findViewById(R.id.ok);
                okBtn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        String txtcomment = ((EditText)(commentDialog.getWindow().findViewById(R.id.body))).getText().toString();
                        pc.addComment(ph.getId(),new Comment(txtcomment,true));


                    }

                });
                Button cancelBtn = (Button) commentDialog.findViewById(R.id.cancel);
                cancelBtn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        commentDialog.dismiss();
                    }
                });
                  commentDialog.show();




            }

            public void onFollowings(View v) {
                final UserController uc = new UserController(getAccessToken(),MainActivity.this);

                uc.setListener(new RequestListener(0) {
                    @Override
                    public void onRequestReady(int requmber, String message) {
                        if (requmber == 204) {
                              final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

                            alertDialog.setTitle("Followings :: #"+uc.getUserFollowing().size()+"");

                            ListFollowAdapter adapter = new ListFollowAdapter(MainActivity.this, R.layout.item_list_view, uc.getUserFollowing(),true);

                            alertDialog.setAdapter(adapter, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                            adapter.registerDataSetObserver(new DataSetObserver() {
                                @Override
                                public void onChanged() {
                                    super.onChanged();
                                    onFollowings(following);
                                    onPhotoGet(getPhotoinf);

                                }
                            });


                            alertDialog.show();
                        }
                    }
                });
                uc.requestUserFollowing(USER_ID, 0, Integer.MAX_VALUE);




            }


    public void onFollowers(View v){

        final UserController uc = new UserController(getAccessToken(),MainActivity.this);

        uc.setListener(new RequestListener(0) {
            @Override
            public void onRequestReady(int requmber, String message) {
                if (requmber == 203) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

                    alertDialog.setTitle("Followers :: #" + uc.getUserFollowers().size() + "");

                    ListFollowAdapter adapter = new ListFollowAdapter(MainActivity.this, R.layout.item_list_view, uc.getUserFollowers(),false);

                    alertDialog.setAdapter(adapter, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    adapter.registerDataSetObserver(new DataSetObserver() {
                        @Override
                        public void onChanged() {
                            super.onChanged();
                            onFollowers(follower);
                            onPhotoGet(getPhotoinf);

                        }
                    });


                    alertDialog.show();
                }
            }
        });
        uc.requestUserFollowers(USER_ID, 0, Integer.MAX_VALUE);
    }


            @Override
            public void onStop() {
                super.onStop();
            }

        }







