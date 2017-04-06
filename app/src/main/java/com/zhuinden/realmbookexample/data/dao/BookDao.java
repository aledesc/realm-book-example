package com.zhuinden.realmbookexample.data.dao;

import com.zhuinden.realmbookexample.application.RealmManager;
import com.zhuinden.realmbookexample.data.entity.Book;
import com.zhuinden.realmbookexample.data.entity.BookFields;

import io.realm.Realm;

import static android.R.attr.id;

/**
 * Created by arquim on 6/04/17.
 */

public class BookDao
{
    private Realm realm;

    public BookDao()
    {
        realm = RealmManager.getRealm();
    }

    private Book getNewInstance(long id, Book book)
    {
        Book b = new Book();
        b.setId(id);
        b.setAuthor(book.getAuthor());
        b.setTitle(book.getTitle());
        return b;
    }

    private void updateInstance(Book newInst, Book oldInst)
    {
        newInst.setAuthor( oldInst.getAuthor() );
        newInst.setTitle( oldInst.getTitle() );
        newInst.setDescription( oldInst.getDescription() );
        newInst.setImageUrl( oldInst.getImageUrl() );
    }

    public void create(final Book book)
    {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm)
            {
                long id = 1;
                if(realm.where(Book.class).count() > 0) {
                    id = realm.where(Book.class).max(BookFields.ID).longValue() + 1; // auto-increment id
                }
                Book b = getNewInstance(id,book);
                realm.insertOrUpdate(b);
            }
        });
    }

    public Book read(long id)
    {
        return realm.where(Book.class).equalTo(BookFields.ID, id).findFirst();
    }

    public void update(final Book book)
    {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
            Book b = realm.where(Book.class).equalTo(BookFields.ID, book.getId()).findFirst();
            if(b != null)
            {
                updateInstance(b, book);
            }
            }
        });
    }

    public void delete(long i)
    {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
            Book book = realm.where(Book.class).equalTo(BookFields.ID, id).findFirst();
            if(book != null) {
                book.deleteFromRealm();
            }
            }
        });
    }


}
