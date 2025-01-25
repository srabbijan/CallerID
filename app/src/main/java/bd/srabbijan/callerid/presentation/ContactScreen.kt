package bd.srabbijan.callerid.presentation

import android.app.Activity
import android.util.Log
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.core.app.ActivityCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import bd.srabbijan.callerid.presentation.composeables.AppDottedLine
import bd.srabbijan.callerid.presentation.composeables.AppSearchField
import bd.srabbijan.callerid.presentation.composeables.AppTextButton
import bd.srabbijan.callerid.presentation.composeables.AppToolbarHome
import bd.srabbijan.callerid.presentation.composeables.EmptyStateScreen
import bd.srabbijan.callerid.presentation.composeables.EmptyStateType
import bd.srabbijan.callerid.presentation.composeables.PermissionDialog
import bd.srabbijan.callerid.presentation.composeables.SecondaryButton
import bd.srabbijan.callerid.theme.AppTheme
import bd.srabbijan.callerid.utils.getInitials
import bd.srabbijan.callerid.utils.goToAppSettings
import bd.srabbijan.callerid.utils.h
import bd.srabbijan.callerid.utils.r
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactScreenRoot(
    viewModel: CallerViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val activity = LocalActivity.current as Activity

    val contractRuntimePermission = remember {
        listOf(
            android.Manifest.permission.READ_CONTACTS,
            android.Manifest.permission.READ_PHONE_STATE
        )
    }
    val deniedPermissionList = remember {
        mutableStateListOf<String>()
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        deniedPermissionList.clear()
        permissions.entries.onEach { entry ->
            if (!entry.value) {
                deniedPermissionList.add(entry.key)
            }
            if (deniedPermissionList.isEmpty()) {
                viewModel.onAction(ContactListAction.RequestPermissionAccess)
            } else {
                viewModel.onAction(ContactListAction.RequestPermissionFailed)
            }
        }
    }

    if (deniedPermissionList.isNotEmpty()) {
        val oneTimeDeniedPermission = deniedPermissionList.filter { permission ->
            ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
        }
        PermissionDialog(
            isPermanentlyDeclined = oneTimeDeniedPermission.isEmpty(),
            onDismiss = {
                deniedPermissionList.clear()
            },
        ) {
            deniedPermissionList.clear()
            if (oneTimeDeniedPermission.isEmpty()) {
                activity.goToAppSettings()
            } else {
                permissionLauncher.launch(oneTimeDeniedPermission.toTypedArray())
            }
        }
    }

    if (uiState.showPermissionDialog) {
        permissionLauncher.launch(contractRuntimePermission.toTypedArray())
    }


    val titles = listOf(
        "All",
        "Block"
    )
    val pagerState = rememberPagerState { 2 }

    LaunchedEffect(uiState.selectedTabIndex) {
        pagerState.animateScrollToPage(uiState.selectedTabIndex)
    }

    LaunchedEffect(pagerState.currentPage) {
        viewModel.onAction(ContactListAction.OnTabSelected(pagerState.currentPage))
    }

    Scaffold(
        topBar = {
            AppToolbarHome(
                label = "Contacts",
                isLoading = uiState.isLoading || uiState.isPermissionGranted==null
            )
        }
    ) { padding ->
        if (uiState.isPermissionGranted == true){
            Column(Modifier.padding(padding)) {
                SecondaryTabRow(
                    selectedTabIndex = uiState.selectedTabIndex,
                    containerColor = AppTheme.colorScheme.background,
                    contentColor = AppTheme.colorScheme.onBackground,
                    indicator = {
                        SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(uiState.selectedTabIndex),
                            color = AppTheme.colorScheme.actionColor
                        )
                    },
                ) {
                    titles.forEachIndexed { index, title ->
                        Tab(
                            selectedContentColor = AppTheme.colorScheme.actionColor,
                            unselectedContentColor = AppTheme.colorScheme.textSecondary,
                            selected = uiState.selectedTabIndex == index,
                            onClick = {
                                viewModel.onAction(ContactListAction.OnTabSelected(index))
                            },
                            text = {
                                Text(
                                    text = title,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    style = AppTheme.typography.titleNormal
                                )
                            }
                        )
                    }
                }
                Column {
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) { pageIndex ->
                        Box(
                            Modifier
                                .fillMaxSize(),
                        ) {
                            when (pageIndex) {
                                0 -> {
                                    //all list
                                    ContactScreen(
                                        uiState = uiState,
                                        onAction = { action ->
                                            when (action) {
                                                is ContactListAction.ContactBlock -> {
                                                    viewModel.onAction(ContactListAction.ContactBlock(action.contact))
                                                }

                                                is ContactListAction.ContactUnBlock -> {
                                                    viewModel.onAction(ContactListAction.ContactUnBlock(action.contact))
                                                }

                                                is ContactListAction.Search -> {
                                                    viewModel.onAction(ContactListAction.Search(action.query))
                                                }

                                                else -> {}
                                            }
                                        }
                                    )
                                }
                                1 -> {
                                    //block contract screen
                                    ContactBlockScreen(
                                        uiState = uiState,
                                        onAction = { action ->
                                            when (action) {

                                                is ContactListAction.ContactUnBlock -> {
                                                    viewModel.onAction(ContactListAction.ContactUnBlock(action.contact))
                                                }

                                                else -> {}
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        else if (uiState.isPermissionGranted == false) {
            EmptyStateScreen(
                type = EmptyStateType.NoPermission
            ) {
                permissionLauncher.launch(contractRuntimePermission.toTypedArray())
            }
        }
    }

}

@Composable
fun ContactScreen(
    uiState: ContactListState,
    onAction: (ContactListAction) -> Unit
) {
    Column{
        AppSearchField(
            modifier = Modifier.fillMaxWidth().padding(AppTheme.size.small),
        ) { query ->
            onAction.invoke(ContactListAction.Search(query))
        }

        if (uiState.data.isEmpty() && !uiState.isLoading) {
            if (uiState.query.isNotEmpty()){
                EmptyStateScreen(
                    type = EmptyStateType.NoResults
                )
            }else{
                EmptyStateScreen(
                    type = EmptyStateType.NoData
                )
            }
        }
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(AppTheme.size.small),
        ) {
            items(uiState.data) {
                ItemContact(
                    name = it.name,
                    phone = it.phoneNumber,
                    isBlocked = it.isBlocked
                ) {
                    if (it.isBlocked) onAction.invoke(ContactListAction.ContactUnBlock(it))
                    else onAction.invoke(ContactListAction.ContactBlock(it))
                }
            }
        }
    }
}

@Composable
fun ContactBlockScreen(
    uiState: ContactListState,
    onAction: (ContactListAction) -> Unit
) {
    Column{
        val filteredData = uiState.data.filter { it.isBlocked }

        if (filteredData.isEmpty() && !uiState.isLoading) {
            EmptyStateScreen(
                type = EmptyStateType.NoData
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(AppTheme.size.small),
        ) {
            items(filteredData) {
                ItemContact(
                    name = it.name,
                    phone = it.phoneNumber,
                    isBlocked = it.isBlocked
                ) {
                    onAction.invoke(ContactListAction.ContactUnBlock(it))
                }
            }
        }
    }
}

@Composable
fun ItemContact(
    modifier: Modifier = Modifier,
    name: String = "Customer Name",
    phone: String = "01711111111",
    isBlocked: Boolean = false,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = modifier,
    ) {
        Row(
            modifier = modifier
                .padding(AppTheme.size.small)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = modifier
                    .size(50.r())
                    .clip(CircleShape)
                    .background(AppTheme.colorScheme.primary)
            ) {
                Text(
                    modifier = modifier
                        .align(Alignment.Center)
                        .padding(AppTheme.size.small),
                    text = name.getInitials(),
                    color = AppTheme.colorScheme.textPrimary,
                    style = AppTheme.typography.titleNormal
                )
            }
            Spacer(modifier = modifier.padding(AppTheme.size.small))
            Column(
                modifier = modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(AppTheme.size.small),
            ) {
                Text(
                    modifier = modifier.fillMaxWidth(),
                    text = name,
                    color = AppTheme.colorScheme.textPrimary,
                    style = AppTheme.typography.paragraph,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = phone,
                    color = AppTheme.colorScheme.textSecondary,
                    style = AppTheme.typography.labelNormal
                )
            }
            if (isBlocked) {
                SecondaryButton(
                    label = "Unblock"
                ) { onClick() }
            } else {
                AppTextButton(
                    label = "Block"
                ) { onClick() }
            }
        }

        Spacer(modifier = modifier.height(4.h()))
        AppDottedLine()
    }
}



