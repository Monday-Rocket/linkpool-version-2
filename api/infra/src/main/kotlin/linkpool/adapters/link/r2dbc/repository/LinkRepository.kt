package linkpool.adapters.link.r2dbc.repository

import linkpool.LinkPoolPageRequest
import linkpool.adapters.link.r2dbc.entity.LinkR2dbcEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

fun LinkRepository.getById(id: Long) = findById(id)
    ?: throw NoSuchElementException("링크가 존재하지 않습니다. id: $id")

interface LinkRepository : ReactiveCrudRepository<LinkR2dbcEntity, Long> {
    suspend fun findAllByFolderId(folderId: Long): Flux<LinkR2dbcEntity>
    suspend fun countByFolderId(folderId: Long): Mono<Long>
    suspend fun countByUserIdAndFolderIdIsNull(userId: Long): Mono<Int>
    @Query("SELECT * FROM link WHERE folder_id =:folderId ORDER BY created_date_time DESC LIMIT :limit OFFSET :offset")
    suspend fun findByFolderIdOrderByCreatedDateTimeDesc(folderId: Long, limit: Int, offset: Int): Flux<LinkR2dbcEntity>
    @Query("SELECT * FROM link WHERE user_id = :userId ORDER BY created_date_time DESC LIMIT :limit OFFSET :offset")
    suspend fun findByUserIdOrderByCreatedDateTimeDesc(userId: Long, limit: Int, offset: Int): Flux<LinkR2dbcEntity>
    suspend fun existsByUserIdAndUrl(userId: Long, url: String): Mono<Boolean>

    suspend fun countByUserId(userId: Long): Mono<Long>

    suspend fun findFirst1ByFolderIdOrderByCreatedDateTimeDesc(folderId: Long): Mono<LinkR2dbcEntity>

//    @Modifying
//    @Query("UPDATE LinkJpaEntity l SET l.deleted = true WHERE l.userId = :userId")
//    suspend fun deleteBatchByUserId(@Param("userId") userId: Long)

    @Query("UPDATE LinkJpaEntity l SET l.deleted = true WHERE l.folderId = :folderId")
    suspend fun deleteBatchByFolderId(@Param("folderId") folderId: Long)


}
