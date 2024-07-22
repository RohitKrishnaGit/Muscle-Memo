package com.cs346.musclememo.screens.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import com.cs346.musclememo.classes.Friend
import com.cs346.musclememo.screens.viewmodels.FriendsScreenViewModel

@Composable
fun FriendCard(friend: Friend, viewModel: FriendsScreenViewModel, idx: Int, onClick: (Friend) -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onClick(friend) }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = friend.username, fontSize = 20.sp)
            Text(text = friend.email)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = {
                        viewModel.removeFriend(friend.id)
                        viewModel.removeFriendIdx(idx)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Delete", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun IncomingFriendRequestCard(friend: Friend, viewModel: FriendsScreenViewModel, requestIndex: Int) {
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
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Green)
                ) {
                    Text("Accept", color = Color.White)
                }
                Button(
                    onClick = {
                        println("reject friend request")
                        viewModel.rejectFriendRequest(friend.id)
                        viewModel.removeIncomingFriendRequest(requestIndex)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Reject", color = Color.White)
                }
            }
        }
    }
}

