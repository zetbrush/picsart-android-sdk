package pArtapibeta;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Arman on 2/23/15.
 */
public class Comment {

    SimpleDateFormat sdf;
    @SerializedName("text")
    @Expose
    private String text;

    @SerializedName("created")
    @Expose
    private String created;

    private Date creat;
    @SerializedName("_id")
    @Expose
    private String id;


    public String getId() {
        return id;
    }

    public Date getCreated() {
        if (creat == null) {
            try {
                sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'");
                return creat = sdf.parse(created);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return creat;
    }

    public String getText() {
        return text;
    }


    public Comment(String text, String created, String id) {


        this.text = text;
        try {
            this.created = created;
            sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'X'");
            this.creat = sdf.parse(created);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.id = id;

    }

    @Override
    public String toString() {

        return this.getText() + " " + this.getCreated() + " " + this.getId();
    }


}
