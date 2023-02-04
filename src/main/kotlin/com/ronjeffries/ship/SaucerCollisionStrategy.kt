package com.ronjeffries.ship

class SaucerCollisionStrategy(): Collider {
    lateinit var saucer: Saucer
    override var position: Point = Point.ZERO
    override val killRadius: Double = U.SAUCER_KILL_RADIUS

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

    private fun checkCollision(other: SpaceObject, trans: Transaction) {
        Collision(other).executeOnHit(saucer) {
            trans.add(Splat(saucer))
            trans.remove(saucer)
        }
    }
}