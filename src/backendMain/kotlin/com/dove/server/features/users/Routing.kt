package com.dove.server.features.users

import com.dove.data.monad.onError
import com.dove.data.monad.onSuccess
import com.dove.data.users.User
import com.dove.server.features.Tags
import com.dove.server.features.users.tokens.tokens
import com.dove.server.utils.openapi.respondError
import com.papsign.ktor.openapigen.annotations.Path
import com.papsign.ktor.openapigen.annotations.parameters.HeaderParam
import com.papsign.ktor.openapigen.annotations.parameters.PathParam
import com.papsign.ktor.openapigen.annotations.parameters.QueryParam
import com.papsign.ktor.openapigen.annotations.type.number.integer.max.Max
import com.papsign.ktor.openapigen.route.path.normal.NormalOpenAPIRoute
import com.papsign.ktor.openapigen.route.path.normal.get
import com.papsign.ktor.openapigen.route.response.respond
import com.papsign.ktor.openapigen.route.route
import com.papsign.ktor.openapigen.route.tag

fun NormalOpenAPIRoute.users() = tag(Tags.Users).route("/users") {
    getUserByIdRequest()
    getUserByEmailRequest()
    getUsersRequest()
    editUserRequest()

    tokens()
}


@Path("/{userId}")
private data class UserByIdRequest(
    @PathParam("User identifier")
    val userId: Long
)

private fun NormalOpenAPIRoute.getUserByIdRequest() {
    get<UserByIdRequest, User> { parameters ->
        UsersAPI.getById(parameters.userId).onSuccess {
            respond(it)
        }.onError {
            respondError(it)
        }
    }
}

private data class GetByEmailRequest(
    @QueryParam("User email")
    val email: String
)

private fun NormalOpenAPIRoute.getUserByEmailRequest() {
    get<GetByEmailRequest, User> { parameters ->
        UsersAPI.getByEmail(parameters.email).onSuccess {
            respond(it)
        }.onError {
            respondError(it)
        }
    }
}

private data class GetUsersRequest(
    @QueryParam("Query")
    val query: String?,
    @QueryParam("Number of rows to load")
    @Max(100)
    val number: Int,
    @QueryParam("Items offset")
    val offset: Long
)

private fun NormalOpenAPIRoute.getUsersRequest() {
    get<GetUsersRequest, List<User>> { parameters ->
        parameters.apply {
            UsersAPI.getUsers(query, number, offset).onSuccess {
                respond(it)
            }.onError {
                respondError(it)
            }
        }
    }
}

private data class EditUserRequest(
    @HeaderParam("token")
    val token: String,
    @QueryParam("New First name")
    val newFirstName: String? = null,
    @QueryParam("New last name")
    val newLastName: String? = null
)

private fun NormalOpenAPIRoute.editUserRequest() {
    get<EditUserRequest, Unit> { parameters ->
        parameters.apply {
            UsersAPI.editProfile(token, newFirstName, newLastName).onSuccess {
                respond(it)
            }.onError {
                respondError(it)
            }
        }
    }
}