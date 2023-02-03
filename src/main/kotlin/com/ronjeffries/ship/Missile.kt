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
    private val missileIsFromShip: Boolean = false,
    val strategy: MissileCollisionStrategy = MissileCollisionStrategy()
): SpaceObject, Collider by strategy {
    constructor(ship: Ship): this(ship.position, ship.heading, ship.killRadius, ship.velocity, ColorRGBa.WHITE, true)
    constructor(saucer: Saucer): this(saucer.position, Random.nextDouble(360.0), saucer.killRadius, saucer.velocity, ColorRGBa.GREEN)
    init { strategy.missile = this }

    override lateinit var position: Point
    override val killRadius: Double = U.MISSILE_KILL_RADIUS
    val velocity: Velocity

    init {
        val missileOwnVelocity = Velocity(U.MISSILE_SPEED, 0.0).rotate(shooterHeading)
        val standardOffset = Point(2 * (shooterKillRadius + killRadius), 0.0)
        val rotatedOffset = standardOffset.rotate(shooterHeading)
        position = shooterPosition + rotatedOffset
        velocity = shooterVelocity + missileOwnVelocity
    }

    override val collisionStrategy: Collider
        get() = this

    private val timeOut = OneShot(U.MISSILE_LIFETIME) {
        it.remove(this)
        it.add(Splat(this))
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

    override fun interactWith(other: SpaceObject, trans: Transaction) {
        other.collisionStrategy.interact(this, trans)
    }

    override fun toString(): String = "Missile $position ($killRadius)"

    fun score(trans: Transaction, score: Int) {
        if (missileIsFromShip) trans.addScore(score)
    }

    fun prepareToDie(trans: Transaction) {
        timeOut.cancel(trans)
    }
}
