package com.ronjeffries.ship

class Collision(private val collider: SpaceObject) {
    private fun hit(other: SpaceObject): Boolean
        = collider.position.distanceTo(other.position) < collider.killRadius + other.killRadius

    fun executeOnHit(other: SpaceObject, action: () -> Unit) {
        if (hit(other)) action()
    }
}
