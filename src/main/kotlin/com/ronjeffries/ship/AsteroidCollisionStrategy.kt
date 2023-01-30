package com.ronjeffries.ship

class AsteroidCollisionStrategy(val asteroid: Asteroid): Collider {
    override val position: Point
        get() = asteroid.position
    override val killRadius: Double
        get() = asteroid.killRadius

    override fun interact(asteroid: Asteroid, trans: Transaction) {}
    override fun interact(missile: Missile, trans: Transaction) =
        checkCollision(missile, trans)
    override fun interact(saucer: Saucer, trans: Transaction) =
        checkCollision(saucer, trans)
    override fun interact(ship: Ship, trans: Transaction) =
        checkCollision(ship, trans)

    private fun checkCollision(other: Collidable, trans: Transaction) {
        Collision(asteroid).executeOnHit(other) {
            dieDueToCollision(trans)
        }
    }

    private fun dieDueToCollision(trans: Transaction) {
        trans.remove(asteroid)
        trans.add(Splat(asteroid))
        asteroid.splitIfPossible(trans)
    }
}