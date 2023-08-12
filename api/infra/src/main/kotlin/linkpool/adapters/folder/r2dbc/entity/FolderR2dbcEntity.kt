package linkpool.adapters.folder.r2dbc.entity

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.relational.core.mapping.Column
import java.time.LocalDateTime

@Table("folder")
class FolderR2dbcEntity(

    @Id
    @Column
    var id: Long? = null,

    @Column
    val userId: Long,

    @Column
    val name: String,

    @Column
    val visible: Boolean,

    @Column
    var image: String? = null,

    @Column
    val deleted: Boolean = false,
) {

    @CreatedDate
    @Column
    var createdDateTime: LocalDateTime = LocalDateTime.now()
        private set

    @LastModifiedDate
    @Column
    var modifiedDateTime: LocalDateTime = LocalDateTime.now()
        private set
}

