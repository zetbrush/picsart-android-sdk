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

    public static final String OAUTH_URL = "https://picsart.com/api/oauth2/authorize";
    public static final String TOKEN_URL = "https://picsart.com/api/oauth2/token";


    public static final String TOKEN_URL_PREFIX = ".json?token=";
    public static final String Get_PHOTO_URL = "http://picsart.com/api/photos/";
    public static final String TOKEN_PREFIX = "?token=";
    public static final String USER_URL_PATH = "http://picsart.com/api/users/";


    public static final String PHOTO_GENERAL_PREFIX = "http://picsart.com/api/photos/";
    public static final String OFFSET = "&offset=";
    public static final String LIMIT = "&limit=";
    public static final String GET_PHOTO_FILTER = "?filter=";


    public static final String PHOTO_UPLOAD_URL = "https://picsart.com/api/photos";
    public static final String PHOTO_COVER_ENDX = "/photos/cover";
    public static final String PHOTO_AVATAR_ENDX = "/photos/avatar";

    public static final String PHOTO_COMMENTS_URL = "https://api.picsart.com/photos/comments/show/";
    public static final String PHOTO_ADD_COMMENT_URL = "/comments";
    public static final String PHOTO_COMMENT_MIDLE = "/comments/";

    public static final String PHOTO_PRE_URL = "https://picsart.com/api/photos/";
    public static final String PHOTO_LIKE_URL = "/likes";
    public static final String API_TEST_PREF = ".json?key=";

    public static final String ME_PREFIX = "me";

    public static final String SHOW_USER = "https://picsart.com/api/users/";
    public static final String FOLLOWERS_PREFIX = "/followers";
    public static final String FOLLOWING_PREFIX = "/following";
    public static final String LIKED_PHOTOS_PREFIX = "/likes";

    public static final String TAGS_PREFIX = "/tags";
    public static final String PLACES_PREFIX = "/places";
    public static final String BLOCKED_PREFIX = "/blocks";
    public static final String PHOTOS_PREFIX = "/photos";
    public static final String DATE_FORMAT = "yyyy-MM-dd'T'hh:mm:ss.SSS'X'";
}