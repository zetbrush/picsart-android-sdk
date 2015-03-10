package pArtapibeta;

import java.net.URL;
import java.util.Date;

/**
 * Created by Arman on 2/23/15.
 */
public class Photo {
    private String path;
    private String id;
    private String url;
    private String title;
    private Tag tags;
    private String created;
    private boolean mature;
    private int width;
    private int height;
    private int likes_count;
    private int views_count;
    private int comments_count;
    private int reposts_count;
    private boolean is_liked;
    private boolean is_reposted;
    private User owner;



    private String ownerID;
    private Location location;
    private Comment[] comments;
    IS isFor;
   public enum IS{AVATAR,COVER,GENERAL}

    public IS getIsFor() {
        return isFor;
    }

    public void setIsFor(IS uiFor) {
        this.isFor = uiFor;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Comment[] getComments() {
        return comments;
    }

    public void setComments(Comment[] comments) {
        this.comments = comments;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public int getWidth() {
        return width;
    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public Tag getTags() {
        return tags;
    }

    public String getCreated() {
        return created;
    }

    public boolean isMature() {
        return mature;
    }

    public int getHeight() {
        return height;
    }

    public int getLikesCount() {
        return likes_count;
    }

    public int getViewsCount() {
        return views_count;
    }

    public int getCommentsCount() {
        return comments_count;
    }

    public int getRepostsCount() {
        return reposts_count;
    }

    public boolean isLiked() {
        return is_liked;
    }

    public boolean isReposted() {
        return is_reposted;
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


    public Photo(String id, String url, String title, Tag tags, String crrated, boolean isMature, int width, int height, int likesCount, int viewsCount, int commentsCount, int repostsCount, boolean isLiked, boolean isReposted, String ownerID, Location location) {
       init(id, url, title, tags, created,
               isMature, width, height, likesCount, viewsCount, commentsCount, repostsCount, isLiked, isReposted,  ownerID, location);
    }



    public Photo(String id, String url, String title, String crrated, String ownerid) {
        this.id = id;
        this.title = title;
        this.created = crrated;
        this.ownerID = ownerid;
        this.url = url;
        this.tags = null;

    }

    public Photo(IS isFor) {
        this(null, null, null, null, null);
        this.isFor=isFor;

    }

    private void init(String id, String url, String title, Tag tags, String crrated, boolean isMature, int width, int height, int likesCount, int viewsCount, int commentsCount, int repostsCount, boolean isLiked, boolean isReposted, String ownerID, Location location){
        this.id = id;
        this.url = url;
        this.title = title;
        this.tags = tags;
        this.created = crrated;
        this.mature = isMature;
        this.width = width;
        this.height = height;
        this.likes_count = likesCount;
        this.views_count = viewsCount;
        this.comments_count = commentsCount;
        this.reposts_count = repostsCount;
        this.is_liked = isLiked;
        this.is_reposted = isReposted;
        this.ownerID = ownerID;
        this.location = location;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public void setTags(Tag tags) {
        this.tags = tags;
    }

    public void setMature(boolean isMature) {
        this.mature = isMature;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}




