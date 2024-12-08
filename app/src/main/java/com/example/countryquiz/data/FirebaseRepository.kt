package com.example.countryquiz.data

import com.example.countryquiz.model.ScoreTable
import com.example.countryquiz.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {

    suspend fun signUp(email: String, username: String, password: String): Result<String> {
        return try {
            val checkUsername = checkUsername(username)
            if (checkUsername.getOrNull() == true) {
                return Result.failure(Exception("Username already exists!"))
            }
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val id = authResult.user?.uid ?: throw Exception("id null")
            val user = User(id, email, username, password)
            firestore.collection("users").document(id).set(user).await()
            Result.success("Successfully registered !")

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signIn(email: String, password: String): Result<String> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            Result.success("Login Successful !")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun checkUsername(username: String): Result<Boolean> {
        return try {
            val check =
                firestore.collection("users").whereEqualTo("username", username).get().await()
            if (!check.isEmpty) {
                Result.success(true)
            } else {
                Result.success(false)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun signOut(): Result<String> {
        return try {
            auth.signOut()
            Result.success("Exit Successful")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    suspend fun addScore(score: Int) {
        val id = auth.currentUser?.uid
        val userRef = firestore.collection("users").document(id!!)

        try {
            val snapshot = userRef.get().await()
            val currentScore = snapshot.getLong("score") ?: -1
            if (score > currentScore.toInt()) {
                userRef.set(mapOf("score" to score), SetOptions.merge()).await()
                println("Score Update")
            } else if (currentScore.toInt() == -1) {
                userRef.set(mapOf("score" to score), SetOptions.merge()).await()
                println("Score Add")
            }
        } catch (e: Exception) {
            println("Error: ${e.message}")
        }
    }

    suspend fun getAllScore(): List<ScoreTable> {
        val userRef = firestore.collection("users")
        return try {
            val snapshot = userRef.get().await()
            val userScores = snapshot.documents.mapNotNull { document ->
                val id = document.getString("id")
                val username = document.getString("username")
                val score = document.getLong("score")?.toInt()
                if (id != null && username != null && score != null) {
                    ScoreTable(id, username, score)
                } else {
                    null
                }
            }
            userScores.sortedByDescending { it.score }
        } catch (e: Exception) {
            println("Error: ${e.message}")
            emptyList()
        }
    }
}