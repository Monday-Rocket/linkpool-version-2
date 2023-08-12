package linkpool.query.userfolder.r2dbc

import java.time.LocalDateTime

data class UserFolderListResult(
  val id: Long? = null,
  val name: String,
  val thumbnail: String? = null,
  val visible: Boolean? = false,
  val linkCount: Int,
  val createdDateTime: LocalDateTime? = null,
)