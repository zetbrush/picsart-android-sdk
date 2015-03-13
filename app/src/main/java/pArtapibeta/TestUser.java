package pArtapibeta;

/**
 * Created by intern on 3/13/15.
 */
public class TestUser {

    public static void testUserProfile(){

        final UserController userController = new UserController(MainActivity.getAppContext());
        userController.setListener(new RequestListener() {
            @Override
            public void onRequestReady(int requmber,String mmsg) {
                User user=userController.getUser();
                if(requmber ==101)
                    UserController.getSt_listener().onRequestReady(111,"PhotoTest get Info --- FAILD || CONNECTION ERROR");
                else if(mmsg.contains("error"))
                    UserController.getSt_listener().onRequestReady(111,"PhotoTest get Info --- FAILD");
                else if(user!=null && requmber ==102)
                    UserController.getSt_listener().onRequestReady(111,"PhotoTest get Info --- PASSED");
            }
        });
        userController.requestUser();
    }

    public static void testUserProfile(String userId){
        final UserController userController = new UserController(MainActivity.getAppContext());
        userController.setListener(new RequestListener() {
            @Override
            public void onRequestReady(int requmber,String mmsg) {
                User user=userController.getUser();
                if(requmber ==101)
                    UserController.getSt_listener().onRequestReady(112,"PhotoTest get Info --- FAILD || CONNECTION ERROR");
                else if(mmsg.contains("error"))
                    UserController.getSt_listener().onRequestReady(112,"PhotoTest get Info --- FAILD");
                else if(user!=null && requmber ==102)
                    UserController.getSt_listener().onRequestReady(112,"PhotoTest get Info --- PASSED");
            }
        });
        userController.requestUser(userId);
    }
}
