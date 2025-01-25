package bd.srabbijan.callerid.presentation.composeables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import bd.srabbijan.callerid.theme.AppTheme

@Composable
fun AppDottedLine(
    color: Color = AppTheme.colorScheme.separator,
    dotRadius: Float = 2f,
    spaceBetweenDots: Float = 10f
) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(3.dp)
    ) {
        val canvasWidth = size.width

        var startX = 0f
        while (startX < canvasWidth) {
            drawCircle(
                color = color,
                radius = dotRadius,
                center = androidx.compose.ui.geometry.Offset(startX, size.height / 2)
            )
            startX += (dotRadius * 2 + spaceBetweenDots)
        }
    }
}