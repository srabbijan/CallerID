package bd.srabbijan.callerid.presentation

import bd.srabbijan.callerid.data.local.entity.Contact
import bd.srabbijan.callerid.utils.UiText

data class ContactListState (
    val isLoading: Boolean = false,
    val error: UiText = UiText.Idle,
    val data: List<Contact> = emptyList(),
    val query: String = "",
    val isPermissionGranted: Boolean? = null,
    val showPermissionDialog: Boolean = false,
    val selectedTabIndex: Int = 0
)