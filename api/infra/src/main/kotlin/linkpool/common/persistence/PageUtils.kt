package linkpool.common.persistence

import linkpool.LinkPoolPage
import linkpool.LinkPoolPageRequest
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest

fun <T> Page<T>.toLinkPoolPage(): LinkPoolPage<T> {
    return LinkPoolPage<T> (
        page_no = this.number,
        page_size = this.size,
        total_count = this.totalElements,
        total_page = this.totalPages,
        contents = this.content
    )
}

fun LinkPoolPageRequest.toPageRequest(): PageRequest {
    return PageRequest.of(this.page_no, this.page_size)
}