package jp.co.yumemi.android.code_check.features.screen.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseError
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import jp.co.yumemi.android.code_check.data.model.Item
import jp.co.yumemi.android.code_check.data.repository.GithubRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.google.firebase.database.DatabaseError

import com.google.firebase.database.DataSnapshot

import com.google.firebase.database.ValueEventListener

import com.google.firebase.database.FirebaseDatabase

import com.google.firebase.database.DatabaseReference
import java.util.*


/**
 * UI state for the Detail screen
 */
data class DetailUiState(
    val isBookMark: Boolean = false,
)

class DetailViewModel(private val repository: GithubRepositoryImpl) : ViewModel() {
    private var _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    fun loadData() {

    }

    fun bookMarkGithubPage(url: String) {
        val dbRef: DatabaseReference = FirebaseDatabase.getInstance().reference
        FirebaseAuth.getInstance().currentUser?.let {
            dbRef.child(it.uid).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    try {
                        if (snapshot.hasChild("book_mark")) {
                            val update = snapshot.child("book_mark").value as MutableMap<String, Any>
                            update[UUID.randomUUID().toString()] = url
                            dbRef.child(it.uid).child("book_mark").updateChildren(update).addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    _uiState.update {
                                        it.copy(isBookMark = true)
                                    }
                                } else {
                                    _uiState.update {
                                        it.copy(isBookMark = false)
                                    }
                                }
                            }
                        } else {
                            val update: MutableMap<String, Any> = hashMapOf()
                            update["${it.uid}/book_mark/${UUID.randomUUID()}"] = url
                            dbRef.updateChildren(update).addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    _uiState.update {
                                        it.copy(isBookMark = true)
                                    }
                                } else {
                                    _uiState.update {
                                        it.copy(isBookMark = false)
                                    }
                                }
                            }
                        }
                    } catch (e: Exception) {
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })

        } ?: kotlin.run {
            _uiState.update {
                it.copy(isBookMark = false)
            }
        }

    }

    fun checkBookMark(url: String) {
        try {
            val database = FirebaseDatabase.getInstance()
            FirebaseAuth.getInstance().currentUser?.let {
                val myRef = database.getReference(it.uid).child("book_mark")
                myRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        try {
                            var isHas = false
                            for (data in dataSnapshot.children) {
                                if (data.value== url) {
                                    isHas = true
                                    break
                                }
                            }
                            _uiState.update {
                                it.copy(isBookMark = isHas)
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }

                })
            }

        } catch (e: Exception) {
            _uiState.update {
                it.copy(isBookMark = false)
            }
        }
    }
}