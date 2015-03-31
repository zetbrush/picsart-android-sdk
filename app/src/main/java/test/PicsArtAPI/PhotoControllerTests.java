package test.picsartapi;

import android.content.Context;

import junit.framework.Assert;
import junit.framework.AssertionFailedError;

import org.junit.Test;

import picsartapi.Comment;
import picsartapi.Photo;
import picsartapi.PhotoController;
import picsartapi.RequestListener;


/**
 * This class consists exclusively test methods, that operate on making
 * requests and asserting for expectation.
 *
 *  If testMethod fails exception with message will logged
 *
 * This class is a member of the www.picsart.com.
 *
 * @author Arman Andreasyan 3/9/15
 */

public class PhotoControllerTests  {



    @Test(expected=AssertionFailedError.class)
    public static void testRequestPhoto(String usid, String token, Context ctx) {

        final PhotoController pc = new PhotoController(ctx, token);
        pc.setListener(new RequestListener(1) {
            @Override
            public void onRequestReady(int requmber,String mmsg) {

                Assert.assertNotSame("testRequestPhoto --- FAILD || CONNECTION ERROR", 203, requmber);
                Assert.assertFalse("testRequestPhoto (error response)--- FAILD", mmsg.contains("error")|| pc.getPhoto()==null);
            }
        });

        pc.requestPhoto(usid);

    }

    @Test(expected=AssertionFailedError.class)
    public static void testLike(String phid, String token,Context ctx) {

        final PhotoController pc = new PhotoController(ctx, token);
        pc.setListener(new RequestListener(1) {
            @Override
            public void onRequestReady(int requmber,String mmsg) {
                Assert.assertNotSame(" testLike --- FAILD || CONNECTION ERROR", 703, requmber);
                Assert.assertFalse("testLike --- FAILD || ERROR RESPONSE", mmsg.contains("error"));
            }
        });
        pc.like(phid);

    }

    @Test(expected=AssertionFailedError.class)
    public static void testUnLike(String phid, String token, Context ctx) {

        final PhotoController pc = new PhotoController(ctx, token);
        pc.setListener(new RequestListener(1) {
            @Override
            public void onRequestReady(int requmber,String mmsg) {

                Assert.assertNotSame("testUnLike --- FAILD || CONNECTION ERROR", 803, requmber);
                Assert.assertFalse("testUnLike --- FAILD || ERROR RESPONSE", mmsg.contains("error"));

            }
        });
        pc.unLike(phid);

    }

    @Test(expected=AssertionFailedError.class)
    public static  void testAddComment(String phid,String comment, String token,Context ctx){

        final PhotoController pc = new PhotoController(ctx, token);
        pc.setListener(new RequestListener(1) {
            @Override
            public void onRequestReady(int requmber,String mmsg) {
                Assert.assertNotSame("testAddComment --- FAILD || CONNECTION ERROR", 403, requmber);
                Assert.assertFalse("testAddComment --- FAILD || ERROR RESPONSE", mmsg.contains("error"));
            }
        });
        pc.addComment(phid,new Comment(comment,true));
    }


    @Test(expected=AssertionFailedError.class)
    public static  void testDeleteComment(String phId,String commentId, String token, Context ctx){
        final PhotoController pc = new PhotoController(ctx, token);
        pc.setListener(new RequestListener(1) {
            @Override
            public void onRequestReady(int requmber,String mmsg) {
                Assert.assertNotSame("testDeleteComment --- FAILD || CONNECTION ERROR", 503, requmber);
                Assert.assertFalse("testDeleteComment --- FAILD || ERROR RESPONSE", mmsg.contains("error"));
            }
        });
        pc.deleteComment(phId, commentId);

    }

    @Test(expected=AssertionFailedError.class)
    public static  void testRequestComments(String phId,int offset, int limit, String token, Context ctx){
        final PhotoController pc = new PhotoController(ctx, token);
        pc.setListener(new RequestListener(1) {
            @Override
            public void onRequestReady(int requmber,String mmsg) {
                Assert.assertNotSame("testRequestComments --- FAILD || CONNECTION ERROR", 303, requmber);
                Assert.assertFalse("testRequestComments --- FAILD || ERROR RESPONSE", mmsg.contains("error"));

            }
        });
        pc.requestComments(phId, offset,limit);

    }



    @Test(expected=AssertionFailedError.class)
    public static  void testGetCommentById(String photoId,String commentId, String token,Context ctx){
        final PhotoController pc = new PhotoController( ctx, token);
        pc.setListener(new RequestListener(1) {
            @Override
            public void onRequestReady(int requmber,String mmsg) {
                Assert.assertNotSame("testGetCommentById --- FAILD || CONNECTION ERROR", 903, requmber);

                Assert.assertFalse("testGetCommentById --- FAILD || ERROR RESPONSE", mmsg.contains("error"));
                Assert.assertNotNull("testGetCommentById --- FAILD || NULL",pc.getComment());
            }
        });
        pc.requestCommentByid(photoId,commentId);

    }

    @Test(expected=AssertionFailedError.class)
    public static  void testGetLikedUsers(String phId, int offset,int limit, String token, Context ctx){
        final PhotoController pc = new PhotoController(ctx, token);
        pc.setListener(new RequestListener(1) {
            @Override
            public void onRequestReady(int requmber,String mmsg) {
                Assert.assertNotSame("testGetLikedUsers --- FAILD || CONNECTION ERROR", 1003, requmber);
                Assert.assertFalse("testGetLikedUsers --- FAILD || ERROR RESPONSE", mmsg.contains("error"));
            }
        });
        pc.requestLikedUsers(phId, offset, limit);

    }

    @Test(expected=AssertionFailedError.class)
    public static  void testUploadImage(String token, Photo... photo){
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

    @Test(expected=AssertionFailedError.class)
    public static  void testUpdatePhotoData(String token,Photo photo){
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
