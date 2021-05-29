package adil.app.memorygame.data.local.db

import adil.app.memorygame.data.local.db.dao.UserDao
import adil.app.memorygame.data.local.db.entity.User
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [User::class], version = 2)
abstract class DatabaseService : RoomDatabase() {

    abstract fun userDao(): UserDao

}