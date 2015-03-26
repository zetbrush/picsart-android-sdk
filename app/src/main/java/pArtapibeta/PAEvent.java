package pArtapibeta;

/**
 * Created by Arman on 3/24/15.
 */
public class PAEvent {
   private  int code;
   private  String msg;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public PAEvent(int code, String msg){
        this.code=code;
        this.msg = msg;
    }

    public PAEvent(PAEvent ev){
        this.msg = ev.getMsg();
        this.code = ev.getCode();

    }
    @Override
    public String toString(){
        return ":: PAEvent ::" + code + " " + msg;
    }


}
