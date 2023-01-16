package com.ronjeffries.ship

interface SpaceObject {
    val subscriptions: Subscriptions
    fun update(deltaTime: Double, trans: Transaction)
}
