package com.ronjeffries.ship

import org.openrndr.draw.Drawer

class Subscriptions(
    val beforeInteractions: () -> Unit = {},

    val interactWithAsteroid: (asteroid: Asteroid, trans: Transaction) -> Unit = { _, _, -> },
    val interactWithMissile: (missile: Missile, trans: Transaction) -> Unit = { _, _, -> },
    val interactWithSaucer: (saucer: Saucer, trans: Transaction) -> Unit = { _, _, -> },
    val interactWithShip: (ship: Ship, trans: Transaction) -> Unit = { _, _, -> },

    val draw: (drawer: Drawer) -> Unit = {_ -> },
    val finalize: (trans: Transaction) -> Unit = { }
)