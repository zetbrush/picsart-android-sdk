package com.picsart.api;

import android.content.Context;

import junit.framework.Assert;
import junit.framework.AssertionFailedError;

import org.junit.Test;


/**
 * This class consists exclusively test methods, that operate on making
 * requests and asserting for expectation.
 * <p/>
 * If testMethod fails exception with message will logged
 * <p/>
 * This class is a member of the www.com.picsart.com.
 *
 * @author Arman Andreasyan 3/9/15
 */

public class PhotoControllerTests {


    /**
     * @param phID  photo ID
     * @param token OAuth 2.0 token
     * @param ctx   Context
     *              <p/>
     *              Tests weather Photo is obtained.
     */
    @Test(expected = AssertionFailedError.class)
    public static void testRequestPhoto(String phID, String token, Context ctx) {

        final PhotoController pc = new PhotoController(ctx, token);
        pc.setListener(new RequestListener(1) {
            @Override
            public void onRequestReady(int requmber, String mmsg) {

                Assert.assertNotSame("testRequestPhoto --- FAILD || CONNECTION ERROR", 203, requmber);
                Assert.assertFalse("testRequestPhoto (error response)--- FAILD", mmsg.contains("error") || pc.getPhoto() == null);
            }
        });

        pc.requestPhoto(phID);

    }

    /**
     * @param phID  photo ID
     * @param token OAuth 2.0 token
     * @param ctx   Context
     *              <p/>
     *              Tests if like was successfull.
     */
    @Test(expected = AssertionFailedError.class)
    public static void testLike(String phID, String token, Context ctx) {

        final PhotoController pc = new PhotoController(ctx, token);
        pc.setListener(new RequestListener(1) {
            @Override
            public void onRequestReady(int requmber, String mmsg) {
                Assert.assertNotSame(" testLike --- FAILD || CONNECTION ERROR", 703, requmber);
                Assert.assertFalse("testLike --- FAILD || ERROR RESPONSE", mmsg.contains("error"));
            }
        });
        pc.like(phID);

    }


    /**
     * @param phID  photo ID
     * @param token OAuth 2.0 token
     * @param ctx   Context
     *              <p/>
     *              tests weather Photo is unliked.
     */
    @Test(expected = AssertionFailedError.class)
    public static void testUnLike(String phID, String token, Context ctx) {

        final PhotoController pc = new PhotoController(ctx, token);
        pc.setListener(new RequestListener(1) {
            @Override
            public void onRequestReady(int requmber, String mmsg) {

                Assert.assertNotSame("testUnLike --- FAILD || CONNECTION ERROR", 803, requmber);
                Assert.assertFalse("testUnLike --- FAILD || ERROR RESPONSE", mmsg.contains("error"));

            }
        });
        pc.unLike(phID);

    }


    /**
     * @param phID    photo ID
     * @param comment commenting text
     * @param token   OAuth 2.0 token
     * @param ctx     Context
     *                <p/>
     *                Tests weather commenting is successfull.
     */
    @Test(expected = AssertionFailedError.class)
    public static void testAddComment(String phID, String comment, String token, Context ctx) {

        final PhotoController pc = new PhotoController(ctx, token);
        pc.setListener(new RequestListener(1) {
            @Override
            public void onRequestReady(int requmber, String mmsg) {
                Assert.assertNotSame("testAddComment --- FAILD || CONNECTION ERROR", 403, requmber);
                Assert.assertFalse("testAddComment --- FAILD || ERROR RESPONSE", mmsg.contains("error"));
            }
        });
        pc.addComment(phID, new Comment(comment, true));
    }


    /**
     * @param phID      photo ID
     * @param commentId comment ID
     * @param token     OAuth 2.0 token
     * @param ctx       Context
     *                  <p/>
     *                  Tests weather deletion of comment is successfull.
     */
    @Test(expected = AssertionFailedError.class)
    public static void testDeleteComment(String phID, String commentId, String token, Context ctx) {
        final PhotoController pc = new PhotoController(ctx, token);
        pc.setListener(new RequestListener(1) {
            @Override
            public void onRequestReady(int requmber, String mmsg) {
                Assert.assertNotSame("testDeleteComment --- FAILD || CONNECTION ERROR", 503, requmber);
                Assert.assertFalse("testDeleteComment --- FAILD || ERROR RESPONSE", mmsg.contains("error"));
            }
        });
        pc.deleteComment(phID, commentId);

    }


