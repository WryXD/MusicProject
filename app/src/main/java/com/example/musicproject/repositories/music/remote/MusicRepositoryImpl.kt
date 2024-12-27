package com.example.musicproject.repositories.music.remote

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class MusicRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val fireStore: FirebaseFirestore,
) : MusicRepository {
    override suspend fun addLikedSong(
        songId: String,
        songTitle: String,
        songArtist: String,
        songAlbumCoverUrl: String,
    ): Result<Unit> {
        return try {
            val userId = firebaseAuth.currentUser?.uid
                ?: return Result.failure(Exception("User not logged in"))

            val songData = mapOf(
                "songId" to songId,
                "songTitle" to songTitle,
                "songArtist" to songArtist,
                "songAlbumCoverUrl" to songAlbumCoverUrl,
            )

            fireStore
                .collection("users")
                .document(userId)
                .collection("likedSongs")
                .add(songData)
                .await()

            Log.d("AddLikedSong", "Song added successfully")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("AddLikedSong", "Failed to add song: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun getLikedSongs(): Result<List<Song>> {
        return try {
            val userId =
                firebaseAuth.currentUser?.uid ?: throw IllegalStateException("User not logged in")

            val querySnapshot = fireStore
                .collection("user")
                .document(userId)
                .collection("likedSongs")
                .get()
                .await()
            val songsList = querySnapshot?.documents?.mapNotNull { document ->
                document.toObject(Song::class.java)
            } ?: emptyList()
            Log.d("GetLikedSongs", "Songs retrieved successfully")
            Result.success(songsList)
        } catch (e: Exception) {
            Log.e("GetLikedSongs", "Failed to retrieve songs: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun getLikedSongsStream(): Flow<Result<List<Song>>> {
        return callbackFlow {
            val userId =
                firebaseAuth.currentUser?.uid ?: throw IllegalStateException("User not logged in")

            val listenerRegistration: ListenerRegistration = fireStore
                .collection("users")
                .document(userId)
                .collection("likedSongs")
                .addSnapshotListener { snapshot, exception ->
                    if (exception != null) {
                        trySend(Result.failure(exception))
                        return@addSnapshotListener
                    }

                    val songsList = snapshot?.documents?.mapNotNull { document ->
                        document.toObject(Song::class.java)
                    } ?: emptyList()
                    Log.e("GetLikedSongsUpdated", "Songs retrieved successfully: $songsList")
                    trySend(Result.success(songsList))
                }

            awaitClose {
                listenerRegistration.remove()
            }
        }
    }


    override suspend fun createPlayList(
        playListName: String,
        song: List<String>,
    ): Result<Unit> {
        return try {
            val userId =
                firebaseAuth.currentUser?.uid ?: throw IllegalStateException("User not logged in")

            val documentRef = fireStore
                .collection("users")
                .document(userId)
                .collection("playList")
                .document()

            val playListId = documentRef.id

            val playList = mapOf(
                "playListName" to playListName,
                "song" to song,
                "playListId" to playListId
            )

            documentRef.set(playList).await()

            Log.e("AddPlayList", "PlayList added successfully")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("AddPlayList", "Failed to add playList: ${e.message}")
            Result.failure(e)
        }

    }

    override suspend fun getPlayList(): Result<List<PlayList>> {
        return try {
            val userId =
                firebaseAuth.currentUser?.uid ?: throw IllegalStateException("User not logged in")

            val querySnapshot = fireStore
                .collection("users")
                .document(userId)
                .collection("playList")
                .get()
                .await()

            val playLists = querySnapshot.documents.mapNotNull { document ->
                document.toObject(PlayList::class.java)
            }

            Log.d("GetPlayList", "PlayList retrieved successfully: $playLists")
            Result.success(playLists)
        } catch (e: Exception) {
            Log.e("GetPlayList", "Failed to retrieve playList: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun deletedPlayList(id: String): Flow<Result<Unit>> = flow {
        try {
            val userId = firebaseAuth.currentUser?.uid
                ?: throw IllegalStateException("User not logged in")

            // Thực hiện xóa tài liệu với ID được cung cấp
            fireStore
                .collection("users")
                .document(userId)
                .collection("playList")
                .document(id)
                .delete()
                .await()
            Log.d("DeletePlayList", "PlayList with ID $id deleted successfully")
            emit(Result.success(Unit))
        } catch (e: Exception) {
            Log.e("DeletePlayList", "Failed to delete playList: ${e.message}")
            emit(Result.failure(e))
        }
    }

    override suspend fun getPlayListStream(): Flow<Result<List<PlayList>>> = callbackFlow {
        val userId =
            firebaseAuth.currentUser?.uid ?: throw IllegalStateException("User not logged in")
        val listenerRegistration: ListenerRegistration = fireStore
            .collection("users")
            .document(userId)
            .collection("playList")
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    Log.e("PlayListUpdated", "Failed to retrieve playList: ${exception.message}")
                    trySend(Result.failure(exception))
                    return@addSnapshotListener
                }
                val playList = snapshot?.documents?.mapNotNull { document ->
                    document.toObject(PlayList::class.java)
                } ?: emptyList()
                Log.e("PlayListUpdated", "PlayList retrieved successfully: $playList")
                trySend(Result.success(playList))
            }
        awaitClose {
            listenerRegistration.remove()
        }

    }
}
