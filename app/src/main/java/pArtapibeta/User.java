package pArtapibeta;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import test.api.picsart.com.picsart_api_test.PicsArtConst;


public class User {

   /* private String id;
    private String name;
    private String username;
    private String photo;
    private String cover;
    private Tag[] tags;
    private int followingCount;
    private int followersCount;
    private int likesCount;
    private int photosCount;
    private Location location;
    private String[] followers;*/




    /*public void parseFrom(Object o) {

        try {

            JSONObject jobj = (JSONObject) o;

            id = String.valueOf(jobj.getString(PicsArtConst.paramsUserProfile[2]));
            name = jobj.getString(PicsArtConst.paramsUserProfile[1]);
            username = jobj.getString(PicsArtConst.paramsUserProfile[0]);
            photo = jobj.getString(PicsArtConst.paramsUserProfile[7]);
            //  cover = (String)jobj.get(PicsArtConst.paramsUserProfile[19]);
            followingCount = jobj.getInt(PicsArtConst.paramsUserProfile[12]);
            followersCount = jobj.getInt(PicsArtConst.paramsUserProfile[20]);
            likesCount = jobj.getInt(PicsArtConst.paramsUserProfile[8]);
            photosCount = jobj.getInt(PicsArtConst.paramsUserProfile[6]);
            //location = (Location)jobj.get(PicsArtConst.paramsUserProfile[9]);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void parseFrom(String id, String name, String username, String photo, String cover, Tag[] tags, int followingCount, int followersCownt, int likesCount, int photosCount, Location location, String[] followers) {

        this.id = id;
        this.name = name;
        this.username = username;
        this.photo = photo;
        this.cover = cover;
        this.tags = tags;
        this.followingCount = followingCount;
        this.followersCount = followersCownt;
        this.likesCount = likesCount;
        this.photosCount = photosCount;
        this.location = location;
        this.followers = followers;

    }
*/

    @SerializedName("likes_count")
    @Expose
    private Integer likesCount;
    @Expose
    private List<String> tags = new ArrayList<String>();
    @SerializedName("is_verified")
    @Expose
    private Boolean isVerified;
    @Expose
    private Location location;
    @Expose
    private String status;
    @SerializedName("locations_count")
    @Expose
    private Integer locationsCount;
    @SerializedName("status_message")
    @Expose
    private String statusMessage;
    @Expose
    private String provider;
    @SerializedName("following_count")
    @Expose
    private Integer followingCount;
    @Expose
    private String photo;
    @SerializedName("streams_count")
    @Expose
    private Integer streamsCount;
    @Expose
    private Long id;
    @Expose
    private Integer balance;
    @Expose
    private String cover;
    @Expose
    private String username;
    @Expose
    private String email;
    @Expose
    private Boolean mature;
    @SerializedName("photos_count")
    @Expose
    private Integer photosCount;
    @Expose
    private String name;
    @SerializedName("followers_count")
    @Expose
    private Integer followersCount;
    @SerializedName("username_changed")
    @Expose
    private Boolean usernameChanged;
    @SerializedName("designs_count")
    @Expose
    private Integer designsCount;
    @Expose
    private String key;
    @SerializedName("tags_count")
    @Expose
    private Integer tagsCount;

    public User(){
    }

    public User(Long id){
        this.id=id;
    }

    /**
     *
     * @return
     * The likesCount
     */
    public Integer getLikesCount() {
        return likesCount;
    }

    /**
     *
     * @param likesCount
     * The likes_count
     */
    public void setLikesCount(Integer likesCount) {
        this.likesCount = likesCount;
    }

    /**
     *
     * @return
     * The tags
     */
    public List<String> getTags() {
        return tags;
    }

    /**
     *
     * @param tags
     * The tags
     */
    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    /**
     *
     * @return
     * The isVerified
     */
    public Boolean getIsVerified() {
        return isVerified;
    }

    /**
     *
     * @param isVerified
     * The is_verified
     */
    public void setIsVerified(Boolean isVerified) {
        this.isVerified = isVerified;
    }

    /**
     *
     * @return
     * The location
     */
    public Location getLocation() {
        return location;
    }

    /**
     *
     * @param location
     * The location
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     *
     * @return
     * The status
     */
    public String getStatus() {
        return status;
    }

