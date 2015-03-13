package pArtapibeta;

import java.util.ArrayList;

/**
 * Created by intern on 3/13/15.
 */
public class TestUser {

    public static void testUserProfile(final boolean print) {

        final UserController userController = new UserController(MainActivity.getAppContext());
        userController.setListener(new RequestListener() {
            @Override
            public void onRequestReady(int requmber, String mmsg) {
                User user = userController.getUser();
                if (requmber == 101)
                    UserController.getSt_listener().onRequestReady(111, "UserTest get Info --- FAILD || CONNECTION ERROR");
                else if (mmsg.contains("error"))
                    UserController.getSt_listener().onRequestReady(111, "UserTest get Info --- FAILD");
                else if (user != null && requmber == 102) {
                    if (print == true) {
                        user.toString();
                    }
                    UserController.getSt_listener().onRequestReady(111, "UserTest get Info --- PASSED");
                }
            }
        });
        userController.requestUser();
    }

    public static void testUserProfile(String userId, final boolean print) {
        final UserController userController = new UserController(MainActivity.getAppContext());
        userController.setListener(new RequestListener() {
            @Override
            public void onRequestReady(int requmber, String mmsg) {
                User user = userController.getUser();
                if (requmber == 101)
                    UserController.getSt_listener().onRequestReady(112, "UserTest get Info --- FAILD || CONNECTION ERROR");
                else if (mmsg.contains("error"))
                    UserController.getSt_listener().onRequestReady(112, "UserTest get Info --- FAILD");
                else if (user != null && requmber == 102)
                    UserController.getSt_listener().onRequestReady(112, "UserTest get Info --- PASSED");
            }
        });
        userController.requestUser(userId);
    }

    public static void testUserFollowers(String userId, final boolean print) {
        final UserController userController = new UserController(MainActivity.getAppContext());
        userController.setListener(new RequestListener() {
            @Override
            public void onRequestReady(int requmber, String mmsg) {
                ArrayList<String> userFollowers=new ArrayList<String>();
                if (requmber == 101)
                    UserController.getSt_listener().onRequestReady(113, "UserTest get Info --- FAILD || CONNECTION ERROR");
                else if (mmsg.contains("error"))
                    UserController.getSt_listener().onRequestReady(113, "UserTest get Info --- FAILD");
                else if (userFollowers != null && requmber == 102) {
                    UserController.getSt_listener().onRequestReady(113, "UserTest get Info --- PASSED");
                }
            }
        });
        userController.requestUserFollowers(userId,0,UserController.MAX_LIMIT);
    }

    public static void testUserFollowing(String userId, final boolean print) {

    }

    public static void testUserLikedPhotos(String userId,final boolean print){

    }

    public static void testUserBlockedUsers(String userId,final boolean print){

    }

    public static void testUserPlaces(String userId,final boolean print){

    }

    public static void testUserTags(String userId,final boolean print){

    }

}
