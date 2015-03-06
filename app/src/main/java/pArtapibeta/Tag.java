package pArtapibeta;

import android.util.Log;

import java.util.LinkedList;

/**
 * Created by Arman on 2/19/15.
 */

public class Tag {
    String[] tagValues;

    public String[] getTagValues() {
        return tagValues;
    }

    public Tag(String... name) {
        this.tagValues = new String[name.length];
        for (int i = 0; i < name.length; i++) {
            tagValues[i] = name[i];
        }
    }

    public Tag(Object name) {
        try {
            this.tagValues = (String[]) name;
        } catch (Exception e) {
            Log.e("PicsArt", "can't init Tag on given Object..");
        }
    }

}
