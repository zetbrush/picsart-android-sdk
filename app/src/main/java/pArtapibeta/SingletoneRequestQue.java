package pArtapibeta;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * This  class serves as Singleton instance provider
 *
 * It hold all requests in one place.
 *
 * <p>This class is a member of the
 * <a href="www.picsart.com">
 * </a>.
 *
 * @author  Arman Andreasyan 2/20/15
 */

public class SingletoneRequestQue {
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static Context mCtx;
    static SingletoneRequestQue mInstance = null;



    private SingletoneRequestQue(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();

        mImageLoader = new ImageLoader(mRequestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<String, Bitmap>(20);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });

    }


    /**
     *  @return Single instance of requests que
     *
     * */
    public static synchronized SingletoneRequestQue getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SingletoneRequestQue(context);
        }
        return mInstance;
    }


    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public <T> void cancelRequestQueue(Request<T> req) {
        getRequestQueue().cancelAll(req);
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

}
