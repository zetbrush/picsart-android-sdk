package pArtapibeta;

/**
 * Created by Arman on 3/25/15.
 */
public class PACommentEvent implements PEvent{
    int code=0;
    int index=0;
    String msg;

    public PACommentEvent(int code, String msg) {
        this.code=code;
        this.msg=msg;

    }


    public PACommentEvent(int code, String msg,int index) {
        this.code=code;
        this.msg=msg;
        this.index = index;
    }


    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public void setCode(int code) {
        this.code =code;
    }

    @Override
    public void setMsg(String msg) {
        this.msg=msg;
    }

    @Override
    public String getMsg() {
        return this.msg;
    }

    @Override
    public int getIndex() {
        return this.index;
    }

    @Override
    public void setIndex(int index) {
    this.index=index;
    }
}
