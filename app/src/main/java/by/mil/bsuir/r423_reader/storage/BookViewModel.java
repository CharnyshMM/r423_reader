package by.mil.bsuir.r423_reader.storage;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class BookViewModel extends ViewModel {
    protected final MutableLiveData<BookEntry> book = new MutableLiveData<>();

    public BookEntry getBook() {
        if (book.getValue() == null) {

        }
        return book.getValue();
    }


}
