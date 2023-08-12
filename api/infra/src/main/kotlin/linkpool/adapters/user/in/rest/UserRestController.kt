package linkpool.adapters.user.`in`.rest

import kotlinx.coroutines.reactor.awaitSingle
import linkpool.adapters.user.`in`.rest.dto.ApiUserInfoRequest
import linkpool.common.rest.ApiResponse
import linkpool.query.userjobgroup.UserJobGroupQuery
import linkpool.query.userjobgroup.r2dbc.UserWithJobGroupResult
import linkpool.user.port.`in`.*
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.web.bind.annotation.*

//@RequestMapping("/users")
//@RestController
class UserRestController(
    private val getUserUseCase: GetUserUseCase,
    private val createUserUseCase: CreateUserUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val userJobGroupQuery: UserJobGroupQuery
) {

    @PostMapping
    suspend fun createUser(): ApiResponse<CreateUserResponse> {
        val context = ReactiveSecurityContextHolder
            .getContext()
            .awaitSingle()
        val uid = context.authentication.name
        val token = createUserUseCase.createUser(uid)
        return ApiResponse.success(token)
    }

    @PatchMapping("/me")
    suspend fun updateMyInfo(
        @RequestBody apiUserInfoRequest: ApiUserInfoRequest,
    ): ApiResponse<Unit> {
        val context = ReactiveSecurityContextHolder
            .getContext()
            .awaitSingle()
        val uid = context.authentication.name
        val user = getUserUseCase.getByUid(uid)
        return ApiResponse.success(
            updateUserUseCase.updateUserInfo(
                user,
                toDomain(apiUserInfoRequest)
            )
        )
    }

    @GetMapping("/me")
    suspend fun getMyInformation(): ApiResponse<UserWithJobGroupResult> {
        val context = ReactiveSecurityContextHolder
            .getContext()
            .awaitSingle()
        val uid = context.authentication.name
        return ApiResponse.success(
            userJobGroupQuery.getInformationByUid(uid)
        )
    }

    @GetMapping("/{userId}")
    suspend fun getUserInfoById(
        @PathVariable("userId") userId: Long,
    ): ApiResponse<UserWithJobGroupResult> {
        return ApiResponse.success(
            userJobGroupQuery.getInformationById(userId)
        )
    }

//    @GetMapping("/{userId}/folders")
//    fun getFoldersByUserId(
//        @PathVariable("userId") userId: Long,
//    ): ResponseEntity<ApiResponse<List<GetByUserIdResponse>>> {
//        return ResponseEntity.ok(ApiResponse.success(folderUseCase.getByUserId(userId)))
//    }

    @DeleteMapping
    suspend fun signOut(): ApiResponse<Unit> {
        val context = ReactiveSecurityContextHolder
            .getContext()
            .awaitSingle()
        val uid = context.authentication.name
        signOutUseCase.signOut(getUserUseCase.getByUid(uid))
        return ApiResponse.success()
    }

    @GetMapping
    suspend fun checkIfExistsByNickname(
        @RequestParam("nickname") nickname: String,
    ): ApiResponse<Boolean> {
        return ApiResponse.success(
                getUserUseCase.existsByNickname(nickname)
        )
    }

    fun toDomain(infra: ApiUserInfoRequest) =
        UserInfoRequest(
            nickname = infra.nickname,
            jobGroupId = infra.job_group_id,
            profileImage = infra.profile_img
        )

}