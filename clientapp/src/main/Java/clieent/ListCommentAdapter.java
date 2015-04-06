package clieent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.picsart.api.Comment;
import com.picsart.api.PhotoController;
import com.picsart.api.RequestListener;

import java.util.List;

/**
 * Created by Arman Andreasyan on 4/3/15.
 */
public class ListCommentAdapter extends ArrayAdapter<Comment> {

    public ListCommentAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public ListCommentAdapter(Context context, int resource, List<Comment> items) {
        super(context, resource, items);
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {

            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.item_list_view, null);

        }

        Comment p = getItem(position);

        if (p != null) {

            TextView tt = (TextView) v.findViewById(R.id.usname);
            TextView tt1 = (TextView) v.findViewById(R.id.comtext);
            TextView ttcr = (TextView) v.findViewById(R.id.created);
            ImageView delv = (ImageView)v.findViewById(R.id.delcomment);

            if (tt != null) {
                tt.setText(p.getCommenterId());
            }
            if (tt1 != null) {

                tt1.setText(p.getText());
            }
            if (ttcr != null) {

                ttcr.setText(p.getCreated().getYear()+ ":"+p.getCreated().getDay()+":"+p.getCreated().getHours());
            }

            delv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PhotoController pc = new PhotoController(ListCommentAdapter.this.getContext(),MainActivity.getAccessToken());
                    pc.setListener(new RequestListener(0) {
                        @Override
                        public void onRequestReady(int requmber, String message) {
                            remove(getItem(position));
                            notifyDataSetChanged();


                        }
                    });

                    pc.deleteComment(getItem(position).getPotoID(),getItem(position).getId());

                }
            });
        }



        return v;

    }

    }
