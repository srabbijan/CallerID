package bd.srabbijan.callerid.presentation

import bd.srabbijan.callerid.data.local.entity.Contact

sealed interface ContactListAction {
    data class ContactBlock(val contact: Contact) : ContactListAction
    data class ContactUnBlock(val contact: Contact) : ContactListAction
    data class Search(val query: String) : ContactListAction
    data object RequestPermissionAccess : ContactListAction
    data object RequestPermissionFailed : ContactListAction
    data class OnTabSelected(val index: Int) : ContactListAction
}