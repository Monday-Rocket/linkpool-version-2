package linkpool.query.userjobgroup.r2dbc

import io.r2dbc.spi.Row
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.r2dbc.core.awaitOne
import org.springframework.r2dbc.core.awaitSingleOrNull
import org.springframework.stereotype.Repository

@Repository
class UserJobGroupRepository(
    private val databaseClient: DatabaseClient,
) {

    suspend fun findInformationByUid(uid: String): UserWithJobGroupResult? {
        return databaseClient.sql(
            """
                SELECT 
                    u.*,
                    j.name as job_group_name
                FROM user AS u
                INNER JOIN job_group AS j ON u.job_group_id = j.id
                WHERE u.uid = :uid
            """
        )
        .bind("uid", uid)
        .map(this::convert)
        .awaitSingleOrNull()
    }

    suspend fun findInformationById(id: Long): UserWithJobGroupResult? {
        return databaseClient.sql(
            """
                SELECT 
                    u.*,
                    j.name as job_group_name
                FROM user AS u
                INNER JOIN job_group AS j ON u.job_group_id = j.id
                WHERE u.id = :id
            """
        )
            .bind("id", id)
            .map(this::convert)
            .awaitSingleOrNull()
    }

    private fun convert(row: Row): UserWithJobGroupResult {
        return UserWithJobGroupResult(
            id = row.get("id").toString().toLong(),
            nickname = row.get("nickname").toString(),
            jobGroup = JobGroupResult(
                id = row.get("job_group_id").toString().toLong(),
                name = row.get("job_group_name").toString()
            ),
            profileImage = row.get("profile_image").toString()
        )
    }
}