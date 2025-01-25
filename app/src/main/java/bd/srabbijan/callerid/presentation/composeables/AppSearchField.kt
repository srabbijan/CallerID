package bd.srabbijan.callerid.presentation.composeables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import bd.srabbijan.callerid.R
import bd.srabbijan.callerid.theme.AppTheme
import bd.srabbijan.callerid.utils.r


@Composable
fun AppSearchField(
    modifier: Modifier = Modifier,
    hint: String = "Search",
    borderColor: Color = AppTheme.colorScheme.separator.copy(alpha = .5f),
    selectedBorderColor: Color = AppTheme.colorScheme.separator,
    onTypeSearch:Boolean = true,
    onSearch: (String) -> Unit
) {
    val query = remember {
        mutableStateOf("")
    }
    var actualBorderColor by remember { mutableStateOf(borderColor) }
    val keyboardController = LocalSoftwareKeyboardController.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(AppTheme.shape.container)
            .border(
                width = 1.dp,
                color = actualBorderColor,
                shape = AppTheme.shape.container
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_search),
            contentDescription = null,
            modifier = Modifier
                .padding(AppTheme.size.small)
                .size(24.r()),
            tint = selectedBorderColor
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .heightIn(48.dp),
            contentAlignment = Alignment.Center
        ) {
            BasicTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { state ->
                        val actualColor = if (state.isFocused) selectedBorderColor else borderColor
                        actualBorderColor = actualColor
                    }
                    .padding(horizontal = AppTheme.size.small),
                value = query.value,
                onValueChange = {
                    query.value = it
                    if (onTypeSearch){
                        onSearch(query.value)
                    }else{
                        if (query.value.isEmpty()) {
                            onSearch(query.value)
                        }
                    }

                },
                textStyle = AppTheme.typography.paragraph,
                cursorBrush = SolidColor(AppTheme.colorScheme.onBackground),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Search,
                ),
                keyboardActions = KeyboardActions(onSearch = {
                    keyboardController?.hide()
                    defaultKeyboardAction(ImeAction.Search)
                    onSearch(query.value)
                }),
            )
            if (query.value.isBlank()) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = AppTheme.size.small),
                    text = hint,
                    style = AppTheme.typography.labelLarge,
                    color = borderColor
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun AppSearchFieldPreview() {
    AppTheme {
        Box(
            modifier = Modifier
                .background(AppTheme.colorScheme.background)
                .padding(AppTheme.size.normal)
        ) {
            AppSearchField(
                hint = "Search",
            ) {

            }
        }
    }
}