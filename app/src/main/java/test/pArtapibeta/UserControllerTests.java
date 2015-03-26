package test.pArtapibeta;

import android.util.Log;

import org.junit.Assert;
import org.junit.Test;

import pArtapibeta.AccessToken;
import pArtapibeta.Main;
import pArtapibeta.RequestListener;
import pArtapibeta.UserController;


public class UserControllerTests {


    //hbgdsfaklgjk;dg
    public static final String MY_LOGS = "My_Logs";
    public static final String ERROR = "error";
    public static final String CONNECTION_ERROR = "Connection Error";
    public static final String RESPONSE_ERROR = "Response Error";
    public static final String NULL_OBJECT = "Null Object";

    /**
     * Testing User Profile Request
     * @param accessToken Access Token
     */
    @Test(expected = AssertionError.class)
    public static void testUserProfile(String accessToken) {
        final UserController userController = new UserController(accessToken, Main.getAppContext());
        userController.setListener(new RequestListener(0) {
            @Override
            public void onRequestReady(int requmber, String msg) {

                Assert.assertTrue(CONNECTION_ERROR, requmber == 201);
                Assert.assertFalse(RESPONSE_ERROR, msg.contains(ERROR));
                Assert.assertNotNull(NULL_OBJECT, userController.getUser());

                Log.i("User Profile Test","Test was successfully passed");

            }
        });

        userController.requestUser();
    }

    /**
     * Testing User Profile Request with ID
     *
     * @param userId ID of the User
     * @param accessToken Access Token
     */
    @Test(expected = AssertionError.class)
    public static void testUserProfile(String accessToken, String userId) {
        final UserController userController = new UserController(accessToken, Main.getAppContext());
        userController.setListener(new RequestListener(0) {
            @Override
            public void onRequestReady(int requmber, String msg) {

                Assert.assertTrue(CONNECTION_ERROR, requmber == 202);
                Assert.assertFalse(RESPONSE_ERROR, msg.contains(ERROR));
                Assert.assertNotNull(NULL_OBJECT, userController.getUser());

                Log.i("User Profile Test", "Test was successfully passed");

            }
        });
        userController.requestUser(userId);
    }

    /**
     * Testing User Followers Request
     *
     * @param userId ID of the User
     * @param accessToken Access Token
     */
    @Test(expected = AssertionError.class)
    public static void testUserFollowers(String accessToken,String userId) {
        final UserController userController = new UserController(accessToken, Main.getAppContext());
        userController.setListener(new RequestListener(0) {
            @Override
            public void onRequestReady(int requmber, String msg) {

                Assert.assertTrue(CONNECTION_ERROR, requmber == 203);
                Assert.assertFalse(RESPONSE_ERROR, msg.contains(ERROR));
                Assert.assertNotNull(NULL_OBJECT, userController.getUserFollowers());

                Log.i("User Followers Test", "Test was successfully passed");

            }
        });
        userController.requestUserFollowers(userId, 0, UserController.MAX_LIMIT);
    }

    /**
     * Testing User Following Request
     *
     * @param userId ID of the User
     * @param accessToken Access Token
     */
    @Test(expected = AssertionError.class)
    public static void testUserFollowing(String accessToken,String userId) {
        final UserController userController = new UserController(accessToken, Main.getAppContext());
        userController.setListener(new RequestListener(0) {
            @Override
            public void onRequestReady(int requmber, String msg) {

                Assert.assertTrue(CONNECTION_ERROR, requmber == 204);
                Assert.assertFalse(RESPONSE_ERROR, msg.contains(ERROR));
                Assert.assertNotNull(NULL_OBJECT, userController.getUserFollowing());

                Log.i("User Following Test", "Test was successfully passed");

            }
        });
        userController.requestUserFollowing(userId, 0, UserController.MAX_LIMIT);
    }

    /**
     * Testing User Liked Photos Request
     *
     * @param userId ID of the User
     * @param accessToken Access Token
     */
    @Test(expected = AssertionError.class)
    public static void testUserLikedPhotos(String accessToken,String userId) {
        final UserController userController = new UserController(accessToken, Main.getAppContext());
        userController.setListener(new RequestListener(0) {
            @Override
            public void onRequestReady(int requmber, String msg) {

                Assert.assertTrue(CONNECTION_ERROR, requmber == 205);
                Assert.assertFalse(RESPONSE_ERROR, msg.contains(ERROR));
                Assert.assertNotNull(NULL_OBJECT, userController.getUserLikedPhotos());

                Log.i("User Liked Photos Test", "Test was successfully passed");

            }
        });
        userController.requestLikedPhotos(userId, 0, UserController.MAX_LIMIT);
    }


