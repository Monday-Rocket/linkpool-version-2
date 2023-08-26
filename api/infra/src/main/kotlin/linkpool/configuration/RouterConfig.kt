package linkpool.configuration

import linkpool.adapters.folder.`in`.rest.FolderHandler
import linkpool.adapters.link.`in`.rest.LinkHandler
import linkpool.adapters.user.`in`.rest.UserHandler
import linkpool.common.rest.DefaultHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class RouterConfig(
    private val userHandler: UserHandler,
    private val linkHandler: LinkHandler,
    private val folderHandler: FolderHandler,
    private val defaultHandler: DefaultHandler
) {

    @Bean
    fun apiRouter(): RouterFunction<ServerResponse> {
        return coRouter {
            (accept(MediaType.APPLICATION_JSON)).nest {
                "/users".nest {
                    POST("", userHandler::createUser)
                    PATCH("/me", userHandler::updateMyInfo)
                    GET("/me", userHandler::getMyInformation)
                    GET("/{userId}", userHandler::getUserInfoById)
//                    GET("/{userId}/folders", userHandler::getFoldersByUserId)
                    DELETE("", userHandler::signOut)
                    HEAD("", queryParam("nickname") { _: String? -> true }, userHandler::checkIfExistsByNickname)
                }
                "/links".nest {
                    POST("", linkHandler::create)
                    PATCH("/{linkId}", linkHandler::update)
                    DELETE("/{linkId}", linkHandler::delete)
                    GET("", linkHandler::getByUserId)
                    GET("/unclassified", linkHandler::getLinksOfFolder)
                    GET("/search", linkHandler::searchLinkByKeyword)
                    GET("/my_link/search/", linkHandler::searchMyLinkByKeyword)
                }
                "/folders".nest {
                    POST("", folderHandler::create)
                    PATCH("/{folderId}", folderHandler::update)
                    DELETE("/{folderId}", folderHandler::delete)
                    GET("", folderHandler::getByUserId)
                    GET("/{folderId}/links", linkHandler::getLinksOfFolder)
                }
                GET ("", defaultHandler::getDefault)
            }

        }
    }
}