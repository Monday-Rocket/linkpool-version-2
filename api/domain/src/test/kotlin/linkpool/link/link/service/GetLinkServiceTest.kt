package linkpool.link.link.service

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import linkpool.LinkPoolPage
import linkpool.LinkPoolPageRequest
import linkpool.link.link.model.InflowType
import linkpool.link.link.model.Link
import linkpool.link.link.port.out.LinkPort
import linkpool.link.link.service.GetLinksService
import linkpool.user.user.model.User
import linkpool.user.user.port.`in`.GetUserUseCase

class GetLinkServiceTest: BehaviorSpec({
	Given("GetLinkService") {
		val linkPort = mockk<LinkPort>()
		val getLinkService = GetLinksService(linkPort)
		
		When("getByUserId 호출 시 LinkPort의 findPageByUserIdOrderByCreatedDateTimeDesc 메서드를 호출하면") {
			val uid = "user123"
			val paging = LinkPoolPageRequest(1, 10)
			val user = User(id = 1L, uid = uid)
			val links = listOf(
					Link(creatorId = 1L, url = "https://example.com", title = "Example", inflowType = InflowType.BRING),
					Link(creatorId = 2L, url = "https://google.com", title = "Google", inflowType = InflowType.BRING)
			)
			
			val expectedPage = LinkPoolPage(
					page_no = 1,
					page_size = 10,
					total_count = 2,
					total_page = 1,
					contents = links
			)
			
			coEvery { linkPort.findPageByCreatorIdOrderByCreatedDateTimeDesc(user.id, paging) } returns expectedPage
			
			val result = getLinkService.getByCreatorId(1L, paging)
			
			Then("LinkPort의 findPageByUserIdOrderByCreatedDateTimeDesc 메서드가 호출되고, 예상된 결과를 반환한다") {
				coVerify (exactly = 1) { linkPort.findPageByCreatorIdOrderByCreatedDateTimeDesc(user.id, paging) }
				result.contents[0].id shouldBe expectedPage.contents[0].id
			}
		}
	}
})