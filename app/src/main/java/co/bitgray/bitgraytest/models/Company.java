package co.bitgray.bitgraytest.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by andrescamacho on 17/11/15.
 */
public class Company {

    @SerializedName("name")
    private String name;

    @SerializedName("catchPhrase")
    private String catchPhrase;

    @SerializedName("bs")
    private String bs;

    public Company() {
    }

    public String getName() {
        return name;
    }

    public String getCatchPhrase() {
        return catchPhrase;
    }

    public String getBs() {
        return bs;
    }
}
