package linkpool.jobgroup.service

import linkpool.common.DomainComponent
import linkpool.exception.DataNotFoundException
import linkpool.jobgroup.model.JobGroup
import linkpool.jobgroup.port.`in`.JobGroupQuery
import linkpool.jobgroup.port.out.JobGroupPort

@DomainComponent
class JobGroupQueryService(
    private val jobGroupPort: JobGroupPort,
): JobGroupQuery {

    override suspend fun getAll(): List<JobGroup> {
        return jobGroupPort.findAll()
    }

    override suspend fun getById(id: Long): JobGroup {
        return jobGroupPort.findById(id) ?: throw DataNotFoundException("직업군이 존재하지 않습니다. id: $id")
    }
    override suspend fun getByIdOrNull(id: Long): JobGroup? {
        return jobGroupPort.findById(id)
    }
}