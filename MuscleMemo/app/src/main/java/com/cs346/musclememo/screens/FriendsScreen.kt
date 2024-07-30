package com.cs346.musclememo.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cs346.musclememo.screens.components.*
import com.cs346.musclememo.screens.viewmodels.FriendsScreenViewModel
import com.cs346.musclememo.screens.viewmodels.Message
import com.cs346.musclememo.screens.viewmodels.Sender
import com.cs346.musclememo.utils.AppPreferences
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendsScreen(
    viewModel: FriendsScreenViewModel = viewModel<FriendsScreenViewModel>(),
    bottomBarState: MutableState<Boolean>
){
    Box (
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Friends",
                fontSize = 40.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Row (
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.1f)
                    .clickable {
                        bottomBarState.value = false
                        viewModel.getIncomingFriendRequests()
                        viewModel.updateShowFriendsList(true)
                    }
                    .padding(10.dp)
            ){
                Icon(Icons.Default.Groups, null, modifier = Modifier.size(60.dp))
                Spacer(modifier = Modifier.width(5.dp))
                Text(text = "Friends List", fontSize = 16.sp)
            }
            PullToRefreshBox(isRefreshing = viewModel.isChatRefreshing, onRefresh = { viewModel.refreshChats() },
                modifier = Modifier.fillMaxSize()){
                Column (
                    modifier = Modifier.fillMaxSize()
                ){
                    Text(text = "Messages", fontSize = 20.sp)
                    Spacer(modifier = Modifier.height(5.dp))
                    LazyColumn {
                        items(items = viewModel.allChats.toList(), key = { it.first.id }) { chat ->
                            if (chat.second.isNotEmpty()) {
                                ChatPreview(friend = chat.first, lastMessage = chat.second.last { it.message.isNotEmpty() }, onClick = {
                                    if (!viewModel.isChatAnimated){
                                        viewModel.updateSelectedFriend(chat.first)
                                        bottomBarState.value = false
                                        viewModel.updateFriendChatVisible(true)
                                    }
                                })
                            }
                        }
                    }
                }
            }
        }
    }
    FriendsList(viewModel = viewModel, bottomBarState = bottomBarState)
    Chat(
        visible = viewModel.friendChatVisible,
        startChat = {
            DisposableEffect(Unit) {
                // Concatenate user ids to generate room id
                if (viewModel.selectedFriend != null){
                    val roomId =
                        viewModel.currentUser?.let {
                            viewModel.generateRoomId(
                                it.id,
                                viewModel.selectedFriend!!.id
                            )
                        }
                    if (roomId != null) {
                        viewModel.connectSocket(roomId)
                    }
                }
                onDispose {
                    viewModel.disconnectSocket()
                    viewModel.updateIsChatAnimated(false)
                }
            }
        },
        onBackPressed = {
            viewModel.updateFriendChatVisible(false)
            if (!viewModel.showFriendsList)
                bottomBarState.value = true
        },
        profilePicture = viewModel.selectedFriend?.profilePicture ?: null,
        friendUsername = viewModel.selectedFriend?.username ?: "",
        gender = viewModel.selectedFriend?.gender,
        report = {viewModel.updateShowReportFriendDialog(true)},
        remove = {viewModel.updateShowRemoveFriendDialog(true)},
        messages = viewModel.getChat(viewModel.selectedFriend),
        currentMessage = viewModel.currentMessage,
        updateCurrentMessage = viewModel::updateCurrentMessage,
        sendMessage = {
            if (AppPreferences.refreshToken != null && viewModel.currentMessage.isNotBlank()) {
                viewModel.sm.sendMessage(viewModel.currentMessage)
                viewModel.updateCurrentMessage("")
            }
        },
        isTransitioning = viewModel::updateIsChatAnimated
    )
    ReportFriendDialog(viewModel = viewModel)
    RemoveFriendDialog(viewModel = viewModel)
}

@Composable
fun Chat(
    visible: Boolean,
    startChat: @Composable () -> Unit,
    profilePicture: Any?,
    friendUsername: String,
    gender: String? = null,
    report: () -> Unit = {},
    remove: () -> Unit = {},
    onBackPressed: () -> Unit = {},
    messages: List<Message>?,
    currentMessage: String,
    updateCurrentMessage: (String) -> Unit,
    sendMessage: () -> Unit,
    isTransitioning: (Boolean) -> Unit
){
    val messageListState = rememberLazyListState()
    val isImeVisible = WindowInsets.ime.getBottom(LocalDensity.current)
    AnimatedVisibility(
        visible = visible,
        enter = slideInHorizontally(initialOffsetX = { fullWidth -> fullWidth }) + fadeIn(),
        exit = slideOutHorizontally(targetOffsetX = { fullWidth -> fullWidth }) + fadeOut()
    ) {
        if (this.transition.currentState == this.transition.targetState) {
            isTransitioning(false)
        }

        Box (modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background))
        {
            startChat()
            Column (modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Bottom) {

                ChatHeader(
                    onBackPressed = onBackPressed,
                    profilePicture = profilePicture,
                    username = friendUsername,
                    gender = gender,
                    report = report,
                    remove = remove
                    )

                // Initial scroll to bottom
                LaunchedEffect(Unit) {
                    messageListState.scrollToItem(messageListState.layoutInfo.totalItemsCount)
                }

                LaunchedEffect(isImeVisible > 0) {
                    // Wait for keyboard to come up
                    delay(200)
                    messageListState.scrollToItem(messageListState.layoutInfo.totalItemsCount)
                }

                // Scroll to bottom on change
                if (messages != null) {
                    LaunchedEffect(messages.size){
                        messageListState.scrollToItem(messageListState.layoutInfo.totalItemsCount)
                    }
                }

                LazyColumn(
                    state = messageListState,
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (messages != null){
                        val sortedMessages = messages.sortedBy { it.timestamp }.filter { it.message.isNotEmpty() }
                        itemsIndexed(
                            items = sortedMessages,
                            key = { index, message -> "${message.id}_$index" } // Ensure unique keys
                        ) { index, message ->
                            ChatBubble(
                                message = message,
                                if (index > 0) sortedMessages[index - 1].sender.username else ""
                            )
                        }
                    }
                }
                FriendChatInput(
                    currentMessage = currentMessage,
                    updateCurrentMessage = updateCurrentMessage,
                    friendUsername = friendUsername,
                    sendMessage = sendMessage
                )
            }
        }
    }
}

