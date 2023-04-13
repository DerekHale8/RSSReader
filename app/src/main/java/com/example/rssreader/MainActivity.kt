package com.example.rssreader

import ArticleListAdapter
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.Menu
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.rssreader.repository.RSSRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {


    private val articleViewModel: ArticleViewModel by viewModels {
        ArticleViewModelFactory((application as RSSApplication).repository)
    }


    val adapter = ArticleListAdapter(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.adapter=adapter

        //val url="https://habr.com/ru/rss/all/all/?fl=ru"
        val mainActivity=this

        Log.d("DebugLog","LIST"+adapter.currentList.size)

        val searchView: SearchView =findViewById(R.id.search_view_title)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if(!isNetworkAvailable(mainActivity)){
                    Toast.makeText(mainActivity,"No Network Connection!", Toast.LENGTH_LONG).show()
                }else{
                    val url=query
                    articleViewModel.getArticles(url!!).observe(mainActivity){
                            articles->articles.let{adapter.submitList(it)}
                        Log.d("DebugLog","SIZE"+articles.size.toString())
                    }
                }

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                return true
            }

        })
    }

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
            return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || capabilities.hasTransport(
                NetworkCapabilities.TRANSPORT_CELLULAR)
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }
    }




}