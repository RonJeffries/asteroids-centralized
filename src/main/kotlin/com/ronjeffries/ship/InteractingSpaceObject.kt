package com.ronjeffries.ship

interface InteractingSpaceObject: SpaceObject {
    val subscriptions: Subscriptions
    fun callOther(other: InteractingSpaceObject, trans: Transaction)
    fun update(deltaTime: Double, trans: Transaction)
}
