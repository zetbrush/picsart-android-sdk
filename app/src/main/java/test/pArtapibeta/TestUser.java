/*
package test.pArtapibeta;


import android.util.Log;

import java.util.ArrayList;

import pArtapibeta.MainActivity;
import pArtapibeta.Photo;

import pArtapibeta.RequestListener;

import pArtapibeta.User;
import pArtapibeta.UserController;

public class TestUser {

    public static final String MY_LOGS = "My_Logs";

    public static void testUserProfile(boolean printLogs) {
        final UserController userController = new UserController(MainActivity.getAppContext());
        userController.setListener(new RequestListener() {
            @Override
            public void onRequestReady(int requmber, String msg) {

                User user = userController.getUser();
                if (requmber == 305)
                    UserController.getSt_listener().onRequestReady(111, "UserTest get Info --- FAILD || CONNECTION ERROR");
                else if (msg.contains("error"))
                    UserController.getSt_listener().onRequestReady(111, "UserTest get Info --- FAILD");
                else if (user != null && requmber == 205)
                    UserController.getSt_listener().onRequestReady(111, "UserTest get Info --- PASSED");

            }
        });
        userController.requestUser();
    }

    public static void testUserProfile(String userId, boolean printLogs) {
        final UserController userController = new UserController(MainActivity.getAppContext());
        userController.setListener(new RequestListener() {
            @Override
            public void onRequestReady(int requmber, String msg) {

                User user = userController.getUser();
                if (requmber == 302)
                    UserController.getSt_listener().onRequestReady(112, "UserTest get Info --- FAILD || CONNECTION ERROR");
                else if (msg.contains("error"))
                    UserController.getSt_listener().onRequestReady(112, "UserTest get Info --- FAILD");
                else if (user != null && requmber == 202)
                    UserController.getSt_listener().onRequestReady(112, "UserTest get Info --- PASSED");

            }
        });
        userController.requestUser(userId);
    }

    public static void testUserFollowers(String userId, final boolean printLogs) {
        final UserController userController = new UserController(MainActivity.getAppContext());
        userController.setListener(new RequestListener() {
            @Override
            public void onRequestReady(int requmber, String msg) {

                ArrayList<String> userFollowers = userController.getUserFollowers();
                if (requmber == 308)
                    UserController.getSt_listener().onRequestReady(113, "UserTest get Info --- FAILD || CONNECTION ERROR");
                else if (msg.contains("error"))
                    UserController.getSt_listener().onRequestReady(113, "UserTest get Info --- FAILD");
                else if (userFollowers != null && requmber == 208) {
                    if (printLogs == true) {
                        for (int i = 0; i < userFollowers.size(); i++) {
                            Log.d(MY_LOGS, "userFollowers:  " + userFollowers.get(i));
                        }
                    }
                    UserController.getSt_listener().onRequestReady(113, "UserTest get Info --- PASSED");
                }

            }
        });
        userController.requestUserFollowers(userId, 0, UserController.MAX_LIMIT);
    }

    public static void testUserFollowing(final String userId, final boolean printLogs) {
        final UserController userController = new UserController(MainActivity.getAppContext());
        userController.setListener(new RequestListener() {
            @Override
            public void onRequestReady(int requmber, String msg) {

                ArrayList<String> userFollowing = userController.getUserFollowing();
                if (requmber == 309)
                    UserController.getSt_listener().onRequestReady(114, "UserTest get Info --- FAILD || CONNECTION ERROR");
                else if (msg.contains("error"))
                    UserController.getSt_listener().onRequestReady(114, "UserTest get Info --- FAILD");
                else if (userFollowing != null && requmber == 209) {

                    if (printLogs == true) {
                        for (int i = 0; i < userFollowing.size(); i++) {
                            Log.d(MY_LOGS, "userFollowing:  " + userFollowing.get(i));
                        }
                    }
                    UserController.getSt_listener().onRequestReady(114, "UserTest get Info --- PASSED");
                }


            }
        });
        userController.requestUserFollowing(userId, 0, UserController.MAX_LIMIT);
    }

    public static void testUserLikedPhotos(final String userId, final boolean printLogs) {
        final UserController userController = new UserController(MainActivity.getAppContext());
        userController.setListener(new RequestListener() {
            @Override
            public void onRequestReady(int requmber, String msg) {

                ArrayList<Photo> userLikedPhotos = userController.getUserLikedPhotos();
                if (requmber == 310)
                    UserController.getSt_listener().onRequestReady(115, "UserTest get Info --- FAILD || CONNECTION ERROR");
                else if (msg.contains("error"))
                    UserController.getSt_listener().onRequestReady(115, "UserTest get Info --- FAILD");
                else if (userLikedPhotos != null && requmber == 210) {

                    if (printLogs == true) {
                        for (int i = 0; i < userLikedPhotos.size(); i++) {
                            Log.d(MY_LOGS, "userLikedPhotos:  " + userLikedPhotos.get(i).getId());
                        }
                    }
                    UserController.getSt_listener().onRequestReady(115, "UserTest get Info --- PASSED");
                }
            }
        });
        userController.requestLikedPhotos(userId, 0, UserController.MAX_LIMIT);
    }

    public static void testUserBlockedUsers(String userId, final boolean printLogs) {
        final UserController userController = new UserController(MainActivity.getAppContext());
        userController.setListener(new RequestListener() {
            @Override
            public void onRequestReady(int requmber, String msg) {

                ArrayList<String> userBlockedUsers = userController.getBlockedUsers();
                if (requmber == 304)
                    UserController.getSt_listener().onRequestReady(116, "UserTest get Info --- FAILD || CONNECTION ERROR");
                else if (msg.contains("error"))
                    UserController.getSt_listener().onRequestReady(116, "UserTest get Info --- FAILD");
                else if (userBlockedUsers != null && requmber == 204) {

                    if (printLogs == true) {
                        for (int i = 0; i < userBlockedUsers.size(); i++) {
                            Log.d(MY_LOGS, "userBlockedUsers:  " + userBlockedUsers.get(i));
                        }
                    }
                    UserController.getSt_listener().onRequestReady(116, "UserTest get Info --- PASSED");
                }
            }
        });
        userController.requestBlockedUsers(userId, 0, UserController.MAX_LIMIT);
    }

    public static void testUserPlaces(String userId, final boolean printLogs) {
        final UserController userController = new UserController(MainActivity.getAppContext());
        userController.setListener(new RequestListener() {
            @Override
            public void onRequestReady(int requmber, String msg) {

                ArrayList<String> userPlaces = userController.getUserPlaces();
                if (requmber == 305)
                    UserController.getSt_listener().onRequestReady(117, "UserTest get Info --- FAILD || CONNECTION ERROR");
                else if (msg.contains("error"))
                    UserController.getSt_listener().onRequestReady(117, "UserTest get Info --- FAILD");
                else if (userPlaces != null && requmber == 205) {

                    if (printLogs == true) {
                        for (int i = 0; i < userPlaces.size(); i++) {
                            Log.d(MY_LOGS, "userPlaces:  " + userPlaces.get(i));
                        }
                    }
                    UserController.getSt_listener().onRequestReady(117, "UserTest get Info --- PASSED");
                }
            }
        });
        userController.requestPlaces(userId, 0, UserController.MAX_LIMIT);
    }

    public static void testUserTags(String userId, final boolean printLogs) {
        final UserController userController = new UserController(MainActivity.getAppContext());
        userController.setListener(new RequestListener() {
                                       @Override
                                       public void onRequestReady(int requmber, String msg) {

                                           ArrayList<String> userTags = userController.getUserTags();
                                           if (requmber == 306)
                                               UserController.getSt_listener().onRequestReady(118, "UserTest get Info --- FAILD || CONNECTION ERROR");
                                           else if (msg.contains("error"))
                                               UserController.getSt_listener().onRequestReady(118, "UserTest get Info --- FAILD");
                                           else if (userTags != null && requmber == 206) {

                                               if (printLogs == true) {
                                                   for (int i = 0; i < userTags.size(); i++) {
                                                       Log.d(MY_LOGS, "userTags:  " + userTags.get(i));
                                                   }
                                               }
                                               UserController.getSt_listener().onRequestReady(118, "UserTest get Info --- PASSED");
                                           }
                                       }
                                   }
        );
        userController.requestTags(userId, 0, UserController.MAX_LIMIT);
    }

}
*/
