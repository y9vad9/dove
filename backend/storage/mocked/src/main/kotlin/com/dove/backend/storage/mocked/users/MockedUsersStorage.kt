package com.dove.backend.storage.mocked.users

import com.dove.backend.storage.core.users.UsersStorage
import com.dove.data.users.User
import com.dove.extensions.limit

class MockedUsersStorage : UsersStorage {
    private val users: MutableList<User> = mutableListOf()

    override suspend fun read(id: Long): User? {
        return users.firstOrNull { it.id == id }
    }

    override suspend fun read(email: String): User? {
        return users.firstOrNull { it.email == email }
    }

    override suspend fun readAll(ids: Collection<Long>): List<User> {
        return ids.map { read(it)!! }
    }

    override suspend fun readAll(query: String?, number: Int, offset: Long): List<User> {
        return users.filter {
            if (query != null) {
                ((it.firstName + it.lastName)).contains(query)
            } else true
        }.limit(offset.toInt()..(offset + number).toInt())
    }

    override suspend fun create(email: String): User {
        val user = User((users.lastOrNull()?.id ?: 0) + 1, "User", null, email)
        users += user
        return user
    }

    override suspend fun delete(id: Long) {
        users.removeIf { it.id == id }
    }

    override suspend fun deleteAll() {
        users.clear()
    }

    override suspend fun update(id: Long, newFirstName: String?, newLastName: String?) {
        val found = read(id) ?: error("User not found")
        val index = users.indexOf(found)
        users[index] = found.copy(firstName = newFirstName ?: found.firstName, lastName = newLastName ?: found.lastName)
    }
}