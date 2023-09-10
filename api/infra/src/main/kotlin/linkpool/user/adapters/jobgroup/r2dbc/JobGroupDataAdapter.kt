package linkpool.user.adapters.jobgroup.r2dbc

import kotlinx.coroutines.reactive.awaitSingle
import linkpool.user.adapters.jobgroup.r2dbc.entity.JobGroupR2dbcEntity
import linkpool.user.adapters.jobgroup.r2dbc.repository.JobGroupRepository
import linkpool.user.jobgroup.model.JobGroup
import linkpool.user.jobgroup.port.out.JobGroupPort
import org.springframework.stereotype.Service


@Service
class JobGroupDataAdapter(
    private val jobGroupRepository: JobGroupRepository
) : JobGroupPort {
    override suspend fun findAll() = toModel(jobGroupRepository.findAll()
        .collectList()
        .awaitSingle()
    )

    override suspend fun findById(id: Long) = toModel(jobGroupRepository.findById(id).awaitSingle())

    private fun toModel(entities: List<JobGroupR2dbcEntity>) =
        entities.map { toModel(it) }

    private fun toModel(entity: JobGroupR2dbcEntity) =
        JobGroup(id = entity.id, name = entity.name)

}