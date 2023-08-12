package linkpool.jobgroup.port.out

import linkpool.jobgroup.model.JobGroup

interface JobGroupPort {
    suspend fun findById(id: Long): JobGroup?
    suspend fun findAll(): List<JobGroup>
}