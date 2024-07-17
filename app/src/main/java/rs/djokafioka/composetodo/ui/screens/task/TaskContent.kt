package rs.djokafioka.composetodo.ui.screens.task

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import rs.djokafioka.composetodo.R
import rs.djokafioka.composetodo.components.PriorityDropDown
import rs.djokafioka.composetodo.data.models.Priority
import rs.djokafioka.composetodo.ui.theme.LARGE_PADDING
import rs.djokafioka.composetodo.ui.theme.MEDIUM_PADDING

/**
 * Created by Djordje on 11.4.2024..
 */
@Composable
fun TaskContent(
    modifier: Modifier = Modifier,
    title: String,
    onTitleChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    priority: Priority,
    onPrioritySelected: (Priority) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(all = LARGE_PADDING)
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = MEDIUM_PADDING),
            value = title,
            onValueChange = { onTitleChange(it) },
            label = {
                Text(text = stringResource(id = R.string.title))
            },
            textStyle = MaterialTheme.typography.bodyMedium,
            singleLine = true,
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
        )
        PriorityDropDown(
            priority = priority,
            onPrioritySelected = onPrioritySelected
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxSize(),
            value = description,
            onValueChange = { onDescriptionChange(it) },
            label = {
                Text(text = stringResource(id = R.string.description))
            },
            textStyle = MaterialTheme.typography.bodyMedium,
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
        )

    }
}

@Preview
@Composable
fun TaskContentPreview() {
    TaskContent(
        title = "Title",
        onTitleChange = {},
        description = "Description",
        onDescriptionChange = {},
        priority = Priority.MEDIUM,
        onPrioritySelected = {}
    )
}