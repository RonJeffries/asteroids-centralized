package com.ronjeffries.ship

import org.openrndr.draw.Drawer

class Subscriptions(
    val interactWithMissile: (missile: Missile, trans: Transaction) -> Unit = { _, _, -> },
    val interactWithSaucer: (saucer: Saucer, trans: Transaction) -> Unit = { _, _, -> },
    val interactWithShip: (ship: Ship, trans: Transaction) -> Unit = { _, _, -> },

    val draw: (drawer: Drawer) -> Unit = {_ -> },
)