    /**
     * Testing User Blocked Users Request
     *
     * @param userId ID of the User
     * @param accessToken Access Token
     */
    @Test(expected = AssertionError.class)
    public static void testUserBlockedUsers(String accessToken,String userId) {
        final UserController userController = new UserController(accessToken, Main.getAppContext());
        userController.setListener(new RequestListener(0) {


            @Override
            public void onRequestReady(int requmber, String msg) {

                Assert.assertTrue(CONNECTION_ERROR, requmber == 206);
                Assert.assertFalse(RESPONSE_ERROR, msg.contains(ERROR));
                Assert.assertNotNull(NULL_OBJECT, userController.getBlockedUsers());

                Log.i("User Liked Photos Test", "Test was successfully passed");

            }

        });
        userController.requestBlockedUsers(userId, 0, UserController.MAX_LIMIT);
    }

    /**
     * Testing User Places Request
     *
     * @param userId ID of the User
     * @param accessToken Access Token
     */
    @Test(expected = AssertionError.class)
    public static void testUserPlaces(String accessToken,String userId) {
        final UserController userController = new UserController(accessToken, Main.getAppContext());
        userController.setListener(new RequestListener(0) {

            @Override
            public void onRequestReady(int requmber, String msg) {

                Assert.assertTrue(CONNECTION_ERROR, requmber == 207);
                Assert.assertFalse(RESPONSE_ERROR, msg.contains(ERROR));
                Assert.assertNotNull(NULL_OBJECT, userController.getUserPlaces());

                Log.i("User Places Test", "Test was successfully passed");

            }

        });
        userController.requestPlaces(userId, 0, UserController.MAX_LIMIT);
    }

    /**
     * Testing User Tags Request
     *
     * @param userId ID of the User
     * @param accessToken Access Token
     */
    @Test(expected = AssertionError.class)
    public static void testUserTags(String accessToken,String userId) {

        final UserController userController = new UserController(accessToken, Main.getAppContext());
        userController.setListener(new RequestListener(0) {

            @Override
            public void onRequestReady(int requmber, String msg) {

                Assert.assertTrue(CONNECTION_ERROR, requmber == 208);
                Assert.assertFalse(RESPONSE_ERROR, msg.contains(ERROR));
                Assert.assertNotNull(NULL_OBJECT, userController.getUserTags());

                Log.i("User Tags Test", "Test was successfully passed");

            }

        });
        userController.requestTags(userId, 0, UserController.MAX_LIMIT);
    }

    /**
     * Testing User Photos Request
     *
     * @param userId ID of the User
     * @param accessToken Access Token
     */
    @Test(expected = AssertionError.class)
    public static void testUserPhotos(String accessToken,String userId) {

        final UserController userController = new UserController(accessToken, Main.getAppContext());
        userController.setListener(new RequestListener(0) {

            @Override
            public void onRequestReady(int requmber, String msg) {

                Assert.assertTrue(CONNECTION_ERROR, requmber == 209);
                Assert.assertFalse(RESPONSE_ERROR, msg.contains(ERROR));
                Assert.assertNotNull(NULL_OBJECT, userController.getPhoto());

                Log.i("User Photos Test", "Test was successfully passed");

            }

        });
        userController.requestUserPhotos(userId, 0, UserController.MAX_LIMIT);
    }

    /**
     * Testing User Block User with ID Request
     *
     * @param userId ID of the User
     * @param accessToken Access Token
     */
    @Test(expected = AssertionError.class)
    public static void testBlockUserWithIdRequest(String accessToken,String userId) {
        final UserController userController = new UserController(accessToken, Main.getAppContext());
        userController.setListener(new RequestListener(0) {

            @Override
            public void onRequestReady(int requmber, String msg) {

                Assert.assertTrue(CONNECTION_ERROR, requmber == 210);
                Assert.assertFalse(RESPONSE_ERROR, msg.contains(ERROR));

                Log.i("Block User Test", "Test was successfully passed");

            }
        });
        userController.blockUserWithID(userId);
    }

    /**
     * Testing User Unblock User with ID Request
     *
     * @param userId ID of the User
     * @param accessToken Access Token
     */
    @Test(expected = AssertionError.class)
    public static void testUnblockUserWithIdRequest(String accessToken,String userId) {
        final UserController userController = new UserController(accessToken, Main.getAppContext());
        userController.setListener(new RequestListener(0) {

            @Override
            public void onRequestReady(int requmber, String msg) {

                Assert.assertTrue(CONNECTION_ERROR, requmber == 211);
                Assert.assertFalse(RESPONSE_ERROR, msg.contains(ERROR));

                Log.i("Unblock User Test", "Test was successfully passed");

            }
        });
        userController.unblockUserWithID(userId);
    }

    /**
     * Testing User Follow User with ID Request
     *
     * @param userId ID of the User
     * @param accessToken Access Token
     */
    @Test(expected = AssertionError.class)
    public static void testFollowUserWithIdRequest(String accessToken,String userId) {
        final UserController userController = new UserController(accessToken, Main.getAppContext());
        userController.setListener(new RequestListener(0) {

            @Override
            public void onRequestReady(int requmber, String msg) {

                Assert.assertTrue(CONNECTION_ERROR, requmber == 212);
                Assert.assertFalse(RESPONSE_ERROR, msg.contains(ERROR));

                Log.i("Follow User Test", "Test was successfully passed");

            }
        });
        userController.followUserWithID(userId);
    }

}
