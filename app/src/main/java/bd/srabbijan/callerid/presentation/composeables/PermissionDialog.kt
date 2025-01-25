package bd.srabbijan.callerid.presentation.composeables

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import bd.srabbijan.callerid.theme.AppTheme

@Composable
fun PermissionDialog(
    isPermanentlyDeclined: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        shape = AppTheme.shape.container,
        containerColor = AppTheme.colorScheme.background,
        title = {
            Text(text = if(isPermanentlyDeclined){
                "Contact Permission Required. Please Grant the permission from App Settings"
            }else{
                "This app needs permission to access your contacts"
            })
        },
        confirmButton = {
            PrimaryButton (
                label = if(isPermanentlyDeclined){
                    "Go to App Settings"
                }else{
                    "Ok"
                }
            ) {
                onConfirm()
            }
        }
    )
}