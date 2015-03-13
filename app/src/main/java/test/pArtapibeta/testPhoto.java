package test.pArtapibeta;

import android.util.Log;

import java.util.logging.Handler;

import pArtapibeta.MainActivity;
import pArtapibeta.Photo;
import pArtapibeta.PhotoController;
import pArtapibeta.RequestListener;

/**
 * Created by Arman on 3/9/15.
 */
public class testPhoto {


    public static void testGetPhotoInfo(String usid, String token) {
        final PhotoController pc = new PhotoController(MainActivity.getAppContext(), token);
        pc.setListener(new RequestListener(1) {
            @Override
            public void onRequestReady(int requmber,String mmsg) {
                Photo redPhto = pc.getPhoto();
               // Log.d("Resp|Photo ", redPhto.getTitle() + redPhto.getOwnerID() + redPhto.getLocation());
              //  Log.d("Resp|Photo ", redPhto.getOwner().getName() + redPhto.getOwner().getUsername() + redPhto.getLocation());
                if(requmber ==101)
                    PhotoController.notifyListeners(0001,"PhotoTest get Info --- FAILD || CONNECTION ERROR");
                else if(mmsg.contains("error"))
                    PhotoController.notifyListeners(0001,"PhotoTest get Info --- FAILD");
                else if(redPhto!=null && requmber ==102)
                PhotoController.notifyListeners(0001,"PhotoTest get Info --- PASSED");

            }
        });
        pc.requestPhoto(usid);

    }

    public static void testLike(String phid, String token) {

        final PhotoController pc = new PhotoController(MainActivity.getAppContext(), token);
        pc.setListener(new RequestListener(1) {
            @Override
            public void onRequestReady(int requmber,String mmsg) {

                    if(requmber ==203)
                        PhotoController.notifyListeners(0002,"PhotoTest Like --- FAILD || CONNECTION ERROR");
                    else if(mmsg.contains("error"))
                    PhotoController.notifyListeners(0002,"PhotoTest Like --- FAILD");
                    else
                    PhotoController.notifyListeners(0002,"PhotoTest Like --- PASSED");
            }
        });
        pc.like(phid);

    }

    public static void testUnLike(String phid, String token) {

        final PhotoController pc = new PhotoController(MainActivity.getAppContext(), token);
        pc.setListener(new RequestListener(1) {
            @Override
            public void onRequestReady(int requmber,String mmsg) {

                if(requmber ==302)
                    PhotoController.notifyListeners(0003,"PhotoTest unLike --- FAILD || CONNECTION ERROR");
                else if(mmsg.contains("error"))
                    PhotoController.notifyListeners(0003,"PhotoTest unLike --- FAILD");
                else
                    PhotoController.notifyListeners(0003,"PhotoTest unLike --- PASSED");
            }
        });
        pc.unLike(phid);

    }

    public static  void testComment(String phid,String comment, String token){

        final PhotoController pc = new PhotoController(MainActivity.getAppContext(), token);
        pc.setListener(new RequestListener(1) {
            @Override
            public void onRequestReady(int requmber,String mmsg) {

                if(requmber ==403)
                    PhotoController.notifyListeners(0004,"PhotoTest unLike --- FAILD || CONNECTION ERROR");
                else if(mmsg.contains("error"))
                    PhotoController.notifyListeners(0004,"PhotoTest unLike --- FAILD");
                else
                    PhotoController.notifyListeners(0004,"PhotoTest unLike --- PASSED");
            }
        });
        pc.comment(phid,comment);
    }









}
