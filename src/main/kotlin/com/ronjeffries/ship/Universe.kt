package com.ronjeffries.ship

import kotlin.random.Random
import org.openrndr.math.Vector2

typealias Point = Vector2
typealias Velocity = Vector2
typealias Acceleration = Vector2

object U {
    const val ASTEROID_KILL_RADIUS = 64.0
    const val ASTEROID_SCALE = 4.0
    const val ASTEROID_SPEED = 100.0
    const val ASTEROID_STARTING_COUNT = 4
    const val DRAW_SCALE = 1.0
    const val DROP_SCALE = 3.0
    const val FONT_SIZE = 64.0
    const val MISSILE_KILL_RADIUS = 1.0
    const val MISSILE_LIFETIME = 3.0
    const val SAUCER_KILL_RADIUS = 10.0 // scaled dx=40 dy=24 suggests 12.0 Make it hard.
    const val SAUCER_LIFETIME = 10.0
    const val SAUCER_SCALE = 4.0
    const val SAUCER_SPEED = 150.0
    const val SHIPS_PER_QUARTER = 4
    const val SHIP_DECELERATION_FACTOR = 0.5 // speed reduces in half every second
    const val SHIP_KILL_RADIUS = 12.0
    const val SHIP_MAKER_DELAY = 3.0
    const val SHIP_ROTATION_SPEED = 200.0 // degrees per second
    const val SHIP_SCALE = 2.0
    const val SPEED_OF_LIGHT = 500.0
    const val SPLAT_LIFETIME = 2.0
    const val STROKE_ALL = 1.0
    const val UNIVERSE_SIZE = 1024.0
    const val WINDOW_SIZE = 1024

    const val MISSILE_SPEED = SPEED_OF_LIGHT / 3.0
    const val SAFE_SHIP_DISTANCE = UNIVERSE_SIZE/10.0

    val CENTER_OF_UNIVERSE = Point(UNIVERSE_SIZE / 2, UNIVERSE_SIZE / 2)
    val SHIP_ACCELERATION = Velocity(120.0, 0.0)

    var AsteroidTally = 0 // evil global value

    fun randomInsidePoint() = Point(randomInsideDouble(), randomInsideDouble())
    private fun randomInsideDouble() = UNIVERSE_SIZE/10.0 + Random.nextDouble(UNIVERSE_SIZE-2* UNIVERSE_SIZE/10.0)
    fun randomPoint() = Point(Random.nextDouble(0.0, UNIVERSE_SIZE), Random.nextDouble(0.0, UNIVERSE_SIZE))
    fun randomVelocity(speed: Double): Velocity = Velocity(speed,0.0).rotate(Random.nextDouble(360.0))

    fun randomEdgePoint(): Point =
        if (Random.nextBoolean()) Point(0.0, Random.nextDouble(UNIVERSE_SIZE))
        else Point(Random.nextDouble(UNIVERSE_SIZE), 0.0)
}

    fun Point.cap(): Point = Point(this.x.cap(), this.y.cap())

    fun Double.cap(): Double = (this + U.UNIVERSE_SIZE) % U.UNIVERSE_SIZE

    fun Velocity.limitedToLightSpeed(): Velocity {
        val speed = this.length
        return if (speed < U.SPEED_OF_LIGHT) this
        else this*(U.SPEED_OF_LIGHT/speed)
    }