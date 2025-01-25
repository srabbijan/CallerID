package bd.srabbijan.callerid.presentation.composeables

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Dataset
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.SignalWifiBad
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import bd.srabbijan.callerid.theme.AppTheme

sealed class EmptyStateType {
    data object Loading : EmptyStateType()
    data object NoData : EmptyStateType()
    data object NoInternet : EmptyStateType()
    data object NoResults : EmptyStateType()
    data object NoFavorites : EmptyStateType()
    data object Error : EmptyStateType()
    data object NoPermission : EmptyStateType()
}

@Composable
fun EmptyStateScreen(
    modifier: Modifier = Modifier,
    type: EmptyStateType,
    onActionClick: (() -> Unit)? = null,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (type) {
            EmptyStateType.Loading -> LoadingContent()
            EmptyStateType.NoData -> NoDataContent(onActionClick)
            EmptyStateType.NoInternet -> NoInternetContent(onActionClick)
            EmptyStateType.NoResults -> NoResultsContent(onActionClick)
            EmptyStateType.NoFavorites -> NoFavoritesContent(onActionClick)
            EmptyStateType.Error -> ErrorContent(onActionClick)
            EmptyStateType.NoPermission -> NoPermission(onActionClick)
        }
    }
}
@Composable
private fun NoPermission(onActionClick: (() -> Unit)?) {
    EmptyStateContent(
        icon = Icons.Outlined.Settings,
        title = "Permission Denied",
        message = "Go to settings and enable the permission",
        actionLabel = "Enable",
        onActionClick = onActionClick
    )
}
@Composable
private fun NoDataContent(onActionClick: (() -> Unit)?) {
    EmptyStateContent(
        icon = Icons.Outlined.Dataset,
        title = "No Data Available",
        message = "There's nothing to display at the moment",
        actionLabel = "Refresh",
        onActionClick = onActionClick
    )
}

@Composable
private fun NoInternetContent(onActionClick: (() -> Unit)?) {
    EmptyStateContent(
        icon = Icons.Outlined.SignalWifiBad,
        title = "No Internet Connection",
        message = "Please check your internet connection and try again",
        actionLabel = "Retry",
        onActionClick = onActionClick
    )
}

@Composable
private fun NoResultsContent(onActionClick: (() -> Unit)?) {
    EmptyStateContent(
        icon = Icons.Outlined.SearchOff,
        title = "No Results Found",
        message = "Try adjusting your search or filters",
        actionLabel = "Clear Filters",
        onActionClick = onActionClick
    )
}

@Composable
private fun NoFavoritesContent(onActionClick: (() -> Unit)?) {
    EmptyStateContent(
        icon = Icons.Outlined.FavoriteBorder,
        title = "No Favorites Yet",
        message = "Items you favorite will appear here",
        actionLabel = "Browse Items",
        onActionClick = onActionClick
    )
}

@Composable
private fun ErrorContent(onActionClick: (() -> Unit)?) {
    EmptyStateContent(
        icon = Icons.Outlined.Error,
        title = "Something Went Wrong",
        message = "An error occurred while loading the data",
        actionLabel = "Try Again",
        onActionClick = onActionClick
    )
}

@Composable
private fun EmptyStateContent(
    icon: ImageVector,
    title: String,
    message: String,
    actionLabel: String? = null,
    onActionClick: (() -> Unit)? = null
) {
    Icon(
        imageVector = icon,
        contentDescription = null,
        modifier = Modifier.size(72.dp),
        tint = AppTheme.colorScheme.separator
    )

    Spacer(modifier = Modifier.height(16.dp))

    Text(
        text = title,
        style = AppTheme.typography.labelLarge,
        textAlign = TextAlign.Center,
        color = AppTheme.colorScheme.error
    )

    Spacer(modifier = Modifier.height(8.dp))

    Text(
        text = message,
        style = AppTheme.typography.paragraph,
        textAlign = TextAlign.Center,
        color = AppTheme.colorScheme.actionColor
    )

    if (actionLabel != null && onActionClick != null) {
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onActionClick
        ) {
            Text(actionLabel)
        }
    }
}

@Composable
private fun LoadingContent() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(5) {
            ShimmerListItem()
        }
    }
}

@Composable
fun ShimmerListItem(
    baseColor: Color = AppTheme.colorScheme.primary,
    highlightColor: Color = AppTheme.colorScheme.background
) {
    // Animation setup
    val shimmerAnimation = rememberInfiniteTransition(label = "")
    val xShimmer = shimmerAnimation.animateFloat(
        initialValue = -1000f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 2500,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ), label = ""
    )

    // Gradient for shimmer effect
    val brush = Brush.linearGradient(
        colors = listOf(baseColor, highlightColor, baseColor),
        start = Offset(xShimmer.value, 0f),
        end = Offset(xShimmer.value + 150f, 0f)
    )

    // Apply shimmer effect to a composable
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(brush, RoundedCornerShape(8.dp))
    )
}

@PreviewLightDark
@Composable
private fun AppEmptyStateScreen() {
    AppTheme {
        Box(
            modifier = Modifier
                .background(AppTheme.colorScheme.background)
                .padding(AppTheme.size.normal)
        ) {
            EmptyStateScreen(
                type = EmptyStateType.NoResults
            )
        }
    }
}