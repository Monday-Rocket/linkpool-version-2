package linkpool.jobgroup.fixtures

import linkpool.user.jobgroup.model.JobGroup

const val JOB_GROUP_NAME: String = "개발자"

fun createJobGroup(
    id: Long = 1L,
    name: String = JOB_GROUP_NAME
): JobGroup {
    return JobGroup(
        id = id,
        name = name,
    )
}

