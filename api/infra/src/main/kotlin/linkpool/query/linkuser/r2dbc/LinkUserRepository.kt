package linkpool.query.linkuser.r2dbc

import kotlinx.coroutines.reactor.awaitSingle
import linkpool.user2.jobgroup.port.`in`.JobGroupResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import java.time.ZonedDateTime

@Repository
class LinkUserRepository(
    private val databaseClient: DatabaseClient,
) {
    suspend fun findUnclassifiedLinks(creatorId: Long, pageable: Pageable): Page<LinkWithUserResult> {
        val list = databaseClient.sql(
            """
                SELECT 
                    l.id,
                    l.folder_id,
                    l.url,
                    l.title,
                    l.image,
                    l.describe,
                    l.created_date_time,
                    l.creator_id,
                    u.nickname,
                    u.job_group_id,
                    u.profile_image,
                    j.name AS job_group_name
                FROM link AS l
                INNER JOIN user AS u ON l.creator_id = u.id
                INNER JOIN job_group AS j ON u.job_group_id = j.id
                WHERE l.folder_id IS NULL
                AND l.creator_id = :creatorId
                AND l.deleted = 0
                ORDER BY created_date_time DESC
                LIMIT :limit
                OFFSET :offset
            """
        )
            .bind("creatorId", creatorId)
            .bind("limit", pageable.pageSize)
            .bind("offset", pageable.pageSize * pageable.pageNumber)
            .fetch().all()
            .map { row -> convert(row) }
            .collectList()
            .awaitSingle()

        val count = databaseClient.sql(
            """
                SELECT 
                    l.*
                FROM link AS l
                INNER JOIN user AS u ON l.creator_id = u.id
                INNER JOIN job_group AS j ON u.job_group_id = j.id
                WHERE l.folder_id IS NULL
                AND l.creator_id = :creatorId
                AND l.deleted = 0
            """
        )
            .bind("creatorId", creatorId)
            .fetch().all().count().awaitSingle()

        return PageImpl(list, pageable, count)
    }

    suspend fun findPageOfMyFolder(ownerId: Long, folderId: Long, pageable: Pageable): Page<LinkWithUserResult> {
        val list = databaseClient.sql(
            """
                SELECT 
                    l.id,
                    l.folder_id,
                    l.url,
                    l.title,
                    l.image,
                    l.describe,
                    l.created_date_time,
                    l.creator_id,
                    u.nickname,
                    u.job_group_id,
                    u.profile_image,
                    j.name AS job_group_name
                FROM link AS l
                INNER JOIN folder AS f ON l.folder_id = f.id
                INNER JOIN user AS u ON l.creator_id = u.id
                INNER JOIN job_group AS j ON u.job_group_id = j.id
                WHERE f.id = :folderId
                AND f.owner_id = :ownerId
                AND l.deleted = 0
                ORDER BY created_date_time DESC
                LIMIT :limit
                OFFSET :offset
            """
        )
            .bind("ownerId", ownerId)
            .bind("folderId", folderId)
            .bind("limit", pageable.pageSize)
            .bind("offset", pageable.pageSize * pageable.pageNumber)
            .fetch().all()
            .map { row -> convert(row) }
            .collectList()
            .awaitSingle()

        val count = databaseClient.sql(
            """
                SELECT 
                    l.*
                FROM link AS l
                INNER JOIN folder AS f ON l.folder_id = f.id
                INNER JOIN user AS u ON l.creator_id = u.id
                INNER JOIN job_group AS j ON u.job_group_id = j.id
                WHERE f.id = :folderId
                AND f.owner_id = :ownerId
                AND l.deleted = 0
            """
        )
            .bind("ownerId", ownerId)
            .bind("folderId", folderId)
            .fetch().all().count().awaitSingle()

        return PageImpl(list, pageable, count)
    }
    private fun convert(row: MutableMap<String, Any>): LinkWithUserResult {
        return LinkWithUserResult(
            id = row["id"].toString().toLong(),
            creator = UserResult(
                id = row["creator_id"].toString().toLong(),
                nickname = row["nickname"].toString(),
                jobGroup = JobGroupResponse(
                    id = row["job_group_id"].toString().toLong(),
                    name = row["job_group_name"].toString(),
                ),
                profileImg = row["profile_image"].toString()
            ),
            folderId = row["folder_id"]?.toString()?.toLong(),
            url = row["url"].toString(),
            title = row["title"]?.toString(),
            image = row["image"]?.toString(),
            describe = row["describe"]?.toString(),
            createdDateTime = (row["created_date_time"] as ZonedDateTime).toLocalDateTime()
        )
    }
}