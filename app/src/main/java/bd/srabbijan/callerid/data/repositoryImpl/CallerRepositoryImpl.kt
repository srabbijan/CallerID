package bd.srabbijan.callerid.data.repositoryImpl


import android.content.Context
import android.provider.ContactsContract
import android.util.Log
import bd.srabbijan.callerid.data.local.dao.ContactDao
import bd.srabbijan.callerid.data.local.entity.Contact
import bd.srabbijan.callerid.domain.repository.CallerRepository
import bd.srabbijan.callerid.utils.DataResource
import bd.srabbijan.callerid.utils.ErrorType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class CallerRepositoryImpl(
    private val dao: ContactDao,
) : CallerRepository {

    override suspend fun searchContacts(q: String): Flow<DataResource<*>> = flow {
        emit(DataResource.loading(null))
        val finalQuery = "%$q%"
        Log.e("TAG", "searchContacts: $finalQuery")
        try {
            val data = suspendCoroutine { continuation ->
                continuation.resume(dao.searchContacts(finalQuery))
            }

            emit(DataResource.success(data = data))
        } catch (e: Exception) {
            emit(DataResource.error(
                errorType = ErrorType.IO,
                code = 400,
                message = e.message,
                data = null
            ))
        }
    }

    private fun fetchContacts(context: Context): List<Contact> {
        val contacts = mutableListOf<Contact>()
        val contentResolver = context.contentResolver
        val cursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )

        cursor?.use {
            val nameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val numberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

            while (it.moveToNext()) {
                val name = it.getString(nameIndex)
                val number = it.getString(numberIndex)
                contacts.add(Contact(
                    name = name,
                    phoneNumber = number
                ))
            }
        }

        return contacts
    }

    override suspend fun insertContacts(context: Context): Flow<DataResource<*>> = flow{
        emit(DataResource.loading(null))
        try {
            val data = fetchContacts(context)
            Log.e("TAG", "insertContact: $data")
            dao.insertContacts(data)
            emit(DataResource.success(data = true))
        } catch (e: Exception) {
            emit(DataResource.error(
                errorType = ErrorType.IO,
                code = 400,
                message = e.message,
                data = null
            ))
        }
    }

    override suspend fun updateContact(contact: Contact): Flow<DataResource<*>> = flow{
        emit(DataResource.loading(null))
        try {
            dao.updateContact(contact)
            emit(DataResource.success(data = true))
        } catch (e: Exception) {
            emit(DataResource.error(
                errorType = ErrorType.IO,
                code = 400,
                message = e.message,
                data = null
            ))
        }
    }
}
