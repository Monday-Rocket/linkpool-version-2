package linkpool.adapters.jobgroup.r2dbc.repository

import linkpool.adapters.jobgroup.r2dbc.entity.JobGroupR2dbcEntity
import org.springframework.data.repository.reactive.ReactiveCrudRepository

fun JobGroupRepository.getById(id: Long) = findById(id)
    ?: throw NoSuchElementException("직업군이 존재하지 않습니다. id: $id")

interface JobGroupRepository : ReactiveCrudRepository<JobGroupR2dbcEntity, Long>