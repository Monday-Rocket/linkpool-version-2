package linkpool.link.folder.port.`in`

import java.time.LocalDateTime


data class SaveFolderRequest(
    val name: String,
    val visible: Boolean? = false,
    val createdAt: LocalDateTime? = null
)

data class UpdateFolderRequest(
    val name: String? = null,
    val visible: Boolean? = null,
)

data class GetByUserIdResponse(
    val id: Long? = null,
    val name: String,
    val thumbnail: String? = null,
    val visible: Boolean? = false,
    val links: Int,
    val createdDateTime: LocalDateTime? = null,
)