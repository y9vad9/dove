package com.dove.server.features.users

import com.dove.data.Constants
import com.dove.data.users.User
import com.dove.server.features.Tags
import com.dove.server.features.users.tokens.tokens
import com.dove.server.utils.openapi.get
import com.dove.server.utils.openapi.user
import com.dove.server.utils.openapi.userAuthorized
import com.papsign.ktor.openapigen.annotations.Path
import com.papsign.ktor.openapigen.annotations.parameters.PathParam
import com.papsign.ktor.openapigen.annotations.parameters.QueryParam
import com.papsign.ktor.openapigen.annotations.type.number.integer.max.Max
import com.papsign.ktor.openapigen.annotations.type.string.length.MaxLength
import com.papsign.ktor.openapigen.annotations.type.string.length.MinLength
import com.papsign.ktor.openapigen.route.path.normal.NormalOpenAPIRoute
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
    get<UserByIdRequest, User> {
        UsersAPI.getById(userId)
    }
}

private data class GetByEmailRequest(
    @QueryParam("User email")
    @MaxLength(Constants.EMAIL_MAX_LEN)
    val email: String
)

private fun NormalOpenAPIRoute.getUserByEmailRequest() {
    get<GetByEmailRequest, User> {
        UsersAPI.getByEmail(email)
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
    get<GetUsersRequest, List<User>> {
        UsersAPI.getUsers(query, number, offset)
    }
}

private data class EditUserRequest(
    @MinLength(Constants.FIRST_NAME_MIN)
    @MaxLength(Constants.FIRST_NAME_MAX_LEN)
    @QueryParam("New First name")
    val newFirstName: String? = null,
    @MaxLength(Constants.LAST_NAME_MAX_LEN)
    @QueryParam("New last name")
    val newLastName: String? = null
)

private fun NormalOpenAPIRoute.editUserRequest() = userAuthorized {
    get<EditUserRequest, Unit> {
        UsersAPI.editProfile(user, newFirstName, newLastName)
    }
}