package com.example.rssreader

import android.app.Application
import com.example.rssreader.repository.RSSRepository
import com.prof.rssparser.Article
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class RSSApplication:Application() {
    val repository by lazy { RSSRepository() }
}