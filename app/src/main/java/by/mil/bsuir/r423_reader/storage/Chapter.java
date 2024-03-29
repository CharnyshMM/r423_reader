package by.mil.bsuir.r423_reader.storage;

import com.google.gson.annotations.SerializedName;

public class Chapter {
    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }

    @SerializedName("id")
    private String Id;

    @SerializedName("text")
    private String Text;


}