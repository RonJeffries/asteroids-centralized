package com.ronjeffries.ship

import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.math.asDegrees
import kotlin.math.atan2
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

private val saucerPoints = listOf(
    Point(-2.0, 1.0), Point(2.0, 1.0), Point(5.0, -1.0),
    Point(-5.0, -1.0), Point(-2.0, -3.0), Point(2.0, -3.0),
    Point(5.0, -1.0), Point(2.0, 1.0), Point(1.0, 3.0),
    Point(-1.0, 3.0), Point(-2.0, 1.0), Point(-5.0, -1.0),
    Point(-2.0, 1.0)
)

private val directions = listOf(
    Velocity(1.0, 0.0), Velocity(1.0, 0.0), Velocity(0.7071, 0.7071), Velocity(0.7071, -0.7071)
)

class Saucer : SpaceObject, Collidable {
    override val collisionStrategy: Collider
        get() = SaucerCollisionStrategy(this)
    override lateinit var position: Point
    override val killRadius = U.SAUCER_KILL_RADIUS

    private var direction: Double = 1.0
    lateinit var velocity: Velocity
    private val speed = U.SAUCER_SPEED
    private var elapsedTime = 0.0
    private var timeSinceSaucerSeen = 0.0
    private var timeSinceLastMissileFired = 0.0
    var sawShip = false
    var shipFuturePosition = Point.ZERO
    var missileReady = true
    var currentMissile: Missile? = null

    init {
        direction = -1.0
        setStartupValues()
    }

    fun start(trans: Transaction) {
        wakeUp()
        trans.add(this)
    }

    private fun wakeUp() {
        direction = -direction
        setStartupValues()
    }

    private fun setStartupValues() {
        position = Point(0.0, Random.nextDouble(U.UNIVERSE_SIZE))
        velocity = Velocity(direction, 0.0) * speed
        elapsedTime = 0.0
    }

    override fun interactWith(other: Collidable, trans: Transaction)
        = other.collisionStrategy.interact(this, trans)

    fun beforeInteractions() {
        sawShip = false; missileReady = true
    }


    override fun update(deltaTime: Double, trans: Transaction) {
        elapsedTime += deltaTime
        if (elapsedTime > U.SAUCER_LIFETIME) trans.remove(this)
        timeSinceSaucerSeen += deltaTime
        if (timeSinceSaucerSeen > 1.5) zigZag()
        timeSinceLastMissileFired += deltaTime
        if (timeSinceLastMissileFired > 0.5) fire(trans)
        position = (position + velocity * deltaTime).cap()
    }

    fun fire(trans: Transaction) {
        if (sawShip && missileReady) {
            timeSinceLastMissileFired = 0.0
            if (Random.nextInt(4) == 0) fireTargeted(trans)
            else fireRandom(trans)
        }
    }

    private fun fireRandom(trans: Transaction) {
        fireMissile(Missile(this), trans)
    }

    private fun fireTargeted(trans: Transaction) {
        val missile = Missile(position, headingToShip(), killRadius, Velocity.ZERO, ColorRGBa.RED)
        fireMissile(missile, trans)
    }

    private fun headingToShip(): Double {
        val directionToShip = (getTargetPosition() - position)
        return atan2(y = directionToShip.y, x = directionToShip.x).asDegrees
    }

    private fun fireMissile(missile: Missile, trans: Transaction) {
        currentMissile = missile
        trans.add(missile)
    }

    fun getTargetPosition() = ShotOptimizer.optimizeShot(position, shipFuturePosition)

    fun zigZag() {
        timeSinceSaucerSeen = 0.0
        velocity = newDirection(Random.nextInt(4)) * speed * direction
    }

    fun newDirection(direction: Int): Velocity = directions[min(max(0, direction), 3)]

    override fun draw(drawer: Drawer) {
        drawer.translate(position)
//        drawKillRadius(drawer)
        drawer.stroke = ColorRGBa.GREEN
        drawer.scale(U.SAUCER_SCALE, -U.SAUCER_SCALE)
        drawer.strokeWeight = U.STROKE_ALL / U.SAUCER_SCALE
        drawer.lineStrip(saucerPoints)
    }

    fun getScore() = 200

//    private fun drawKillRadius(drawer: Drawer) {
//        drawer.stroke = ColorRGBa.RED // delete comment even more
//        drawer.fill =ColorRGBa.RED
//        drawer.circle(0.0, 0.0, killRadius)
//    }
}
