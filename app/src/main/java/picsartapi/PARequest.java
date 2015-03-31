package picsartapi;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * This class is main request provider for PicsArt api calls. It extends Volley
 * JsonObjectRequest, and encapsulates it's listeners.
 *
 * <p>Some Getters of this class  throw a <tt>NullPointerException</tt>
 * if the collections or class objects provided to them are null.
 *
 * <p>This class is a member of the
 * <a href="www.picsart.com">
 * </a>.
 *
 * @author  Arman Andreasyan 2/24/15
 */




public class PARequest extends JsonObjectRequest  {

    JSONObject mResponse;
    PARequestListener requestListener;


    public PARequest(int method, String url, JSONObject jsonRequest, PARequestListener listener) {

        super(method, url, jsonRequest, listener, listener);
    }

    public void setRequestListener(PARequestListener listener) {
        this.requestListener = listener;
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));

            this.mResponse = new JSONObject(jsonString);

            return Response.success(new JSONObject(jsonString), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }

    }

    @Override
    protected void deliverResponse(JSONObject response) {
        this.requestListener.onResponse(response);
    }

    @Override
    public void deliverError(VolleyError e){
        this.requestListener.onErrorResponse(e);
    }


    public static abstract class PARequestListener<T> implements Response.Listener, Response.ErrorListener {

    }


}
