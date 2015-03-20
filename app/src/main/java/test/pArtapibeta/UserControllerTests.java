package test.pArtapibeta;

/*
public class UserControllerTests {

    public static final String MY_LOGS = "My_Logs";

    */
/**
     * Testing User Profile Request
     *//*

    @Test(expected = AssertionError.class)
    public static void testUserProfile() {
        final UserController userController = new UserController(Main.getAppContext());
        userController.setListener(new RequestListener(0) {
            @Override
            public void onRequestReady(int requmber, String msg) {

                Assert.assertTrue("Connection Error", requmber == 205);
                Assert.assertFalse("Response Error", msg.contains("error"));
                Assert.assertNotNull("null", userController.getUser());

            }
        });

        //userController.requestUser();
    }

    */
/**
     * Testing User Profile Request with ID
     *
     * @param userId ID of the User
     *//*

    @Test(expected = AssertionError.class)
    public static void testUserProfile(String userId) {
        final UserController userController = new UserController(Main.getAppContext());
        userController.setListener(new RequestListener(0) {
            @Override
            public void onRequestReady(int requmber, String msg) {

                Assert.assertTrue("Connection Error", requmber == 202);
                Assert.assertFalse("Response Error", msg.contains("error"));
                Assert.assertNotNull("null", userController.getUser());

            }
        });
        userController.requestUser(userId);
    }

    */
/**
     * Testing User Followers Request
     *
     * @param userId ID of the User
     *//*

    @Test(expected = AssertionError.class)
    public static void testUserFollowers(String userId) {
        final UserController userController = new UserController(MainActivity.getAppContext());
        userController.setListener(new RequestListener(0) {
            @Override
            public void onRequestReady(int requmber, String msg) {

                Assert.assertTrue("Connection Error", requmber == 208);
                Assert.assertFalse("Response Error", msg.contains("error"));
                Assert.assertNotNull("user followers null", userController.getUserFollowers());

            }
        });
        userController.requestUserFollowers(userId, 0, UserController.MAX_LIMIT);
    }

    */
/**
     * Testing User Following Request
     *
     * @param userId ID of the User
     *//*

    @Test(expected = AssertionError.class)
    public static void testUserFollowing(final String userId) {
        final UserController userController = new UserController(MainActivity.getAppContext());
        userController.setListener(new RequestListener(0) {
            @Override
            public void onRequestReady(int requmber, String msg) {

                Assert.assertTrue("Connection Error", requmber == 209);
                Assert.assertFalse("Response Error", msg.contains("error"));
                Assert.assertNotNull("user following null", userController.getUserFollowing());

            }
        });
        userController.requestUserFollowing(userId, 0, UserController.MAX_LIMIT);
    }

    */
/**
     * Testing User Liked Photos Request
     *
     * @param userId ID of the User
     *//*

    @Test(expected = AssertionError.class)
    public static void testUserLikedPhotos(final String userId) {
        final UserController userController = new UserController(MainActivity.getAppContext());
        userController.setListener(new RequestListener(0) {
            @Override
            public void onRequestReady(int requmber, String msg) {


                Assert.assertTrue("Connection Error", requmber == 210);
                Assert.assertFalse("Response Error", msg.contains("error"));
                Assert.assertNotNull("user liked photos is null", userController.getUserLikedPhotos());

            }
        });
        userController.requestLikedPhotos(userId, 0, UserController.MAX_LIMIT);
    }

    */
/**
     * Testing User Blocked Users Request
     *
     * @param userId ID of the User
     *//*

    @Test(expected = AssertionError.class)
    public static void testUserBlockedUsers(String userId) {
        final UserController userController = new UserController(MainActivity.getAppContext());
        userController.setListener(new RequestListener(0) {
            @Override
            public void onRequestReady(int requmber, String msg) {

                Assert.assertTrue("Connection Error", requmber == 204);
                Assert.assertFalse("Response Error", msg.contains("error"));
                Assert.assertNotNull("user's blocked users null", userController.getBlockedUsers());

            }
        });
        userController.requestBlockedUsers(userId, 0, UserController.MAX_LIMIT);
    }

    */
