package com.cs346.musclememo.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cs346.musclememo.SocketManager
import com.cs346.musclememo.screens.components.DisplayProfile
import com.cs346.musclememo.screens.components.FriendCard
import com.cs346.musclememo.screens.components.IncomingFriendRequestCard
import com.cs346.musclememo.screens.components.MMDialog
import com.cs346.musclememo.screens.components.MMButton
import com.cs346.musclememo.screens.viewmodels.FriendsScreenViewModel
import com.cs346.musclememo.utils.AppPreferences

@Composable
fun FriendsScreen(
    viewModel: FriendsScreenViewModel
) {
    val friends = viewModel.friends
    val incomingRequests = viewModel.incomingRequests

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Text(
                text = "Friends Screen",
                fontSize = 40.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
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
                        IncomingFriendRequestCard(
                            friend = friend,
                            viewModel = viewModel,
                            requestIndex = index
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Friends list
            if (friends.isNotEmpty()) {
                Text(text = "Friends", fontSize = 30.sp)
                val friendsListState = rememberLazyListState()
                LazyColumn(
                    state = friendsListState,
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    itemsIndexed(items = friends) { index, friend ->
                        FriendCard(
                            friend = friend,
                            viewModel = viewModel,
                            idx = index
                        ) { viewModel.selectFriendProfile(friend.id) }
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = viewModel.friendChatVisible,
            enter = slideInHorizontally(initialOffsetX = { fullWidth -> fullWidth }) + fadeIn(),
            exit = slideOutHorizontally(targetOffsetX = { fullWidth -> fullWidth }) + fadeOut()
        ) {
            viewModel.selectedFriend?.let { friend ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        LaunchedEffect(Unit) {
                            // Concatenate user ids to generate room id
                            viewModel.connectSocket("2")
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.secondary)
                                .heightIn(min = 60.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            IconButton(onClick = {
                                viewModel.updateFriendChatVisible(false)
                                viewModel.sm.disconnect()
                            }) {
                                Icon(
                                    Icons.Filled.Close,
                                    contentDescription = "Close",
                                    tint = Color.White
                                )
                            }
                            Text(
                                "Friend Chat",
                                modifier = Modifier.padding(end = 8.dp),
                                color = Color.White,
                                fontSize = 20.sp
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextField(
                                value = viewModel.currentMessage,
                                onValueChange = { viewModel.updateCurrentMessage(it) },
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 8.dp),
                                placeholder = { Text("Type your message here...") },
                            )
                            IconButton(
                                onClick = {
                                    if (AppPreferences.refreshToken != null && viewModel.currentMessage.isNotBlank()) {
                                        viewModel.sm.sendMessage(viewModel.currentMessage)
                                        viewModel.updateCurrentMessage("")
                                    }
                                }
                            ) {
                                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send", tint = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                }
            }
        }

        // Friend Profile Visibility
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
                                "User Profile",
                                modifier = Modifier.padding(end = 8.dp),
                                color = Color.White,
                                fontSize = 20.sp
                            )
                            Row(
                                modifier = Modifier.weight(1f),
                                horizontalArrangement = Arrangement.End
                            ) {
                                IconButton(onClick = { viewModel.updateShowReportFriendDialog(true) }) {
                                    Icon(
                                        Icons.Filled.Warning,
                                        contentDescription = "Report",
                                        tint = Color.White
                                    )
                                }
                            }
                        }
                        DisplayProfile(user = friend, me = false)
                    }
                }
            }
        }

        // Add Friend Dialog
        if (viewModel.showAddFriendDialog) {
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
        }

        // Report Friend Dialog
        if (viewModel.showReportFriendDialog) {
            MMDialog(
                showDialog = viewModel.showReportFriendDialog,
                title = "Report User",
                onConfirm = {
                    viewModel.updateDialogErrorMessage("")
                    viewModel.updateDialogSuccessMessage("")
                    viewModel.reportUser(viewModel.reportUserReason)
                },
                onDismissRequest = {
                    viewModel.updateShowReportFriendDialog(false)
                    viewModel.updateDialogErrorMessage("")
                    viewModel.updateDialogSuccessMessage("")
                },
                body = {
                    TextField(
                        value = viewModel.reportUserReason,
                        onValueChange = { viewModel.reportUserReason = it },
                        label = { Text("Reason") }
                    )
                },
                errorText = viewModel.dialogErrorMessage,
                successText = viewModel.dialogSuccessMessage,
                confirmButtonText = "Report",
                cancelButtonText = "Cancel"
            )
        }
    }
}