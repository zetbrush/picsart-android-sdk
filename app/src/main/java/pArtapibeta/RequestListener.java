package pArtapibeta;

/**
 * Created by Arman on 2/20/15.
 */
    public abstract class RequestListener {
    public int getIndexOfListener() {
        return indexOfListener;
    }

    public void setIndexOfListener(int indexOfListener) {
        this.indexOfListener = indexOfListener;
    }

    int indexOfListener;
    int indexInList;

    public int getIndexInList() {
        return indexInList;
    }

    public void setIndexInList(int indexInList) {
        this.indexInList = indexInList;
    }


    public abstract void onRequestReady(int requmber, String message);
    public RequestListener(int idOfListener){
        this.indexOfListener=idOfListener;
    }




}
