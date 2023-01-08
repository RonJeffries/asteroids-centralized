package com.ronjeffries.ship

class Score(val score: Int): InteractingSpaceObject {
    override val subscriptions = Subscriptions()
    override fun callOther(other: InteractingSpaceObject, trans: Transaction) {}
    override fun update(deltaTime: Double, trans: Transaction) { }
}
