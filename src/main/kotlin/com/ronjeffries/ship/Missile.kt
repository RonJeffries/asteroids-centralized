package com.ronjeffries.ship

import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import kotlin.random.Random

class Missile(
    shooterPosition: Point,
    shooterHeading: Double = 0.0,
    shooterKillRadius: Double = U.SHIP_KILL_RADIUS,
    shooterVelocity: Velocity = Velocity.ZERO,
    val color: ColorRGBa = ColorRGBa.WHITE,
    private val missileIsFromShip: Boolean = false
): SpaceObject, Collider {
    constructor(ship: Ship): this(ship.position, ship.heading, ship.killRadius, ship.velocity, ColorRGBa.WHITE, true)
    constructor(saucer: Saucer): this(saucer.position, Random.nextDouble(360.0), saucer.killRadius, saucer.velocity, ColorRGBa.GREEN)

    override var position: Point = Point.ZERO
    override val killRadius: Double = U.MISSILE_KILL_RADIUS
    val velocity: Velocity
    private val timeOut = OneShot(U.MISSILE_LIFETIME) {
        it.remove(this)
        it.add(Splat(this))
    }

    init {
        val missileOwnVelocity = Velocity(U.MISSILE_SPEED, 0.0).rotate(shooterHeading)
        val standardOffset = Point(2 * (shooterKillRadius + killRadius), 0.0)
        val rotatedOffset = standardOffset.rotate(shooterHeading)
        position = shooterPosition + rotatedOffset
        velocity = shooterVelocity + missileOwnVelocity
    }

    override fun update(deltaTime: Double, trans: Transaction) {
        timeOut.execute(trans)
        position = (position + velocity * deltaTime).cap()
    }

    override fun draw(drawer: Drawer) {
        drawer.translate(position)
        drawer.stroke = color
        drawer.fill = color
        drawer.circle(Point.ZERO, killRadius * 2.0)
    }

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

    override fun interactWith(other: Collider, trans: Transaction) {
        other.interact(this, trans)
    }

    private fun checkAndScoreCollision(other: Collider, trans: Transaction, score: Int) {
        Collision(other).executeOnHit(this) {
            terminateMissile(trans)
            if (missileIsFromShip) trans.addScore(score)
        }
    }

    private fun terminateMissile(trans: Transaction) {
        timeOut.cancel(trans)
        trans.remove(this)
    }

    override fun toString(): String = "Missile $position ($killRadius)"
}
