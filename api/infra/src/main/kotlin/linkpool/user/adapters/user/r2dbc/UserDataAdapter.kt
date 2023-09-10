package linkpool.user.adapters.user.r2dbc

import kotlinx.coroutines.reactor.awaitSingle
import linkpool.user.adapters.user.r2dbc.entity.UserR2dbcEntity
import linkpool.user.adapters.user.r2dbc.repository.UserRepository
import linkpool.user2.user.model.User
import linkpool.user2.user.model.Profile
import linkpool.user2.user.port.out.UserPort
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

    override suspend fun existsByNickname(nickname: String): Boolean {
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
            profile = if (
                entity.nickname != null
                && entity.jobGroupId != null
            ) Profile(
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
            nickname = model.profile?.nickname,
            jobGroupId = model.profile?.jobGroupId,
            profileImage = model.profile?.profileImage,
            createdDateTime = model.createdDateTime,
            deleted = model.isNotActivated()
        )

}