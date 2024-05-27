package com.example.vivacventuresmobile.ui.screens.myfriends

import com.example.vivacventuresmobile.domain.modelo.FriendRequest

sealed class MyFriendsEvent {
    object GetFriends : MyFriendsEvent()
    class SaveUsername(val username: String) : MyFriendsEvent()
    class AcceptFriendRequest(val friendRequest: FriendRequest) : MyFriendsEvent()
    class RejectFriendRequest(val friendRequest: FriendRequest) : MyFriendsEvent()
    class DeleteFriendRequest(val friendRequest: FriendRequest) : MyFriendsEvent()
    object ErrorVisto : MyFriendsEvent()
}