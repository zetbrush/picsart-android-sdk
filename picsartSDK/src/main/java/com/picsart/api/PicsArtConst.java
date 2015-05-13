package com.picsart.api;

/**
 * This class serves as placeholder for all URLs
 * Created by Arman on 2/9/15.
 *
 */
public class PicsArtConst {


    public static String CLIENT_ID = "";
    public static String CLIENT_SECRET = "";
    public static String REDIRECT_URI = "localhost";
    public static String GRANT_TYPE = "authorization_code";

    public static final String OAUTH_URL = "https://picsart.com/oauth2/authorize";
    public static final String TOKEN_URL = "https://api.picsart.com/oauth2/token";


    public static final String TOKEN_URL_PREFIX = ".json?token=";
    public static final String Get_PHOTO_URL = "http://api.picsart.com/photos/";
    public static final String TOKEN_PREFIX = "?token=";
    public static final String USER_URL_PATH = "http://api.picsart.com/users/";


    public static final String PHOTO_GENERAL_PREFIX = "http://api.picsart.com/photos/";
    public static final String OFFSET = "&offset=";
    public static final String LIMIT = "&limit=";
    public static final String GET_PHOTO_FILTER = "?filter=";


    public static final String PHOTO_UPLOAD_URL = "https://api.picsart.com/photos";
    public static final String PHOTO_COVER_ENDX = "/photos/cover";
    public static final String PHOTO_AVATAR_ENDX = "/photos/avatar";

    public static final String PHOTO_COMMENTS_URL = "https://api.picsart.com/photos/comments/show/";
    public static final String PHOTO_ADD_COMMENT_URL = "/comments";
    public static final String PHOTO_COMMENT_MIDLE = "/comments/";

    public static final String PHOTO_PRE_URL = "https://api.picsart.com/photos/";
    public static final String PHOTO_LIKE_URL = "/likes";
    public static final String API_TEST_PREF = ".json?key=";

    public static final String ME_PREFIX = "me";

    public static final String SHOW_USER = "https://api.picsart.com/users/";
    public static final String FOLLOWERS_PREFIX = "/followers";
    public static final String FOLLOWING_PREFIX = "/following";
    public static final String LIKED_PHOTOS_PREFIX = "/likes";

    public static final String TAGS_PREFIX = "/tags";
    public static final String PLACES_PREFIX = "/places";
    public static final String BLOCKED_PREFIX = "/blocks";
    public static final String PHOTOS_PREFIX = "/photos";
    public static final String DATE_FORMAT = "yyyy-MM-dd'T'hh:mm:ss.SSS'X'";

    /////PhotoController codes//////
    public static final int OK_CODE_LOGIN=7777;
    public static final int BAD_CODE_LOGIN=6666;

    public static final int UPLOAD_OK_CODE =101;
    public static final int UPLOAD_BAD_CODE=103;

    public static final int REQ_OK_PHOTO =201;
    public static final int REQ_BAD_PHOTO =203;

    public static final int REQ_OK_COMMS=301;
    public static final int REQ_BAD_COMMS =303;

    public static final int REQ_OK_ADDCOMM=401;
    public static final int REQ_BAD_ADDCOMM =403;

    public static final int REQ_OK_DELCOMM = 501;
    public static final int REQ_BAD_DELCOMM = 503;

    public static final int REQ_OK_UPDPH =601;
    public static final int REQ_BAD_UPDPH=603;

    public static final int REQ_OK_LIKE = 701;
    public static final int REQ_BAD_LIKE =703;

    public static final int REQ_OK_UNLKE=801;
    public static final int REQ_BAD_UNLKE = 803;


    public static final int REQ_OK_COMID = 901;
    public static final int REQ_BAD_COMID = 903;

    public static final int REQ_OK_LKUS = 1001;
    public static final int REQ_BAD_LKUS = 1003;

    ////UserControllerCodes////





}