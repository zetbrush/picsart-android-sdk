package com.picsart.api;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.preference.PreferenceManager;
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
 * This singleton class handles access, validation
 * and OAuth 2.0 Token management.
 *
 *
 * @author  Arman Andreasyan on 4/20/15.
 */
public class LoginManager {

    private static final int OK_CODE=PicsArtConst.OK_CODE_LOGIN;
    private static final int BAD_CODE=PicsArtConst.OK_CODE_LOGIN;

    private static LoginManager loginManager;
    private SharedPreferences pref;

    public static String getAccessToken() {
        return AccessToken.getAccessToken();
    }

    public static  LoginManager getInstance(){

        if(loginManager==null){
            synchronized (LoginManager.class){
                loginManager = new LoginManager();
            }
        }
        return loginManager;

    }
    private LoginManager(){}

    /**
     * Checks if token is still presented, i.e. User has logged in.
     *
     * */
    public boolean hasValidSession(Context ctx){
        if(AccessToken.getAccessToken()!=null && AccessToken.getAccessToken()!="" ){
            return true;
        } else {
            return getFromPrefs(ctx);
        }


    }

    /**
     * Checks for persisted token if any. otherwise authentication dialog will be opened
     * to obtain OAuth 2.0 token
     * @param ctx Application context
     * @param isSuccess Listener for successful token obtaining.
     *
     *
     * */
    public void openSession(Context ctx, final RequestListener isSuccess){

            pref = PreferenceManager.getDefaultSharedPreferences(ctx);

            AccessToken.setListener(new RequestListener(1) {
                @Override
                public void onRequestReady(int reqnumber, String mmsg) {
                    SharedPreferences.Editor edit = pref.edit();
                    if (mmsg.contains("logout")) {
                        edit.clear();
                        edit.commit();
                    } else if (mmsg.contains("access_denied")) {
                        Log.d("Token info: ", mmsg);
                    } else {
                        Log.d("Token info: ", mmsg);
                        edit.putString("access_token", AccessToken.getAccessToken());
                        edit.commit();
                        isSuccess.onRequestReady(OK_CODE,"Success");
                    }
                }
            });
            AccessToken.requestAccessToken(ctx);

    }

    /**
     * Deletes current token, also persisted token will be deleted.
     *
     * */
    public boolean closeSession(Context ctx){
        try {
            pref = PreferenceManager.getDefaultSharedPreferences(ctx);
            SharedPreferences.Editor edit = pref.edit();
            edit.clear();
            edit.commit();
            AccessToken.setAccessToken("");
        }catch (Exception e) {
            return false;
        }
        finally {

        }
        return true;
    }


    private boolean getFromPrefs(Context ctx){
        try {
            pref = PreferenceManager.getDefaultSharedPreferences(ctx);
            String token = pref.getString("access_token", "");
            if (token != "") {
                AccessToken.setAccessToken(token);
                return true;
            } else return false;

        } catch (Exception c) {
            Log.i("ERROR Loading Token ", ": no token is persisted ");
            return false;
        }
    }


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
    private static class AccessToken {

        private static String accessToken;
        private static RequestListener listener = null;
        private static String code;
        private static Context ctx=null;

        public static String getAccessToken() {
            return accessToken;
        }
        public static void setAccessToken(String accessToken) {
            AccessToken.accessToken = accessToken;
            PhotoController.setAccessToken(accessToken);
            UserController.setAccessToken(accessToken);
        }

        public static void setListener(RequestListener listener) {
            AccessToken.listener = listener;
        }

        private AccessToken() {}


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
                    Log.i("ShouldOver", "rideUrlLoading " + url);
                    if (url.contains("logout") || url.contains("error") ||url.contains("access_denied") )  {
                        LoginManager.getInstance().closeSession(ctx);
                        authDialog.dismiss();
                        AccessToken.listener.onRequestReady(OK_CODE, "logout");

                    }/*else if(url.contains("code=")){
                        onPageFinished(view,url);
                    }*/


                    return false;
                }

                @Override
                public void onPageStarted(WebView view, String url,
                                          Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);

                    authComplete = false;

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
                        requestToken(ctx,authDialog);
                        authDialog.dismiss();

                    } else if (url.contains("error=access_denied")) {
                        AccessToken.listener.onRequestReady(OK_CODE,"access_denied");
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


        private static void requestToken(Context ctx, final Dialog authdialog) {

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
                        AccessToken.setAccessToken(tok);
                        AccessToken.listener.onRequestReady(OK_CODE, tok);

                    } catch (JSONException e) {
                        AccessToken.listener.onRequestReady(BAD_CODE, response.toString());
                    }

                }
            }
                    , null) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("code", AccessToken.code);
                    params.put("client_id", client_id);
                    params.put("client_secret", client_secret);
                    params.put("redirect_uri", redirect_uri);
                    params.put("grant_type", grant_type);
                    return params;

                }

                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<>();
                    params.put("Authorization", "Basic " + base64EncodedCredentials);
                    params.put("Content-Type", "application/x-www-form-urlencoded");
                    return params;

                }
            };

            SingletoneRequestQue.getInstance(ctx).addToRequestQueue(req);

        }




    }
}
