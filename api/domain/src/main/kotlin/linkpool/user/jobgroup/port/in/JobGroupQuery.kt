package linkpool.user.jobgroup.port.`in`

import linkpool.user.jobgroup.model.JobGroup

interface JobGroupQuery {
    suspend fun getAll(): List<JobGroup>
    suspend fun getById(id: Long): JobGroup
    suspend fun getByIdOrNull(id: Long): JobGroup?
}