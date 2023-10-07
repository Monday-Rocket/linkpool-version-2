package linkpool.link.folder.model

import linkpool.link.folder.port.`in`.UpdateFolderRequest
import java.time.LocalDateTime

class Folder(
  val id: Long? = null,
  val ownerId: Long,
  var name: String,
  var visible: Boolean = false,
  var thumbnail: Thumbnail? = null,
  val createdDateTime: LocalDateTime = LocalDateTime.now(),
  var modifiedDateTime: LocalDateTime = LocalDateTime.now(),
) {
  private var deleted: Boolean = false

  fun update(request: UpdateFolderRequest): Folder {
    name = request.name ?: ""
    visible = request.visible ?: true
    return this
  }

  fun delete() {
    deleted = true
  }

  fun isDeleted(): Boolean {
    return deleted
  }

  fun isOwner(userId: Long): Boolean =
    this.ownerId == userId
}