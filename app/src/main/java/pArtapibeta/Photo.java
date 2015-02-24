package pArtapibeta;

import java.net.URL;
import java.util.Date;

/**
 * Created by Arman on 2/23/15.
 */
public class Photo {
    private String id;
    private URL url;
    private String title;
    private Tag[] tags;
    private Date created;
    private boolean isMature;
    private int width;
    private int height;
    private int likesCount;
    private int viewsCount;
    private int commentsCount;
    private int repostsCount;
    private boolean isLiked;
    private boolean isReposted;
    private User owner;
    private String ownerID;
    private Location location;


    public String getTitle() {
        return title;
    }

    public int getWidth() {
        return width;
    }

    public void updateData(){
        //TODO
    }

    public void comment(String comment){
        //TODO
    }

    public Comment[] getComments(){

    //TODO
        return new Comment[1];
    }

    public Comment getCommentByid(String id){
        //TODO
        return new Comment(null,null,null);
    }

    public void removeComment(String id){

       //TODO
    }

    /*public User[] getLikes(){
        //TODO

    }*/

    public boolean like(){
        //TODO
        return false;
    }

    public boolean unLike(){
        //TODO
        return false;
    }


    public Photo(String id, URL url, String title, Tag[] tags, Date crrated, boolean isMature, int width, int height, int likesCount, int viewsCount, int commentsCount, int repostsCount, boolean isLiked, boolean isReposted, String ownerid, Location location) {
        this.id = id;
        this.url = url;
        this.title = title;
        this.tags = tags;
        this.created = crrated;
        this.isMature = isMature;
        this.width = width;
        this.height = height;
        this.likesCount = likesCount;
        this.viewsCount = viewsCount;
        this.commentsCount = commentsCount;
        this.repostsCount = repostsCount;
        this.isLiked = isLiked;
        this.isReposted = isReposted;
        this.ownerID = ownerid;
        this.location = location;
    }

    public Photo(String id, URL url, String title, Date crrated, String ownerid) {
        this.id = id;
        this.title = title;
        this.created = crrated;
        this.ownerID = ownerid;
        this.url = url;
        this.tags = null;
        //this.isMature = false;
        //this.width = width;
        //this.height = height;
        //this.likesCount = likesCount;
        //this.viewsCount = viewsCount;
       // this.commentsCount = commentsCount;
        //this.repostsCount = repostsCount;
        //this.isLiked = isLiked;
        //this.isReposted = isReposted;
        //this.location = location;
    }

    public Photo(){
        this(null,null,null,null,null);

    }


}
