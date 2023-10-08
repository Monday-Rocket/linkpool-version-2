package linkpool.link.folder.model

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import linkpool.link.folder.model.Folder

class FolderTest : BehaviorSpec({
  Given("폴더의 주인이 아닌 경우") {
    val other = 2L
    val otherFolder = Folder(ownerId = 1L, name = "")
    When("내 폴더인지 확인하면 ") {
      val result = otherFolder.isOwner(other)
      Then("`FALSE`가 반환된다.") {
        result shouldBe false
      }
    }
    And("폴더의 주인인 경우") {
      val me = 1L
      val myFolder = Folder(ownerId = 1L, name = "")
      When("내 폴더인지 확인하면 ") {
        val result = myFolder.isOwner(me)
        Then("`TRUE`가 반환된다.") {
          result shouldBe true
        }
      }
    }
  }
})