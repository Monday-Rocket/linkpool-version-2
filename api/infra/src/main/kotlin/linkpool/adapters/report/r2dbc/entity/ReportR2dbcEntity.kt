package linkpool.adapters.report.r2dbc.entity

import linkpool.report.model.ReportReasonType
import linkpool.report.model.ReportTargetType
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
//@SQLDelete(sql = "update report set deleted = true where id = ?")
@Table(name = "report")
class ReportR2dbcEntity (
    @Id
    @Column
    val id: Long = 0L,

    @Column
    var reporterId: Long,

    @Column
    val reason: ReportReasonType,

    @Column
    val otherReason: String? = null,

    @Column
    val targetType: Int,

    @Column
    val targetId: Long,

    @CreatedDate
    @Column
    val createdDateTime: LocalDateTime = LocalDateTime.now(),
){
    @LastModifiedDate
    @Column("modified_date_time")
    var modifiedDateTime: LocalDateTime? = null
    private set

        @Column
        var deleted: Boolean = false
    private set

    fun delete() {
        deleted = true
    }
}
