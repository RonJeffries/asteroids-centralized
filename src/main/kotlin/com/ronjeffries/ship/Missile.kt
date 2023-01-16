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
    val missileIsFromShip: Boolean = false
): SpaceObject, Collider {
    constructor(ship: Ship): this(ship.position, ship.heading, ship.killRadius, ship.velocity, ColorRGBa.WHITE, true)
    constructor(saucer: Saucer): this(saucer.position, Random.nextDouble(360.0), saucer.killRadius, saucer.velocity, ColorRGBa.GREEN)

    override var position: Point = Point.ZERO
    var velocity: Velocity = Velocity.ZERO
    override val killRadius: Double = U.MISSILE_KILL_RADIUS
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

    fun interact(asteroid: Asteroid, trans: Transaction) {
        if (checkCollision(asteroid)) {
            if (missileIsFromShip) trans.addScore(asteroid.getScore())
            terminateMissile(trans)
        }
    }

    fun interact(saucer: Saucer, trans: Transaction) {
        if (checkCollision(saucer)) {
            if (missileIsFromShip) trans.addScore(saucer.getScore())
            terminateMissile(trans)
        }
    }

    fun interact(ship: Ship, trans: Transaction) {
        if (checkCollision(ship)) terminateMissile(trans)
    }

    fun interact(missile: Missile, trans: Transaction) {
        if (checkCollision(missile)) terminateMissile(trans)
    }

    private fun terminateMissile(trans: Transaction) {
        timeOut.cancel(trans)
        trans.remove(this)
    }

    private fun checkCollision(other: Collider) = Collision(other).hit(this)

    override fun toString(): String = "Missile $position ($killRadius)"

}
