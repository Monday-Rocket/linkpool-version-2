package linkpool.user2.jobgroup.port.`in`

import linkpool.user2.jobgroup.model.JobGroup

data class JobGroupResponse(
    val id: Long,
    val name: String
) {
    constructor(jobGroup: JobGroup) : this(
        jobGroup.id,
        jobGroup.name,
    )
}