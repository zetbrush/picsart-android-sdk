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
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.picsart.api.AccessToken;
import com.picsart.api.Comment;
import com.picsart.api.Photo;
import com.picsart.api.PhotoController;
import com.picsart.api.PicsArtConst;
import com.picsart.api.RequestListener;
import com.picsart.api.UserController;

import java.util.ArrayList;


public class MainActivity extends Activity {


    private static Context context;

    SharedPreferences pref;
    Button signInBttn;
    TextView userName;
    Button myPageBtn;
    Button followingBtn;
    Button followerBtn;
    Button getPhotoinf;
    Button uploadbtn;
    ViewPager viewPager;
    static String token;
    ArrayList<String> imagePaths = null;
    final int IMAGE_PICK = 789654;
    public static String USER_ID = "me";
    public static String USER_NM = "";

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

        PicsArtConst.CLIENT_ID = "ZetOmniaexo1SNtY52usPTry";
        PicsArtConst.CLIENT_SECRET = "yY2fEJU8R9rFmuwtOZRQhm4ZK2Kdwqhk";

        setContentView(R.layout.activity_main);

        getPhotoinf = (Button) findViewById(R.id.photoinf);
        uploadbtn = (Button) findViewById(R.id.uploadph);
        myPageBtn = (Button) findViewById(R.id.mypagebtn);
        viewPager = (ViewPager) findViewById(R.id.myviewpager);
        followingBtn = (Button) findViewById(R.id.followings);
        followerBtn = (Button) findViewById(R.id.followers);
        userName = (TextView) findViewById(R.id.usernametext);
        signInBttn = (Button) findViewById(R.id.auth);

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
        if(getAccessToken()==null || getAccessToken()==""){
            token=("");
        }
        else {
            onPhotoGet(getPhotoinf);
        }
        signInBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AccessToken.setListener(new RequestListener(1) {
                    @Override
                    public void onRequestReady(int requmber, String mmsg) {
                        SharedPreferences.Editor edit = pref.edit();
                        if (mmsg.contains("logout")) {
                            edit.clear();
                            edit.commit();
                        } else if (mmsg.contains("denied")) {
                            Log.d("Token info: ", MainActivity.token + "::::: as message " + mmsg);
                        } else {
                            MainActivity.token = AccessToken.getAccessToken();
                            PhotoController.setAccessToken(mmsg);
                            UserController.setAccessToken(mmsg);
                            Log.d("Token info: ","::::: " + mmsg);
                            edit.putString("access_token", mmsg);
                            edit.commit();
                        }
                    }
                });
                AccessToken.requestAccessToken(MainActivity.this);


            }
        });
    }

    //////listener for  image picking

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == 88) {    //Picking path of photo
            if (resultCode == RESULT_OK) {

            }
        }

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
        onPhotoGet(getPhotoinf);

    }


    public void onPhotoGet(View v) {
            if(getAccessToken()==null || getAccessToken()=="" )
                MainActivity.token="";

        else {
                final UserController uc = new UserController(getAccessToken(), getAppContext());

                uc.setListener(new RequestListener(0) {
                    @Override
                    public void onRequestReady(int requmber, String message) {
                        if (requmber == 209) {


                            final ImagePagerAdapter adapter = new ImagePagerAdapter(uc.getPhotos(), getAppContext(), new ImagePagerAdapter.onDoneClick() {
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

                                    }
                                }

                            });

                            viewPager.setAdapter(adapter);

                        }

                    }
                });

                uc.requestUserPhotos(USER_ID, 0, Integer.MAX_VALUE);
            }
    }

    public void onUpload(View v) {
        //registering listener for uploading task, which will be called if IMAGE_PICK  Listener will be notified

        if(getAccessToken()==null || token=="")
        {
            MainActivity.token = "";
        }
        else {

            PhotoController.resgisterListener(new RequestListener(IMAGE_PICK) {
                @Override
                public void onRequestReady(int requmber, String message) {
                    if (requmber == IMAGE_PICK) {
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
                                    onPhotoGet(getPhotoinf);
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

    public void onLikeUnlike(View v, int position, final Photo ph) {
        PhotoController pc = new PhotoController(MainActivity.this);
        if(PhotoController.getAccessToken()==null){
            PhotoController.setAccessToken("");
        }
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

        if(PhotoController.getAccessToken()==null){
            PhotoController.setAccessToken("");
        }

        pc.setListener(new RequestListener(0) {
            @Override
            public void onRequestReady(int requmber, String message) {
                if (requmber == 301) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

                    alertDialog.setTitle("Comments");

                    ListCommentAdapter adapter = new ListCommentAdapter(MainActivity.this, R.layout.item_list_view, pc.getCommentsLists());
                    alertDialog.setAdapter(adapter, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });


                    alertDialog.show();
                } else
                    Toast.makeText(MainActivity.this, message.toString(), Toast.LENGTH_SHORT).show();

            }
        });
        pc.requestComments(ph.getId(), ph.getCommentsCount(), Integer.MAX_VALUE);
    }


    public void onAddCommentClick(View v, int position, final Photo ph) {
        if(PhotoController.getAccessToken()==null){
            PhotoController.setAccessToken("");
        }
        final PhotoController pc = new PhotoController(this);
        final Dialog commentDialog = new Dialog(MainActivity.this);
        commentDialog.setTitle("New Comment");
        pc.setListener(new RequestListener(0) {
            @Override
            public void onRequestReady(int requmber, String message) {
                if (requmber == 401) {
                    commentDialog.dismiss();
                    Toast.makeText(MainActivity.this, "Successfully commented", Toast.LENGTH_SHORT).show();
                }
            }
        });


        commentDialog.setContentView(R.layout.add_comment);
        Button okBtn = (Button) commentDialog.findViewById(R.id.ok);
        okBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String txtcomment = ((EditText) (commentDialog.getWindow().findViewById(R.id.body))).getText().toString();
                pc.addComment(ph.getId(), new Comment(txtcomment, true));


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
        if(getAccessToken()==null || token==""){
           token=("");
        }
        else {
            final UserController uc = new UserController(getAccessToken(), MainActivity.this);

            uc.setListener(new RequestListener(0) {
                @Override
                public void onRequestReady(int requmber, String message) {
                    if (requmber == 204) {
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
                                onPhotoGet(getPhotoinf);

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
        if (getAccessToken() == null || token=="") {
            token = ("");
        } else {
            final UserController uc = new UserController(getAccessToken(), MainActivity.this);

            uc.setListener(new RequestListener(0) {
                @Override
                public void onRequestReady(int requmber, String message) {
                    if (requmber == 203) {
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
                                onPhotoGet(getPhotoinf);

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

}







