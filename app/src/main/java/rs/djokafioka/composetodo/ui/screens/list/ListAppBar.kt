package rs.djokafioka.composetodo.ui.screens.list

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import rs.djokafioka.composetodo.R
import rs.djokafioka.composetodo.components.DisplayAlertDialog
import rs.djokafioka.composetodo.components.PriorityItem
import rs.djokafioka.composetodo.data.models.Priority
import rs.djokafioka.composetodo.ui.theme.LARGE_PADDING
import rs.djokafioka.composetodo.ui.theme.TOP_APP_BAR_HEIGHT
import rs.djokafioka.composetodo.ui.viewmodel.SharedViewModel
import rs.djokafioka.composetodo.util.Action
import rs.djokafioka.composetodo.util.SearchAppBarState

/**
 * Created by Djordje on 7.4.2024..
 */

@Composable
fun ListAppBar(
    sharedViewModel: SharedViewModel,
    searchAppBarState: SearchAppBarState,
    searchTextState: String
) {
    Log.d("MyTAG", "ListAppBar: Composition")
    when (searchAppBarState) {
        SearchAppBarState.CLOSED -> {
            DefaultListAppBar(
                onSearchClicked = {
//                    sharedViewModel.searchAppBarState.value = SearchAppBarState.OPENED
                    Log.d("MyTAG", "ListAppBar: onSearchClicked")
                    sharedViewModel.updateAppBarState(SearchAppBarState.OPENED)
                },
                onSortClicked = {
                    sharedViewModel.saveSortingState(priority = it)
                },
                onDeleteAllConfirmed = {
//                    sharedViewModel.action.value = Action.DELETE_ALL
                    sharedViewModel.updateAction(Action.DELETE_ALL)
                }
            )
        }
        else -> {
            SearchAppBar(
                text = searchTextState,
                onTextChanged = { newText ->
                    sharedViewModel.updateSearchTextState(newText)
                },
                onCloseClicked = {
//                    sharedViewModel.searchAppBarState.value = SearchAppBarState.CLOSED
                    sharedViewModel.updateAppBarState(SearchAppBarState.CLOSED)
                    sharedViewModel.updateSearchTextState("")
                },
                onSearchClicked = {
                    Log.d("MyTAG", "SearchAppBar: Search clicked")
                    sharedViewModel.searchTasks(searchQuery = it)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultListAppBar(
    onSearchClicked: () -> Unit,
    onSortClicked: (Priority) -> Unit,
    onDeleteAllConfirmed: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.tasks_title),
                color = MaterialTheme.colorScheme.onPrimary
            )
        },
        actions = {
            ListAppBarActions(
                onSearchClicked = onSearchClicked,
                onSortClicked = onSortClicked,
                onDeleteAllConfirmed = onDeleteAllConfirmed
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
    )
}

@Composable
fun ListAppBarActions(
    onSearchClicked: () -> Unit,
    onSortClicked: (Priority) -> Unit,
    onDeleteAllConfirmed: () -> Unit
) {
    var openDialog by remember {
        mutableStateOf(false)
    }

    DisplayAlertDialog(
        title = stringResource(id = R.string.delete_all_tasks_title),
        message = stringResource(id = R.string.delete_all_tasks_confirmation),
        openDialog = openDialog,
        closeDialog = {
            openDialog = false
        }
    ) {
        //on confirmed clicked
        onDeleteAllConfirmed()
    }
    SearchAction(onSearchClicked = onSearchClicked)
    SortAction(onSortClicked = onSortClicked)
    DeleteAllAction(
        onDeleteAllConfirmed = {
            openDialog = true
        }
    )

}

@Composable
fun SearchAction(
    onSearchClicked: () -> Unit
) {
    IconButton(onClick = {
        onSearchClicked()
    }) {
        Icon(
            imageVector = Icons.Filled.Search,
            contentDescription = stringResource(R.string.search_tasks),
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
fun SortAction(
    onSortClicked: (Priority) -> Unit
) {
    var expanded by remember {
        mutableStateOf(false)
    }

    IconButton(
        onClick = {
            expanded = true
        }) {
        Icon(
            painter = painterResource(id = R.drawable.ic_filter_list),
            contentDescription = stringResource(R.string.sort_tasks),
            tint = MaterialTheme.colorScheme.onPrimary
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            Priority.entries.slice(setOf(0, 2, 3)).forEach { priority ->
                DropdownMenuItem(
                    text = {
                        PriorityItem(priority = priority)
                    },
                    onClick = {
                        expanded = false
                        onSortClicked(priority)
                    })
            }
        }
    }
}

@Composable
fun DeleteAllAction(
    onDeleteAllConfirmed: () -> Unit
) {
    var expanded by remember {
        mutableStateOf(false)
    }

    IconButton(
        onClick = {
            expanded = true
        }) {
        Icon(
            painter = painterResource(id = R.drawable.ic_vertical_menu),
            contentDescription = stringResource(R.string.delete_all),
            tint = MaterialTheme.colorScheme.onPrimary
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            DropdownMenuItem(
                text = {
                    Text(
                        modifier = Modifier
                            .padding(start = LARGE_PADDING),
                        text = stringResource(R.string.delete_all),
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                onClick = {
                    expanded = false
                    onDeleteAllConfirmed()
                }
            )
        }
    }
}

@Composable
fun SearchAppBar(
    text: String,
    onTextChanged: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onSearchClicked: (String) -> Unit
) {
    //Removed it because of the bug with closing the searchView - it didn't want to close the first time we clicked on X icon
//    var trailingIconState by remember {
//        mutableStateOf(TrailingIconState.READY_TO_DELETE)
//    }
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(TOP_APP_BAR_HEIGHT),
        shadowElevation = 8.dp,
//        //color = MaterialTheme.colorScheme.topAppBarBackgroundColor
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = text,
            onValueChange = {
                onTextChanged(it)
            },
            placeholder = {
                Text(
                    modifier = Modifier
                        .alpha(0.5f),
                    text = stringResource(R.string.search),
                    color = Color.White
                )
            },
            textStyle = TextStyle(
                //color = MaterialTheme.colorScheme.topAppBarContentColor,
                fontSize = MaterialTheme.typography.bodyMedium.fontSize
            ),
            singleLine = true,
            leadingIcon = {
                IconButton(
                    modifier = Modifier
                        .alpha(0.38f),
                    onClick = {}
                ) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = stringResource(R.string.search_icon),
                        //tint = MaterialTheme.colorScheme.topAppBarContentColor
                    )
                }
            },
            trailingIcon = {
                IconButton(
//                    modifier = Modifier
//                        .alpha(0.5f),
                    onClick = {
//                        onCloseClicked()
//                        when (trailingIconState) {
//                            TrailingIconState.READY_TO_DELETE -> {
//                                onTextChanged("")
//                                trailingIconState = TrailingIconState.READY_TO_CLOSE
//                            }
//                            TrailingIconState.READY_TO_CLOSE -> {
//                                if (text.isNotEmpty()) {
//                                    onTextChanged("")
//                                } else {
//                                    onCloseClicked()
//                                    trailingIconState = TrailingIconState.READY_TO_DELETE
//                                }
//                            }
//                        }
                        if (text.isNotEmpty()) {
                            onTextChanged("")
                        } else {
                            onCloseClicked()
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = stringResource(R.string.close_icon),
                        //tint = MaterialTheme.colorScheme.topAppBarContentColor
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearchClicked(text)
                }
            ),
//            colors = TextFieldDefaults.colors(
//                cursorColor = MaterialTheme.colorScheme.topAppBarContentColor,
//                focusedIndicatorColor = Color.Transparent,
//                disabledIndicatorColor = Color.Transparent,
//                unfocusedIndicatorColor = Color.Transparent,
//                focusedContainerColor = Color.Transparent,
//                unfocusedContainerColor = Color.Transparent,
//                errorContainerColor = Color.Transparent,
//                disabledContainerColor = Color.Transparent
//            )
        )
    }
}

@Preview
@Composable
fun DefaultListAppBarPreview() {
    DefaultListAppBar(
        onSearchClicked = {},
        onSortClicked = {},
        onDeleteAllConfirmed = {}
    )
}

@Preview
@Composable
fun SearchAppBarPreview() {
    SearchAppBar(
        text = "",
        onTextChanged = {},
        onCloseClicked = {},
        onSearchClicked = {}
    )
}