package com.openclassrooms.magicgithub.api

import com.openclassrooms.magicgithub.model.User
import java.util.*

object FakeApiServiceGenerator {

    @JvmField
    var FAKE_USERS = mutableListOf(
        User("001", "Jake", "https://robohash.org/Jake"),
        User("002", "Paul", "https://robohash.org/Paul"),
        User("003", "Phil", "https://robohash.org/Phil"),
        User("004", "Guillaume", "https://robohash.org/Guillaume"),
        User("005", "Francis", "https://robohash.org/Francis"),
        User("006", "George", "https://robohash.org/George"),
        User("007", "Louis", "https://robohash.org/Louis"),
        User("008", "Mateo", "https://robohash.org/Mateo"),
        User("009", "April", "https://robohash.org/April"),
        User("010", "Louise", "https://robohash.org/Louise"),
        User("011", "Elodie", "https://robohash.org/Elodie"),
        User("012", "Helene", "https://robohash.org/Helene"),
        User("013", "Fanny", "https://robohash.org/Fanny"),
        User("014", "Laura", "https://robohash.org/Laura"),
        User("015", "Gertrude", "https://robohash.org/Gertrude"),
        User("016", "Chloé", "https://robohash.org/Chloe"),
        User("017", "April", "https://robohash.org/April2"),
        User("018", "Marie", "https://robohash.org/Marie"),
        User("019", "Henri", "https://robohash.org/Henri"),
        User("020", "Rémi", "https://robohash.org/Remi")
    )

    @JvmField
    var FAKE_USERS_RANDOM = Arrays.asList(
        User("021", "Lea", "https://robohash.org/Lea"),
        User("022", "Geoffrey", "https://robohash.org/Geoffrey"),
        User("023", "Simon", "https://robohash.org/Simon"),
        User("024", "André", "https://robohash.org/Andre"),
        User("025", "Leopold", "https://robohash.org/Leopold")
    )
}