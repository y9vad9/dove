package com.dove.backend.server.api.routings.users

import com.dove.backend.apis.users.UsersAPI
import com.dove.backend.apis.users.tokens.TokensAPI
import com.dove.backend.apis.users.verifications.VerificationsAPI
import com.dove.backend.server.api.routings.Tags
import com.dove.backend.server.api.routings.users.tokens.tokens
import com.dove.backend.server.api.routings.users.verifications.tokenVerifications
import com.dove.data.Constants
import com.dove.data.users.User
import com.papsign.ktor.openapigen.annotations.Path
import com.papsign.ktor.openapigen.annotations.parameters.PathParam
import com.papsign.ktor.openapigen.annotations.parameters.QueryParam
import com.papsign.ktor.openapigen.annotations.type.number.integer.max.Max
import com.papsign.ktor.openapigen.annotations.type.string.length.MaxLength
import com.papsign.ktor.openapigen.annotations.type.string.length.MinLength
import com.papsign.ktor.openapigen.route.path.normal.NormalOpenAPIRoute
import com.papsign.ktor.openapigen.route.route
import com.papsign.ktor.openapigen.route.tag
import com.y9neon.middleware.authorization.Authorization
import com.y9neon.openapi.get
import com.y9neon.openapi.patch

fun NormalOpenAPIRoute.users(
    api: UsersAPI,
    tokensAPI: TokensAPI,
    verificationsAPI: VerificationsAPI,
    feature: Authorization.Feature<User>
) =
    tag(Tags.Users).route("/users") {
        fun userAuthorized(block: UsersAuthorizedScope<NormalOpenAPIRoute>.() -> Unit) {
            block(UsersAuthorizedScope(this, feature))
        }

        @Path("/{userId}")
        data class UserByIdRequest(
            @PathParam("User identifier")
            val userId: Long
        )

        fun getUserByIdRequest() {
            get<UserByIdRequest, User> {
                api.getById(userId)
            }
        }

        data class GetByEmailRequest(
            @QueryParam("User email")
            @MaxLength(Constants.EMAIL_MAX_LEN)
            val email: String
        )

        fun getUserByEmailRequest() {
            get<GetByEmailRequest, User> {
                api.getByEmail(email)
            }
        }

        data class GetUsersRequest(
            @QueryParam("Query")
            val query: String?,
            @QueryParam("Number of rows to load")
            @Max(100)
            val number: Int,
            @QueryParam("Items offset")
            val offset: Long
        )

        fun getUsersRequest() = route("all") {
            get<GetUsersRequest, List<User>> {
                api.getUsers(query, number, offset)
            }
        }

        data class EditUserRequest(
            @MinLength(Constants.FIRST_NAME_MIN)
            @MaxLength(Constants.FIRST_NAME_MAX_LEN)
            @QueryParam("New First name")
            val newFirstName: String? = null,
            @MaxLength(Constants.LAST_NAME_MAX_LEN)
            @QueryParam("New last name")
            val newLastName: String? = null
        )

        fun editUserRequest() = route("edit") {
            userAuthorized {
                patch<EditUserRequest, Unit, Unit> {
                    api.editProfile(user, newFirstName, newLastName)
                }
            }
        }

        getUserByIdRequest()
        getUserByEmailRequest()
        getUsersRequest()
        editUserRequest()

        tokens(tokensAPI, feature)
        tokenVerifications(verificationsAPI)
    }