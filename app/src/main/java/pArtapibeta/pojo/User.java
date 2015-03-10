package pArtapibeta.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import pArtapibeta.Location;

@JsonInclude(JsonInclude.Include.NON_NULL)

@JsonPropertyOrder({

        "status",
        "key",
        "id",
        "name",
        "username",
        "photo",
        "email",
        "provider",
        "tags",
        "following_count",
        "followers_count",
        "streams_count",
        "likes_count",
        "photos_count",
        "designs_count",
        "tags_count",
        "locations_count",
        "mature",
        "username_changed",
        "balance",
        "is_verified",
        "location"
})
public class User {

    @JsonProperty("status")
    private String status;
    @JsonProperty("key")
    private String key;
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("username")
    private String username;
    @JsonProperty("photo")
    private String photo;
    @JsonProperty("email")
    private String email;
    @JsonProperty("provider")
    private String provider;
    @JsonProperty("tags")
    private List<Object> tags = new ArrayList<Object>();
    @JsonProperty("following_count")
    private Integer followingCount;
    @JsonProperty("followers_count")
    private Integer followersCount;
    @JsonProperty("streams_count")
    private Integer streamsCount;
    @JsonProperty("likes_count")
    private Integer likesCount;
    @JsonProperty("photos_count")
    private Integer photosCount;
    @JsonProperty("designs_count")
    private Integer designsCount;
    @JsonProperty("tags_count")
    private Integer tagsCount;
    @JsonProperty("locations_count")
    private Integer locationsCount;
    @JsonProperty("mature")
    private Boolean mature;
    @JsonProperty("username_changed")
    private Boolean usernameChanged;
    @JsonProperty("balance")
    private Integer balance;
    @JsonProperty("is_verified")
    private Boolean isVerified;
    @JsonProperty("location")
    private Location location;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * @return The status
     */
    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    /**
     * @param status The status
     */
    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return The key
     */
    @JsonProperty("key")
    public String getKey() {
        return key;
    }

    /**
     * @param key The key
     */
    @JsonProperty("key")
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * @return The id
     */
    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    /**
     * @param id The id
     */
    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return The name
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * @param name The name
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The username
     */
    @JsonProperty("username")
    public String getUsername() {
        return username;
    }

    /**
     * @param username The username
     */
    @JsonProperty("username")
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return The photo
     */
    @JsonProperty("photo")
    public String getPhoto() {
        return photo;
    }

    /**
     * @param photo The photo
     */
    @JsonProperty("photo")
    public void setPhoto(String photo) {
        this.photo = photo;
    }

    /**
     * @return The email
     */
    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    /**
     * @param email The email
     */
    @JsonProperty("email")
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return The provider
     */
    @JsonProperty("provider")
    public String getProvider() {
        return provider;
    }

    /**
     * @param provider The provider
     */
    @JsonProperty("provider")
    public void setProvider(String provider) {
        this.provider = provider;
    }

    /**
     * @return The tags
     */
    @JsonProperty("tags")
    public List<Object> getTags() {
        return tags;
    }

    /**
     * @param tags The tags
     */
    @JsonProperty("tags")
    public void setTags(List<Object> tags) {
        this.tags = tags;
    }

    /**
     * @return The followingCount
     */
    @JsonProperty("following_count")
    public Integer getFollowingCount() {
        return followingCount;
    }

    /**
     * @param followingCount The following_count
     */
    @JsonProperty("following_count")
    public void setFollowingCount(Integer followingCount) {
        this.followingCount = followingCount;
    }

    /**
     * @return The followersCount
     */
    @JsonProperty("followers_count")
    public Integer getFollowersCount() {
        return followersCount;
    }

    /**
     * @param followersCount The followers_count
     */
    @JsonProperty("followers_count")
    public void setFollowersCount(Integer followersCount) {
        this.followersCount = followersCount;
    }

    /**
     * @return The streamsCount
     */
    @JsonProperty("streams_count")
    public Integer getStreamsCount() {
        return streamsCount;
    }

    /**
     * @param streamsCount The streams_count
     */
    @JsonProperty("streams_count")
    public void setStreamsCount(Integer streamsCount) {
        this.streamsCount = streamsCount;
    }

    /**
     * @return The likesCount
     */
    @JsonProperty("likes_count")
    public Integer getLikesCount() {
        return likesCount;
    }

    /**
     * @param likesCount The likes_count
     */
    @JsonProperty("likes_count")
    public void setLikesCount(Integer likesCount) {
        this.likesCount = likesCount;
    }

    /**
     * @return The photosCount
     */
    @JsonProperty("photos_count")
    public Integer getPhotosCount() {
        return photosCount;
    }

    /**
     * @param photosCount The photos_count
     */
    @JsonProperty("photos_count")
    public void setPhotosCount(Integer photosCount) {
        this.photosCount = photosCount;
    }

    /**
     * @return The designsCount
     */
    @JsonProperty("designs_count")
    public Integer getDesignsCount() {
        return designsCount;
    }

    /**
     * @param designsCount The designs_count
     */
    @JsonProperty("designs_count")
    public void setDesignsCount(Integer designsCount) {
        this.designsCount = designsCount;
    }

    /**
     * @return The tagsCount
     */
    @JsonProperty("tags_count")
    public Integer getTagsCount() {
        return tagsCount;
    }

    /**
     * @param tagsCount The tags_count
     */
    @JsonProperty("tags_count")
    public void setTagsCount(Integer tagsCount) {
        this.tagsCount = tagsCount;
    }

    /**
     * @return The locationsCount
     */
    @JsonProperty("locations_count")
    public Integer getLocationsCount() {
        return locationsCount;
    }

    /**
     * @param locationsCount The locations_count
     */
    @JsonProperty("locations_count")
    public void setLocationsCount(Integer locationsCount) {
        this.locationsCount = locationsCount;
    }

    /**
     * @return The mature
     */
    @JsonProperty("mature")
    public Boolean getMature() {
        return mature;
    }

    /**
     * @param mature The mature
     */
    @JsonProperty("mature")
    public void setMature(Boolean mature) {
        this.mature = mature;
    }

    /**
     * @return The usernameChanged
     */
    @JsonProperty("username_changed")
    public Boolean getUsernameChanged() {
        return usernameChanged;
    }

    /**
     * @param usernameChanged The username_changed
     */
    @JsonProperty("username_changed")
    public void setUsernameChanged(Boolean usernameChanged) {
        this.usernameChanged = usernameChanged;
    }

    /**
     * @return The balance
     */
    @JsonProperty("balance")
    public Integer getBalance() {
        return balance;
    }

    /**
     * @param balance The balance
     */
    @JsonProperty("balance")
    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    /**
     * @return The isVerified
     */
    @JsonProperty("is_verified")
    public Boolean getIsVerified() {
        return isVerified;
    }

    /**
     * @param isVerified The is_verified
     */
    @JsonProperty("is_verified")
    public void setIsVerified(Boolean isVerified) {
        this.isVerified = isVerified;
    }

    /**
     * @return The location
     */
    @JsonProperty("location")
    public Location getLocation() {
        return location;
    }

    /**
     * @param location The location
     */
    @JsonProperty("location")
    public void setLocation(Location location) {
        this.location = location;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}