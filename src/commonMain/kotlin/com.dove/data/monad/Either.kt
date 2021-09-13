package com.dove.data.monad

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

sealed interface Either<out TSuccess, out TError> {
    class Success<TSuccess>(val value: TSuccess) : Either<TSuccess, Nothing>
    class Failure<TError>(val error: TError) : Either<Nothing, TError>

    companion object {
        fun <TSuccess> success(value: TSuccess): Either<TSuccess, Nothing> {
            return Success(value)
        }

        fun <TError> error(error: TError): Either<Nothing, TError> {
            return Failure(error)
        }
    }
}

@OptIn(ExperimentalContracts::class)
fun <TSuccess, TError> Either<TSuccess, TError>.isSuccess(): Boolean {
    contract {
        returns(true) implies (this@isSuccess is Either.Success<TSuccess>)
        returns(false) implies (this@isSuccess is Either.Failure<TError>)
    }
    return this is Either.Success<TSuccess>
}

fun <TSuccess, TError> Either<TSuccess, TError>.valueOrNull() = (this as? Either.Success)?.value
fun <TSuccess, TError> Either<TSuccess, TError>.errorOrNull() = (this as? Either.Failure)?.error

fun <TSuccess, TError> Either<TSuccess, TError>.valueOrThrow(): TSuccess =
    valueOrNull() ?: throw NullPointerException("`value` is null")

fun <TSuccess, TError> Either<TSuccess, TError>.errorOrThrow(): TError =
    errorOrNull() ?: throw NullPointerException("`error` is null")

@OptIn(ExperimentalContracts::class)
inline fun <TSuccess, TError> Either<TSuccess, TError>.onSuccess(block: (TSuccess) -> Unit): Either<TSuccess, TError> {
    contract {
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
    }
    if (isSuccess())
        block(value)
    return this
}

@OptIn(ExperimentalContracts::class)
fun <TSuccess, TError, RSuccess, RError> Either<TSuccess, TError>.map(
    block: (Either<TSuccess, TError>) -> Either<RSuccess, RError>
): Either<RSuccess, RError> {
    contract {
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
    }
    return block(this)
}

@OptIn(ExperimentalContracts::class)
inline fun <TSuccess, TError> Either<TSuccess, TError>.onError(block: (TError) -> Unit): Either<TSuccess, TError> {
    contract {
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
    }
    if (!isSuccess())
        block(error)
    return this
}