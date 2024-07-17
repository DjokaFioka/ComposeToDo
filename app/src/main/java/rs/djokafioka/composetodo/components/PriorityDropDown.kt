package rs.djokafioka.composetodo.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import rs.djokafioka.composetodo.R
import rs.djokafioka.composetodo.data.models.Priority
import rs.djokafioka.composetodo.ui.theme.PRIORITY_DROP_DOWN_HEIGHT
import rs.djokafioka.composetodo.ui.theme.PRIORITY_INDICATOR_SIZE

/**
 * Created by Djordje on 9.4.2024..
 */
@Composable
fun PriorityDropDown(
    priority: Priority,
    onPrioritySelected: (Priority) -> Unit
) {
    var expanded by remember {
        mutableStateOf(false)
    }
    val angle: Float by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        label = "Angle Animation"
    )

    var parentSize by remember {
        mutableStateOf(IntSize.Zero)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .onGloballyPositioned {
                parentSize = it.size
            }
            .background(MaterialTheme.colorScheme.background)
            .height(PRIORITY_DROP_DOWN_HEIGHT)
            .clickable {
                expanded = true
            }
            .border(
                width = 1.dp,
//                color = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.disabled),
                color = MaterialTheme.colorScheme.onSurface,
                shape = MaterialTheme.shapes.small
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Canvas(
            modifier = Modifier
                .size(PRIORITY_INDICATOR_SIZE)
                .weight(1f)
        ) {
            drawCircle(
                color = priority.color
            )
        }
        Text(
            modifier = Modifier
                .weight(8f),
            text = priority.name,
            //color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.bodyMedium
        )
        IconButton(
            modifier = Modifier
                .alpha(0.5f)
                .rotate(degrees = angle)
                .weight(1.5f),
            onClick = {
                expanded = true
            }) {
            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = stringResource(id = R.string.drop_down_arrow),
//                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
        DropdownMenu(
            modifier = Modifier
//                .fillMaxWidth(fraction = 0.95f), //instead of this we use this below
                .width(with(LocalDensity.current) {
                    parentSize.width.toDp()
                }),
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            Priority.entries.slice(0..2) //We need only the first 3 priority values
                .forEach { priority ->
                    DropdownMenuItem(
                        text = {
                            PriorityItem(priority = priority)
                        },
                        onClick = {
                        expanded = false
                        onPrioritySelected(priority)
                    })
                }
        }
    }
}

@Preview
@Composable
fun PriorityDropDownPreview() {
    PriorityDropDown(
        priority = Priority.LOW,
        onPrioritySelected = {}
    )
}