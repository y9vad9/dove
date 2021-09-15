package com.dove.server.features.users.verifications.storage

import com.dove.data.users.verifications.Verification
import com.dove.data.users.verifications.VerificationType
import com.dove.server.utils.time.timeInMs
import org.jetbrains.annotations.TestOnly

interface VerificationsStorage {

    companion object Default : VerificationsStorage by DatabaseVerificationsStorage

    /**
     * Creates new verification field in storage.
     * @param email - email that will be confirmed by user.
     * @param code - code that was sent to user.
     * @param type - verification type.
     * @param time - time in milliseconds
     */
    suspend fun create(email: String, code: String, type: VerificationType, time: Long = timeInMs)

    /**
     * Gets [Verification] by [email] & [code] & [type].
     * @return found [Verification] or null if not found.
     */
    suspend fun read(email: String, code: String, type: VerificationType?): Verification?

    /**
     * Deletes verification by [email] & [code] & [type].
     */
    suspend fun delete(email: String, code: String, type: VerificationType)

    /**
     * Deletes everything in storage.
     */
    @TestOnly
    suspend fun deleteAll()
}