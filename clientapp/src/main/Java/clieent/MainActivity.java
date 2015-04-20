package clieent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.picsart.api.LoginManager;
import com.picsart.api.Photo;
import com.picsart.api.PhotoController;
import com.picsart.api.PicsArtConst;
import com.picsart.api.RequestListener;
import com.picsart.api.UserController;

import java.util.ArrayList;


public class MainActivity extends Activity {


    private static Context context;

    SharedPreferences pref;
    Button signInBtn;
    TextView userName;
    Button myPageBtn;
    Button followingBtn;
    Button followerBtn;
    Button getPhotosBtn;
    Button uploadBtn;
    ViewPager viewPager;
    static String token;
    ArrayList<String> imagePaths = null;
    final int IMAGE_PICK = 789654;
    public static String USER_ID = "me";
    public static String USER_NM = "";


    public static Context getAppContext() {
        return context;
    }

    public static String getAccessToken() {
        return token;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PicsArtConst.CLIENT_ID = "ZetOmniaexo1SNtY52usPTry";
        PicsArtConst.CLIENT_SECRET = "yY2fEJU8R9rFmuwtOZRQhm4ZK2Kdwqhk";
        PicsArtConst.REDIRECT_URI="localhost";

        setContentView(R.layout.activity_main);

        getPhotosBtn = (Button) findViewById(R.id.photoinf);
        uploadBtn = (Button) findViewById(R.id.uploadph);
        myPageBtn = (Button) findViewById(R.id.mypagebtn);
        viewPager = (ViewPager) findViewById(R.id.myviewpager);
        followingBtn = (Button) findViewById(R.id.followings);
        followerBtn = (Button) findViewById(R.id.followers);
        userName = (TextView) findViewById(R.id.usernametext);
        signInBtn = (Button) findViewById(R.id.auth);

        MainActivity.context = this.getApplicationContext();


       /* try {
            pref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
            token = pref.getString("access_token", "");
            AccessToken.setAccessToken(token);

        } catch (Exception c) {
            Log.i("ERROR Loading Token ", ": no token is persisted ");
        }*/

    }

    @Override
    public void onResume() {
        super.onResume();
        initing();

    }

