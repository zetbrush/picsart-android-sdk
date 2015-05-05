package com.picsart.api;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;

/**
 *
 * This class handles requests that are expected
 * to response as arrays. It extends Volley's JsonArrayRequest
 * and encapsulates it's listeners.
 *
 @author Arman Andreasyan on 4/16/15.
 *
 */
public class PaArrayRequest extends JsonArrayRequest {
    JSONArray  mResponseArr;
    PARequestListener requestListener;

    /**
     * Creates a new request.
     *
     * @param url           URL to fetch the JSON from
     * @param listener      Listener to receive the JSON response
     * @param errorListener Error listener, or null to ignore errors.
     */
    public PaArrayRequest(String url, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        super(url, listener, errorListener);
    }



    public PaArrayRequest( String url,  PARequest.PARequestListener listener) {

        super( url,listener,listener);
    }

    public void setRequestListener(PARequestListener listener) {
        this.requestListener = listener;
    }

    @Override
    protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
            this.mResponseArr= new JSONArray(jsonString);
            return Response.success(mResponseArr,
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }

    }

    @Override
    protected void deliverResponse(JSONArray response) {
        this.requestListener.onResponse(response);
    }

    @Override
    public void deliverError(VolleyError e){

        this.requestListener.onErrorResponse(e);
    }

    protected static abstract class PARequestListener<T> implements Response.Listener, Response.ErrorListener {

    }



}
