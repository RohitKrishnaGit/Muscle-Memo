package com.cs346.musclememo.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cs346.musclememo.screens.components.*
import com.cs346.musclememo.screens.viewmodels.FriendsScreenViewModel
import com.cs346.musclememo.screens.viewmodels.Message
import com.cs346.musclememo.utils.AppPreferences

@Composable
fun FriendsScreen() {
    val viewModel = viewModel<FriendsScreenViewModel>()
    Box(modifier = Modifier.fillMaxSize()) {
        FriendsContent(viewModel = viewModel)
        FriendChat(viewModel = viewModel)
        FriendProfile(viewModel = viewModel)
        AddFriendDialog(viewModel = viewModel)
        ReportFriendDialog(viewModel = viewModel)
    }
}

@Composable
fun FriendsContent(viewModel: FriendsScreenViewModel) {
    val friends = viewModel.friends
    val incomingRequests = viewModel.incomingRequests

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = "Friends",
            fontSize = 40.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))

        MMButton(
            onClick = { viewModel.updateShowAddFriendDialog(true) },
            text = "Add Friend",
            maxWidth = true,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

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
}

@Composable
fun FriendChat(viewModel: FriendsScreenViewModel) {
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
                        val roomId =
                            viewModel.currentUser?.let { viewModel.generateRoomId(it.id, friend.id) }
                        if (roomId != null) {
                            viewModel.connectSocket(roomId)
                        }
                    }
                    FriendChatHeader(onClose = {
                        viewModel.updateFriendChatVisible(false)
                        viewModel.sm.disconnect()
                    })
                    FriendChatMessages(viewModel = viewModel)
                    FriendChatInput(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun FriendChatHeader(onClose: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondary)
            .heightIn(min = 60.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = onClose) {
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
}

@Composable
fun FriendChatMessages(viewModel: FriendsScreenViewModel) {
    val messageListState = rememberLazyListState()
    LazyColumn(
        state = messageListState,
        modifier = Modifier
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(
            items = viewModel.receivedMessages,
            key = { index, message -> "${message.id}_$index" } // Ensure unique keys
        ) { _, message ->
            MessageBubble(message = message, currentUserId = viewModel.currentUser?.id)
        }
    }
}


@Composable
fun FriendChatInput(viewModel: FriendsScreenViewModel) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
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

@Composable
fun FriendProfile(viewModel: FriendsScreenViewModel) {
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
                    FriendProfileHeader(
                        onClose = { viewModel.updateFriendProfileVisible(false) },
                        onReport = { viewModel.updateShowReportFriendDialog(true) }
                    )
                    DisplayProfile(user = friend, me = false)
                }
            }
        }
    }
}

@Composable
fun FriendProfileHeader(onClose: () -> Unit, onReport: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondary)
            .heightIn(min = 60.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = onClose) {
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
            IconButton(onClick = onReport) {
                Icon(
                    Icons.Filled.Warning,
                    contentDescription = "Report",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun AddFriendDialog(viewModel: FriendsScreenViewModel) {
    if (viewModel.showAddFriendDialog) {
        MMDialog(
            showDialog = viewModel.showAddFriendDialog,
            title = "Add Friend",
            onConfirm = {
                viewModel.updateDialogErrorMessage("")
                viewModel.updateDialogSuccessMessage("")
                viewModel.sendFriendRequest(viewModel.addFriendCode)
            },
            onDismissRequest = {
                viewModel.updateShowAddFriendDialog(false)
                viewModel.updateDialogErrorMessage("")
                viewModel.updateDialogSuccessMessage("")
            },
            body = {
                TextField(
                    value = viewModel.addFriendCode,
                    onValueChange = { viewModel.addFriendCode = it },
                    label = { Text("Enter Friend Code") }
                )
            },
            errorText = viewModel.dialogErrorMessage,
            successText = viewModel.dialogSuccessMessage,
            confirmButtonText = "Add",
            cancelButtonText = "Cancel"
        )
    }
}

@Composable
fun ReportFriendDialog(viewModel: FriendsScreenViewModel) {
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

@Composable
fun MessageBubble(message: Message, currentUserId: Int?) {
    val isCurrentUser = message.sender.id.toInt() == currentUserId
    val bubbleColor = if (isCurrentUser) Color.Blue else Color.White
    val textColor = if (isCurrentUser) Color.White else Color.Black
    val horizontalArrangement = if (isCurrentUser) Arrangement.End else Arrangement.Start

    if (!isCurrentUser) {
        Text(
            text = message.sender.username,
            color = Color.Gray,
            fontSize = 12.sp,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = horizontalArrangement
    ) {
        Box(
            modifier = Modifier
                .background(bubbleColor, shape = MaterialTheme.shapes.medium)
                .padding(8.dp)
        ) {
            Text(
                text = message.message,
                color = textColor
            )
        }
    }
}
