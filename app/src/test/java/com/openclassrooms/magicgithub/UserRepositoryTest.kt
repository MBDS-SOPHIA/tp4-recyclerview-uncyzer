package com.openclassrooms.magicgithub

import com.openclassrooms.magicgithub.api.FakeApiServiceGenerator.FAKE_USERS
import com.openclassrooms.magicgithub.api.FakeApiServiceGenerator.FAKE_USERS_RANDOM
import com.openclassrooms.magicgithub.di.Injection
import com.openclassrooms.magicgithub.model.User
import com.openclassrooms.magicgithub.repository.UserRepository
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * Unit test, which will execute on a JVM.
 * Testing UserRepository.
 */
@RunWith(JUnit4::class)
class UserRepositoryTest {
    private lateinit var userRepository: UserRepository

    @Before
    fun setup() {
        userRepository = Injection.getRepository()
    }

    @Test
    fun getUsersWithSuccess() {
        val usersActual = userRepository.getUsers()
        val usersExpected: List<User> = FAKE_USERS
        assertEquals(usersActual, usersExpected)
    }

    @Test
    fun generateRandomUserWithSuccess() {
        val initialSize = userRepository.getUsers().size
        userRepository.addRandomUser()
        val user = userRepository.getUsers().last()
        assertEquals(userRepository.getUsers().size, initialSize + 1)
        assertTrue(FAKE_USERS_RANDOM.any { it == user })
        assertTrue(user.isActive) // Vérifier que le nouvel utilisateur est actif par défaut
    }

    @Test
    fun toggleUserActiveStateWithSuccess() {
        val user = userRepository.getUsers()[0]
        val initialState = user.isActive
        user.isActive = !initialState
        assertTrue(user.isActive != initialState)
    }

    @Test
    fun deleteUserWithSuccess() {
        val userToDelete = userRepository.getUsers()[0]
        userRepository.deleteUser(userToDelete)
        assertFalse(userRepository.getUsers().contains(userToDelete))
    }

    @Test
    fun updateUserPositionWithSuccess() {
        val users = userRepository.getUsers()
        val firstUser = users[0]
        val secondUser = users[1]

        userRepository.updateUserPosition(0, 1)

        val updatedUsers = userRepository.getUsers()
        assertEquals(secondUser, updatedUsers[0])
        assertEquals(firstUser, updatedUsers[1])
    }
}