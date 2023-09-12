package linkpool.adapters.jobgroup.`in`.rest

import linkpool.LinkPoolPageRequest
import linkpool.common.rest.ApiResponse
import linkpool.jobgroup.port.`in`.JobGroupQuery
import linkpool.jobgroup.port.`in`.JobGroupResponse
import linkpool.query.linkuser.MainPageQuery
import linkpool.security.getPrincipal
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait

@Component
class JobGroupHandler(
    private val jobGroupQuery: JobGroupQuery,
    private val mainPageQuery: MainPageQuery
) {
  suspend fun getJobGroups(request: ServerRequest): ServerResponse{
    val jobGroups = jobGroupQuery.getAll().map{ JobGroupResponse(it) }
    return ServerResponse.ok().bodyValueAndAwait(ApiResponse.success(jobGroups))
  }

  suspend fun getLinkByJobGroups(request: ServerRequest): ServerResponse{
    val jobGroupId = request.pathVariable("jobGroupId").toLong()
    val pageNo = request.queryParam("page_no").get().toInt()
    val pageSize = request.queryParam("page_size").get().toInt()
    val principal = getPrincipal()

    val links = mainPageQuery.getByJobGroupId(jobGroupId, LinkPoolPageRequest(pageNo, pageSize), principal.id)
    return ServerResponse.ok().bodyValueAndAwait(ApiResponse.success(links))
  }

  suspend fun getLinks(request: ServerRequest): ServerResponse{
    val pageNo = request.queryParam("page_no").get().toInt()
    val pageSize = request.queryParam("page_size").get().toInt()
    val principal = getPrincipal()

    val links = mainPageQuery.getAll(LinkPoolPageRequest(pageNo, pageSize), principal.id)

    return ServerResponse.ok().bodyValueAndAwait(ApiResponse.success(links))



  }
}