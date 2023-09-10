package linkpool.user.adapters.jobgroup.r2dbc.repository

import linkpool.user.adapters.jobgroup.r2dbc.entity.JobGroupR2dbcEntity
import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface JobGroupRepository : ReactiveCrudRepository<JobGroupR2dbcEntity, Long>