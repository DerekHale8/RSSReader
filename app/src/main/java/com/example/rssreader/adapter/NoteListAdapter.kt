import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rssreader.MainActivity
import com.example.rssreader.R
import com.example.rssreader.WebActivity
import com.prof.rssparser.Article
import java.net.URL


class ArticleListAdapter(private val context: Context) : ListAdapter<Article, ArticleListAdapter.ArticleViewHolder>(WordsComparator()) {

    class ArticleViewHolder(itemView: View,val contextViewHolder:Context) : RecyclerView.ViewHolder(itemView) {
        val textViewTitle:TextView=itemView.findViewById(R.id.text_view_title)
        val imageViewArticleImage:ImageView=itemView.findViewById(R.id.image_view_article_image)

        fun bind(title: String?,imageUrl: String?) {
            textViewTitle.text = title
            if(imageUrl==null){
                imageViewArticleImage.visibility= View.GONE
            }else{
                Glide.with(contextViewHolder)
                    .load(imageUrl)
                    .into(imageViewArticleImage)
                imageViewArticleImage.visibility= View.VISIBLE
            }


        }

        companion object {
            fun create(parent: ViewGroup,contextViewHolder: Context): ArticleViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.article_item, parent, false)
                return ArticleViewHolder(view,contextViewHolder)
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder.create(parent,context)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val current = currentList[position]
        holder.bind(current.title,current.image)

        holder.itemView.setOnClickListener {

            if (!isNetworkAvailable(context)) {
                Toast.makeText(context,"No Network Connection!",Toast.LENGTH_LONG).show()
            }else{
                    Toast.makeText(context,"Have Network Connection!",Toast.LENGTH_LONG).show()
                val intent=Intent(holder.contextViewHolder,WebActivity::class.java).apply {
                    putExtra("ArticleURL",current.link)
                }
                holder.contextViewHolder.startActivity(intent)
            }

        }

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






    class WordsComparator : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.title == newItem.title
        }
    }
}