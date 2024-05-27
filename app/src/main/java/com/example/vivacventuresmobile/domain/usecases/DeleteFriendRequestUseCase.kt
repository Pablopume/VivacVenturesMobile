package com.example.vivacventuresmobile.domain.usecases

import com.example.vivacventuresmobile.data.repositories.FriendsRepository
import com.example.vivacventuresmobile.domain.modelo.FriendRequest
import javax.inject.Inject

class DeleteFriendRequestUseCase @Inject constructor(private val repository: FriendsRepository){
    operator fun invoke(friendRequest: FriendRequest) = repository.deleteFriend(friendRequest)
}