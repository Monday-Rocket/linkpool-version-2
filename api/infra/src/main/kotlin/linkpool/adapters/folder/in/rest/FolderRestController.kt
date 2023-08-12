package linkpool.adapters.folder.`in`.rest

import kotlinx.coroutines.reactor.awaitSingle
import linkpool.common.rest.ApiResponse
import linkpool.folder.port.`in`.*
import linkpool.query.userfolder.UserFolderQuery
import linkpool.query.userfolder.r2dbc.UserFolderListResult
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.web.bind.annotation.*

//@RequestMapping("/folders")
//@RestController
class FolderRestController(
    private val userFolderQuery: UserFolderQuery,
    private val deleteFolderUseCase: DeleteFolderUseCase,
    private val updateFolderUserUseCase: UpdateFolderUseCase,
    private val createFolderUseCase: CreateFolderUseCase,
) {
  @PostMapping
  suspend fun create(
    @RequestBody request: SaveFolderRequest,
  ): ApiResponse<Unit> {
    val context = ReactiveSecurityContextHolder
      .getContext()
      .awaitSingle()
    val uid = context.authentication.name

    createFolderUseCase.create(uid, request)
    return ApiResponse.success(null)
  }

  @PatchMapping("/{folderId}")
  suspend fun update(
    @PathVariable("folderId") folderId: Long,
    @RequestBody request: UpdateFolderRequest,
  ): ApiResponse<Unit> {
    val context = ReactiveSecurityContextHolder
      .getContext()
      .awaitSingle()
    val uid = context.authentication.name

    updateFolderUserUseCase.update(uid, folderId, request)
    return ApiResponse.success(null)
  }

  @DeleteMapping("/{folderId}")
  suspend fun update(
    @PathVariable("folderId") folderId: Long,
  ): ApiResponse<Unit> {
    val context = ReactiveSecurityContextHolder
      .getContext()
      .awaitSingle()
    val uid = context.authentication.name

    deleteFolderUseCase.delete(uid, folderId)
    return ApiResponse.success(null)
  }

  @GetMapping
  suspend fun getByUserId(
  ): ApiResponse<List<UserFolderListResult>> {
    val context = ReactiveSecurityContextHolder
      .getContext()
      .awaitSingle()
    val uid = context.authentication.name
    return ApiResponse.success(userFolderQuery.findFoldersByUid(uid))
  }
}