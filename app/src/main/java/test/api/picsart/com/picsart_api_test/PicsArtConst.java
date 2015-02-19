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
    public static final String USER_PROFILE_URL = "http://stage.i.picsart.com/api/users/show/me";
    public static final String User_PROFILE_UR = "d97fb4fa-63e5-4a59-a84a-5124376b11ed";
    public static final String User_PROFILE_U= "d97fb4fa-63e5-4a59-a84a-5124376b11ed";
    public static final String API_KEY_URL = "d97fb4fa-63e5-4a59-a84a-5124376b11ed";


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
            } ;
}
