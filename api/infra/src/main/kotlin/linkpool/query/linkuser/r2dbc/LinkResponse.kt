package linkpool.query.linkuser.r2dbc
import java.time.LocalDateTime

data class LinkResponse(
    val id: Long,
    val url: String,
    val title: String? = null,
    val image: String? = null,
    val folderId: Long? = null,
    val describe: String? = null,
    val createdDateTime: LocalDateTime,
)