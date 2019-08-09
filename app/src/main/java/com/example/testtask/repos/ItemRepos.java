package com.example.testtask.repos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.testtask.domain.GithubItem;

import java.util.ArrayList;
import java.util.List;

public final class ItemRepos extends SQLiteOpenHelper implements Repository<GithubItem, Long> {

    private static final String LOG_TAG = " [REPOSITORY] ";
    private static int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "git_data";
    private static final String TABLE_GITS = "gits";
    private static final String KEY_ID = "id";
    private static final String KEY_URL = "url";
    private static final String KEY_NAME = "name";
    private static final String KEY_IMG = "img";

    public ItemRepos(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.i(LOG_TAG, "DATABASE HAS BEEN INITIALIZED");
    }

    @Override
    public GithubItem save(GithubItem item) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_URL, item.getUrl());
        values.put(KEY_NAME, item.getName());
        values.put(KEY_IMG, item.getImg());

        db.insert(TABLE_GITS, null, values);
        db.close();

        Log.i(LOG_TAG, "ITEM : " + item.toString() + "HAS BEEN SAVED");
        return item;
    }

    @Override
    public void delete(GithubItem item) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(
                TABLE_GITS,
                KEY_ID + " = ?",
                new String[] { String.valueOf(item.getId()) });
        db.close();

        Log.i(LOG_TAG, "ITEM : " + item.toString() + "HAS BEEN DELETED");


    }

    @Override
    public void deleteAll() {
        List<GithubItem> items = this.findAll();
        for (GithubItem item :
                items) {
            this.delete(item);
        }
        Log.i(LOG_TAG, "TABLE AS BEEN CLEARED");
    }

    @Override
    public List<GithubItem> findAll() {
        List<GithubItem> contactList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_GITS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                GithubItem contact = new GithubItem();
                contact.setId(Long.parseLong(cursor.getString(0)));
                contact.setUrl(cursor.getString(1));
                contact.setName(cursor.getString(2));
                contact.setImg(cursor.getString(3));
                contactList.add(contact);
            } while (cursor.moveToNext());
        }
        Log.i(LOG_TAG, "DATABASE HAS BEEN PARSED");
        return contactList;
    }

    @Override
    public GithubItem findById(Long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_GITS, new String[] {
                KEY_ID,
                KEY_URL,
                KEY_NAME,
                KEY_IMG }, KEY_ID + "=?",
                new String[] {
                        String.valueOf(id) }, null, null, null, null);

        if (cursor != null){
            cursor.moveToFirst();
        }

        GithubItem item = new GithubItem(
                Long.parseLong(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3)
        );

        Log.i(LOG_TAG, "ITEM : "+ item.toString() + " \n HAS BEEN UPGRADED");

        return item;
    }

    @Override
    public GithubItem findByName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_GITS, new String[] {
                        KEY_ID,
                        KEY_URL,
                        KEY_NAME,
                        KEY_IMG }, KEY_NAME + "=?",
                new String[] {
                        String.valueOf(name) }, null, null, null, null);

        if (cursor != null){
            cursor.moveToFirst();
        }

        GithubItem item = new GithubItem(
                Long.parseLong(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3));
        return item;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_GITS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_URL + " TEXT,"
                + KEY_NAME + " TEXT,"
                + KEY_IMG + " TEXT"
                + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
        Log.i(LOG_TAG, "DATABASE HAS BEEN CREATED");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GITS);
        onCreate(db);
        Log.i(LOG_TAG, "DATABASE HAS BEEN UPGRADED");
    }
}
