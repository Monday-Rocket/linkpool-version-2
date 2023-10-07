package linkpool.link.adapters.folder.`in`.rest

import linkpool.common.rest.ApiResponse
import linkpool.link.folder.port.`in`.*
import linkpool.query.userfolder.UserFolderQuery
import linkpool.security.getPrincipal
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
    val principal = getPrincipal()
    val saveFolderRequest = request.awaitBody<SaveFolderRequest>()

    createFolderUseCase.create(principal.id, saveFolderRequest)
    return ServerResponse.ok().bodyValueAndAwait(ApiResponse.success(null))
  }

  suspend fun update(request: ServerRequest): ServerResponse {
    val principal = getPrincipal()
    val folderId = request.pathVariable("folderId").toLong()
    val updateFolderRequest = request.awaitBody<UpdateFolderRequest>()

    updateFolderUserUseCase.update(principal.id, folderId, updateFolderRequest)
    return ServerResponse.ok().bodyValueAndAwait(ApiResponse.success(null))
  }

  suspend fun delete(request: ServerRequest): ServerResponse {
    val principal = getPrincipal()
    val folderId = request.pathVariable("folderId").toLong()

    deleteFolderUseCase.delete(principal.id, folderId)
    return ServerResponse.ok().bodyValueAndAwait(ApiResponse.success(null))
  }

  suspend fun getByOwnerId(request: ServerRequest): ServerResponse {
    val principal = getPrincipal()

    userFolderQuery.findFoldersByOwnerId(principal.id)
    return ServerResponse.ok().bodyValueAndAwait(userFolderQuery.findFoldersByOwnerId(principal.id))
  }
}