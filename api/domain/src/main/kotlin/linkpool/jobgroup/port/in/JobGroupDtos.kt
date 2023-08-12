package linkpool.jobgroup.port.`in`

import linkpool.jobgroup.model.JobGroup

data class JobGroupResponse(
    val id: Long,
    val name: String
) {
    constructor(jobGroup: JobGroup) : this(
        jobGroup.id,
        jobGroup.name,
    )
}