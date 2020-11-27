package ru.netology.androidbasic_3_1_1.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.netology.androidbasic_3_1_1.dao.PostDao
import ru.netology.androidbasic_3_1_1.entity.PostEntity

@Database(entities = [PostEntity::class], version = 1)
abstract class AppDb : RoomDatabase(){
    abstract val postDao: PostDao

    companion object{
        @Volatile
        private var instance: AppDb? = null

        fun getInstance(context: Context): AppDb{
            return instance ?: synchronized(this){
                instance ?: buildDatabase(context).also {
                    instance = it
                }
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, AppDb::class.java, "app.db")
                .allowMainThreadQueries()
                .build()
    }
}