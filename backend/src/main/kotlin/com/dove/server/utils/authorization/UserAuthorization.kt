package com.dove.server.utils.authorization

import com.dove.data.users.User
import com.dove.server.middleware.authorization.Authorization

val UserAuthorization = Authorization.Feature<User>()