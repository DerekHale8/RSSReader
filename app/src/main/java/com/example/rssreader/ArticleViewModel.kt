package com.example.rssreader

import android.util.Log
import androidx.lifecycle.*
import com.example.rssreader.repository.RSSRepository
import com.prof.rssparser.Article
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import okhttp3.internal.EMPTY_BYTE_ARRAY

class ArticleViewModel(val repository:RSSRepository):ViewModel(){

    fun getArticles(url:String):LiveData<List<Article>>{
        val result = repository.getArticles(url).asLiveData()
        Log.d("DebugLog","RESULT"+result.value?.size.toString())
        return result

    }

}



class ArticleViewModelFactory(val repository:RSSRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ArticleViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ArticleViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}