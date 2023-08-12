package linkpool.link.service

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import linkpool.LinkPoolPage
import linkpool.LinkPoolPageRequest
import linkpool.jobgroup.model.JobGroup
import linkpool.jobgroup.port.`in`.JobGroupQuery
import linkpool.link.model.InflowType
import linkpool.link.model.Link
import linkpool.link.port.out.LinkPort
import linkpool.user.model.User
import linkpool.user.model.UserInformation
import linkpool.user.port.`in`.GetUserUseCase

class SearchLinkUseCaseTest: BehaviorSpec({
  val linkPort = mockk<LinkPort>()
  val getUserUseCase = mockk<GetUserUseCase>()
  val jobGroupQuery = mockk<JobGroupQuery>()

  val searchLinkUseCase = SearchLinkService(getUserUseCase, jobGroupQuery, linkPort)

  afterContainer {
    clearAllMocks()
  }
  Given("링크가 3개 주어지고") {
    val uid = "12345"
    val keyword = "피파"
    var myLinksOnly: Boolean = false
    val links = listOf(
      Link(
        id = 1,
        userId = 1,
        title = "피파 온라인의 굴리트의 사기성",
        describe = "중앙에 굴리트 무조건 하나 넣어야 하는 게임",
        url = "링크 URL 1",
        folderId = 0L,
        inflowType = InflowType.BRING
      ),
      Link(
        id = 2,
        userId = 1,
        title = "피파에서는 킹한민국이 월드컵 우승 쌉가능함",
        describe = "박주영이 홀란드보다 좋은게 말이되냐",
        url = "링크 URL 2",
        folderId = 0L,
        inflowType = InflowType.BRING
      ),
      Link(
        id = 3,
        userId = 2,
        title = "피파에서는 킹한민국이 월드컵 우승 쌉가능함",
        describe = "12박주영이 홀란드보다 좋은게 말이되냐",
        url = "링크 URL 2",
        folderId = 0L,
        inflowType = InflowType.BRING
      )
    )

    val paging = LinkPoolPageRequest(
      page_no = 1,
      page_size = 10,
      total_count = 2,
      total_page = 1
    )
    val linkPoolPage = LinkPoolPage(
      page_no = paging.page_no,
      page_size = paging.page_size,
      total_count = paging.total_count,
      total_page = paging.total_page,
      contents = links
    )

    val me = User(
      id = 1L,
      uid = uid,
      info = UserInformation(
        nickname = "대훈",
        jobGroupId = 2L
      )
    )

    val user = User(
      id = 2L,
      uid = "56789",
      info = UserInformation(
        nickname = "다른 사람",
        jobGroupId = 2L
      )
    )

    val myJob = JobGroup(
      id = 0L,
      name ="대훈"
    )

    And("myLinksOnly가 참일 때") {
      myLinksOnly = true
      every { getUserUseCase.getByUid(uid) } returns me
      every { jobGroupQuery.getById(me.info!!.jobGroupId) } returns myJob
      every { linkPort.findPageByUserIdAndTitleContains(1, keyword, paging) } returns linkPoolPage.copy(contents = listOf(links[0], links[1]))

      When("링크를 조회하면") {
        val result = searchLinkUseCase.searchByKeyword(myLinksOnly, uid, keyword, paging)

        Then("아래와 결과를 반환해야 합니다.") {
          result.contents.size shouldBe 2
          result.contents[0].id shouldBe 1
          result.contents[0].user.id shouldBe 1

          result.contents[1].id shouldBe 2
          result.contents[1].user.id shouldBe 1

        }
      }
    }

    And("myLinksOnly가 거짓일 때") {
      myLinksOnly = false
      every { getUserUseCase.getByUid(uid) } returns me
      every { getUserUseCase.getById(1) } returns me
      every { getUserUseCase.getById(2) } returns user
      every { jobGroupQuery.getById(me.info!!.jobGroupId) } returns myJob
      every { linkPort.findPageByTitleContains( keyword, me.id, paging) } returns linkPoolPage

      When("링크를 조회하면") {
        val result = searchLinkUseCase.searchByKeyword(myLinksOnly, uid, keyword, paging)

        Then("아래와 같은 결과를 반환해야 합니다.") {
          result.contents.size shouldBe 3
          result.contents[0].id shouldBe 1
          result.contents[0].user.id shouldBe 1

          result.contents[1].id shouldBe 2
          result.contents[1].user.id shouldBe 1

          result.contents[2].id shouldBe 3
          result.contents[2].user.id shouldBe 2
        }
      }
    }
  }
  }
)
