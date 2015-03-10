
package pArtapibeta.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.ToStringBuilder;


public class PojoUser {

    @Expose
    private String id;
    @SerializedName("is_follow")
    @Expose
    private Boolean isFollow;
    @Expose
    private String username;
    @SerializedName("is_verified")
    @Expose
    private Boolean isVerified;
    @SerializedName("photos_count")
    @Expose
    private Integer photosCount;
    @Expose
    private String name;
    @SerializedName("followers_count")
    @Expose
    private Integer followersCount;
    @Expose
    private String photo;

    /**
     *
     * @return
     *     The id
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @param id
     *     The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     * @return
     *     The isFollow
     */
    public Boolean getIsFollow() {
        return isFollow;
    }

    /**
     *
     * @param isFollow
     *     The is_follow
     */
    public void setIsFollow(Boolean isFollow) {
        this.isFollow = isFollow;
    }

    /**
     *
     * @return
     *     The username
     */
    public String getUsername() {
        return username;
    }

    /**
     *
     * @param username
     *     The username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     *
     * @return
     *     The isVerified
     */
    public Boolean getIsVerified() {
        return isVerified;
    }

    /**
     *
     * @param isVerified
     *     The is_verified
     */
    public void setIsVerified(Boolean isVerified) {
        this.isVerified = isVerified;
    }

    /**
     *
     * @return
     *     The photosCount
     */
    public Integer getPhotosCount() {
        return photosCount;
    }

    /**
     *
     * @param photosCount
     *     The photos_count
     */
    public void setPhotosCount(Integer photosCount) {
        this.photosCount = photosCount;
    }

    /**
     *
     * @return
     *     The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     *     The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     *     The followersCount
     */
    public Integer getFollowersCount() {
        return followersCount;
    }

    /**
     *
     * @param followersCount
     *     The followers_count
     */
    public void setFollowersCount(Integer followersCount) {
        this.followersCount = followersCount;
    }

    /**
     *
     * @return
     *     The photo
     */
    public String getPhoto() {
        return photo;
    }

    /**
     *
     * @param photo
     *     The photo
     */
    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
