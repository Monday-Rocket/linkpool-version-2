package linkpool.user.user.fixtures

import linkpool.user.user.model.User
import linkpool.user.user.model.Profile
import linkpool.user.user.port.`in`.ProfileRequest
import java.time.LocalDateTime

const val USER_UID: String = "TestUid123456789"
const val USER_NICKNAME: String = "사용자1"
const val USER_PROFILE_IMAGE: String = "01"

fun createUser(
    id: Long = 1L,
    uid: String = USER_UID,
    nickname: String = USER_NICKNAME,
    jobGroupId: Long = 1L,
    profileImage: String = USER_PROFILE_IMAGE,
    deleted: Boolean = false,
    createdDateTime: LocalDateTime = LocalDateTime.now(),
    modifiedDateTime: LocalDateTime = LocalDateTime.now(),
): User {
    return User(
        id,
        uid,
        Profile(
            nickname = nickname,
            jobGroupId = jobGroupId,
            profileImage = profileImage
        ),
        createdDateTime,
        modifiedDateTime,
        deleted
    )
}

fun createUserWithoutInfo(
    id: Long = 1L,
    uid: String = USER_UID,
    deleted: Boolean = false,
    createdDateTime: LocalDateTime = LocalDateTime.now(),
    modifiedDateTime: LocalDateTime = LocalDateTime.now(),
): User {
    return User(
        id = id,
        uid = uid,
        deleted = deleted,
        createdDateTime = createdDateTime,
        modifiedDateTime = modifiedDateTime
    )
}

fun createUserInformation(
    nickname: String = USER_NICKNAME,
    jobGroupId: Long = 1L,
    profileImage: String? = USER_PROFILE_IMAGE
): Profile {
    return Profile(
        nickname = nickname,
        jobGroupId = jobGroupId,
        profileImage = profileImage
    )
}

fun createUserInfoRequest(
    nickname: String? = USER_NICKNAME,
    jobGroupId: Long? = 1L,
    profileImage: String? = USER_PROFILE_IMAGE,
): ProfileRequest {
    return ProfileRequest(
        nickname = nickname,
        jobGroupId = jobGroupId,
        profileImage = profileImage,
    )
}
