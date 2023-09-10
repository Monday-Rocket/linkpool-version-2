package linkpool.adapters.jobgroup.`in`.rest

import linkpool.LinkPoolPage
import linkpool.LinkPoolPageRequest
import linkpool.common.rest.ApiResponse
import linkpool.jobgroup.port.`in`.JobGroupQuery
import linkpool.jobgroup.port.`in`.JobGroupResponse
import linkpool.query.linkuser.MainPageQuery
import linkpool.query.linkuser.r2dbc.LinkWithUserResult
import linkpool.security.getPrincipal
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping("/job-groups")
@RestController
class JobGroupRestController(
    private val jobGroupQuery: JobGroupQuery,
    private val mainPageQuery: MainPageQuery
) {
    @GetMapping
    suspend fun getJobGroups()
    : ResponseEntity<ApiResponse<List<JobGroupResponse>>> {
        return ResponseEntity.ok(ApiResponse.success(
            jobGroupQuery.getAll().map { JobGroupResponse(it) }))
    }

    @GetMapping("/{jobGroupId}/links")
    suspend fun getLinkByJobGroups(
        @PathVariable("jobGroupId") jobGroupId: Long,
        @RequestParam(value = "page_no", required = false) pageNo: Int = 0,
        @RequestParam(value = "page_size", required = false) pageSize: Int = 10,
    ): ResponseEntity<ApiResponse<LinkPoolPage<LinkWithUserResult>>> {
        val principal = getPrincipal()

        return ResponseEntity.ok(
            ApiResponse.success(mainPageQuery.getByJobGroupId(jobGroupId, LinkPoolPageRequest(pageNo, pageSize), principal.id))
        )
    }
    @GetMapping("/links")
    suspend fun getLinks(
        @RequestParam(value = "page_no", required = false) pageNo: Int = 0,
        @RequestParam(value = "page_size", required = false) pageSize: Int = 10,
    ): ResponseEntity<ApiResponse<LinkPoolPage<LinkWithUserResult>>> {
        val principal = getPrincipal()

        return ResponseEntity.ok(
            ApiResponse.success(mainPageQuery.getAll(LinkPoolPageRequest(pageNo, pageSize), principal.id))
        )
    }



}