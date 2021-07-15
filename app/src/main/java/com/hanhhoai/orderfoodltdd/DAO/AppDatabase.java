package com.hanhhoai.orderfoodltdd.DAO;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.hanhhoai.orderfoodltdd.Entity.Foody;


@Database(entities = Foody.class,version = 1,exportSchema = false)

public abstract class AppDatabase extends RoomDatabase {
    public abstract com.hanhhoai.orderfoodltdd.DAO.DaoFood daoFood();

    private static final String DB_NAME="foody_db";

    private static AppDatabase instance;

    public static synchronized AppDatabase getInstance(Context context){
        if(instance == null ){
            instance= Room.databaseBuilder(context.getApplicationContext(),AppDatabase.class,DB_NAME).
                    fallbackToDestructiveMigration().allowMainThreadQueries()
                    .build();
        }
        return instance;
    }




}
