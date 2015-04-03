package clieent;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.picsart.api.Photo;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import clieent.Utils_Image.ImageLoader;

public class ImagePagerAdapter extends PagerAdapter {
    Context ctx;
    static  ImageLoader il;

    public ArrayList<Photo> getmImages() {
        return mImages;
    }

    private ArrayList<Photo> mImages = new ArrayList<>();

    private onDoneClick clickList;

    public ImagePagerAdapter(ArrayList<Photo> imPaths, Context ctx, onDoneClick onclicklisten ) {
        this.mImages = imPaths;
        this.ctx = ctx;
        clickList = onclicklisten;
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
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);


        //il = new ImageLoader(ctx);
        //il.DisplayImage((mImages.get(position)).getUrl()+"?r1024x1024", R.drawable.preloader, (ImageView) fl.findViewById(R.id.image_only));

        URL url = null;
        try {
            url = new URL(mImages.get(position).getUrl()+"?r1024x1024");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Bitmap bmp = null;

        new BitmapWorkerTask(((ImageView) fl.findViewById(R.id.image_only))).execute(url);

        ((ImageView) fl.findViewById(R.id.image_only)).setImageBitmap(bmp);

        ImageView lkim = (ImageView) fl.findViewById(R.id.likeic);
        ImageView cmmim = (ImageView) fl.findViewById(R.id.commentic);
        TextView titxt = (TextView) fl.findViewById(R.id.titletext);
        TextView tagst = (TextView) fl.findViewById(R.id.tags);
        ImageView addcomm = (ImageView) fl.findViewById(R.id.addcomm);
        if (mImages.get(position).getLikesCount() > 0) {
            lkim.setImageBitmap(glowimage(R.drawable.likeic));

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


    private Bitmap glowimage(int resource) {

        int margin = 24;
        int halfMargin = margin / 2;


        int glowRadius = 25;

        // the glow color
        int glowColor = Color.rgb(0, 192, 255);

        // The original image to use
        Bitmap src = BitmapFactory.decodeResource(ctx.getResources(),resource);

        // extract the alpha from the source image
        Bitmap alpha = src.extractAlpha();

        // The output bitmap (with the icon + glow)
        Bitmap bmp = Bitmap.createBitmap(src.getWidth() + margin,
                src.getHeight() + margin, Bitmap.Config.ARGB_8888);

        // The canvas to paint on the image
        Canvas canvas = new Canvas(bmp);

        Paint paint = new Paint();
        paint.setColor(glowColor);

        // outer glow
        paint.setMaskFilter(new BlurMaskFilter(glowRadius, BlurMaskFilter.Blur.OUTER));
        canvas.drawBitmap(alpha, halfMargin, halfMargin, paint);

        // original icon
        canvas.drawBitmap(src, halfMargin, halfMargin, null);
        return bmp;
    }



    class BitmapWorkerTask extends AsyncTask<URL, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;
        private URL url;
        Bitmap bmp = null;
        ImageView iv;


        public BitmapWorkerTask(ImageView imageView) {
            imageView.setImageResource(R.drawable.preloader);
            iv = imageView;
            // Use a WeakReference to ensure the ImageView can be garbage collected
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        // Decode image in background.
        @Override
        protected Bitmap doInBackground(URL... params) {
            url = params[0];

            if (url != null) {
                try {

                    bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return bmp;
        }



        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (imageViewReference != null && bitmap != null) {
                final ImageView imageView = imageViewReference.get();
                imageView.animate().cancel();
                if (imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }


    public interface  onDoneClick {
        void onPagerVClick(View v, int position, Photo ph);

    }



}





