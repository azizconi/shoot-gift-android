package com.example.myapplication.presentation.ui.news.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.dao.NewsDao
import com.example.myapplication.data.local.entity.news.ArticleEntity
import com.example.myapplication.domain.repository.NewsRepository
import com.example.myapplication.presentation.ui.news.viewModel.state.NewsState
import com.example.myapplication.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val newsDao: NewsDao
): ViewModel() {

    val getNewsFromDB = newsDao.getNews()

    private val _newsState = MutableLiveData(NewsState())
    val newsState = _newsState

    init {
        getNews()
    }

    private fun getNews() {
        newsRepository.getNews().onEach { result ->
            when(result) {
                is Resource.Success -> {
                    Log.e("TAG", "getNews: Resource.Success", )
                    _newsState.value = newsState.value?.copy(
                        isLoading = false,
                        data = result.data?.articleEntities
                    )
                    addNewsToDB(_newsState.value?.data!!)
                }
                is Resource.Error -> {
                    Log.e("TAG", "getNews: Resource.Error", )
                    if (getNewsFromDB.value != null) {
                        _newsState.value = newsState.value?.copy(
                            isLoading = false,
                            data = getNewsFromDB.value
                        )
                    } else {
                        _newsState.value = newsState.value?.copy(isLoading = false, data = null)
                    }
                }
                is Resource.Loading -> {
                    _newsState.value = newsState.value?.copy(isLoading = true, data = null)
                }
            }
        }.launchIn(viewModelScope)
    }


    private fun addNewsToDB(list: List<ArticleEntity>) = viewModelScope.launch {
        newsDao.addNews(list)
    }


}