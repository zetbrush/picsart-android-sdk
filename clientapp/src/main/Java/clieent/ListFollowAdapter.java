package clieent;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.picsart.api.RequestListener;
import com.picsart.api.User;
import com.picsart.api.UserController;

import java.util.List;

/**
 * Created by Arman Andreasyan on 4/6/15.
 */
public class ListFollowAdapter extends ArrayAdapter<User> {
    boolean isFollowing = false;

    public ListFollowAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public ListFollowAdapter(Context context, int resource, List<User> items,boolean isFollowing) {
        super(context, resource, items);
        this.isFollowing=isFollowing;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {

            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.item_list_view, null);

        }

        User p = getItem(position);

        if (p != null) {

            TextView tt = (TextView) v.findViewById(R.id.usname);
            TextView tt1 = (TextView) v.findViewById(R.id.comtext);
            TextView ttcr = (TextView) v.findViewById(R.id.created);
            ImageView delv = (ImageView)v.findViewById(R.id.delcomment);

            if (tt != null) {
                tt.setText(p.getUsername());
            }
            if (tt1 != null) {

                tt1.setText(p.getName());
            }
            if (ttcr != null) {

                ttcr.setText("ID "+p.getId());
            }

            delv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserController uc = new UserController(ListFollowAdapter.this.getContext());
                    uc.setListener(new RequestListener(0) {
                        @Override
                        public void onRequestReady(int reqnumber, String message) {
                            if (reqnumber == 210) {
                                remove(getItem(position));
                                notifyDataSetChanged();

                            } else if (reqnumber == 213) {
                                remove(getItem(position));
                                notifyDataSetChanged();

                            } else Log.i("UnFoll", message);
                        }
                    });
                    if(MainActivity.USER_NM=="me"){
                        if(ListFollowAdapter.this.isFollowing)
                            uc.unfollowUserWithID(getItem(position).getId().toString());//unFollowing if is Follower othrwise blocking
                        else uc.blockUserWithID(getItem(position).getId().toString());

                    }
                    else {
                        uc.blockUserWithID(getItem(position).getId().toString());
                    }


                }
            });
        }

        v.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                MainActivity.USER_ID= getItem(position).getId().toString();
                MainActivity.USER_NM= getItem(position).getUsername();
                notifyDataSetChanged();
                return true;
            }
        });






        return v;

    }




}