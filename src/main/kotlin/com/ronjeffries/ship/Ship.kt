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
    val controls: Controls = Controls()
) : SpaceObject, Collidable {
    override val killRadius = U.SHIP_KILL_RADIUS
    var velocity:  Velocity = Velocity.ZERO
    var heading: Double = 0.0

    private var dropScale = U.DROP_SCALE
    private var accelerating: Boolean = false
    private var displayAcceleration: Int = 0

    override val collisionStrategy: Collider
        get() = ShipCollisionStrategy(this)
    override fun interactWith(other: Collidable, trans: Transaction)
        = other.collisionStrategy.interact(this, trans)

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

    fun accelerate(deltaV: Acceleration) {
        accelerating = true
        velocity = (velocity + deltaV).limitedToLightSpeed()
    }

    fun dropIn() {
        dropScale = U.DROP_SCALE
    }

    override fun draw(drawer: Drawer) {
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

    private fun accelerateToNewSpeedInOneSecond(vNew:Velocity, vCurrent: Velocity): Velocity {
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