package com.nitin.birthdayremainder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by nitin on 1/10/2017.
 */
public class DatabaseHandler  extends SQLiteOpenHelper {

    Context context;
    SQLiteDatabase db ;

    public DatabaseHandler(Context context) {
        super(context,"FriendsDB" , null ,1);

        this.context = context ;
        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE friends (id INTEGER PRIMARY KEY , name TEXT , dob TEXT ,phone TEXT , photo TEXT)");

        Log.d("BR" , "Table Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addFriend(Friend friend){
        ContentValues cv = new ContentValues();
     //   cv.put("id",friend.getId());
        cv.put("name" , friend.getName());
        cv.put("dob",friend.getDob());
        cv.put("phone",friend.getPhone());
        cv.put("photo",friend.getPhoto());
        db.insert("friends",null,cv);
        Log.d("Add","friend inserted");
    }

    public void updateFriend(int id , Friend friend){

        ContentValues cv = new ContentValues();
        cv.put("name" , friend.getName());
        cv.put("dob",friend.getDob());
        cv.put("phone",friend.getPhone());
        cv.put("photo",friend.getPhoto());
        db.update("friends" , cv , "id=?" , new String[] {String.valueOf(id)});
        Log.d("update","friend inserted");

    }

    public  void delete(int id){


        db.delete("friends" , "id="+id , null);
        Log.d("delete","friend deleted");
    }

    public ArrayList<Friend> getAllFriends(){

        ArrayList<Friend> friendList = new ArrayList<>();

        Cursor cursor = db.query("friends",null,null,null,null,null,null);
        cursor.moveToFirst();

        if(cursor != null && cursor.getCount() > 0 ){
            do{
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String dob = cursor.getString(2);
                String phone = cursor.getString(3);
                String photo = cursor.getString(4);

                Friend friend = new Friend();
                friend.setId(id);
                friend.setName(name);
                friend.setDob(dob);
                friend.setPhone(phone);
                friend.setPhoto(photo);

                friendList.add(friend);


            }while (cursor.moveToNext());
        }

        return friendList;
    }
}
