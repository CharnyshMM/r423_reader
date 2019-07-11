package by.mil.bsuir.r423_reader.storage;

import java.util.List;

public class CurrentBookStorage {
    private static CurrentBookStorage instance;
    private List<Chapter> chapters;
    private String bookName;
    private String bookPath;

    public String getBookPath() {
        return bookPath;
    }

    public void setBookPath(String bookPath) {
        this.bookPath = bookPath;
    }

    private CurrentBookStorage() {
        chapters = null;
        bookName = null;
        bookPath = null;
    }

    public static CurrentBookStorage getInstance(){
        if (instance == null) {
            instance = new CurrentBookStorage();
        }
        return instance;
    }

    public List<Chapter> getChapters() {
        return chapters;
    }

    public void setChapters(List<Chapter> chapters) {
        instance.chapters = chapters;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }
}
