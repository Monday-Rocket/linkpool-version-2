package linkpool.configuration

import linkpool.user.adapters.jobgroup.`in`.rest.JobGroupHandler
import linkpool.report.adapters.report.`in`.rest.ReportHandler
import linkpool.link.adapters.folder.`in`.rest.FolderHandler
import linkpool.link.adapters.link.`in`.rest.LinkHandler
import linkpool.user.adapters.user.`in`.rest.UserHandler
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
    private val jobGroupHandler: JobGroupHandler,
    private val reportHandler: ReportHandler,
    private val defaultHandler: DefaultHandler,
) {

    @Bean
    fun apiRouter(): RouterFunction<ServerResponse> {
        return coRouter {
            (accept(MediaType.APPLICATION_JSON)).nest {
                "/users".nest {
                    POST("", userHandler::createUser)
                    PATCH("/me", userHandler::updateProfile)
                    GET("/me", userHandler::getMyProfile)
                    GET("/{userId}", userHandler::getProfileById)
                    GET("/{userId}/folders", folderHandler::getByOwnerId)
                    DELETE("", userHandler::signOut)
                    HEAD("", queryParam("nickname") { _: String? -> true }, userHandler::checkIfExistsByNickname)
                }
                "/links".nest {
                    POST("", linkHandler::create)
                    PATCH("/{linkId}", linkHandler::update)
                    DELETE("/{linkId}", linkHandler::delete)
                    GET("", linkHandler::getByCreatorId)
                    GET("/unclassified", linkHandler::getMyUnclassifiedLinks)
                    GET("/search", queryParam("my_links_only") { value -> !value.toBoolean() }, linkHandler::searchLinkByKeyword)
                    GET("/search", queryParam("my_links_only") { value -> value.toBoolean() },linkHandler::searchMyLinkByKeyword)
                }
                "/folders".nest {
                    POST("", folderHandler::create)
                    PATCH("/{folderId}", folderHandler::update)
                    DELETE("/{folderId}", folderHandler::delete)
                    GET("", folderHandler::getByOwnerId)
                    GET("/{folderId}/links", linkHandler::getLinksOfFolder)
                }
                "/job-groups".nest {
                    GET("", jobGroupHandler::getJobGroups)
                    GET("/{jobGroupId}/links", jobGroupHandler::getLinkByJobGroups)
                    GET("/links", jobGroupHandler::getLinks)
                }
                "/reports".nest {
                    POST("", reportHandler::report)
                }
                GET ("", defaultHandler::getDefault)
            }

        }
    }
}