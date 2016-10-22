package biz.somos.sqlite.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by sbv23 on 21/10/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    static final String DB_NAME = "personas.sqlite";
    static int version = 1;


    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE persona (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, nombre text NOT NULL, " +
                "apellido text, identificacion text NOT NULL, email text NOT NULL)";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
