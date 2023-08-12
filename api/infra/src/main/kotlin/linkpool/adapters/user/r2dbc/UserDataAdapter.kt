package linkpool.adapters.user.r2dbc

import kotlinx.coroutines.reactor.awaitSingle
import linkpool.adapters.user.r2dbc.entity.UserR2dbcEntity
import linkpool.adapters.user.r2dbc.repository.UserRepository
import linkpool.user.model.User
import linkpool.user.model.UserInformation
import linkpool.user.port.out.UserPort
import org.springframework.stereotype.Service


@Service
class UserDataAdapter(private val userRepository: UserRepository) : UserPort {
    override suspend fun save(user: User): User {
        return toModel(userRepository.save(toR2dbc(user)).awaitSingle())
    }

    override suspend fun findById(id: Long): User? {
        return toModel(userRepository.findById(id).awaitSingle())
    }

    override suspend fun findByUid(uid: String): User? {
        return userRepository.findByUid(uid)?.let(this::toModel)
    }

    override suspend fun findByUidIncludingDeleted(uid: String): User? {
        return userRepository.findByUidIncludingDeleted(uid)?.let { toModel(it) }
    }

    override suspend fun existsByInfoNickname(nickname: String): Boolean {
        return userRepository.existsByNickname(nickname)
    }

    override suspend fun patch(user: User) {
        userRepository.save(toR2dbc(user)).awaitSingle()
    }

    fun toModel(entities: List<UserR2dbcEntity>): List<User>
     = entities.map(this::toModel)

    fun toModel(entity: UserR2dbcEntity) =
        User(
            id = entity.id,
            uid = entity.uid,
            info = if (
                entity.nickname != null
                && entity.jobGroupId != null
            ) UserInformation(
                nickname = entity.nickname!!,
                jobGroupId = entity.jobGroupId!!,
                profileImage = entity.profileImage
            ) else null,
            deleted = entity.deleted,
            createdDateTime = entity.createdDateTime
        )

    fun toR2dbc(model: User) =
        UserR2dbcEntity(
            id = model.id,
            uid = model.uid,
            nickname = model.info?.nickname,
            jobGroupId = model.info?.jobGroupId,
            profileImage = model.info?.profileImage,
            createdDateTime = model.createdDateTime,
            deleted = model.isNotActivated()
        )

}