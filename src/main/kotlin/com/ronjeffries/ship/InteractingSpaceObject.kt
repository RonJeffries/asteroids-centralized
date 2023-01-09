package com.ronjeffries.ship

interface SpaceObject {
    val subscriptions: Subscriptions
    fun callOther(other: SpaceObject, trans: Transaction)
    fun update(deltaTime: Double, trans: Transaction)
}
