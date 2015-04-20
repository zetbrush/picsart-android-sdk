package clieent;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.picsart.api.Comment;
import com.picsart.api.LoginManager;
import com.picsart.api.Photo;
import com.picsart.api.PhotoController;
import com.picsart.api.RequestListener;

/**
 * Created by Arman on 4/15/15.
 */
public class ClickActionHelper {

    private ClickActionHelper(){}

    public static void onLikeUnlike(View v, int position, final Photo ph, Context ctx) {
        PhotoController pc = new PhotoController(ctx);
        if (!LoginManager.getInstance().hasValidSession()) {
            Toast.makeText(ctx,"You hav't logged in",Toast.LENGTH_SHORT).show();

        }
        pc.setListener(new RequestListener(0) {
            @Override
            public void onRequestReady(int reqnumber, String message) {

                if (reqnumber == 701) {
                    Log.d("Like", message);
                    ph.setIsLiked(true);

                }
                if (reqnumber == 801) {
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


    public static void onCommentsClick(View v, int position, final Photo ph, final Context ctx) {
        final PhotoController pc = new PhotoController(ctx);

        if (!LoginManager.getInstance().hasValidSession()) {
            Toast.makeText(ctx,"You hav't logged in",Toast.LENGTH_SHORT).show();

        }

        pc.setListener(new RequestListener(0) {
            @Override
            public void onRequestReady(int reqnumber, String message) {
                if (reqnumber == 301) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(ctx);

                    alertDialog.setTitle("Comments");

                    ListCommentAdapter adapter = new ListCommentAdapter(ctx, R.layout.item_list_view, pc.getCommentsLists());
                    alertDialog.setAdapter(adapter, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });


                    alertDialog.show();
                } else
                    Toast.makeText(ctx, message.toString(), Toast.LENGTH_SHORT).show();

            }
        });
        pc.requestComments(ph.getId(), ph.getCommentsCount(), Integer.MAX_VALUE);
    }


    public static void onAddCommentClick(View v, int position, final Photo ph, final Context ctx) {
        if (!LoginManager.getInstance().hasValidSession()) {
            Toast.makeText(ctx,"You hav't logged in",Toast.LENGTH_SHORT).show();

        }
        final PhotoController pc = new PhotoController(ctx);
        final Dialog commentDialog = new Dialog(ctx);
        commentDialog.setTitle("New Comment");
        pc.setListener(new RequestListener(0) {
            @Override
            public void onRequestReady(int reqnumber, String message) {
                if (reqnumber == 401) {
                    commentDialog.dismiss();
                    Toast.makeText(ctx, "Successfully commented", Toast.LENGTH_SHORT).show();
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



}
