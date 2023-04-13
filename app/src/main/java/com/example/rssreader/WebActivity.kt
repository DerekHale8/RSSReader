package com.example.rssreader

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.*
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast

class WebActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
        val activity=this
        getIntents(activity)
    }

    fun getIntents(context:Context){
        val webView:WebView=findViewById(R.id.webview)
        val textViewErrorConnection: TextView =findViewById(R.id.text_view_error_connection)
        val progressBar:ProgressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.VISIBLE

        val intent=intent

        webView.settings.javaScriptEnabled = true
        webView.settings.builtInZoomControls=true
        webView.settings.setSupportZoom(true)

        try {
            webView.loadUrl(intent.getStringExtra("ArticleURL")!!)
        }catch (exception:java.lang.Exception){
            Toast.makeText(context,exception.toString(),Toast.LENGTH_LONG).show()
        }
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                progressBar.visibility = View.GONE
            }

            override fun onScaleChanged(view: WebView?, oldScale: Float, newScale: Float) {
                super.onScaleChanged(view, oldScale, newScale)
            }



            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                textViewErrorConnection.visibility= View.VISIBLE
                request.toString()
                Toast.makeText(context,error.toString(),Toast.LENGTH_LONG).show()
                progressBar.visibility = View.GONE
            }
            override fun onReceivedHttpError(view: WebView?, request: WebResourceRequest?, errorResponse: WebResourceResponse?) {
                textViewErrorConnection.visibility= View.VISIBLE
                Toast.makeText(applicationContext, "Error loading page", Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.GONE
            }
        }


    }


}