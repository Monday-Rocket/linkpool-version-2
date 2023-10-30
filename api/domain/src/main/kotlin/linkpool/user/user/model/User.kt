package linkpool.user.user.model

import linkpool.exception.NotEnoughForSigningUpException
import linkpool.exception.NotSignedUpException
import linkpool.user.user.port.`in`.ProfileRequest
import java.time.LocalDateTime

class User(
    val id: Long = 0L,
    val uid: String,
    var profile: Profile? = null,
    val createdDateTime: LocalDateTime = LocalDateTime.now(),
    var modifiedDateTime: LocalDateTime = LocalDateTime.now(),
    private var deleted: Boolean = false
) {

    fun signOut(): UserSignedOutEvent {
        profile = null
        deleted = true
        return UserSignedOutEvent(id)
    }

    fun activate() {
        deleted = false
    }

    fun isActivated(): Boolean {
        return !deleted
    }

    fun isNotActivated(): Boolean {
        return deleted
    }

    fun signedUp(): Boolean {
        return this.profile != null
    }


    fun createProfile(userInfo: Profile) {
        profile = userInfo
    }

    fun updateProfile(profileRequest: ProfileRequest) {
        createProfile(
            Profile(
                nickname = profileRequest.nickname ?: profile?.nickname
                ?: throw NotEnoughForSigningUpException(),
                jobGroupId = profileRequest.jobGroupId ?: profile?.jobGroupId
                ?: throw NotEnoughForSigningUpException(),
                profileImage = profileRequest.profileImage ?: profile?.profileImage
                ?: throw NotEnoughForSigningUpException()
            )
        )
    }

    fun updateNickname(nickname: String) {
        profile ?.let {
            createProfile(
                Profile(
                    nickname = nickname,
                    jobGroupId = it.jobGroupId,
                    profileImage = it.profileImage
                )
            )
        } ?: throw NotSignedUpException()
    }

    fun updateJobGroupId(jobGroupId: Long) {
        profile ?.let {
            createProfile(
                Profile(
                    nickname = it.nickname,
                    jobGroupId = jobGroupId,
                    profileImage = it.profileImage
                )
            )
        } ?: throw NotSignedUpException()
    }

    fun updateProfileImage(profileImage: String) {
        profile ?.let {
            createProfile(
                Profile(
                    nickname = it.nickname,
                    jobGroupId = it.jobGroupId,
                    profileImage = profileImage
                )
            )
        } ?: throw NotSignedUpException()
    }

}