/**
     * Testing User Places Request
     *
     * @param userId ID of the User
     *//*

    @Test(expected = AssertionError.class)
    public static void testUserPlaces(String userId) {
        final UserController userController = new UserController(MainActivity.getAppContext());
        userController.setListener(new RequestListener(0) {
            @Override
            public void onRequestReady(int requmber, String msg) {


                Assert.assertTrue("Connection Error", requmber == 205);
                Assert.assertFalse("Response Error", msg.contains("error"));
                Assert.assertNotNull("user places null", userController.getUserPlaces());

            }
        });
        userController.requestPlaces(userId, 0, UserController.MAX_LIMIT);
    }

    */
/**
     * Testing User Tags Request
     *
     * @param userId ID of the User
     *//*

    @Test(expected = AssertionError.class)
    public static void testUserTags(String userId) {
        final UserController userController = new UserController(MainActivity.getAppContext());
        userController.setListener(new RequestListener(0) {
                                       @Override
                                       public void onRequestReady(int requmber, String msg) {


                                           Assert.assertTrue("Connection Error", requmber == 206);
                                           Assert.assertFalse("Response Error", msg.contains("error"));
                                           Assert.assertNotNull("user tags null", userController.getUserTags());

                                       }
                                   }
        );
        userController.requestTags(userId, 0, UserController.MAX_LIMIT);
    }

    */
/**
     * Testing User Photos Request
     *
     * @param userId ID of the User
     *//*

    @Test(expected = AssertionError.class)
    public static void testUserPhotos(String userId) {
        final UserController userController = new UserController(MainActivity.getAppContext());
        userController.setListener(new RequestListener(0) {
                                       @Override
                                       public void onRequestReady(int requmber, String msg) {


                                           Assert.assertTrue("Connection Error", requmber == 207);
                                           Assert.assertFalse("Response Error", msg.contains("error"));
                                           Assert.assertNotNull("user photos null", userController.getPhotoUrl());

                                       }
                                   }
        );
        userController.requestTags(userId, 0, UserController.MAX_LIMIT);
    }

    */
/**
     * Testing User Block User with ID Request
     *
     * @param userId ID of the User
     *//*

    @Test(expected = AssertionError.class)
    public static void testBlockUserWithIdRequest(String userId) {
        final UserController userController = new UserController(MainActivity.getAppContext());
        userController.setListener(new RequestListener(0) {
            @Override
            public void onRequestReady(int requmber, String msg) {

                Assert.assertTrue("Connection Error", requmber == 211);
                Assert.assertFalse("Response Error", msg.contains("error"));

            }
        });
        userController.blockUserWithID(userId);
    }

    */
/**
     * Testing User Unblock User with ID Request
     *
     * @param userId ID of the User
     *//*

    @Test(expected = AssertionError.class)
    public static void testUnblockUserWithIdRequest(String userId) {
        final UserController userController = new UserController(MainActivity.getAppContext());
        userController.setListener(new RequestListener(0) {
            @Override
            public void onRequestReady(int requmber, String msg) {

                Assert.assertTrue("Connection Error", requmber == 212);
                Assert.assertFalse("Response Error", msg.contains("error"));

            }
        });
        userController.unblockUserWithID(userId);
    }

    */
/**
     * Testing User Follow User with ID Request
     *
     * @param userId ID of the User
     *//*

    @Test(expected = AssertionError.class)
    public static void testFollowUserWithIdRequest(String userId) {
        final UserController userController = new UserController(MainActivity.getAppContext());
        userController.setListener(new RequestListener(0) {
            @Override
            public void onRequestReady(int requmber, String msg) {

                Assert.assertTrue("Connection Error", requmber == 217);
                Assert.assertFalse("Response Error", msg.contains("error"));

            }
        });
        userController.followUserWithID(userId);
    }

}

*/
