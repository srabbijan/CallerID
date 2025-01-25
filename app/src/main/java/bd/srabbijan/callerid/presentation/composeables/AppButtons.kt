package bd.srabbijan.callerid.presentation.composeables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import bd.srabbijan.callerid.theme.AppTheme
import bd.srabbijan.callerid.theme.DarkGray
import bd.srabbijan.callerid.theme.LightGray

enum class ButtonIconPosition {
    START, END
}

@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    label: String,
    icon: Painter? = null,
    isEnable: Boolean = true,
    iconGravity: ButtonIconPosition = ButtonIconPosition.START,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        enabled = isEnable,
        colors = ButtonDefaults.buttonColors(
            containerColor = AppTheme.colorScheme.actionColor,
            contentColor = AppTheme.colorScheme.onActionColor,
            disabledContainerColor = LightGray,
            disabledContentColor = DarkGray
        ),
        shape = AppTheme.shape.button
    ) {
        if (icon == null) {
            Text(
                text = label,
                style = AppTheme.typography.paragraph
            )
        } else {
            when (iconGravity) {
                ButtonIconPosition.START -> {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(AppTheme.size.small)
                    ) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = icon,
                            contentDescription = null
                        )

                        Text(
                            text = label,
                            style = AppTheme.typography.paragraph
                        )
                    }
                }

                ButtonIconPosition.END -> {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(AppTheme.size.small)
                    ) {
                        Text(
                            text = label,
                            style = AppTheme.typography.paragraph
                        )
                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = icon,
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SecondaryButton(
    modifier: Modifier = Modifier,
    label: String,
    icon: Painter? = null,
    iconGravity: ButtonIconPosition = ButtonIconPosition.START,
    onClick: () -> Unit
) {
    OutlinedButton(
        modifier = modifier,
        onClick = onClick,
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = AppTheme.colorScheme.background,
            contentColor = AppTheme.colorScheme.actionColor
        ),
        shape = AppTheme.shape.button,
        border = BorderStroke(1.dp, AppTheme.colorScheme.actionColor)
    ) {
        if (icon == null) {
            Text(
                text = label,
                style = AppTheme.typography.paragraph
            )
        } else {
            when (iconGravity) {
                ButtonIconPosition.START -> {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(AppTheme.size.small)
                    ) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = icon,
                            contentDescription = null
                        )

                        Text(
                            text = label,
                            style = AppTheme.typography.paragraph
                        )
                    }
                }

                ButtonIconPosition.END -> {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(AppTheme.size.small)
                    ) {
                        Text(
                            text = label,
                            style = AppTheme.typography.paragraph
                        )
                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = icon,
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AppTextButton(
    modifier: Modifier = Modifier,
    label: String,
    onClick: () -> Unit
) {
    TextButton(
        modifier = modifier,
        onClick = onClick,

        ) {
        Text(
            text = label,
            style = AppTheme.typography.labelLarge.copy(
                color = AppTheme.colorScheme.actionColor
            )
        )
    }
}
