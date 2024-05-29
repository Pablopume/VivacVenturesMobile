package com.example.vivacventuresmobile.data.sources.remote

import com.example.vivacventuresmobile.data.model.toListFavs
import com.example.vivacventuresmobile.data.model.toListResponse
import com.example.vivacventuresmobile.domain.modelo.ListFavs
import com.example.vivacventuresmobile.utils.NetworkResult
import javax.inject.Inject

class ListsRemoteDataSource @Inject constructor(
    private val listsService: ListsService
) {
    suspend fun getLists(username: String): NetworkResult<List<ListFavs>> {
        try {
            val response = listsService.getLists(username)
            if (response.isSuccessful) {
                val body = response.body()
                body?.let {
                    return NetworkResult.Success(body.map { it.toListFavs() })
                }
            } else {
                return NetworkResult.Error("${response.code()} ${response.errorBody()}")
            }

        } catch (e: Exception) {
            return NetworkResult.Error(e.message ?: e.toString())
        }
        return NetworkResult.Error("Error")
    }

    suspend fun getList(id: Int): NetworkResult<ListFavs> {
        try {
            val response = listsService.getList(id)
            if (response.isSuccessful) {
                val body = response.body()
                body?.let {
                    return NetworkResult.Success(body.toListFavs())
                }
            } else {
                return NetworkResult.Error("${response.code()} ${response.errorBody()}")
            }
        } catch (e: Exception) {
            return NetworkResult.Error(e.message ?: e.toString())
        }
        return NetworkResult.Error("Error")
    }

    suspend fun getListsByUserAndVivacPlace(username: String, id: Int): NetworkResult<List<ListFavs>> {
        try {
            val response = listsService.getListsByUserAndVivacPlace(username, id)
            if (response.isSuccessful) {
                val body = response.body()
                body?.let {
                    return NetworkResult.Success(body.map { it.toListFavs() })
                }
            } else {
                return NetworkResult.Error("${response.code()} ${response.errorBody()}")
            }
        } catch (e: Exception) {
            return NetworkResult.Error(e.message ?: e.toString())
        }
        return NetworkResult.Error("Error")
    }

    suspend fun getListSharedWith(id: Int): NetworkResult<List<String>> {
        try {
            val response = listsService.getWhoIsListShareWith(id)
            if (response.isSuccessful) {
                val body = response.body()
                body?.let {
                    return NetworkResult.Success(body)
                }
            } else {
                return NetworkResult.Error("${response.code()} ${response.errorBody()}")
            }
        } catch (e: Exception) {
            return NetworkResult.Error(e.message ?: e.toString())
        }
        return NetworkResult.Error("Error")
    }

    suspend fun saveList(listFavs: ListFavs): NetworkResult<Unit> {
        return try {
            val response = listsService.saveList(listFavs.toListResponse())
            if (response.isSuccessful) {
                NetworkResult.Success(Unit)
            } else {
                NetworkResult.Error("${response.code()} ${response.errorBody()}")
            }
        } catch (e: Exception) {
            NetworkResult.Error(e.message ?: e.toString())
        }
    }

    suspend fun shareList(id: Int, username: String): NetworkResult<Unit> {
        return try {
            val response = listsService.shareList(id, username)
            if (response.isSuccessful) {
                NetworkResult.Success(Unit)
            } else {
                NetworkResult.Error("${response.code()} ${response.errorBody()}")
            }
        } catch (e: Exception) {
            NetworkResult.Error(e.message ?: e.toString())
        }
    }

    suspend fun deleteSharedList(id: Int, username: String): NetworkResult<Unit> {
        return try {
            val response = listsService.deleteSharedList(id, username)
            if (response.isSuccessful) {
                NetworkResult.Success(Unit)
            } else {
                NetworkResult.Error("${response.code()} ${response.errorBody()}")
            }
        } catch (e: Exception) {
            NetworkResult.Error(e.message ?: e.toString())
        }
    }

    suspend fun deleteList(id: Int): NetworkResult<Unit> {
        return try {
            val response = listsService.deleteList(id)
            if (response.isSuccessful) {
                NetworkResult.Success(Unit)
            } else {
                NetworkResult.Error("${response.code()} ${response.errorBody()}")
            }
        } catch (e: Exception) {
            NetworkResult.Error(e.message ?: e.toString())
        }
    }

    suspend fun addFavoriteToList(id: Int, vivacId: Int): NetworkResult<Unit> {
        return try {
            val response = listsService.addFavoriteToList(id, vivacId)
            if (response.isSuccessful) {
                NetworkResult.Success(Unit)
            } else {
                NetworkResult.Error("${response.code()} ${response.errorBody()}")
            }
        } catch (e: Exception) {
            NetworkResult.Error(e.message ?: e.toString())
        }
    }

    suspend fun removeFavoriteFromList(id: Int, vivacId: Int): NetworkResult<Unit> {
        return try {
            val response = listsService.removeFavoriteFromList(id, vivacId)
            if (response.isSuccessful) {
                NetworkResult.Success(Unit)
            } else {
                NetworkResult.Error("${response.code()} ${response.errorBody()}")
            }
        } catch (e: Exception) {
            NetworkResult.Error(e.message ?: e.toString())
        }
    }
}