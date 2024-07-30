package com.cs346.musclememo.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.PersonRemove
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cs346.musclememo.classes.Friend
import com.cs346.musclememo.screens.viewmodels.FriendsScreenViewModel
import com.cs346.musclememo.screens.viewmodels.Message
import com.cs346.musclememo.utils.epochToDate
import kotlin.math.max
import kotlin.math.min

@Composable
fun ChatPreview(
    friend: Friend,
    lastMessage: Message?,
    onClick: () -> Unit = {},
    request: @Composable () -> Unit = {}
) {
    val modifier = if (onClick == {}) Modifier else Modifier.clickable {
        onClick()
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        DisplayProfilePicture(model = friend.profilePicture, size = 50.dp)
        Spacer(modifier = Modifier.width(10.dp))
        if (lastMessage != null) {
            Column(
                modifier = Modifier.fillMaxHeight()
            ) {
                val message = (lastMessage.sender.username + ": " + lastMessage.message).replace("\n", " ")
                val maxLength = min(message.length, 30)
                Text(text = friend.username)
                Text(
                    text = message.substring(0, maxLength).plus(if (maxLength == 30) "..." else ""),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp
                )
            }
        } else {
            Column(
                modifier = Modifier.fillMaxHeight()
            ) {
                Text(text = friend.username)
                Text(
                    text = friend.gender,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        request()
    }
}

@Composable
fun ChatHeader(
    onBackPressed: () -> Unit = {},
    profilePicture: Any? = null,
    username: String,
    gender: String? = null,
    report: () -> Unit = {},
    remove: () -> Unit = {},
    reportable: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .background(MaterialTheme.colorScheme.primaryContainer),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackPressed) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
        }
        Spacer(modifier = Modifier.width(10.dp))
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            if (profilePicture != null){
                DisplayProfilePicture(model = profilePicture, size = 50.dp)
            }
            Spacer(modifier = Modifier.width(15.dp))
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = username,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontSize = 20.sp
                )
                if (gender != null)
                    Text(
                        text = gender,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontSize = 12.sp
                    )
            }
            Spacer(modifier = Modifier.weight(1f))
            if (reportable){
                IconButton(onClick = report) {
                    Icon(Icons.Default.Flag, null, tint = MaterialTheme.colorScheme.error)
                }
                Spacer(modifier = Modifier.width(5.dp))
            }
            IconButton(
                onClick = remove
            ) {
                Icon(Icons.Default.PersonRemove, null, tint = MaterialTheme.colorScheme.error)
            }
            Spacer(modifier = Modifier.width(5.dp))
        }
    }
}

@Composable
fun ChatBubble(
    message: Message,
    lastMessageUser: String
) {
    val diffUser = lastMessageUser != message.sender.username
    val modifier = Modifier

    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        if (diffUser)
            DisplayProfilePicture(model = message.sender.profilePicture, size = 50.dp)
        else
            Spacer(modifier = Modifier.width(50.dp))
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.height(IntrinsicSize.Min)) {
            if (diffUser) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(text = message.sender.username)
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = epochToDate(message.timestamp, time = true),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 9.sp
                    )
                }
            }
            Text(text = message.message)
        }

    }
}


@Composable
fun IncomingFriendRequestCard(
    friend: Friend,
    viewModel: FriendsScreenViewModel,
    requestIndex: Int
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = friend.username, fontSize = 20.sp)
            Text(text = friend.email)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        viewModel.acceptFriendRequest(friend.id)
                        viewModel.removeIncomingFriendRequest(requestIndex)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Accept", color = MaterialTheme.colorScheme.onPrimary)
                }
                Button(
                    onClick = {
                        println("reject friend request")
                        viewModel.rejectFriendRequest(friend.id)
                        viewModel.removeIncomingFriendRequest(requestIndex)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Reject", color = MaterialTheme.colorScheme.onError)
                }
            }
        }
    }
}
