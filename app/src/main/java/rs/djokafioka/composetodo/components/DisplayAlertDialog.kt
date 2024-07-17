package rs.djokafioka.composetodo.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import rs.djokafioka.composetodo.R

/**
 * Created by Djordje on 15.4.2024..
 */

@Composable
fun DisplayAlertDialog(
    title: String,
    message: String,
    openDialog: Boolean,
    closeDialog: () -> Unit,
    onConfirmed: () -> Unit
) {
    if (openDialog) {
        AlertDialog(
            title = {
                Text(
                    text = title,
//                    fontSize = MaterialTheme.typography.h5.fontSize, //Material2
                    fontSize = MaterialTheme.typography.titleMedium.fontSize, //Material3
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = message,
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    fontWeight = FontWeight.Normal
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onConfirmed()
                        closeDialog()
                    }) {
                    Text(text = stringResource(id = R.string.yes))
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = {
                        closeDialog()
                    }) {
                    Text(text = stringResource(id = R.string.no))
                }
            },
            onDismissRequest = {
                closeDialog()
            }
        )

    }
}