    /**
     *
     * @param status
     * The status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     *
     * @return
     * The locationsCount
     */
    public Integer getLocationsCount() {
        return locationsCount;
    }

    /**
     *
     * @param locationsCount
     * The locations_count
     */
    public void setLocationsCount(Integer locationsCount) {
        this.locationsCount = locationsCount;
    }

    /**
     *
     * @return
     * The statusMessage
     */
    public String getStatusMessage() {
        return statusMessage;
    }

    /**
     *
     * @param statusMessage
     * The status_message
     */
    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    /**
     *
     * @return
     * The provider
     */
    public String getProvider() {
        return provider;
    }

    /**
     *
     * @param provider
     * The provider
     */
    public void setProvider(String provider) {
        this.provider = provider;
    }

    /**
     *
     * @return
     * The followingCount
     */
    public Integer getFollowingCount() {
        return followingCount;
    }

    /**
     *
     * @param followingCount
     * The following_count
     */
    public void setFollowingCount(Integer followingCount) {
        this.followingCount = followingCount;
    }

    /**
     *
     * @return
     * The photo
     */
    public String getPhoto() {
        return photo;
    }

    /**
     *
     * @param photo
     * The photo
     */
    public void setPhoto(String photo) {
        this.photo = photo;
    }

    /**
     *
     * @return
     * The streamsCount
     */
    public Integer getStreamsCount() {
        return streamsCount;
    }

    /**
     *
     * @param streamsCount
     * The streams_count
     */
    public void setStreamsCount(Integer streamsCount) {
        this.streamsCount = streamsCount;
    }

    /**
     *
     * @return
     * The id
     */
    public Long getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The balance
     */
    public Integer getBalance() {
        return balance;
    }

    /**
     *
     * @param balance
     * The balance
     */
    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    /**
     *
     * @return
     * The cover
     */
    public String getCover() {
        return cover;
    }

    /**
     *
     * @param cover
     * The cover
     */
    public void setCover(String cover) {
        this.cover = cover;
    }

    /**
     *
     * @return
     * The username
     */
    public String getUsername() {
        return username;
    }

    /**
     *
     * @param username
     * The username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     *
     * @return
     * The email
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @param email
     * The email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     *
     * @return
     * The mature
     */
    public Boolean getMature() {
        return mature;
    }

    /**
     *
     * @param mature
     * The mature
     */
    public void setMature(Boolean mature) {
        this.mature = mature;
    }

    /**
     *
     * @return
     * The photosCount
     */
    public Integer getPhotosCount() {
        return photosCount;
    }

    /**
     *
     * @param photosCount
     * The photos_count
     */
    public void setPhotosCount(Integer photosCount) {
        this.photosCount = photosCount;
    }

    /**
     *
     * @return
     * The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     * The followersCount
     */
    public Integer getFollowersCount() {
        return followersCount;
    }

    /**
     *
     * @param followersCount
     * The followers_count
     */
    public void setFollowersCount(Integer followersCount) {
        this.followersCount = followersCount;
    }

    /**
     *
     * @return
     * The usernameChanged
     */
    public Boolean getUsernameChanged() {
        return usernameChanged;
    }

    /**
     *
     * @param usernameChanged
     * The username_changed
     */
    public void setUsernameChanged(Boolean usernameChanged) {
        this.usernameChanged = usernameChanged;
    }

    /**
     *
     * @return
     * The designsCount
     */
    public Integer getDesignsCount() {
        return designsCount;
    }

    /**
     *
     * @param designsCount
     * The designs_count
     */
    public void setDesignsCount(Integer designsCount) {
        this.designsCount = designsCount;
    }

    /**
     *
     * @return
     * The key
     */
    public String getKey() {
        return key;
    }

    /**
     *
     * @param key
     * The key
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     *
     * @return
     * The tagsCount
     */
    public Integer getTagsCount() {
        return tagsCount;
    }

    /**
     *
     * @param tagsCount
     * The tags_count
     */
    public void setTagsCount(Integer tagsCount) {
        this.tagsCount = tagsCount;
    }

    @Override
    public String toString() {
        return "id: "+id+"\tname:  "+name+"\tusername:  "+username+"\tstatus:  "+status+"\tphoto:  "+
                photo+"\tfollowing count:  "+followersCount+"\tfollowers count:  "+followersCount+
                "\tuser likes count:  "+likesCount+"\temail:  "+email+"\tkey:  "+key;
    }
}