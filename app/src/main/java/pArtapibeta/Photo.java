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


    public String getId() {
        return id;
    }

    public URL getUrl() {
        return url;
    }

    public Tag[] getTags() {
        return tags;
    }

    public Date getCreated() {
        return created;
    }

    public boolean isMature() {
        return isMature;
    }

    public int getHeight() {
        return height;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public int getViewsCount() {
        return viewsCount;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public int getRepostsCount() {
        return repostsCount;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public boolean isReposted() {
        return isReposted;
    }

    public User getOwner() {
        return owner;
    }

    public String getOwnerID() {
        return ownerID;
    }

    public Location getLocation() {
        return location;
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