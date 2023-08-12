package linkpool.query.searchlink.r2dbc

import linkpool.jobgroup.port.`in`.JobGroupResponse
import linkpool.link.port.`in`.LinkWithUserResponse
import linkpool.user.port.`in`.UserResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import java.time.LocalDateTime

@Repository
class SearchLinkRepository(
    private val databaseClient: DatabaseClient
) {
    suspend fun findPageByUserIdAndTitleContains(userId: Long, title: String, pageable: Pageable): Mono<Page<LinkWithUserResponse>> {
        return databaseClient.sql(
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
                    jg.id                   as jobGrouopId,
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
                    u.jog_group_id = jg.id
                WHERE
                    l.user_id = :userId
                AND 
                    l.title LIKE CONCAT('%', :title, '%')
            """
        ).bind("userId", userId)
            .bind("title", title)
            .fetch().all()
            .map { row -> convert(row) }
            .collectList()
            .map { list -> PageImpl(list, pageable, list.size.toLong()) }
    }


    suspend fun findPageByTitleContains(loggedInUserId: Long, keyword: String, pageable: Pageable): Mono<Page<LinkWithUserResponse>> {
        return databaseClient.sql(
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
                jg.id                   as jobGrouopId,
                jg.name                 as jobGroupName
            FROM 
                link as l
            INNER JOIN 
                folder as f ON l.folderId = f.id
            WHERE 
                REPLACE(REPLACE(l.title, ' ', ''), '\n', '') like %:keyword%
            AND 
                f.visible = true
            AND 
                l.inflow_type = :inflowType
            AND 
                l.id NOT IN
            (
                SELECT 
                    r.target_id
                FROM Report r
                WHERE 
                    r.target_type = :targetType
                AND r.reporter_id = :loggedInUserId
            )
                ORDER BY l.created_date_time DESC
        """
        )
            .bind("keyword", keyword)
            .bind("inflowType", "CREATE")
            .bind("targetType", "LINK")
            .bind("loggedInUserId", loggedInUserId)
            .fetch().all()
            .map { row -> convert(row) }
            .collectList()
            .map { list -> PageImpl(list, pageable, list.size.toLong()) }
    }
    private fun convert(row: MutableMap<String, Any>): LinkWithUserResponse {
        return LinkWithUserResponse(
            id = row["linkId"].toString().toLong(),
            user = UserResponse(
                row["userId"].toString().toLong(),
                row["nickname"].toString(),
                JobGroupResponse(
                    row["jobGrouopId"].toString().toLong(),
                    row["jobGroupName"].toString(),
                ),
                row["profileImage"].toString()
            ),
            url = row["url"].toString(),
            title = row["itle"].toString(),
            image = row["image"].toString(),
            folderId = row["folderId"].toString().toLong(),
            describe = row["linkDescribe"].toString(),
            createdDateTime = LocalDateTime.parse(row["createdDateTime"].toString())
        )
    }
}