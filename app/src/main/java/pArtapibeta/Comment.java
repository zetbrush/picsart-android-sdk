package pArtapibeta;

import java.util.Date;

/**
 * Created by Arman on 2/23/15.
 */
public class Comment {

    private String text;
    private Date created;
    private String id;

    public String getId() {
        return id;
    }

    public Date getCreated() {
        return created;
    }

    public String getText() {
        return text;
    }


    public Comment(String text, Date created, String id) {
        this.text = text;
        this.created = created;
        this.id = id;
    }


}
