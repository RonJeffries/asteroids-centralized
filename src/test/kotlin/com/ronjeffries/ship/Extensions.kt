package com.ronjeffries.ship

fun Transaction.asteroids(): List<Asteroid> = spaceObjects().filterIsInstance<Asteroid>()
fun Transaction.missiles(): List<Missile> = spaceObjects().filterIsInstance<Missile>()