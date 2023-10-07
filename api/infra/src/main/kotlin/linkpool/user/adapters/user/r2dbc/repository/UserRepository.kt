package linkpool.user.adapters.user.r2dbc.repository

import linkpool.user.adapters.user.r2dbc.entity.UserR2dbcEntity
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface UserRepository : ReactiveCrudRepository<UserR2dbcEntity, Long> {

    @Query("""
        SELECT * FROM user 
            WHERE uid = :uid
            AND deleted = false
    """)
    suspend fun findByUid(uid: String): UserR2dbcEntity?

    @Query("""
        SELECT * FROM user
            WHERE deleted = true
            AND uid = :uid
    """)
    suspend fun findByUidIncludingDeleted(uid: String): UserR2dbcEntity?

    suspend fun existsByNickname(nickname: String): Boolean
}
