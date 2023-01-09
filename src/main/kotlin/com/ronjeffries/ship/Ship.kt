package com.ronjeffries.ship

import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import kotlin.random.Random

private val points = listOf(
    Point(-3.0, -2.0), Point(-3.0, 2.0), Point(-5.0, 4.0),
    Point(7.0, 0.0), Point(-5.0, -4.0), Point(-3.0, -2.0)
)

private val flare = listOf(
    Point(-3.0,-2.0), Point(-7.0,0.0), Point(-3.0, 2.0)
)

class Ship(
    override var position: Point,
    val controls: Controls = Controls(),
    override val killRadius: Double = U.SHIP_KILL_RADIUS
) : SpaceObject, Collider {
    var velocity:  Velocity = Velocity.ZERO
    var heading: Double = 0.0
    private var dropScale = U.DROP_SCALE
    var accelerating: Boolean = false
    var displayAcceleration: Int = 0

    override val subscriptions = Subscriptions(
        interactWithAsteroid = { asteroid, trans -> checkCollision(asteroid, trans) },
        interactWithSaucer = { saucer, trans -> checkCollision(saucer, trans) },
        interactWithMissile = { missile, trans -> checkCollision(missile, trans) },
        draw = this::draw,
        finalize = this::finalizeObject
    )

    private fun checkCollision(other: Collider, trans: Transaction) {
        if (weAreCollidingWith(other)) {
            collision(trans)
        }
    }

    override fun callOther(other: SpaceObject, trans: Transaction) {
        other.subscriptions.interactWithShip(this, trans)
    }

    override fun update(deltaTime: Double, trans: Transaction) {
        accelerating = false
        dropScale -= U.DROP_SCALE/60.0
        if (dropScale < 1.0 ) dropScale = 1.0
        controls.control(this, deltaTime, trans)
        move(deltaTime)
    }

    fun enterHyperspace(trans: Transaction) {
        position = U.randomInsidePoint()
        if (hyperspaceOK()) {
            dropIn()
        } else {
            trans.add(Splat(this))
            position = U.CENTER_OF_UNIVERSE
            trans.remove(this)
        }
    }

    private fun hyperspaceOK(): Boolean = !hyperspaceFailure(Random.nextInt(0, 63), U.AsteroidTally)

    // allegedly the original arcade rule
    fun hyperspaceFailure(random0thru62: Int, asteroidTally: Int): Boolean
            = random0thru62 >= (asteroidTally + 44)

    fun collision(trans: Transaction) {
        trans.add(Splat(this))
        trans.remove(this)
    }

    fun accelerate(deltaV: Acceleration) {
        accelerating = true
        velocity = (velocity + deltaV).limitedToLightSpeed()
    }

    fun dropIn() {
        dropScale = U.DROP_SCALE
    }

    fun draw(drawer: Drawer) {
        drawer.translate(position)
//        drawKillRadius(drawer)
        drawer.strokeWeight = U.STROKE_ALL
        drawer.scale(U.SHIP_SCALE, U.SHIP_SCALE)
        drawer.scale(dropScale, dropScale)
        drawer.rotate(heading )
        drawer.stroke = ColorRGBa.WHITE
        drawer.lineStrip(points)
        if ( accelerating ) {
            displayAcceleration = (displayAcceleration + 1)%3
            if ( displayAcceleration == 0 ) {
                drawer.strokeWeight = 2.0*U.STROKE_ALL
                drawer.lineStrip(flare)
            }
        }
    }

//    private fun drawKillRadius(drawer: Drawer) {
//        drawer.stroke = ColorRGBa.RED
//        drawer.strokeWeight = 1.0
//        drawer.fill = null
//        drawer.circle(0.0, 0.0, killRadius)
//    }

    private fun weAreCollidingWith(other: Collider): Boolean = Collision(other).hit(this)

    fun finalizeObject(): List<SpaceObject> {
        position = U.CENTER_OF_UNIVERSE
        velocity = Velocity.ZERO
        heading = 0.0
        return emptyList()
    }

    fun accelerateToNewSpeedInOneSecond(vNew:Velocity, vCurrent: Velocity): Velocity {
//        vNew = vCurrent + a*t
//        t = 1
//        a = vNew - vCurrent
        return vNew - vCurrent
    }

    private fun move(deltaTime: Double) {
        position = (position + velocity * deltaTime).cap()
        if (! accelerating ) {
            val acceleration = accelerateToNewSpeedInOneSecond(velocity*U.SHIP_DECELERATION_FACTOR, velocity)*deltaTime
            velocity += acceleration
        }
    }

    override fun toString(): String = "Ship $position ($killRadius)"

    fun turnBy(degrees: Double) {
        heading += degrees
    }

}