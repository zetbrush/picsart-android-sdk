package com.picsart.api;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;



/**
 * This class consists exclusively of static methods, that operate on
 * requesting OAuth 2.0 token.
 *
 * Static listener is used to notify client.
 *
 * <p>This class is a member of the
 * <a href="www.com.picsart.com">
 * </a>.
 *
 * @author  Arman Andreasyan 3/9/15
 */

public class AccessToken {

    private static String accessToken;

    public static RequestListener getListener() {
        return listener;
    }

    private static RequestListener listener = null;
    private static String code;
    private static Context ctx=null;

    public static String getAccessToken() {
        return accessToken;
    }
    public static void setAccessToken(String accessToken) {
        AccessToken.accessToken = accessToken;
    }

    public static void setListener(RequestListener listener) {
        AccessToken.listener = listener;
    }


    private AccessToken() {
    }


    public static void requestAccessToken(final Context ctx){
        AccessToken.ctx=ctx;
        final WebView web = new WebView(ctx);
        final Dialog authDialog;
        final ProgressDialog[] pDialog = new ProgressDialog[1];
        SharedPreferences pref = null;

        authDialog = new Dialog(ctx);
        authDialog.setContentView(web);
        web.getSettings().setJavaScriptEnabled(true);
        web.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        web.getSettings().setSupportMultipleWindows(true);


        final SharedPreferences finalPref = pref;
        web.setWebViewClient(new WebViewClient() {
            boolean authComplete = false;
            Intent resultIntent = new Intent();

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.i("ShouldOvver", " rideUrlLoading" + url);
                if (url.contains("logout")) {
                 AccessToken.accessToken=null;
                    PhotoController.setAccessToken(null);
                    UserController.setAccessToken(null);
                    authDialog.dismiss();
                    AccessToken.listener.onRequestReady(7777, "logout");

                }


                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url,
                                      Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

                authComplete = false;

                //pDialog[0].dismiss();

            }

            String authCode;

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                authDialog.setContentView(web);


                if (url.contains("?code=") && authComplete != true) {

                    Uri uri = Uri.parse(url);
                    authCode = uri.getQueryParameter("code");
                    Log.i("CDE", " : " + authCode);
                    authComplete = true;
                    resultIntent.putExtra("code", authCode);
                    AccessToken.code = authCode;
                    requestToken(ctx);
                    authDialog.dismiss();

                } else if (url.contains("error=access_denied")) {
                    AccessToken.listener.onRequestReady(7777,"access denied");
                    Log.i(" ", "ACCESS_DENIED_HERE");
                    authComplete = true;

                }
            }
        });

        String authURL = PicsArtConst.OAUTH_URL + "?redirect_uri=" + PicsArtConst.REDIRECT_URI + "&response_type=code&client_id=" + PicsArtConst.CLIENT_ID;
        web.loadUrl(authURL);
        authDialog.show();
        authDialog.setTitle("Authorize PicsArt");
        authDialog.setCancelable(true);
    }


    private static void requestToken(Context ctx) {

        final String address=PicsArtConst.TOKEN_URL;
        final String client_id = PicsArtConst.CLIENT_ID;
        final String client_secret = PicsArtConst.CLIENT_SECRET;
        final String redirect_uri = PicsArtConst.REDIRECT_URI;
        final String grant_type= PicsArtConst.GRANT_TYPE;

        String userCredentials = client_id + ":" + client_secret;
        final String base64EncodedCredentials = Base64.encodeToString(userCredentials.getBytes(), Base64.NO_WRAP);

        StringRequest req = new StringRequest(PARequest.Method.POST, address, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {

                JSONObject jsOOb;
                Log.d("accessTokenResp: ", response);
                try {
                    jsOOb = new JSONObject(response);
                    String tok;
                    tok = jsOOb.getString("access_token");
                    AccessToken.accessToken = tok;

                    AccessToken.listener.onRequestReady(7777, tok);

                } catch (JSONException e) {

                }




            }
        }
                , null) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("code", AccessToken.code);
                params.put("client_id", client_id);
                params.put("client_secret", client_secret);
                params.put("redirect_uri", redirect_uri);
                params.put("grant_type", grant_type);

                return params;

            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Basic " + base64EncodedCredentials);
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;

            }
        };

        SingletoneRequestQue.getInstance(AccessToken.ctx).addToRequestQueue(req);

    }


}
