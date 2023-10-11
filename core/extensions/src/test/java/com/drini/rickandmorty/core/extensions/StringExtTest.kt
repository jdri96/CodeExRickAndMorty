package com.drini.rickandmorty.core.extensions

import com.drini.rickandmorty.core.theme.StatusAlive
import com.drini.rickandmorty.core.theme.StatusDead
import com.drini.rickandmorty.core.theme.StatusUndefined
import org.junit.Assert.assertEquals
import org.junit.Test

internal class StringExtKtTest {

    @Test
    fun `getStatusColor with ALIVE should return StatusAlive color`() {
        val color = ALIVE.getStatusColor()
        assertEquals(StatusAlive, color)
    }

    @Test
    fun `getStatusColor with DEAD should return StatusDead color`() {
        val color = DEAD.getStatusColor()
        assertEquals(StatusDead, color)
    }

    @Test
    fun `getStatusColor with undefined status should return StatusUndefined color`() {
        val undefinedStatus = "UndefinedStatus"
        val color = undefinedStatus.getStatusColor()
        assertEquals(StatusUndefined, color)
    }

    companion object {
        const val ALIVE = "Alive"
        const val DEAD = "Dead"
    }
}