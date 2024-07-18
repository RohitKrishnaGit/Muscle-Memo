package com.cs346.musclememo.screens.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cs346.musclememo.classes.Friend

@Composable
fun FriendCard(friend: Friend) {
    Card(
        modifier = Modifier.padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = friend.fullName, fontSize = 20.sp)
            Text(text = friend.username)
            Text(text = friend.email)
        }
    }
}