    public void initing() {
        onPhotoGet(getPhotosBtn);

        if(!LoginManager.getInstance().hasValidSession()){
            signInBtn.setText("Login with PicsArt");
        }
        else signInBtn.setText("Logout from PicsArt");

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!LoginManager.getInstance().hasValidSession()) {
                    LoginManager.getInstance().openSession(MainActivity.this, new RequestListener(0) {
                        @Override
                        public void onRequestReady(int reqnumber, String message) {
                            if (reqnumber == 7777) {
                                signInBtn.setText("Logout from PicsArt");
                                Toast.makeText(MainActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    onPhotoGet(getPhotosBtn);
                } else LoginManager.getInstance().closeSession(MainActivity.this);


            }
        });
    }

    //////listener for  image picking
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

            //Picking path of photo and notifying to upload when selection is made
        if (requestCode == IMAGE_PICK && resultCode == RESULT_OK) {
            Uri path = data.getData();

            Cursor cursor = null;
            try {
                String[] proj = {MediaStore.Images.Media.DATA};
                cursor = context.getContentResolver().query(path, proj, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                imagePaths = new ArrayList<>();
                imagePaths.add(cursor.getString(column_index));
                PhotoController.notifyListener(IMAGE_PICK, IMAGE_PICK, "");
            } finally {
                if (cursor != null) {
                    cursor.close();
                }

            }


        }
    }


    public void myPageClick(View v) {
        USER_ID = "me";
        USER_NM = "ME";
        userName.setText(USER_NM);
        onPhotoGet(getPhotosBtn);

    }


    public void onPhotoGet(View v) {
        if (!LoginManager.getInstance().hasValidSession()) {
            Toast.makeText(MainActivity.this,"You hav't logged in",Toast.LENGTH_SHORT).show();

        } else {
                final UserController uc = new UserController(getAppContext());

                uc.setListener(new RequestListener(0) {
                    @Override
                    public void onRequestReady(int reqnumber, String message) {
                        if (reqnumber == 209) {


                            final ImagePagerAdapter adapter = new ImagePagerAdapter(uc.getPhotos(), getAppContext(), new ImagePagerAdapter.onDoneClick() {
                                @Override
                                public void onPagerVClick(View v, int position, Photo ph) {
                                    switch (v.getId()) {

                                        case R.id.likeic:
                                            ClickActionHelper.onLikeUnlike(v, position, ph, MainActivity.this);
                                            return;

                                        case R.id.commentic:
                                            ClickActionHelper.onCommentsClick(v, position, ph, MainActivity.this);
                                            return;

                                        case R.id.addcomm:
                                            ClickActionHelper.onAddCommentClick(v, position, ph, MainActivity.this);

                                        default:

                                    }
                                }

                            });

                            viewPager.setAdapter(adapter);

                        }


                        Log.d("ERR", reqnumber + "  " + message);

                    }
                });

                uc.requestUserPhotos(USER_ID, 0, Integer.MAX_VALUE);
            }
    }

    public void onUpload(View v) {
        //registering listener with id IMAGE_PICK for uploading task, which will be called if IMAGE_PICK Listener will be notified

        if (!LoginManager.getInstance().hasValidSession()) {
            Toast.makeText(MainActivity.this,"You hav't logged in",Toast.LENGTH_SHORT).show();
        }
        else {

            PhotoController.resgisterListener(new RequestListener(IMAGE_PICK) {
                @Override
                public void onRequestReady(int reqnumber, String message) {
                    if (reqnumber == IMAGE_PICK) {
                        final ProgressDialog pd = new ProgressDialog(MainActivity.this, AlertDialog.THEME_HOLO_DARK);
                        pd.setCanceledOnTouchOutside(true);
                        String path = imagePaths.get(0);
                        Photo ph = new Photo(Photo.IS.GENERAL);
                        ph.setPath(path);
                        PhotoController.ProgressListener progrs = new PhotoController.ProgressListener() {
                            @Override
                            public boolean doneFlag(boolean b) {
                                if (b) {
                                    pd.dismiss();
                                    Toast.makeText(MainActivity.this, "Successfully uploaded", Toast.LENGTH_SHORT).show();
                                    onPhotoGet(getPhotosBtn);
                                }
                                return b;
                            }

                            @Override
                            public void transferred(long num) {
                                pd.setProgress((int) num);
                            }
                        };


                        pd.setTitle("Uploading Photo..");
                        pd.setCancelable(true);
                        pd.setProgressStyle(pd.STYLE_HORIZONTAL);
                        pd.show();
                        pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
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

    }


    public void onFollowings(View v) {
        if (!LoginManager.getInstance().hasValidSession()) {
            Toast.makeText(MainActivity.this,"You hav't logged in",Toast.LENGTH_SHORT).show();
        }
        else {
            final UserController uc = new UserController(MainActivity.this);

            uc.setListener(new RequestListener(0) {
                @Override
                public void onRequestReady(int reqnumber, String message) {
                    if (reqnumber == 204) {
                        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

                        alertDialog.setTitle("Followings :: #" + uc.getUserFollowing().size() + "");

                        ListFollowAdapter adapter = new ListFollowAdapter(MainActivity.this, R.layout.item_list_view, uc.getUserFollowing(), true);

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
                                userName.setText(USER_NM);
                                onPhotoGet(getPhotosBtn);

                            }
                        });


                        alertDialog.show();
                    }
                }
            });
            uc.requestUserFollowing(USER_ID, 0, Integer.MAX_VALUE);
        }

    }


    public void onFollowers(View v) {
        if (!LoginManager.getInstance().hasValidSession()) {
            Toast.makeText(MainActivity.this,"You hav't logged in",Toast.LENGTH_SHORT).show();
        }
        else {
            final UserController uc = new UserController(MainActivity.this);
            uc.setListener(new RequestListener(0) {
                @Override
                public void onRequestReady(int reqnumber, String message) {
                    if (reqnumber == 203) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                        alertDialog.setTitle("Followers :: #" + uc.getUserFollowers().size() + "");
                        ListFollowAdapter adapter = new ListFollowAdapter(MainActivity.this, R.layout.item_list_view, uc.getUserFollowers(), false);

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
                                userName.setText(USER_NM);
                                onPhotoGet(getPhotosBtn);

                            }
                        });


                        alertDialog.show();
                    }
                }
            });
            uc.requestUserFollowers(USER_ID, 0, Integer.MAX_VALUE);
        }
    }



    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed(){
     super.onBackPressed();
        if(true) {
            pref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
            SharedPreferences.Editor edit = pref.edit();

            edit.putString("access_token", "87AXn93Tz52Hkx7Up6sNeipJT9LGTlc0ZetOmniaexo1SNtY52usPTry");
            edit.commit();
        }
    }

}



