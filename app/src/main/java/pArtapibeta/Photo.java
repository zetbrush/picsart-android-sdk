package pArtapibeta;

import org.json.JSONObject;

import java.net.URL;
import java.util.Date;

import test.api.picsart.com.picsart_api_test.PicsArtConst;

/**
 * Created by Arman on 2/23/15.
 */
public class Photo {
    private String path;
    private String id;
    private URL url;
    private String title;
    private Tag tags;
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
    private Comment[] comments;
    IS isFor;
    enum IS{AVATAR,COVER,GENERAL}

    public IS getIsFor() {
        return isFor;
    }
    public void setIsFor(IS uiFor) {
        this.isFor = uiFor;
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

    public URL getUrl() {
        return url;
    }

    public Tag getTags() {
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


    public Photo(String id, URL url, String title, Tag tags, Date crrated, boolean isMature, int width, int height, int likesCount, int viewsCount, int commentsCount, int repostsCount, boolean isLiked, boolean isReposted, String ownerid, Location location) {
       init(id, url, title, tags, created,
               isMature, width, height, likesCount, viewsCount, commentsCount, repostsCount, isLiked, isReposted, ownerid, location);
    }

    public void parseFrom(Object o) throws ClassCastException{
        JSONObject oo = (JSONObject) o;
        parseFrom(oo);
    }

    public void parseFrom(JSONObject jobj){
        try {
            String id = jobj.getString(PicsArtConst.paramsPhotoInfo[0]);
            URL url = (URL) jobj.get(PicsArtConst.paramsPhotoInfo[1]);
            String title = (String) jobj.get(PicsArtConst.paramsPhotoInfo[2]);
            Tag tags = new Tag( jobj.getJSONArray(PicsArtConst.paramsPhotoInfo[22]));
            Date created = (Date) jobj.get(PicsArtConst.paramsPhotoInfo[3]);
            boolean isMature = (boolean) jobj.get(PicsArtConst.paramsPhotoInfo[4]);
            int width = (int) jobj.get(PicsArtConst.paramsPhotoInfo[5]);
            int height = (int) jobj.get(PicsArtConst.paramsPhotoInfo[6]);
            int likesCount = (int) jobj.get(PicsArtConst.paramsPhotoInfo[7]);
            int viewsCount = (int) jobj.get(PicsArtConst.paramsPhotoInfo[8]);
            int commentsCount = (int) jobj.get(PicsArtConst.paramsPhotoInfo[9]);
            int repostsCount = (int) jobj.get(PicsArtConst.paramsPhotoInfo[10]);
            boolean isLiked = (boolean) jobj.get(PicsArtConst.paramsPhotoInfo[11]);
            boolean isReposted = (boolean) jobj.get(PicsArtConst.paramsPhotoInfo[12]);
            String ownerid = (String) jobj.get(PicsArtConst.paramsPhotoInfo[13]);
            Location location = (Location)jobj.get(PicsArtConst.paramsPhotoInfo[21]);
            init(id, url, title, tags, created,
                    isMature, width, height, likesCount, viewsCount, commentsCount, repostsCount, isLiked, isReposted, ownerid, location);

        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public Photo(IS isFor) {
        this(null, null, null, null, null);
        this.isFor=isFor;

    }

    private void init(String id, URL url, String title, Tag tags, Date crrated, boolean isMature, int width, int height, int likesCount, int viewsCount, int commentsCount, int repostsCount, boolean isLiked, boolean isReposted, String ownerid, Location location){
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


    public void setTitle(String title) {
        this.title = title;
    }

    public void setTags(Tag tags) {
        this.tags = tags;
    }

    public void setMature(boolean isMature) {
        this.isMature = isMature;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}




