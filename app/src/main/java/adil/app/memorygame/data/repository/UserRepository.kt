package adil.app.memorygame.data.repository

import adil.app.memorygame.data.local.db.DatabaseService
import adil.app.memorygame.data.local.db.entity.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class UserRepository(private val databaseService: DatabaseService) {

    fun saveUser(user: User): Long {
        var id = -1L
        runBlocking(Dispatchers.IO) {
            id = databaseService.userDao().insert(user)
        }
        return id
    }

    fun getAllUsers(): List<User> {
        lateinit var  list: List<User>
        runBlocking(Dispatchers.IO) {
            list = databaseService.userDao().getAllUsers()
        }
        return list
    }

}