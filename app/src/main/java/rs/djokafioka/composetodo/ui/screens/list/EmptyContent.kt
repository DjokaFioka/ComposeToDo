package rs.djokafioka.composetodo.ui.screens.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import rs.djokafioka.composetodo.R

/**
 * Created by Djordje on 9.4.2024..
 */
@Composable
fun EmptyContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier
                .size(128.dp),
            painter = painterResource(id = R.drawable.ic_sad_face),
            contentDescription = stringResource(R.string.no_items_icon),
            tint = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
        )
        Text(
            text = stringResource(R.string.empty_content),
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
            fontWeight = FontWeight.Bold,
            fontSize = MaterialTheme.typography.titleSmall.fontSize
        )
    }
}

@Preview
@Composable
fun EmptyContentPreview() {
    EmptyContent()
}