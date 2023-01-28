package com.ronjeffries.ship

class Collision(private val collider: Collidable) {
    private fun hit(other: Collidable): Boolean
        = collider.position.distanceTo(other.position) < collider.killRadius + other.killRadius

    fun executeOnHit(other: Collidable, action: () -> Unit) {
        if (hit(other)) action()
    }
}
