package bd.srabbijan.callerid.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings

fun Activity.goToAppSettings(){
    val appSettingsIntent = Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    )
    startActivity(appSettingsIntent, )
}
fun String?.getInitials(): String {
    if (this == null) {
        return ""
    } else {
        this.split(" ").forEach {
            return if (it.isNotEmpty()) {
                it[0].toString().uppercase()
            } else ""
        }
    }
    return ""
}