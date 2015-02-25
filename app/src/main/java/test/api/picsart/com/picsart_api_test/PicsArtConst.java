package test.api.picsart.com.picsart_api_test;

/**
 * Created by Arman on 2/9/15.
 */
public class PicsArtConst {

    public static final String CLIENT_ID = "WorkingClientPYzCIqGMBt0xx4fV";
    public static final String CLIENT_SECRET = "Efm7ASWYgAZQ81HkbVhl6EdogwTn8d5c";
    public static final String REDIRECT_URI = "localhost";
    public static final String GRANT_TYPE = "authorization_code";
    public static final String TOKEN_URL = "http://stage.i.picsart.com/api/oauth2/token";
    public static final String OAUTH_URL = "http://stage.i.picsart.com/api/oauth2/authorize";
    public static final String TOKEN_URL_PREFIX = ".json?token=";
    public static final String MY_PROFILE_URL = "http://stage.i.picsart.com/api/users/show/me";
    public static final String USE_PROFILE_URL = "http://stage.i.picsart.com/api/users/show/";
    public static final String Get_PHOTO_URL ="http://stage.i.picsart.com/api/photos/";
    public static final String Get_PHOTO_URL_PUB ="https://api.picsart.com/photos/";
    public static final String TOKEN_PREFIX = "?token=";

    public static final String GET_USER_PHOTOS_LIST = "http://stage.i.picsart.com/api/users/";
    public static final String GET_PHOTO_FILTER = "?filter=";

    public static final String[] paramsUserProfile =
            new String[]{
                    "username",         //0
                    "name",             //1
                    "id",               //2
                    "is_verified",      //3
                    "email",            //4
                    "balance",          //5
                    "photos_count",     //6
                    "photo",            //7
                    "likes_count",      //8
                    "location",         //9
                    "coordinates",      //10
                    "locations_count",  //11
                    "following_count",  //12
                    "street",           //13
                    "city",             //14
                    "state",            //15
                    "zip",              //16
                    "country",          //17
                    "coordinates",      //18
                    "cover",             //19
                    "followers_count"   //20
            };

    public static final String[] paramsPhotoInfo =
            new String[]{
                    "id",               //0
                    "url",              //1
                    "title",            //2
                    "created",          //3
                    "mature",           //4
                    "width",            //5
                    "height",           //6
                    "likes_count",      //7
                    "views_count",      //8
                    "comments_count",   //9
                    "reposts_count",    //10
                    "is_liked",         //11
                    "is_reposted",      //12
                    "user_id",          //13
                    "city",             //14
                    "state",            //15
                    "zip",              //16
                    "country",          //17
                    "coordinates",      //18
                    "cover",            //19
                    "followers_count",  //20
                    "location",         // 21
                    "tags"              //22
            };

}
