package com.ronjeffries.ship

class ShipCollisionStrategy(val ship: Ship): Collider {
    override val position: Point
        get() = ship.position
    override val killRadius: Double
        get() = ship.killRadius
    override val collisionStrategy: Collider
        get() = this
    override fun interact(asteroid: Asteroid, trans: Transaction) = checkCollision(asteroid, trans)
    override fun interact(missile: Missile, trans: Transaction) = checkCollision(missile, trans)
    override fun interact(saucer: Saucer, trans: Transaction) = checkCollision(saucer, trans)
    override fun interact(ship: Ship, trans: Transaction) { }

    override fun interactWith(other: Collider, trans: Transaction) {}

    private fun checkCollision(other: Collider, trans: Transaction) {
        Collision(other).executeOnHit(ship) {
            trans.add(Splat(ship))
            trans.remove(ship)
        }
    }
}