    /**
     * @param phID   photo ID
     * @param limit  max limit of result
     * @param offset offset for result
     * @param token  OAuth 2.0 token
     * @param ctx    Context
     *               <p/>
     *               Tests weather requested comments are obtained.
     */
    @Test(expected = AssertionFailedError.class)
    public static void testRequestComments(String phID, int offset, int limit, String token, Context ctx) {
        final PhotoController pc = new PhotoController(ctx, token);
        pc.setListener(new RequestListener(1) {
            @Override
            public void onRequestReady(int requmber, String mmsg) {
                Assert.assertNotSame("testRequestComments --- FAILD || CONNECTION ERROR", 303, requmber);
                Assert.assertFalse("testRequestComments --- FAILD || ERROR RESPONSE", mmsg.contains("error"));

            }
        });
        pc.requestComments(phID, offset, limit);

    }


    /**
     * @param phID      photo ID
     * @param commentId ID of comment
     * @param token     OAuth 2.0 token
     * @param ctx       Context
     *                  <p/>
     *                  Tests weather requested concrete comment is obtained.
     */
    @Test(expected = AssertionFailedError.class)
    public static void testGetCommentById(String phID, String commentId, String token, Context ctx) {
        final PhotoController pc = new PhotoController(ctx, token);
        pc.setListener(new RequestListener(1) {
            @Override
            public void onRequestReady(int requmber, String mmsg) {
                Assert.assertNotSame("testGetCommentById --- FAILD || CONNECTION ERROR", 903, requmber);

                Assert.assertFalse("testGetCommentById --- FAILD || ERROR RESPONSE", mmsg.contains("error"));
                Assert.assertNotNull("testGetCommentById --- FAILD || NULL", pc.getComment());
            }
        });
        pc.requestCommentByid(phID, commentId);

    }


    /**
     * @param phID   photo ID
     * @param limit  max limit of result
     * @param offset offset for result
     * @param token  OAuth 2.0 token
     * @param ctx    Context
     *               <p/>
     *               Tests weather Liked Users are obtained successfully.
     */
    @Test(expected = AssertionFailedError.class)
    public static void testGetLikedUsers(String phID, int offset, int limit, String token, Context ctx) {
        final PhotoController pc = new PhotoController(ctx, token);
        pc.setListener(new RequestListener(1) {
            @Override
            public void onRequestReady(int requmber, String mmsg) {
                Assert.assertNotSame("testGetLikedUsers --- FAILD || CONNECTION ERROR", 1003, requmber);
                Assert.assertFalse("testGetLikedUsers --- FAILD || ERROR RESPONSE", mmsg.contains("error"));
            }
        });
        pc.requestLikedUsers(phID, offset, limit);

    }

    /**
     * @param photo Photo's to be uploaded
     * @param token OAuth 2.0 token
     *              <p/>
     *              Tests weather given Photos are uploaded successfully.
     */

    @Test(expected = AssertionFailedError.class)
    public static void testUploadImage(String token, Photo... photo) {
        // final PhotoController pc = new PhotoController(MainActivity.getAppContext(), token);
        PhotoController.resgisterListener(new RequestListener(123) {
            @Override
            public void onRequestReady(int requmber, String mmsg) {
                Assert.assertSame("testUploadPhoto --- FAILD || " + mmsg, 101, requmber);
                Assert.assertFalse("testUploadPhoto --- FAILD || ERROR RESPONSE", mmsg.contains("error"));

            }
        });
        PhotoController.uploadPhoto(photo);

    }


    /**
     * @param photo Photo to be updated
     * @param token OAuth 2.0 token
     *              <p/>
     *              Tests weather Photo's info is updated successfully.
     */
    @Test(expected = AssertionFailedError.class)
    public static void testUpdatePhotoData(String token, Photo photo) {
        // final PhotoController pc = new PhotoController(MainActivity.getAppContext(), token);
        PhotoController.resgisterListener(new RequestListener(124) {
            @Override
            public void onRequestReady(int requmber, String mmsg) {
                Assert.assertNotSame("testUpdatePhotoData --- FAILD || CONNECTION ERROR", 603, requmber);
                Assert.assertFalse("testUpdatePhotoData --- FAILD || ERROR RESPONSE", mmsg.contains("error"));

            }
        });
        PhotoController.updatePhotoData(photo);

    }


}