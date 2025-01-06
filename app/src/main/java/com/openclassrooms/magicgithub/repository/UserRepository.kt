package com.openclassrooms.magicgithub.repository

import com.openclassrooms.magicgithub.api.ApiService
import com.openclassrooms.magicgithub.model.User

class UserRepository(
    private val apiService: ApiService
) {
    // Cette méthode doit retourner la liste des utilisateurs depuis l'apiService
    fun getUsers(): List<User> {
        return apiService.getUsers()
    }

    // Cette méthode doit ajouter un utilisateur aléatoire via l'apiService
    fun addRandomUser() {
        apiService.addRandomUser()
    }

    // Cette méthode doit supprimer l'utilisateur spécifié via l'apiService
    fun deleteUser(user: User) {
        apiService.deleteUser(user)
    }
}