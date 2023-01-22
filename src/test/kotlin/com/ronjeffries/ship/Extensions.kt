package com.ronjeffries.ship

fun Transaction.asteroids(): List<Asteroid> = spaceObjects().filterIsInstance<Asteroid>()
fun Transaction.missiles(): List<Missile> = spaceObjects().filterIsInstance<Missile>()
fun Transaction.saucers(): List<Saucer> = spaceObjects().filterIsInstance<Saucer>()
fun Transaction.splats(): List<Splat> = spaceObjects().filterIsInstance<Splat>()