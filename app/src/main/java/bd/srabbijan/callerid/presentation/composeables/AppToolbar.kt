package bd.srabbijan.callerid.presentation.composeables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import bd.srabbijan.callerid.theme.AppTheme
import bd.srabbijan.callerid.theme.buttonColor


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppToolbarHome(
    label: String = "Home",
    isLoading: Boolean = false,
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable (RowScope.() -> Unit)? = null,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        TopAppBar(
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = AppTheme.colorScheme.primary,
                titleContentColor = AppTheme.colorScheme.onPrimary,
            ),
            title = {
                Text(
                    label,
                    style = AppTheme.typography.titleNormal.copy(
                        color = AppTheme.colorScheme.onPrimary
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            navigationIcon = {
                navigationIcon?.invoke()
            },
            actions = {
                actions?.invoke(this)
            }
        )
        HorizontalDivider(
            thickness = 1.dp,
            color = AppTheme.colorScheme.onPrimary.copy(alpha = .2f)
        )
        if (isLoading){
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp),
                color = buttonColor,
            )
        }
    }

}

@PreviewLightDark
@Composable
private fun PreviewAppToolbar() {
    AppTheme {
        Column(
            modifier = Modifier
                .padding(AppTheme.size.medium),
            verticalArrangement = Arrangement.spacedBy(AppTheme.size.normal)
        ) {

            AppToolbarHome(
                label = "Home",
            )
        }
    }
}