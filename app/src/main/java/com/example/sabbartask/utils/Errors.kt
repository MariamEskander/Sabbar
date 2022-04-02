package com.example.sabbartask.utils

//
// Created by Mariam Eskander on 13/08/2021.
// Copyright (c) 2021 Bosta. All rights reserved.
//

sealed class ERRORS {
    object NO_INTRERNET : ERRORS()
    object UN_EXPECTED : ERRORS()
    object UNKNOWN : ERRORS()
    object UN_AUTHORIZED : ERRORS()
    object SESSION_EXPIRED : ERRORS()
    object EMPTY : ERRORS()
}


sealed class ListError {
    object NO_INTRERNET : ListError()
    object UN_EXPECTED : ListError()
    object EMPTY_LIST : ListError()
    object UN_AUTHORIZED : ListError()
    object SESSION_EXPIRED : ListError()
}