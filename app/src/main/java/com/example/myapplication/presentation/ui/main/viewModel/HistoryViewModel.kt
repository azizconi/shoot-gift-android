package com.example.myapplication.presentation.ui.main.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.dao.HistoryDao
import com.example.myapplication.data.local.entity.history.HistoryEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val historyDao: HistoryDao
): ViewModel() {

    val histories = historyDao.getHistories()

    fun addHistory(historyEntity: HistoryEntity) = viewModelScope.launch {
        historyDao.addHistory(historyEntity)
    }
}