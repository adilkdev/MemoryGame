package adil.app.memorygame.data.local.db.dao

import adil.app.memorygame.data.local.db.entity.Player
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PlayerDao {

    @Query("SELECT * FROM player")
    suspend fun getAllUsers(): List<Player>

    @Insert
    suspend fun insert(user: Player): Long
}