package linkpool.link.link.service

import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.*
import linkpool.link.link.model.InflowType
import linkpool.link.link.model.Link
import linkpool.link.link.port.`in`.SaveLinkRequest
import linkpool.link.link.port.out.LinkPort
import linkpool.user.user.model.User
import linkpool.user.user.port.`in`.GetUserUseCase
import java.time.LocalDateTime

class CreateLinkServiceTest: BehaviorSpec({
  val linkPort = mockk<LinkPort>()
  val getUserUseCase = mockk<GetUserUseCase>()

  val createLinkService = CreateLinkService(linkPort)

  afterContainer {
    clearAllMocks()
  }
  Given("링크 저장 유즈케이스에") {
    val uid = "12345"
    val user = User(
      uid = uid
    )

    val request = SaveLinkRequest(
      url = "linkpool.co.kr",
      title = "링크풀임",
      describe = null,
      image = null,
      folderId = 1,
      inflowType = InflowType.CREATE
      )
    val link = Link(
      creatorId = user.id,
      url = request.url,
      title = request.title,
      describe = request.describe,
      image = request.image,
      folderId = request.folderId,
      createdDateTime = request.createdAt ?: LocalDateTime.now(),
      inflowType = request.inflowType ?: InflowType.CREATE
    )

    coEvery { linkPort.save(any()) }  returns link

    When("요청을 보낼 시") {
      createLinkService.create(1L, request)

      Then("링크가 저장이 된다") {
        coVerify(exactly = 1) { linkPort.save(any()) }
      }
    }
  }
}
) {

}
