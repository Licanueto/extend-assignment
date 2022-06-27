package com.paywithextend.assignment.controllers

import com.paywithextend.assignment.models.UserModel
import com.paywithextend.assignment.repositories.UserRepositoryInterface
import com.paywithextend.assignment.services.UserService
import org.junit.jupiter.api.Test

import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import reactor.core.publisher.Mono
import java.time.Instant
import java.util.UUID

@WebFluxTest(UserController::class)
internal class UserControllerTest {

    @Autowired
    private lateinit var client: WebTestClient

    @MockBean private lateinit var userServiceMock:UserService

    @MockBean private lateinit var userRepositoryMock:UserRepositoryInterface

    private val now: Instant = Instant.now()

    private val userId: UUID = UUID.randomUUID()

    @Test
    fun `should get all users without exposing their token and password`() {

        val repositoryResponse: Mono<List<UserModel>> = Mono.just(listOf(
            UserModel(id = userId, extendEmail = "licanueto@paywithextend.com", extendPassword = "p@ss",
                extendToken = UserModel.ExtendToken(lastUpdated = now, value = "abc.def.ghi")),
            UserModel(id = userId, extendEmail = "john.cena@paywithextend.com", extendPassword = "its",
                extendToken = UserModel.ExtendToken(lastUpdated = now, value = "abc.def.ghi"))))

        val expected: String = """
            [
                {
                    "id": "$userId",
                    "extendEmail": "licanueto@paywithextend.com",
                    "extendPassword": "*****",
                    "extendToken": {
                        "lastUpdated": "${now.toString()}",
                        "value": "***.***.***"
                    }
                },
                {
                    "id": "$userId",
                    "extendEmail": "john.cena@paywithextend.com",
                    "extendPassword": "*****",
                    "extendToken": {
                        "lastUpdated": "${now.toString()}",
                        "value": "***.***.***"
                    }
                }
            ] """.trimIndent()

        Mockito.`when`(userRepositoryMock.getAllUsers())
               .thenReturn(repositoryResponse)

        client
            .get()
            .uri("/users")
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.OK)
            .expectBody().json(expected)
    }

    @Test
    fun `should get the user without exposing its token and password`() {

        val repositoryResponse: Mono<UserModel> = Mono.just(
            UserModel(id = userId, extendEmail = "licanueto@paywithextend.com", extendPassword = "p@ss",
                extendToken = UserModel.ExtendToken(lastUpdated = now, value = "abc.def.ghi")))

        val expected: String = """
            {
                "id": "$userId",
                "extendEmail": "licanueto@paywithextend.com",
                "extendPassword": "*****",
                "extendToken": {
                    "lastUpdated": "${now.toString()}",
                    "value": "***.***.***"
                }
            }""".trimIndent()

        Mockito.`when`(userRepositoryMock.findById(userId))
            .thenReturn(repositoryResponse)

        client
            .get()
            .uri("/user/$userId")
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.OK)
            .expectBody().json(expected)
    }

    @Test
    fun `should invoke the creation of the right user`() {

        val userSentInPOSTBody: Mono<String> = Mono.just("""
            {
                "id": "$userId",
                "extendEmail": "licanueto@paywithextend.com",
                "extendPassword": "asdfghj"
            }""".trimIndent())

        val expectedUserToAdd = UserModel(
            id = userId,
            extendEmail = "licanueto@paywithextend.com",
            extendPassword = "asdfghj",
            extendToken = null)

        Mockito.`when`(userServiceMock.addUser(expectedUserToAdd))
               .thenReturn(Mono.just(true))

        client
            .post()
            .uri("/user")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromPublisher(userSentInPOSTBody, String::class.java))
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.CREATED)

        Mockito.verify(userServiceMock, Mockito.atLeastOnce()).addUser(expectedUserToAdd)
    }

    @Test
    fun `should invoke the replacement of the right user`() {
        val userSentInPUTBody: Mono<String> = Mono.just("""
            {
                "id": "$userId",
                "extendEmail": "licanueto@paywithextend.com",
                "extendPassword": "asdfghj"
            }""".trimIndent())

        val expectedUserToReplace = UserModel(
            id = userId,
            extendEmail = "licanueto@paywithextend.com",
            extendPassword = "asdfghj",
            extendToken = null)

        Mockito.`when`(userServiceMock.replaceUser(expectedUserToReplace))
            .thenReturn(Mono.just(true))

        client
            .put()
            .uri("/user")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromPublisher(userSentInPUTBody, String::class.java))
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.NO_CONTENT)

        Mockito.verify(userServiceMock, Mockito.atLeastOnce()).replaceUser(expectedUserToReplace)
    }

    @Test
    fun `should invoke the removal of the user`() {

        Mockito.`when`(userServiceMock.removeUser(userId))
            .thenReturn(Mono.just(true))

        client
            .delete()
            .uri("/user/$userId")
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.NO_CONTENT)

        Mockito.verify(userServiceMock, Mockito.atLeastOnce()).removeUser(userId)
    }
}
