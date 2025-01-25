package bd.srabbijan.callerid.theme

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import bd.srabbijan.callerid.utils.ssp

private val darkColorScheme = AppColorScheme(
    background = onBackgroundColor ,
    onBackground = backgroundColor,
    primary = onPrimaryColor ,
    onPrimary = primaryColor,
    textPrimary = backgroundColor,
    textSecondary = LightGray,
    actionColor = buttonColor,
    onActionColor = onButtonColor,
    separator = LightGray,
    error = DarkRed,
    success = DarkGreen
)

private val lightColorScheme = AppColorScheme(
    background = backgroundColor,
    onBackground = onBackgroundColor,
    primary = primaryColor,
    onPrimary = onPrimaryColor,
    textPrimary = primaryTextColor,
    textSecondary = secondaryTextColor,
    actionColor = buttonColor,
    onActionColor = onButtonColor,
    separator = LightGray,
    error = LightRed,
    success = DarkGreen
)

private val typography = AppTypography(
    titleLarge = TextStyle(
//        fontFamily = OpenSans,
        fontWeight = FontWeight.Bold,
        fontSize = 22.ssp()
    ),
    titleNormal = TextStyle(
        fontFamily = OpenSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.ssp()
    ),
    paragraph = TextStyle(
        fontFamily = OpenSans,
        fontWeight = FontWeight.Normal,
        fontSize = 16.ssp()
    ),
    labelLarge = TextStyle(
        fontFamily = OpenSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.ssp()
    ),
    labelNormal = TextStyle(
        fontFamily = OpenSans,
        fontWeight = FontWeight.Normal,
        fontSize = 12.ssp()
    ),
    labelSmall = TextStyle(
        fontFamily = OpenSans,
        fontWeight = FontWeight.Light,
        fontSize = 10.ssp()
    )
)

private val shape = AppShape(
    container = RoundedCornerShape(12.dp),
    button = RoundedCornerShape(50),
    circular = RoundedCornerShape(50),
)

private val size = AppSize(
    large = 24.dp,
    medium = 16.dp,
    normal = 12.dp,
    small = 8.dp
)

@Composable
fun AppTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (isDarkTheme) darkColorScheme else lightColorScheme
    CompositionLocalProvider(
        LocalAppColorScheme provides colorScheme,
        LocalAppTypography provides typography,
        LocalAppShape provides shape,
        LocalAppSize provides size,
        LocalIndication provides ripple(),
        content = content
    )
}

object AppTheme {

    val colorScheme: AppColorScheme
        @Composable get() = LocalAppColorScheme.current

    val typography: AppTypography
        @Composable get() = LocalAppTypography.current

    val shape: AppShape
        @Composable get() = LocalAppShape.current

    val size: AppSize
        @Composable get() = LocalAppSize.current
}