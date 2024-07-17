package rs.djokafioka.composetodo.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import rs.djokafioka.composetodo.data.models.Priority
import rs.djokafioka.composetodo.ui.theme.LARGE_PADDING
import rs.djokafioka.composetodo.ui.theme.PRIORITY_INDICATOR_SIZE

/**
 * Created by Djordje on 7.4.2024..
 */

@Composable
fun PriorityItem(priority: Priority) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Canvas(
            modifier = Modifier
                .size(PRIORITY_INDICATOR_SIZE)
        ) {
            drawCircle(
                color = priority.color
            )
        }
        Text(
            modifier = Modifier.padding(start = LARGE_PADDING),
            text = priority.name,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Preview
@Composable
fun PriorityItemPreview() {
    PriorityItem(priority = Priority.LOW)
}