package com.ronjeffries.ship

class MissileCollisionStrategy(): Collider {
    lateinit var missile: Missile
    override lateinit var position: Point
    override val killRadius: Double = U.MISSILE_KILL_RADIUS

    override fun interact(asteroid: Asteroid, trans: Transaction) =
        checkAndScoreCollision(asteroid, trans, asteroid.getScore())
    override fun interact(missile: Missile, trans: Transaction) =
        checkAndScoreCollision(missile, trans, 0)
    override fun interact(saucer: Saucer, trans: Transaction) =
        checkAndScoreCollision(saucer, trans,saucer.getScore())
    override fun interact(ship: Ship, trans: Transaction) =
        checkAndScoreCollision(ship, trans, 0)

    private fun checkAndScoreCollision(other: SpaceObject, trans: Transaction, score: Int) {
        Collision(other).executeOnHit(missile) {
            missile.score(trans, score)
            terminateMissile(trans)
        }
    }

    private fun terminateMissile(trans: Transaction) {
        missile.prepareToDie(trans)
        trans.remove(missile)
    }
}