package com.drini.rickandmorty.core.extensions

import androidx.compose.ui.graphics.Color
import com.drini.rickandmorty.core.theme.StatusAlive
import com.drini.rickandmorty.core.theme.StatusDead
import com.drini.rickandmorty.core.theme.StatusUndefined

const val ALIVE = "Alive"
const val DEAD = "Dead"

fun String.getStatusColor(): Color {
    return when (this) {
        ALIVE -> StatusAlive
        DEAD -> StatusDead
        else -> StatusUndefined
    }
}
