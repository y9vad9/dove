package com.dove.server.features.users.verifications.storage

import com.dove.data.users.verifications.Verification
import com.dove.data.users.verifications.VerificationType

class MockedVerificationsStorage : VerificationsStorage {
    private val verifications: MutableList<Verification> = mutableListOf()

    override suspend fun create(email: String, code: String, type: VerificationType, time: Long) {
        verifications += Verification(email, code, type, time)
    }

    override suspend fun read(email: String, code: String, type: VerificationType?): Verification? {
        return verifications.firstOrNull { it.email == email && it.code == code && if (type != null) it.type == type else true }
    }

    override suspend fun delete(email: String, code: String, type: VerificationType) {
        verifications.removeIf { it.email == email && it.code == code && it.type == type }
    }

    override suspend fun deleteAll() {
        verifications.clear()
    }
}