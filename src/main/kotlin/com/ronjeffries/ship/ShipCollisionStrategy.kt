package com.ronjeffries.ship

class ShipCollisionStrategy(): Collider {
    lateinit var ship: Ship
    override var position: Point = Point.ZERO
    override val killRadius = U.SHIP_KILL_RADIUS

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