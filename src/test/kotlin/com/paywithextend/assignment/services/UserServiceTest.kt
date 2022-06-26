package com.paywithextend.assignment.services

import com.paywithextend.assignment.clients.SignInClient
import com.paywithextend.assignment.models.UserModel
import com.paywithextend.assignment.repositories.UserRepositoryInterface
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.time.Instant
import java.util.UUID

@SpringBootTest
internal class UserServiceTest {

    @Autowired private lateinit var userService: UserService

    @MockBean private lateinit var signInClient: SignInClient

    @MockBean private lateinit var userRepository: UserRepositoryInterface

    private val now: Instant = Instant.now()

    private val userId = UUID.randomUUID()

    @Test
    fun `should add new user`() {

        val user = UserModel(id = userId, extendEmail = "name.sur@aol.com", extendPassword = "p@ss", extendToken = null)
        val signedInUser = user.copy(extendToken = UserModel.ExtendToken(lastUpdated = now, value = "abc.def.ghi"))

        Mockito.`when`(userRepository.exists(userId))
               .thenReturn(Mono.just(false))
        Mockito.`when`(signInClient.signIn(user))
               .thenReturn(Mono.just(signedInUser))
        Mockito.`when`(userRepository.add(signedInUser))
               .thenReturn(Mono.just(true))

        StepVerifier.create(userService.addUser(user))
            .expectNext(true)
    }

    @Test
    fun `should not add already existent user`() {
        val user = UserModel(id = userId, extendEmail = "name.sur@aol.com", extendPassword = "p@ss", extendToken = null)

        Mockito.`when`(userRepository.exists(userId))
            .thenReturn(Mono.just(true))

        StepVerifier.create(userService.addUser(user))
//            .expectErrorMessage("User already exists. Trying to update it? Consider making a PUT request")
            .expectError(ResponseStatusException::class.java)
    }

    @Test
    fun `should retrieve already signed in user without needing to call the client`() {
        val user = UserModel(id = userId, extendEmail = "name.sur@aol.com", extendPassword = "p@ss",
                             extendToken = UserModel.ExtendToken(lastUpdated = now, value = "abc.def.ghi"))

        Mockito.`when`(userRepository.findById(userId))
            .thenReturn(Mono.just(user))

        StepVerifier.create(userService.getSignedInUser(user.id))
            .expectNext(user)

        Mockito.verify(signInClient, Mockito.never()).signIn(user)
    }

    @Test
    fun `should retrieve signed in user with new token`() {
        val user = UserModel(id = userId, extendEmail = "name.sur@aol.com", extendPassword = "p@ss",
            extendToken = UserModel.ExtendToken(lastUpdated = now.minusSeconds(20*60), value = "abc.def.ghi"))

        val signedInUser = user.copy(extendToken = UserModel.ExtendToken(lastUpdated = now, value = "def.ghi.jkl"))

        Mockito.`when`(userRepository.findById(userId))
            .thenReturn(Mono.just(user))
        Mockito.`when`(signInClient.signIn(user))
            .thenReturn(Mono.just(signedInUser))
        Mockito.`when`(userRepository.replace(signedInUser))
            .thenReturn(Mono.just(true))

        StepVerifier.create(userService.getSignedInUser(user.id))
            .expectNext(signedInUser)
    }

    @Test
    fun `should return exception when querying nonexistent user`() {
        Mockito.`when`(userRepository.findById(userId))
            .thenReturn(Mono.empty())

        StepVerifier.create(userService.getSignedInUser(userId))
            .expectError(ResponseStatusException::class.java)
    }

    @Test
    fun `should replace user and update its token`() {
        val userToReplace = UserModel(id = userId, extendEmail = "name.sur@aol.com", extendPassword = "p@ss", extendToken = null)
        val signedInUser = userToReplace.copy(extendToken = UserModel.ExtendToken(lastUpdated = now, value = "abc.def.ghi"))

        Mockito.`when`(userRepository.exists(userId))
            .thenReturn(Mono.just(true))
        Mockito.`when`(signInClient.signIn(userToReplace))
            .thenReturn(Mono.just(signedInUser))
        Mockito.`when`(userRepository.replace(signedInUser))
            .thenReturn(Mono.just(true))

        StepVerifier.create(userService.replaceUser(userToReplace))
            .expectNext(true)
    }

    @Test
    fun `should not replace nonexistent user`() {
        val userToReplace = UserModel(id = userId, extendEmail = "name.sur@aol.com", extendPassword = "p@ss", extendToken = null)

        Mockito.`when`(userRepository.exists(userId))
            .thenReturn(Mono.just(false))

        StepVerifier.create(userService.replaceUser(userToReplace))
            .expectError(ResponseStatusException::class.java)
    }

    @Test
    fun `should not replace user with wrong credentials`() {
        val userToReplace = UserModel(id = userId, extendEmail = "name.sur@aol.com", extendPassword = "p@ss", extendToken = null)

        Mockito.`when`(userRepository.exists(userId))
            .thenReturn(Mono.just(true))
        Mockito.`when`(signInClient.signIn(userToReplace))
            .thenReturn(Mono.error(ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong email or password")))

        StepVerifier.create(userService.replaceUser(userToReplace))
            .expectError(ResponseStatusException::class.java)
    }

    @Test
    fun `should remove existent user`() {

        Mockito.`when`(userRepository.remove(userId))
            .thenReturn(Mono.just(true))

        StepVerifier.create(userService.removeUser(userId))
            .expectNext(true)
    }

    @Test
    fun `should not remove nonexistent user`() {

        Mockito.`when`(userRepository.remove(userId))
            .thenReturn(Mono.just(false))

        StepVerifier.create(userService.removeUser(userId))
            .expectError(ResponseStatusException::class.java)
    }
}
