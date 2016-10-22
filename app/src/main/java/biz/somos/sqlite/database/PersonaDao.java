package biz.somos.sqlite.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import biz.somos.sqlite.Model.Persona;

/**
 * Created by sbv23 on 21/10/2016.
 */
public class PersonaDao  {

    protected static String TABLE_NAME = "persona";
    protected static String ID = "id";
    protected static String NAME = "nombre";
    protected static String LASTNAME = "apellido";
    protected static String IDENTIFICATION = "identificacion";
    protected static String EMAIL = "email";


    protected SQLiteDatabase db;

    public PersonaDao(Context context) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        db = databaseHelper.getWritableDatabase();
    }

    public ArrayList<Persona> getAllPersona(){
        ArrayList<Persona> personas = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(sql,null);
        if (cursor.getCount() != 0){
            for (int i=0; i<cursor.getCount(); i++){
                cursor.moveToPosition(i);
                personas.add(new Persona(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4)));
            }
            return personas;
        }else{
            return personas;
        }
    }

    public Persona findById(int id){
        Persona persona = new Persona();
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE "+ ID +" = "+ Integer.toString(id);
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount() == 1){
            cursor.moveToPosition(0);
            return new Persona(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
        }else{
            return persona;
        }
    }

    public int insertPersona(Persona persona){
        ContentValues cv = new ContentValues();
        cv.put(NAME,persona.getNombre());
        cv.put(LASTNAME,persona.getApellido());
        cv.put(IDENTIFICATION, persona.getIdentificacion());
        cv.put(EMAIL,persona.getEmail());

        return (int) db.insert(TABLE_NAME,null,cv);
    }

    public int updatePersona (Persona persona){
        ContentValues cv = new ContentValues();
        cv.put(NAME,persona.getNombre());
        cv.put(LASTNAME,persona.getApellido());
        cv.put(IDENTIFICATION, persona.getIdentificacion());
        cv.put(EMAIL,persona.getEmail());

        return db.update(TABLE_NAME,cv,"id = ?",new String[]{Integer.toString(persona.getId())});
    }

    public int deletePersona (int id){
       return db.delete(TABLE_NAME,"id = ?",new String[]{Integer.toString(id)});
    }
}
