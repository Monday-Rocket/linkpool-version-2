package linkpool.query.linkuser.r2dbc

import linkpool.adapters.link.r2dbc.entity.LinkR2dbcEntity
import linkpool.link.model.InflowType
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import java.time.LocalDateTime

@Repository
class LinkUserRepository(
    private val databaseClient: DatabaseClient,
) {
    suspend fun findUnclassifiedLinks(userId: Long, pageable: Pageable): Mono<Page<LinkR2dbcEntity>> {
        return databaseClient.sql(
            """
                SELECT l.*
                FROM link as l
                WHERE user_id = :userId
                AND folder_id IS NULL
                ORDER BY created_date_time DESC
            """
        ).bind("userId", userId)
            .fetch().all()
            .map { row -> convert(row) }
            .collectList()
            .map { list -> PageImpl(list, pageable, list.size.toLong()) }
    }
    private fun convert(row: MutableMap<String, Any>): LinkR2dbcEntity {
        return LinkR2dbcEntity(
            id = row["id"].toString().toLong(),
            userId = row["userId"].toString().toLong(),
            folderId = row["folderId"]?.toString()?.toLong(),
            url = row["url"].toString(),
            title = row["url"]?.toString(),
            image = row["image"]?.toString(),
            describe = row["describe"]?.toString(),
            inflowType = InflowType.valueOf(row["inflowType"].toString()),
            createdDateTime = LocalDateTime.parse(row["createdDateTime"].toString()
            )
        )
    }
}