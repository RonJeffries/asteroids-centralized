package com.ronjeffries.ship

class SaucerCollisionStrategy(val saucer: Saucer): Collider {
    override val position: Point
        get() = saucer.position
    override val killRadius: Double
        get() = saucer.killRadius

    override fun interact(asteroid: Asteroid, trans: Transaction) =
        checkCollision(asteroid, trans)
    override fun interact(missile: Missile, trans: Transaction) {
        saucer.inspectMissile(missile)
        checkCollision(missile, trans)
    }
    override fun interact(saucer: Saucer, trans: Transaction) =
        Unit
    override fun interact(ship: Ship, trans: Transaction) {
        saucer.inspectShip(ship)
        checkCollision(ship, trans)
    }

    private fun checkCollision(collider: Collidable, trans: Transaction) {
        Collision(collider).executeOnHit(saucer) {
            trans.add(Splat(saucer))
            trans.remove(saucer)
        }
    }
}