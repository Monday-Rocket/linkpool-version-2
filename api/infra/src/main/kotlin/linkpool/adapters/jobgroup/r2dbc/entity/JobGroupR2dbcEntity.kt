package linkpool.adapters.jobgroup.r2dbc.entity

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table(name = "job_group")
class JobGroupR2dbcEntity(
    @Id
    @Column
    val id: Long = 0L,

    @Column
    val name: String,

    @CreatedDate
    @Column
    val createdDateTime: LocalDateTime = LocalDateTime.now(),
)