package jp.co.yumemi.android.code_check.features.screen.bookmark

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.*

data class BookMarkUIState(
    val listPage: List<String> = emptyList(),
    var loading : Boolean = false
)

class BookMarkViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(BookMarkUIState())
    val uiState: StateFlow<BookMarkUIState> = _uiState.asStateFlow()

    init {
        getListPage()
    }

    private fun getListPage() {
        _uiState.update {
            it.copy(loading = true)
        }
        FirebaseAuth.getInstance().currentUser?.let {
            val myRef = FirebaseDatabase.getInstance().getReference(it.uid).child("book_mark")
            myRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    try {
                        val list :MutableList<String> = mutableListOf()
                        for (data in dataSnapshot.children) {
                            list.add(data.value.toString())
                        }
                        _uiState.update {
                            it.copy(listPage = list,loading = false)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
        }
    }
}