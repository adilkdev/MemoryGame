package adil.app.memorygame.data.local.db

import adil.app.memorygame.data.local.db.dao.PlayerDao
import adil.app.memorygame.data.local.db.entity.Player
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Player::class], version = 1, exportSchema = false)
abstract class DatabaseService : RoomDatabase() {

    abstract fun playerDao(): PlayerDao

}