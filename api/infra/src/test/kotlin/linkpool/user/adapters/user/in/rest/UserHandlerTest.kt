package linkpool.user.adapters.user.`in`.rest

import io.kotest.core.spec.style.BehaviorSpec
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
@ActiveProfiles("integration")
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserHandlerTest(
    private val webTestClient: WebTestClient,
): BehaviorSpec({
  Given("내 프로필 조회") {
    When("local-tester라는 아이디를 조회하면") {
      val principalId = "local-tester"
      Then("PK는 1이고 아이디가 일치해야한다.")
      webTestClient.get()
          .uri("/users/me")
          .exchange()
          .expectStatus().isOk
          .expectBody()
          .jsonPath("$.data.id").isEqualTo(1)
          .jsonPath("$.data.nickname").isEqualTo(principalId)
    }
  }
})