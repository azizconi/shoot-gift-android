import com.example.myapplication.data.local.entity.news.ArticleEntity
import com.google.gson.annotations.SerializedName

data class NewsResponse (
	@SerializedName("status") val status : String,
	@SerializedName("totalResults") val totalResults : Int,
	@SerializedName("articles") val articleEntities : List<ArticleEntity>
)