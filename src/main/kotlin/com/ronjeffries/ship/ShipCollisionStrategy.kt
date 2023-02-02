package com.ronjeffries.ship

class ShipCollisionStrategy(val ship: Ship): Collider {
    override val position: Point
        get() = ship.position
    override val killRadius: Double
        get() = ship.killRadius

    override fun interact(asteroid: Asteroid, trans: Transaction) =
        checkCollision(asteroid, trans)
    override fun interact(missile: Missile, trans: Transaction) =
        checkCollision(missile, trans)
    override fun interact(saucer: Saucer, trans: Transaction) =
        checkCollision(saucer, trans)
    override fun interact(ship: Ship, trans: Transaction) =
        Unit

    private fun checkCollision(other: SpaceObject, trans: Transaction) {
        Collision(other).executeOnHit(ship) {
            trans.add(Splat(ship))
            trans.remove(ship)
        }
    }
}