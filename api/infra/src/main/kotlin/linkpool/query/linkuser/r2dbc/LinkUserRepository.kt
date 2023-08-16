package linkpool.query.linkuser.r2dbc

import linkpool.LinkPoolPageRequest
import linkpool.adapters.link.r2dbc.entity.LinkR2dbcEntity
import linkpool.link.model.InflowType
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Repository
class LinkUserRepository(
    private val databaseClient: DatabaseClient,
) {
    suspend fun findUnclassifiedLinks(userId: Long, paging: LinkPoolPageRequest): Mono<Page<LinkR2dbcEntity>> {
        return databaseClient.sql(
            """
                SELECT l.*
                FROM link as l
                WHERE user_id = :userId
                AND folder_id IS NULL
                ORDER BY created_date_time DESC
                LIMIT :limit
                OFFSET :offset
            """
        ).bind("userId", userId)
            .bind("limit", paging.page_size)
            .bind("offset", paging.page_size * paging.page_no)
            .fetch().all()
            .map { row -> convert(row) }
            .collectList()
            .map { list -> PageImpl(list, PageRequest.of(paging.page_no, paging.page_size), list.size.toLong()) }
    }
    private fun convert(row: MutableMap<String, Any>): LinkR2dbcEntity {
        return LinkR2dbcEntity(
            id = row["id"].toString().toLong(),
            userId = row["user_id"].toString().toLong(),
            folderId = row["folderId"]?.toString()?.toLong(),
            url = row["url"].toString(),
            title = row["title"]?.toString(),
            image = row["image"]?.toString(),
            describe = row["describe"]?.toString(),
            inflowType = row["inflow_type"].toString().toInt(),
            createdDateTime = LocalDateTime.parse(row["created_date_time"].toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z[UTC]'"))
        )
    }
}