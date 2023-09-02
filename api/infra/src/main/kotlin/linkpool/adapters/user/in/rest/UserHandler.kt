package linkpool.adapters.user.`in`.rest

import linkpool.adapters.user.`in`.rest.dto.ApiUserInfoRequest
import linkpool.common.rest.ApiResponse
import linkpool.query.userjobgroup.UserJobGroupQuery
import linkpool.security.getPrincipal
import linkpool.user.port.`in`.*
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

    suspend fun updateMyInfo(request: ServerRequest): ServerResponse {
        val principal = getPrincipal()
        val updateUserRequest = request.awaitBody<ApiUserInfoRequest>()

        updateUserUseCase.updateUserInfo(
            principal.id,
            toDomain(updateUserRequest)
        )
        return ServerResponse.ok().bodyValueAndAwait(ApiResponse.success(null))
    }

    suspend fun getMyInformation(request: ServerRequest): ServerResponse {
        val principal = getPrincipal()

        return ServerResponse.ok().bodyValueAndAwait(
            ApiResponse.success(
                userJobGroupQuery.getInformationById(principal.id)
            )
        )
    }

    suspend fun getUserInfoById(request: ServerRequest): ServerResponse {
        val userId = request.pathVariable("userId").toLong()

        return ServerResponse.ok().bodyValueAndAwait(
            ApiResponse.success(
                userJobGroupQuery.getInformationById(userId)
            )
        )
    }

//    suspend fun getFoldersByUserId(request: ServerRequest): ServerResponse {
//        val userId = request.pathVariable("id").toLong()
//        return ServerResponse.ok().bodyValueAndAwait(
//            ApiResponse.success(
//                folderUseCase.getByUserId(userId)
//            )
//        )
//    }

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

    fun toDomain(infra: ApiUserInfoRequest) =
        UserInfoRequest(
            nickname = infra.nickname,
            jobGroupId = infra.job_group_id,
            profileImage = infra.profile_img
        )

}
