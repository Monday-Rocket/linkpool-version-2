package linkpool.user.jobgroup.port.out

import linkpool.user.jobgroup.model.JobGroup

interface JobGroupPort {
    suspend fun findById(id: Long): JobGroup?
    suspend fun findAll(): List<JobGroup>
}