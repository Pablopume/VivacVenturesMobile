package com.example.vivacventuresmobile.domain.usecases

import com.example.vivacventuresmobile.data.repositories.FriendsRepository
import javax.inject.Inject

class SearchFriendUseCase @Inject constructor(private val repository: FriendsRepository){
    operator fun invoke(username: String) = repository.getAmigo(username)
}