
package pArtapibeta.pojo;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.ToStringBuilder;


public class PojoPhoto {

    @SerializedName("likes_count")
    @Expose
    private Integer likesCount;
    @Expose
    private List<String> tags = new ArrayList<String>();
    @SerializedName("views_count")
    @Expose
    private Integer viewsCount;
    @SerializedName("comments_count")
    @Expose
    private Integer commentsCount;
    @Expose
    private PojoLocation pojoLocation;
    @Expose
    private String status;
    @Expose
    private Integer width;
    @Expose
    private String type;
    @SerializedName("streams_count")
    @Expose
    private Integer streamsCount;
    @Expose
    private String url;
    @SerializedName("is_liked")
    @Expose
    private Boolean isLiked;
    @Expose
    private String id;
    @Expose
    private String title;
    @SerializedName("is_reposted")
    @Expose
    private Boolean isReposted;
    @Expose
    private Integer height;
    @Expose
    private String created;
    @Expose
    private Boolean mature;
    @SerializedName("reposts_count")
    @Expose
    private Integer repostsCount;
    @SerializedName("public")
    @Expose
    private Boolean _public;
    @Expose
    private PojoUser pojoUser;

    /**
     *
     * @return
     *     The likesCount
     */
    public Integer getLikesCount() {
        return likesCount;
    }

    /**
     *
     * @param likesCount
     *     The likes_count
     */
    public void setLikesCount(Integer likesCount) {
        this.likesCount = likesCount;
    }

    /**
     *
     * @return
     *     The tags
     */
    public List<String> getTags() {
        return tags;
    }

    /**
     *
     * @param tags
     *     The tags
     */
    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    /**
     *
     * @return
     *     The viewsCount
     */
    public Integer getViewsCount() {
        return viewsCount;
    }

    /**
     *
     * @param viewsCount
     *     The views_count
     */
    public void setViewsCount(Integer viewsCount) {
        this.viewsCount = viewsCount;
    }

    /**
     *
     * @return
     *     The commentsCount
     */
    public Integer getCommentsCount() {
        return commentsCount;
    }

    /**
     *
     * @param commentsCount
     *     The comments_count
     */
    public void setCommentsCount(Integer commentsCount) {
        this.commentsCount = commentsCount;
    }

    /**
     *
     * @return
     *     The location
     */
    public PojoLocation getPojoLocation() {
        return pojoLocation;
    }

    /**
     *
     * @param pojoLocation
     *     The location
     */
    public void setPojoLocation(PojoLocation pojoLocation) {
        this.pojoLocation = pojoLocation;
    }

    /**
     *
     * @return
     *     The status
     */
    public String getStatus() {
        return status;
    }

    /**
     *
     * @param status
     *     The status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     *
     * @return
     *     The width
     */
    public Integer getWidth() {
        return width;
    }

    /**
     *
     * @param width
     *     The width
     */
    public void setWidth(Integer width) {
        this.width = width;
    }

    /**
     *
     * @return
     *     The type
     */
    public String getType() {
        return type;
    }

    /**
     *
     * @param type
     *     The type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     *
     * @return
     *     The streamsCount
     */
    public Integer getStreamsCount() {
        return streamsCount;
    }

    /**
     *
     * @param streamsCount
     *     The streams_count
     */
    public void setStreamsCount(Integer streamsCount) {
        this.streamsCount = streamsCount;
    }

    /**
     *
     * @return
     *     The url
     */
    public String getUrl() {
        return url;
    }

    /**
     *
     * @param url
     *     The url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     *
     * @return
     *     The isLiked
     */
    public Boolean getIsLiked() {
        return isLiked;
    }

    /**
     *
     * @param isLiked
     *     The is_liked
     */
    public void setIsLiked(Boolean isLiked) {
        this.isLiked = isLiked;
    }

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
     *     The title
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @param title
     *     The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     *
     * @return
     *     The isReposted
     */
    public Boolean getIsReposted() {
        return isReposted;
    }

    /**
     *
     * @param isReposted
     *     The is_reposted
     */
    public void setIsReposted(Boolean isReposted) {
        this.isReposted = isReposted;
    }

    /**
     *
     * @return
     *     The height
     */
    public Integer getHeight() {
        return height;
    }

    /**
     *
     * @param height
     *     The height
     */
    public void setHeight(Integer height) {
        this.height = height;
    }

    /**
     *
     * @return
     *     The created
     */
    public String getCreated() {
        return created;
    }

    /**
     *
     * @param created
     *     The created
     */
    public void setCreated(String created) {
        this.created = created;
    }

    /**
     *
     * @return
     *     The mature
     */
    public Boolean getMature() {
        return mature;
    }

    /**
     *
     * @param mature
     *     The mature
     */
    public void setMature(Boolean mature) {
        this.mature = mature;
    }

    /**
     *
     * @return
     *     The repostsCount
     */
    public Integer getRepostsCount() {
        return repostsCount;
    }

    /**
     *
     * @param repostsCount
     *     The reposts_count
     */
    public void setRepostsCount(Integer repostsCount) {
        this.repostsCount = repostsCount;
    }

    /**
     *
     * @return
     *     The _public
     */
    public Boolean getPublic() {
        return _public;
    }

    /**
     *
     * @param _public
     *     The public
     */
    public void setPublic(Boolean _public) {
        this._public = _public;
    }

    /**
     *
     * @return
     *     The user
     */
    public PojoUser getPojoUser() {
        return pojoUser;
    }

    /**
     *
     * @param pojoUser
     *     The user
     */
    public void setPojoUser(PojoUser pojoUser) {
        this.pojoUser = pojoUser;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
