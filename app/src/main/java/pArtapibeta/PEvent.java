package pArtapibeta;

/**
 * Created by Arman on 3/26/15.
 */
public interface PEvent {

    public int getCode();
    public void setCode(int code);
    public void setMsg(String msg);
    public String getMsg();
    public int getIndex();
    public void setIndex(int index);

}
