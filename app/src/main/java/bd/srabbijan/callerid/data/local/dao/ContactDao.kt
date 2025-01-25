package bd.srabbijan.callerid.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import bd.srabbijan.callerid.data.local.entity.Contact

@Dao
interface ContactDao {
    @Query("SELECT * FROM contacts WHERE name LIKE :query OR phoneNumber LIKE :query")
    fun searchContacts(query: String): List<Contact>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContacts(contacts: List<Contact>)

    @Update
    suspend fun updateContact(contact: Contact)
}