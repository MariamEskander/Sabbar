package com.example.sabbartask.ui.features.userDriver

import com.example.sabbartask.data.remote.entities.FirebaseData


class UserDriverContract {
    data class State(
        val isUser: Boolean?
    )

    data class Data(
        val data: FirebaseData?
    )
}