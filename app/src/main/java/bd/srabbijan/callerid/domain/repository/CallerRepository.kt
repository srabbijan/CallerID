package bd.srabbijan.callerid.domain.repository

import android.content.Context
import bd.srabbijan.callerid.data.local.entity.Contact
import bd.srabbijan.callerid.utils.DataResource
import kotlinx.coroutines.flow.Flow

interface CallerRepository {
    suspend fun searchContacts(q:String) : Flow<DataResource<*>>

    suspend fun insertContacts(context: Context) : Flow<DataResource<*>>

    suspend fun updateContact(contact: Contact) : Flow<DataResource<*>>
}