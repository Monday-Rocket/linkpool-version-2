package linkpool.link.link.model

import java.time.LocalDateTime

class Link(
    val id: Long = 0L,
    val creatorId: Long,
    var folderId: Long? = null,
    var url: String,
    var title: String? = null,
    var image: String? = null,
    var describe: String? = null,
    var inflowType: InflowType,
    val createdDateTime: LocalDateTime = LocalDateTime.now(),
    var modifiedDateTime: LocalDateTime = LocalDateTime.now(),
) {

    private var deleted: Boolean = false

    fun delete() {
        deleted = true
    }

    fun isDeleted()
    = deleted

    fun isOwner(userId: Long)
        = id == userId

    fun updateUrl(url: String){
        this.url = url
    }

    fun updateTitle(title: String?){
        this.title = title
    }

    fun updateDescribe(describe: String?){
        this.describe = describe
    }

    fun updateImage(image: String?){
        this.image = image
    }



}
