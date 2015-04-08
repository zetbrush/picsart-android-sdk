package clieent;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.picsart.api.Photo;

import java.util.ArrayList;

public class ImagePagerAdapter extends PagerAdapter {
    Context ctx;
    ImageLoaderConfiguration conf;
    com.nostra13.universalimageloader.core.ImageLoader imageLoader;

    public ArrayList<Photo> getmImages() {
        return mImages;
    }

    private ArrayList<Photo> mImages = new ArrayList<>();

    private onDoneClick clickList;

    public ImagePagerAdapter(ArrayList<Photo> imPaths, Context ctx, onDoneClick onclicklisten) {
        this.mImages = imPaths;
        this.ctx = ctx;
        clickList = onclicklisten;
        conf = ImageLoaderConfiguration.createDefault(ctx);
        imageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
        imageLoader.init(conf);
    }

    @Override
    public int getCount() {
        return mImages.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((FrameLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        final ImageView imageView = new ImageView(ctx);

        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);

        FrameLayout fl = (FrameLayout) inflater.inflate(R.layout.main_image_frame, container, false);

        int padding = 3;
        imageView.setPadding(padding, padding, padding, padding);


        ImageView img = (ImageView) fl.findViewById(R.id.image_only);

        final DisplayImageOptions optionsImg = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.preloader)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                .considerExifParams(true)
                .showImageOnLoading(R.drawable.preloader)
                .displayer(new FadeInBitmapDisplayer(1555))
                .build();

        imageLoader.displayImage((mImages.get(position)).getUrl() + "?r1024x1024", img, optionsImg, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {
                view.startAnimation(rotateLoadingAnim());
            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {
                if(view !=null) {
                    view.setAnimation(null);
                    view.setBackgroundResource(R.drawable.noimageavailable);
                }
            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                view.setAnimation(null);

            }

            @Override
            public void onLoadingCancelled(String s, View view) {
                if(view !=null) {
                    view.setAnimation(null);
                    view.setBackgroundResource(R.drawable.noimageavailable);
                }
            }
        });


        final ImageView lkim = (ImageView) fl.findViewById(R.id.likeic);
        ImageView cmmim = (ImageView) fl.findViewById(R.id.commentic);
        TextView titxt = (TextView) fl.findViewById(R.id.titletext);
        TextView tagst = (TextView) fl.findViewById(R.id.tags);
        ImageView addcomm = (ImageView) fl.findViewById(R.id.addcomm);
        if (mImages.get(position).getIsLiked() ==null || !mImages.get(position).getIsLiked() ) {
            lkim.startAnimation(blinkImage());

        }



        if (mImages.get(position).getTitle() != null && mImages.get(position).getTitle() != "") {
            titxt.setText(mImages.get(position).getTitle());

        }

        if (mImages.get(position).getTags() != null) {
            StringBuilder sb = new StringBuilder();
            for (String tag : mImages.get(position).getTags()) {
                sb.append(tag + " ");
            }
            tagst.setText(sb.toString());

        }

        TextView info = (TextView) fl.findViewById(R.id.infotext);

        info.setText(mImages.get(position).getLikesCount() + " likes\n" + mImages.get(position).getCommentsCount() + " comments\n" + mImages.get(position).getViewsCount() + " Views");






        //Like and Unlike  Listener Redirection///
        lkim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePagerAdapter.this.clickList.onPagerVClick(v, position, mImages.get(position));

                if(mImages.get(position).getIsLiked()!=null && mImages.get(position).getIsLiked()){
                    lkim.startAnimation(blinkImage());
                }
                else lkim.setAnimation(null);

                    notifyDataSetChanged();
                 }});


            ///////////////


            ///Show Comments  Listener Redirection/////
            cmmim.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {

                    ImagePagerAdapter.this.clickList.onPagerVClick(v, position, mImages.get(position));

              }});

        //Add Comment Listener Redirection ////
        addcomm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePagerAdapter.this.clickList.onPagerVClick(v, position, mImages.get(position));
            }
        });




        ((ViewPager) container).addView(fl, 0);
        return fl;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((FrameLayout) object);
    }


    private AlphaAnimation blinkImage() {

        AlphaAnimation blinkanimation= new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
        blinkanimation.setDuration(1000); // duration - half a second
        blinkanimation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
        blinkanimation.setRepeatCount(20); // Repeat animation infinitely
        blinkanimation.setRepeatMode(Animation.REVERSE);
        return blinkanimation;
    }

    private Animation rotateLoadingAnim(){

        RotateAnimation anim = new RotateAnimation(0.0f, 360.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(Animation.INFINITE);
        anim.setDuration(700);
       return anim;
    }




    public interface  onDoneClick {
        void onPagerVClick(View v, int position, Photo ph);

    }



}





