package linkpool.user.jobgroup.port.`in`

import linkpool.user.jobgroup.model.JobGroup

data class JobGroupResponse(
    val id: Long,
    val name: String
) {
    constructor(jobGroup: JobGroup) : this(
        jobGroup.id,
        jobGroup.name,
    )
}