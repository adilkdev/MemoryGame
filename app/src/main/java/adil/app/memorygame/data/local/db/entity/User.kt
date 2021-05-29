package adil.app.memorygame.data.local.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val uid: Long = 0,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "score")
    val score: Int
) {

    @Ignore
    var rank: Int = 0

    @Ignore
    var shouldHighlight: Boolean = false
}