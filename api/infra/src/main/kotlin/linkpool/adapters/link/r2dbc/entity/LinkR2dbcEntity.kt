package linkpool.adapters.link.r2dbc.entity

import linkpool.link.model.InflowType
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table(name = "link")
class LinkR2dbcEntity(
    @Id
    @Column
    val id: Long = 0L,

    @Column
    val userId: Long,

    @Column
    var folderId: Long? = null,

    @Column
    var url: String,

    @Column
    var title: String? = null,

    @Column
    var image: String? = null,

    @Column
    var describe: String? = null,

    @Column
    var inflowType: Int,

    @CreatedDate
    @Column
    val createdDateTime: LocalDateTime = LocalDateTime.now(),
){
    @LastModifiedDate
    @Column("modified_date_time")
    lateinit var modifiedDateTime: LocalDateTime
        private set
    @Column
    var deleted: Boolean = false
        private set
}
