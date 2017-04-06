package com.zhuinden.realmbookexample.paths.books;

import android.content.Context;

import com.zhuinden.realmbookexample.data.entity.Book;
import com.zhuinden.realmbookexample.data.dao.BookDao;

/**
 * Created by Zhuinden on 2016.08.16..
 */
public class BooksPresenter {
    public static BooksPresenter getService(Context context) {
        //noinspection ResourceType
        return (BooksPresenter) context.getSystemService(TAG);
    }

    public static final String TAG = "BooksPresenter";

    public interface ViewContract {
        void showAddBookDialog();

        void showMissingTitle();

        void showEditBookDialog(Book book);

        interface DialogContract {
            String getTitle();
            String getAuthor();
            String getThumbnail();

            void bind(Book book);
        }
    }

    ViewContract viewContract;

    boolean isDialogShowing;

    boolean hasView() {
        return viewContract != null;
    }

    public void bindView(ViewContract viewContract) {
        this.viewContract = viewContract;
        if(isDialogShowing) {
            showAddDialog();
        }
    }

    public void unbindView() {
        this.viewContract = null;
    }

    public void showAddDialog() {
        if(hasView()) {
            isDialogShowing = true;
            viewContract.showAddBookDialog();
        }
    }

    public void dismissAddDialog() {
        isDialogShowing = false;
    }

    public void showEditDialog(Book book) {
        if(hasView()) {
            viewContract.showEditBookDialog(book);
        }
    }

    private Book getBookFromDC(ViewContract.DialogContract dialogContract,long id)
    {
        Book b = new Book();
        b.setId(id);
        b.setAuthor(dialogContract.getAuthor());
        b.setImageUrl(dialogContract.getThumbnail());
        b.setTitle(dialogContract.getTitle());

        return b;
    }

    public void saveBook(ViewContract.DialogContract dialogContract)
    {
        if(hasView())
        {
            String title = dialogContract.getTitle();

            if(title == null || "".equals(title.trim())) {
                viewContract.showMissingTitle();
            }
            else
            {
                BookDao bookDao = new BookDao();
                bookDao.create( getBookFromDC(dialogContract,0));
            }
        }
    }

    public void deleteBookById(final long id)
    {
        BookDao bookDao = new BookDao();
        bookDao.delete(id);
    }

    public void editBook(final ViewContract.DialogContract dialogContract, final long id)
    {
        BookDao bookDao = new BookDao();
        bookDao.update(getBookFromDC(dialogContract, id));
    }
}
