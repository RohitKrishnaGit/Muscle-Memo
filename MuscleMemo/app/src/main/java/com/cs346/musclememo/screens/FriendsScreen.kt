package com.cs346.musclememo.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cs346.musclememo.screens.components.DisplayProfile
import com.cs346.musclememo.screens.components.FriendCard
import com.cs346.musclememo.screens.components.IncomingFriendRequestCard
import com.cs346.musclememo.screens.components.MMDialog
import com.cs346.musclememo.screens.components.MMButton
import com.cs346.musclememo.screens.viewmodels.FriendsScreenViewModel

@Composable
fun FriendsScreen() {
    val viewModel = viewModel<FriendsScreenViewModel>()
    val friends = viewModel.friends
    val incomingRequests = viewModel.incomingRequests

    MMDialog(
        showDialog = viewModel.showAddFriendDialog,
        title = "Add Friend",
        onConfirm = {
            viewModel.updateDialogErrorMessage("")
            viewModel.updateDialogSuccessMessage("")
            viewModel.sendFriendRequest(viewModel.addFriendEmail)
        },
        onDismissRequest = {
            viewModel.updateShowAddFriendDialog(false)
            viewModel.updateDialogErrorMessage("")
            viewModel.updateDialogSuccessMessage("")
        },
        body = {
            TextField(
                value = viewModel.addFriendEmail,
                onValueChange = { viewModel.addFriendEmail = it },
                label = { Text("Username") }
            )
        },
        errorText = viewModel.dialogErrorMessage,
        successText = viewModel.dialogSuccessMessage,
        confirmButtonText = "Add",
        cancelButtonText = "Cancel"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Text(text = "Friends Screen", fontSize = 40.sp)
            Spacer(modifier = Modifier.height(8.dp))

            MMButton(
                onClick = {
                    viewModel.updateShowAddFriendDialog(true)
                },
                text = "Add Friend",
                maxWidth = true,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Incoming friend requests
            if (incomingRequests.isNotEmpty()) {
                Text(text = "Requests", fontSize = 30.sp)
                val incomingListState = rememberLazyListState()
                LazyColumn(
                    state = incomingListState,
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    itemsIndexed(incomingRequests) { index, friend ->
                        IncomingFriendRequestCard(friend = friend, viewModel = viewModel, requestIndex = index)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (friends.isNotEmpty()) {
                Text(text = "Friends", fontSize = 30.sp)
                val friendsListState = rememberLazyListState()
                LazyColumn(
                    state = friendsListState,
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    itemsIndexed(items = friends) { index, friend ->
                        FriendCard(friend = friend, viewModel = viewModel, idx = index) { viewModel.selectFriend(friend.id) }
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = viewModel.friendProfileVisible,
            enter = slideInHorizontally(initialOffsetX = { fullWidth -> fullWidth }) + fadeIn(),
            exit = slideOutHorizontally(targetOffsetX = { fullWidth -> fullWidth }) + fadeOut()
        ) {
            viewModel.selectedFriend?.let { friend ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surface)
                ) {
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.secondary)
                                .heightIn(min = 60.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            IconButton(onClick = { viewModel.updateFriendProfileVisible(false) }) {
                                Icon(
                                    Icons.Filled.Close,
                                    contentDescription = "Close",
                                    tint = Color.White
                                )
                            }
                            Text(
                                "Friend Profile",
                                modifier = Modifier.padding(end = 8.dp),
                                color = Color.White,
                                fontSize = 20.sp
                            )
                        }
                        DisplayProfile(user = friend, me = false)
                    }
                }
            }
        }
    }
}
