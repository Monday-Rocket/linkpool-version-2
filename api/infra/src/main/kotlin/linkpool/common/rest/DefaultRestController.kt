package linkpool.common.rest

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class DefaultRestController {
    @GetMapping
    suspend fun getDefault(): ApiResponse<Unit> {
        return ApiResponse.success()
    }
}
