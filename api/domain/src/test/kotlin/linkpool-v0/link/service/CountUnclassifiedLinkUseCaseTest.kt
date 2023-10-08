package linkpool.link.service

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import linkpool.link.link.port.out.LinkPort
class CountUnclassifiedLinkUseCaseTest: BehaviorSpec({
	
	Given("CountUnclassifiedLinkService") {
		val linkPort = mockk<LinkPort>()
		val countUnclassifiedLinkService = CountUnclassifiedLinkService(linkPort)
		
		When("countUnClassified 호출 시 LinkPort의 countByUserIdAndFolderIdIsNull 메서드를 호출하면") {
			val userId = 1L
			val expectedCount = 5
			
			every { linkPort.countByUserIdAndFolderIdIsNull(userId) } returns expectedCount
			
			val result = countUnclassifiedLinkService.countUnClassified(userId)
			
			Then("LinkPort의 countByUserIdAndFolderIdIsNull 메서드가 호출되고, 예상된 결과를 반환한다") {
				result shouldBe expectedCount
			}
		}
	}
})