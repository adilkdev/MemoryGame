package adil.app.memorygame.data.local.db.dao

import adil.app.memorygame.data.local.db.entity.User
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {

    @Query("SELECT * FROM user")
    suspend fun getAllUsers(): List<User>

    @Insert
    suspend fun insert(user: User)
}