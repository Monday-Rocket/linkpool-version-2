package linkpool.adapters.folder.`in`.rest

import kotlinx.coroutines.reactor.awaitSingle
import linkpool.common.rest.ApiResponse
import linkpool.folder.port.`in`.*
import linkpool.query.userfolder.UserFolderQuery
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.awaitBody
import org.springframework.web.reactive.function.server.bodyValueAndAwait

@Component
class FolderHandler(
  private val userFolderQuery: UserFolderQuery,
  private val deleteFolderUseCase: DeleteFolderUseCase,
  private val updateFolderUserUseCase: UpdateFolderUseCase,
  private val createFolderUseCase: CreateFolderUseCase,
) {

  suspend fun create(request: ServerRequest): ServerResponse {
    val context = ReactiveSecurityContextHolder
      .getContext()
      .awaitSingle()
    val uid = context.authentication.name

    val saveFolderRequest = request.awaitBody<SaveFolderRequest>()
    createFolderUseCase.create(uid, saveFolderRequest)
    return ServerResponse.ok().bodyValueAndAwait(ApiResponse.success(null))
  }

  suspend fun update(request: ServerRequest): ServerResponse {
    val context = ReactiveSecurityContextHolder
      .getContext()
      .awaitSingle()
    val uid = context.authentication.name

    val folderId = request.pathVariable("folderId").toLong()
    val updateFolderRequest = request.awaitBody<UpdateFolderRequest>()
    updateFolderUserUseCase.update(uid, folderId, updateFolderRequest)
    return ServerResponse.ok().bodyValueAndAwait(ApiResponse.success(null))
  }

  suspend fun delete(request: ServerRequest): ServerResponse {
    val context = ReactiveSecurityContextHolder
      .getContext()
      .awaitSingle()
    val uid = context.authentication.name

    val folderId = request.pathVariable("folderId").toLong()
    deleteFolderUseCase.delete(uid, folderId)
    return ServerResponse.ok().bodyValueAndAwait(ApiResponse.success(null))
  }

  suspend fun getByUserId(): ServerResponse {
    val context = ReactiveSecurityContextHolder
      .getContext()
      .awaitSingle()
    val uid = context.authentication.name

    userFolderQuery.findFoldersByUid(uid)
    return ServerResponse.ok().bodyValueAndAwait(userFolderQuery.findFoldersByUid(uid))
  }
}