package linkpool.user.adapters.user.`in`.rest

import linkpool.user.adapters.user.`in`.rest.dto.ApiUserProfileRequest
import linkpool.common.rest.ApiResponse
import linkpool.query.userjobgroup.UserJobGroupQuery
import linkpool.security.getPrincipal
import linkpool.user.user.port.`in`.*
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.awaitBody
import org.springframework.web.reactive.function.server.bodyValueAndAwait

@Component
class UserHandler(
    private val getUserUseCase: GetUserUseCase,
    private val createUserUseCase: CreateUserUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val userJobGroupQuery: UserJobGroupQuery
) {

    suspend fun createUser(request: ServerRequest): ServerResponse {
        val principal = getPrincipal()

        val token = createUserUseCase.createUser(principal.uid)
        return ServerResponse.ok().bodyValueAndAwait(ApiResponse.success(token))
    }

    suspend fun updateProfile(request: ServerRequest): ServerResponse {
        val principal = getPrincipal()
        val updateUserRequest = request.awaitBody<ApiUserProfileRequest>()

        updateUserUseCase.updateProfile(
            principal.id,
            toDomain(updateUserRequest)
        )
        return ServerResponse.ok().bodyValueAndAwait(ApiResponse.success(null))
    }

    suspend fun getMyProfile(request: ServerRequest): ServerResponse {
        val principal = getPrincipal()

        return ServerResponse.ok().bodyValueAndAwait(
            ApiResponse.success(
                userJobGroupQuery.getProfileById(principal.id)
            )
        )
    }

    suspend fun getProfileById(request: ServerRequest): ServerResponse {
        val userId = request.pathVariable("userId").toLong()

        return ServerResponse.ok().bodyValueAndAwait(
            ApiResponse.success(
                userJobGroupQuery.getProfileById(userId)
            )
        )
    }

    suspend fun signOut(request: ServerRequest): ServerResponse {
        val principal = getPrincipal()

        signOutUseCase.signOut(principal.id)
        return ServerResponse.ok().bodyValueAndAwait(ApiResponse.success(null))
    }

    suspend fun checkIfExistsByNickname(request: ServerRequest): ServerResponse {
        val nickname = request.queryParam("nickname")
        require(!nickname.isEmpty) {
            throw IllegalArgumentException("nickname is needed")
        }

        val result = getUserUseCase.existsByNickname(nickname.get())
        return ServerResponse.ok().bodyValueAndAwait(ApiResponse.success(result))
    }

    fun toDomain(infra: ApiUserProfileRequest) =
        ProfileRequest(
            nickname = infra.nickname,
            jobGroupId = infra.job_group_id,
            profileImage = infra.profile_img
        )

}
