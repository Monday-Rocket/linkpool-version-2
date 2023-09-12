package linkpool.link.port.`in`

import linkpool.link.model.InflowType
import java.time.LocalDateTime

data class SaveLinkWithFolderNameRequest(
    val url: String,
    val title: String? = null,
    val describe: String? = null,
    val image: String? = null,
    val folderName: String? = null,
    val createdAt: LocalDateTime? = null
)

data class SaveLinkRequest(
    val url: String,
    val title: String? = null,
    val describe: String? = null,
    val image: String? = null,
    val folderId: Long? = null,
    val inflowType: InflowType? = null,
    val createdAt: LocalDateTime? = null
)

data class UpdateLinkRequest(
    val url: String? = null,
    val title: String? = null,
    val describe: String? = null,
    val image: String? = null,
    val folderId: Long? = null,
)