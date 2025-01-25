package bd.srabbijan.callerid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import bd.srabbijan.callerid.presentation.ContactScreenRoot
import bd.srabbijan.callerid.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme  {
                ContactScreenRoot()
            }
        }
    }
}
