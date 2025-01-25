package bd.srabbijan.callerid.presentation

import android.content.Context
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bd.srabbijan.callerid.data.local.entity.Contact
import bd.srabbijan.callerid.domain.repository.CallerRepository
import bd.srabbijan.callerid.utils.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CallerViewModel(
    private val repository: CallerRepository,
    private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(ContactListState())
    val uiState = _uiState
        .onStart {
            checkPermission()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            _uiState.value
        )

    fun onAction(action: ContactListAction) {
        when (action) {
            is ContactListAction.ContactBlock -> {
                viewModelScope.launch(Dispatchers.IO) {
                    repository.updateContact(action.contact.copy(isBlocked = true))
                    _uiState.update {
                        it.copy(
                            data = it.data.map { contact ->
                                if (contact.phoneNumber == action.contact.phoneNumber) {
                                    contact.copy(isBlocked = true)
                                } else {
                                    contact
                                }
                            }
                        )
                    }
                }
            }

            is ContactListAction.ContactUnBlock -> {
                viewModelScope.launch(Dispatchers.IO) {
                    repository.updateContact(action.contact.copy(isBlocked = false))
                    _uiState.update {
                        it.copy(
                            data = it.data.map { contact ->
                                if (contact.phoneNumber == action.contact.phoneNumber) {
                                    contact.copy(isBlocked = false)
                                } else {
                                    contact
                                }
                            }
                        )
                    }
                }
            }

            is ContactListAction.Search -> {
                _uiState.update { it.copy(query = action.query) }
                searchContacts(action.query)
            }

            ContactListAction.RequestPermissionAccess -> {
                insertContact(context)
                _uiState.update {
                    it.copy(
                        showPermissionDialog = false,
                        isPermissionGranted = true
                    )
                }
                searchContacts()
            }

            ContactListAction.RequestPermissionFailed -> {
                _uiState.update {
                    it.copy(
                        showPermissionDialog = false,
                        isPermissionGranted = false
                    )
                }
            }

            is ContactListAction.OnTabSelected -> {
                _uiState.update { it.copy(selectedTabIndex = action.index) }
            }
        }
    }

    private fun insertContact(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertContacts(context).collectLatest { response->
//                response.toString().logError("response")
                Log.e("TAG", "insertContact: $response")
            }
        }
    }

    private fun checkPermission() {
        if (uiState.value.isPermissionGranted==true) {
            searchContacts()
        } else {
            _uiState.update { it.copy(showPermissionDialog = true) }
        }
    }

    private fun searchContacts(query: String = "") {
        viewModelScope.launch(Dispatchers.IO) {
            repository.searchContacts(query).collectLatest { response ->
                when (response.status) {
                    Status.SUCCESS -> {
                        _uiState.update { it.copy(isLoading = false) }
                        val data = response.data as? List<Contact> ?: return@collectLatest
                        if (data.isEmpty() && query.isEmpty()) {
                            searchContacts()
                        }
                        _uiState.update { it.copy(data = data) }
                    }

                    Status.ERROR -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                data = emptyList()
                            )
                        }
                    }

                    Status.LOADING -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                }
            }
        }
    }


}