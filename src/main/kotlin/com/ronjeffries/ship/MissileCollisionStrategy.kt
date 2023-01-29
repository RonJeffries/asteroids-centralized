package com.ronjeffries.ship

class MissileCollisionStrategy(val missile: Missile): Collider {
    override val position: Point
        get() = missile.position
    override val killRadius: Double
        get() = missile.killRadius

    override fun interact(asteroid: Asteroid, trans: Transaction) {
        checkAndScoreCollision(asteroid, trans, asteroid.getScore())
    }

    override fun interact(missile: Missile, trans: Transaction) {
        checkAndScoreCollision(missile, trans, 0)
    }

    override fun interact(saucer: Saucer, trans: Transaction) {
        checkAndScoreCollision(saucer, trans,saucer.getScore())
    }

    override fun interact(ship: Ship, trans: Transaction) {
        checkAndScoreCollision(ship, trans, 0)
    }

    private fun checkAndScoreCollision(other: Collidable, trans: Transaction, score: Int) {
        Collision(other).executeOnHit(missile) {
            terminateMissile(trans)
            if (missile.missileIsFromShip) trans.addScore(score)
        }
    }

    private fun terminateMissile(trans: Transaction) {
        missile.timeOut.cancel(trans)
        trans.remove(missile)
    }
}