package linkpool.user2.jobgroup.port.out

import linkpool.user2.jobgroup.model.JobGroup

interface JobGroupPort {
    suspend fun findById(id: Long): JobGroup?
    suspend fun findAll(): List<JobGroup>
}