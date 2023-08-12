package linkpool.link.port.`in`

import linkpool.jobgroup.model.JobGroup
import linkpool.link.model.InflowType
import linkpool.link.model.Link
import linkpool.user.model.User
import linkpool.user.port.`in`.UserResponse
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


data class FolderLinkCountResponse(
    val folderId: Long,
    val linkCount: Int
)
data class LinkResponse(
    val id: Long,
    val url: String,
    val title: String? = null,
    val image: String? = null,
    val folderId: Long? = null,
    val describe: String? = null,
    val createdDateTime: LocalDateTime,
)

data class LinkWithUserResponse(
    val id: Long,
    val user: UserResponse,
    val url: String,
    val title: String? = null,
    val image: String? = null,
    val folderId: Long? = null,
    val describe: String? = null,
    val createdDateTime: LocalDateTime,
) {
    constructor(user: User, jobGroup: JobGroup, link: Link) : this(
        link.id,
        UserResponse(user, jobGroup),
        link.url,
        link.title,
        link.image,
        link.folderId,
        link.describe,
        link.createdDateTime
    )
}