@Composable
fun FriendChatInput(
    currentMessage: String,
    updateCurrentMessage: (String) -> Unit,
    friendUsername: String,
    sendMessage: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = currentMessage,
                onValueChange = { updateCurrentMessage(it) },
                modifier = Modifier
                    .weight(1f),
                placeholder = { Text("Message $friendUsername") },
                shape = RoundedCornerShape(20.dp)
            )

            AnimatedVisibility(
                visible = currentMessage != "",
                enter = slideInHorizontally(initialOffsetX = { fullWidth -> fullWidth }),
                exit = slideOutHorizontally(targetOffsetX = { fullWidth -> fullWidth })
            ){
                IconButton(
                    onClick = sendMessage
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
fun FriendsList(
    viewModel: FriendsScreenViewModel,
    bottomBarState: MutableState<Boolean>
){
    AnimatedVisibility(
        visible = viewModel.showFriendsList,
        enter = slideInHorizontally(initialOffsetX = { fullWidth -> fullWidth }),
        exit = slideOutHorizontally(targetOffsetX = { fullWidth -> fullWidth })
    ){
        Box (
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ){
            Column(
                modifier = Modifier.fillMaxSize()
            ){
                TopAppBar(text = "Friends List") {
                    viewModel.updateErrorMessage("")
                    viewModel.updateSuccessMessage("")
                    bottomBarState.value = true
                    viewModel.updateShowFriendsList(false)
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp)
                ) {
                    Text(
                        text = "Add a Friend",
                        fontSize = 20.sp
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Your Friend Code: ", fontSize = 14.sp)
                        Text(
                            text = viewModel.currentUser?.id.toString(),
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Column {
                            OutlinedTextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = viewModel.addFriendCode,
                                onValueChange = viewModel::updateAddFriendCode,
                                placeholder = { Text("Enter friend code") },
                                trailingIcon = {
                                    IconButton(onClick = {
                                        viewModel.updateErrorMessage("")
                                        viewModel.updateSuccessMessage("")
                                        viewModel.sendFriendRequest(viewModel.addFriendCode.toString()) {
                                            viewModel.updateAddFriendCode("")
                                            viewModel.updateSuccessMessage("Friend Added Successfully!")
                                            viewModel.refreshChats()
                                        }
                                    }) {
                                        Icon(
                                            Icons.AutoMirrored.Filled.Send,
                                            contentDescription = "Send",
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                })
                            if (viewModel.errorMessage != "") {
                                Spacer(modifier = Modifier.height(5.dp))
                                Text(
                                    text = viewModel.errorMessage,
                                    color = MaterialTheme.colorScheme.error,
                                    fontSize = 14.sp
                                )
                            } else if (viewModel.successMessage != "") {
                                Spacer(modifier = Modifier.height(5.dp))
                                Text(
                                    text = viewModel.successMessage,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontSize = 14.sp
                                )
                            }
                        }

                    }
                    Spacer(modifier = Modifier.height(5.dp))
                    if (viewModel.incomingRequests.isNotEmpty()){
                        Text(text = "Incoming Requests")
                        LazyColumn {
                            items (viewModel.incomingRequests){
                                ChatPreview(friend = it, lastMessage = null, onClick = {}, request = {
                                    IconButton(onClick = { viewModel.acceptFriendRequest(it.id) }) {
                                        Icon(Icons.Default.Check, null, tint = MaterialTheme.colorScheme.primary)
                                    }
                                    Spacer(modifier = Modifier.width(5.dp))
                                    IconButton(onClick = { viewModel.rejectFriendRequest(it.id) }) {
                                        Icon(Icons.Default.Close, null, tint = MaterialTheme.colorScheme.error)
                                    }
                                })
                            }
                        }
                    }
                    Text(text = "Friends", fontSize = 20.sp)
                    LazyColumn {
                        items(items = viewModel.friends) {
                            ChatPreview(friend = it, lastMessage = null, onClick =  {
                                viewModel.updateErrorMessage("")
                                viewModel.updateSuccessMessage("")
                                viewModel.updateSelectedFriend(it)
                                bottomBarState.value = false
                                viewModel.updateFriendChatVisible(true)
                            })
                        }
                    }
                }
            }
        }
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
                viewModel.updateShowReportFriendDialog(false)
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
            confirmButtonText = "Report",
            cancelButtonText = "Cancel"
        )
    }
}

@Composable
fun RemoveFriendDialog(viewModel: FriendsScreenViewModel){
    if (viewModel.showRemoveFriendDialog){
        MMDialog(
            showDialog = viewModel.showRemoveFriendDialog,
            title = "Are you sure?",
            onConfirm = {
                viewModel.updateShowRemoveFriendDialog(false)
                viewModel.removeFriend(viewModel.selectedFriend?.id ?: -1){
                    viewModel.refreshChats()
                }
                viewModel.updateFriendChatVisible(false)
            },
            onDismissRequest = {
                viewModel.updateShowRemoveFriendDialog(false)
            },
            body = { Text("Are you sure you want to permanently remove your friend ${viewModel.selectedFriend?.username}?") })
    }
}
