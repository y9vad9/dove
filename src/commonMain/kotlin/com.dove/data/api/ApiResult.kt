package com.dove.data.api

import com.dove.data.monad.Either

typealias ApiResult<T> = Either<T, ApiError>