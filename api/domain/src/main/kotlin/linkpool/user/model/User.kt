package linkpool.user.model

import linkpool.exception.NotEnoughForSigningUpException
import linkpool.exception.NotSignedUpException
import linkpool.user.port.`in`.UserInfoRequest
import java.time.LocalDateTime

class User(
    val id: Long = 0L,
    val uid: String,
    var info: UserInformation? = null,
    val createdDateTime: LocalDateTime = LocalDateTime.now(),
    var modifiedDateTime: LocalDateTime = LocalDateTime.now(),
    private var deleted: Boolean = false
) {

    fun checkInfoCreated(): Boolean {
        return info != null
    }

    fun delete() {
        info = null
        deleted = true
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

    fun createInfo(userInfo: UserInformation) {
        info = userInfo
    }

    fun updateNickname(nickname: String) {
        info ?.let {
            createInfo(
                UserInformation(
                    nickname = nickname,
                    jobGroupId = it.jobGroupId,
                    profileImage = it.profileImage
                )
            )
        } ?: throw NotSignedUpException()
    }

    fun updateJobGroupId(jobGroupId: Long) {
        info ?.let {
            createInfo(
                UserInformation(
                    nickname = it.nickname,
                    jobGroupId = jobGroupId,
                    profileImage = it.profileImage
                )
            )
        } ?: throw NotSignedUpException()
    }

    fun updateProfileImage(profileImage: String) {
        info ?.let {
            createInfo(
                UserInformation(
                    nickname = it.nickname,
                    jobGroupId = it.jobGroupId,
                    profileImage = profileImage
                )
            )
        } ?: throw NotSignedUpException()
    }

    fun signedUp(): Boolean {
        return this.info != null
    }

    fun patchInfo(userInfoRequest: UserInfoRequest) {
        createInfo(
            UserInformation(
                nickname = userInfoRequest.nickname ?: info?.nickname
                    ?: throw NotEnoughForSigningUpException(),
                jobGroupId = userInfoRequest.jobGroupId ?: info?.jobGroupId
                    ?: throw NotEnoughForSigningUpException(),
                profileImage = userInfoRequest.profileImage ?: info?.profileImage
                    ?: throw NotEnoughForSigningUpException()
            )
        )
    }

}

