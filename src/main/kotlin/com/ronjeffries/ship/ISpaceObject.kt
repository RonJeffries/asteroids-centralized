package com.ronjeffries.ship

interface ISpaceObject: InteractingSpaceObject {
    val subscriptions: Subscriptions
    fun callOther(other: ISpaceObject, trans: Transaction)
    fun update(deltaTime: Double, trans: Transaction)
}
