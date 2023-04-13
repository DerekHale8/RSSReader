package com.example.rssreader.repository

import android.util.Log
import androidx.annotation.WorkerThread
import com.prof.rssparser.Article
import com.prof.rssparser.Parser
import kotlinx.coroutines.flow.*

class RSSRepository {

    fun getArticles(url: String): Flow<List<Article>> = flow {
        val parser = Parser.Builder()
            .cacheExpirationMillis(24L * 60L * 60L * 1000L) // one day
            .build()
        val channel = parser.getChannel(url)
        emit(channel.articles)
    }
}

