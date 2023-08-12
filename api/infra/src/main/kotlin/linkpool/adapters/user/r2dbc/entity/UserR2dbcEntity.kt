package linkpool.adapters.user.r2dbc.entity

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.relational.core.mapping.Column
import java.time.LocalDateTime

@Table("user")
class UserR2dbcEntity(

    @Id
    @Column
    val id: Long = 0L,

    @Column
    val uid: String,

    @Column
    var nickname: String?,

    @Column
    var jobGroupId: Long?,

    @Column
    var profileImage: String? = null,

    @CreatedDate
    @Column
    val createdDateTime: LocalDateTime = LocalDateTime.now(),

    deleted: Boolean = false

) {

    @LastModifiedDate
    @Column("modified_date_time")
    var modifiedDateTime: LocalDateTime? = null
        private set

    @Column
    var deleted: Boolean = deleted
        private set

}

