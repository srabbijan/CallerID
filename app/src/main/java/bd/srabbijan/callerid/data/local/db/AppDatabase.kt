package bd.srabbijan.callerid.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import bd.srabbijan.callerid.data.local.dao.ContactDao
import bd.srabbijan.callerid.data.local.entity.Contact

@Database(entities = [Contact::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun contactDao(): ContactDao
}