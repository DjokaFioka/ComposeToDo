package rs.djokafioka.composetodo.data.models

import androidx.compose.ui.graphics.Color
import rs.djokafioka.composetodo.ui.theme.HighPriority
import rs.djokafioka.composetodo.ui.theme.LowPriority
import rs.djokafioka.composetodo.ui.theme.MediumPriority

/**
 * Created by Djordje on 6.4.2024..
 */
enum class Priority(val color: Color) {
    HIGH(HighPriority),
    MEDIUM(MediumPriority),
    LOW(LowPriority),
    NONE(Color.Transparent)
}