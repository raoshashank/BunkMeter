package com.example.shashank.bunkmeter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shashank Rao M on 05-07-2016.
 */
public class DB_Provider {


    static SQLiteOpenHelper mHelper ;
    static SQLiteDatabase db;

    public DB_Provider(Context context) {
        mHelper = new DB_Helper(context);
    }

    public static void test_data(Context context){
        Subject temp = new Subject("Chemistry",5,6);
        saveSubjectInDatabase(temp);

        Subject temp1 = new Subject("Physics",2,3);
        saveSubjectInDatabase(temp1);

        Subject temp2 = new Subject("Maths",1,6);
       saveSubjectInDatabase(temp2);

        Subject temp3 = new Subject("English",2,3);
        saveSubjectInDatabase(temp3);

        Subject temp4 = new Subject("Mechanical",4,5);
        saveSubjectInDatabase(temp4);





    }
    public void open(){
        db=mHelper.getWritableDatabase();

    }
    public void close(){
        db.close();
    }


    public static void saveSubjectInDatabase(Subject subject){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DB_Helper.COLUMN_SUBJECT_NAME,subject.getSubject_name());
        contentValues.put(DB_Helper.COLUMN_MAX_HOURS_BUNKED,subject.getMax_bunkhours());
        contentValues.put(DB_Helper.COLUMN_HOURS_BUNKED,subject.getHours_bunked());
       try{
           db.insert(DB_Helper.TABLE_NAME,null,contentValues);
           }catch(SQLException s){

           Log.i("ERROR!!!",s.toString());
       }


    }

    public void updatehours(Subject subject){
        try {
            db=mHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
            values.put(DB_Helper.COLUMN_HOURS_BUNKED, subject.getHours_bunked());

                db.update(DB_Helper.TABLE_NAME,values,DB_Helper.COLUMN_SUBJECT_NAME + "='" + subject.getSubject_name()+"'",null);

        }catch (Exception s){
            Log.i("Error",s.toString());
        }
    }



    public void deleteFromDatabase(Subject subject){
        db=mHelper.getWritableDatabase();
        try {
            db.delete(DB_Helper.TABLE_NAME,DB_Helper.COLUMN_SUBJECT_NAME + "='"+subject.getSubject_name()+"'",null);
        }catch (SQLException s){
            Log.i("Error",s.toString());
        }
    }

    public void updatecompletesubject(Subject subject,String oldname) {

        db=mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DB_Helper.COLUMN_HOURS_BUNKED,subject.getHours_bunked());
        values.put(DB_Helper.COLUMN_MAX_HOURS_BUNKED,subject.getMax_bunkhours());
        values.put(DB_Helper.COLUMN_SUBJECT_NAME,subject.getSubject_name());
        try{
        db.update(DB_Helper.TABLE_NAME,values,DB_Helper.COLUMN_SUBJECT_NAME+"='"+oldname+"'",null);

        }catch (SQLException s){

        }
    }


    public void deleteall(){
    db=mHelper.getWritableDatabase();
        db.delete(DB_Helper.TABLE_NAME,null,null);
    }

    public  List<Subject> getSubjectsFromDatabase(){
        db=mHelper.getReadableDatabase();
        List<Subject> list = new ArrayList<Subject>();
        Cursor mCursor= db.rawQuery("SELECT * FROM  "+DB_Helper.TABLE_NAME,null);
        mCursor.moveToFirst();
        if(mCursor.getCount()!= 0){


        } while(!mCursor.isAfterLast())
        {    Subject temp = new Subject();
            temp.setSubject_name(mCursor.getString(mCursor.getColumnIndex(DB_Helper.COLUMN_SUBJECT_NAME)));
            temp.setHours_bunked(mCursor.getInt(mCursor.getColumnIndex(DB_Helper.COLUMN_HOURS_BUNKED)));
            temp.setMax_bunkhours(mCursor.getInt(mCursor.getColumnIndex(DB_Helper.COLUMN_MAX_HOURS_BUNKED)));
            list.add(temp);
            mCursor.moveToNext();
        }
        return  list;

    }


}
