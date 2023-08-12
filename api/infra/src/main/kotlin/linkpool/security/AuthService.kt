package linkpool.security

import com.google.api.core.ApiFuture
import com.google.firebase.auth.FirebaseAuth
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

@Service
class AuthService(
    private val firebaseAuth: FirebaseAuth
) {
    fun getNameByToken(token: String): Mono<String> {
        return firebaseAuth.verifyIdTokenAsync(token).toMono()
            .map { it -> it.uid }
            .onErrorMap { _ -> UnauthorizationException() }
    }

    fun <F : Any?> ApiFuture<F>.toMono(): Mono<F> {
        return Mono.fromCallable {
            try {
                return@fromCallable this.get()
            } catch (e: Exception) {
                throw RuntimeException(e)
            }
        }.subscribeOn(Schedulers.boundedElastic())
    }



}
