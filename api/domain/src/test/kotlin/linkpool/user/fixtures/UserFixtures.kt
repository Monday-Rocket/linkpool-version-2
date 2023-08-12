package linkpool.user.fixtures

import linkpool.user.model.User
import linkpool.user.model.UserInformation
import linkpool.user.port.`in`.UserInfoRequest
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
        UserInformation(
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
): UserInformation {
    return UserInformation(
        nickname = nickname,
        jobGroupId = jobGroupId,
        profileImage = profileImage
    )
}

fun createUserInfoRequest(
    nickname: String? = USER_NICKNAME,
    jobGroupId: Long? = 1L,
    profileImage: String? = USER_PROFILE_IMAGE,
): UserInfoRequest {
    return UserInfoRequest(
        nickname = nickname,
        jobGroupId = jobGroupId,
        profileImage = profileImage,
    )
}
