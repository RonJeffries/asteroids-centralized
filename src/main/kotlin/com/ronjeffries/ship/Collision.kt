package com.ronjeffries.ship

class Collision(private val collider: Collider) {
    fun hit(other: Collider): Boolean
        = collider.position.distanceTo(other.position) < collider.killRadius + other.killRadius

    fun executeOnHit(other: Collider, action: () -> Unit) {
        if (hit(other)) action()
    }
}
