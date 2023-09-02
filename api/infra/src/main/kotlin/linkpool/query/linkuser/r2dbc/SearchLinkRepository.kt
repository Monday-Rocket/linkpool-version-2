package linkpool.query.linkuser.r2dbc

import kotlinx.coroutines.reactor.awaitSingle
import linkpool.jobgroup.port.`in`.JobGroupResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.ZonedDateTime

@Repository
@Transactional(readOnly = true)
class SearchLinkRepository(
    private val databaseClient: DatabaseClient
) {
    suspend fun findPageByUserIdAndTitleContains(userId: Long, title: String, pageable: Pageable): Page<LinkWithUserResult> {
        val list = databaseClient.sql(
            """
               SELECT 
                    l.id                    as linkId,
                    l.url                   as url, 
                    l.title                 as title,
                    l.image                 as image,
                    l.folder_id             as folderId,
                    l.describe              as linkDescribe,
                    l.created_date_time     as linkCreatedDateTime,
                    u.id                    as userId,
                    u.nickname              as nickname,
                    u.profile_image         as profileImage,
                    jg.id                   as jobGroupId,
                    jg.name                 as jobGroupName
                FROM 
                    link as l
                INNER JOIN
                    user as u
                ON
                    l.user_id = u.id
                INNER JOIN 
                    job_group as jg
                ON 
                    u.job_group_id = jg.id
                WHERE
                    l.user_id = :userId
                AND 
                    l.title LIKE CONCAT('%', :title, '%')
                AND
                    l.deleted = 0
                LIMIT :limit
                OFFSET :offset
            """
        )
            .bind("userId", userId)
            .bind("title", title)
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
                FROM link as l
                INNER JOIN user as u ON l.user_id = u.id
                INNER JOIN job_group as jg ON u.job_group_id = jg.id
                WHERE l.user_id = :userId
                AND l.title LIKE CONCAT('%', :title, '%')
                AND l.deleted = 0
            """
        )
            .bind("userId", userId)
            .bind("title", title)
            .fetch().all().count().awaitSingle()

        return PageImpl(list, pageable, count)
    }


    suspend fun findPageByTitleContains(loggedInUserId: Long, keyword: String, pageable: Pageable): Page<LinkWithUserResult> {
        val list = databaseClient.sql(
            """
                SELECT 
                    l.id                    as linkId,
                    l.url                   as url, 
                    l.title                 as title,
                    l.image                 as image,
                    l.folder_id             as folderId,
                    l.describe              as linkDescribe,
                    l.created_date_time     as linkCreatedDateTime,
                    u.id                    as userId,
                    u.nickname              as nickname,
                    u.profile_image         as profileImage,
                    jg.id                   as jobGroupId,
                    jg.name                 as jobGroupName
                FROM 
                    link as l
                INNER JOIN 
                    folder as f ON l.folder_id = f.id
                INNER JOIN 
                    user u ON l.user_id = u.id
                INNER JOIN 
                    job_group jg ON u.job_group_id = jg.id
                WHERE 
                    REPLACE(REPLACE(l.title, ' ', ''), '\n', '') like CONCAT('%', :keyword, '%')
                AND 
                    f.visible = 1
                AND 
                    l.inflow_type = :inflowType
                AND 
                    l.id NOT IN
                (
                    SELECT 
                        r.target_id
                    FROM report r
                    WHERE 
                        r.target_type = :targetType
                    AND r.reporter_id = :loggedInUserId
                )
                AND l.deleted = 0
                ORDER BY
                    l.created_date_time DESC
                LIMIT :limit
                OFFSET :offset
                    
        """

        )
            .bind("keyword", keyword)
            .bind("inflowType", 0)
            .bind("targetType", 1)
            .bind("loggedInUserId", loggedInUserId)
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
                FROM link as l
                INNER JOIN folder as f ON l.folder_id = f.id
                INNER JOIN user u ON l.user_id = u.id
                INNER JOIN job_group jg ON u.job_group_id = jg.id
                WHERE 
                    REPLACE(REPLACE(l.title, ' ', ''), '\n', '') like CONCAT('%', :keyword, '%')
                AND 
                    f.visible = 1
                AND 
                    l.inflow_type = :inflowType
                AND 
                    l.id NOT IN
                (
                    SELECT 
                        r.target_id
                    FROM report r
                    WHERE 
                        r.target_type = :targetType
                    AND r.reporter_id = :loggedInUserId
                )
                AND l.deleted = 0
        """

        )
            .bind("keyword", keyword)
            .bind("inflowType", 0)
            .bind("targetType", 1)
            .bind("loggedInUserId", loggedInUserId)
            .fetch().all().count().awaitSingle()

        return PageImpl(list, pageable, count)
    }
    private fun convert(row: MutableMap<String, Any>): LinkWithUserResult {
        return LinkWithUserResult(
            id = row["linkId"].toString().toLong(),
            user = UserResult(
                row["userId"].toString().toLong(),
                row["nickname"]?.toString(),
                JobGroupResponse(
                    row["jobGroupId"].toString().toLong(),
                    row["jobGroupName"].toString(),
                ),
                row["profileImage"].toString()
            ),
            url = row["url"].toString(),
            title = row["title"]?.toString(),
            image = row["image"]?.toString(),
            folderId = row["folderId"]?.toString()?.toLong(),
            describe = row["linkDescribe"]?.toString(),
            createdDateTime = (row["linkCreatedDateTime"] as ZonedDateTime).toLocalDateTime()
        )
    }
}