package com.example.vivacventuresmobile.domain.usecases

import com.example.vivacventuresmobile.data.repositories.FriendsRepository
import javax.inject.Inject

class GetFriendsUseCase @Inject constructor(private val repository: FriendsRepository){
    operator fun invoke(username: String) = repository.getFriends(username)
}