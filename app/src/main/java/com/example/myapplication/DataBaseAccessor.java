package com.example.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.SimpleCursorAdapter;

//Класс для доступа к БД
public class DataBaseAccessor extends SQLiteOpenHelper {
    // Основные данные базы
    private static final String DATABASE_NAME = "1.db";
    private static final int DB_VERSION = 4;

    // таблицы
    private static final String TABLE_NAME = "NOTE";

    // столбцы таблицы Note
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_SURNAME = "surname";


    public DataBaseAccessor(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Создать таблицу
        db.execSQL("CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_SURNAME + " TEXT);");

        // Добавить пару записей в таблицу
        db.execSQL("INSERT INTO " + TABLE_NAME + "(" + COLUMN_NAME + ", "+ COLUMN_SURNAME +") values('Имя1','Фамилия1')");
        db.execSQL("INSERT INTO " + TABLE_NAME + "(" + COLUMN_NAME + ", "+ COLUMN_SURNAME +") values('Имя2','Фамилия2')");
    }

    /**
     * получить адаптер для listview.
     * @param layout  - разметка отдельного элемента listview
     * @param viewIds - идентификаторы элементов разметки ListView
     */
    public SimpleCursorAdapter getCursorAdapter(Context context,int layout, int[] viewIds) {
        // запрос данных для отображения
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_NAME,null);
        // какие столбцы и в каком порядке показывать в listview
        String[] columns = new  String[] {COLUMN_NAME,COLUMN_SURNAME};
        // создание адаптера
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(context,layout,cursor,columns,viewIds,0);
        return  adapter;
    }

    public void updateNote(int id, String name,String surname) {
        // выполнить запрос на обновление БД
        getReadableDatabase().execSQL("UPDATE "+ TABLE_NAME
                + " SET "
                + COLUMN_NAME + "='" + name + "', "
                + COLUMN_SURNAME + "='" + surname + "'"
                + " WHERE "
                + COLUMN_ID + "=" + id);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            //удалить старую таблицу
            db.execSQL("DROP TABLE " + TABLE_NAME);
        }
        catch (Exception exception) {
        }
        finally {
            //создать новую и заполнить ее
            onCreate(db);
        }
    }
}
