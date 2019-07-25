package by.mil.bsuir.r423_reader.storage;

import java.util.ArrayList;
import java.util.List;

public class BookEntry { //TODO: suggest a better name for this class
    public BookEntry() {

    }

    public BookEntry(String name, String chapter) {
        this.name = name;
        this.chapter = chapter;
    }

    public String getCurrentChapter() {
        return chapter;
    }

    public void setCurrentChapter(String chapter) {
        this.chapter = chapter;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        if (name != null && chapter != null) {
            return name + "/" + chapter + "/";
        } else if (name != null) {
            return name + "/";
        }
        return null;
    }

    public void setPath(ArrayList<String> path) {
        if (path.size() == 2) {

        }
    }

    String chapter;
    String name;

    public List<ContentsItem> getContents() {
        return contents;
    }

    public void setContents(List<ContentsItem> contents) {
        this.contents = contents;
    }

    List<ContentsItem> contents;
}
