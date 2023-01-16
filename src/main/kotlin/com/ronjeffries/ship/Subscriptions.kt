package com.ronjeffries.ship

import org.openrndr.draw.Drawer

class Subscriptions(
    val draw: (drawer: Drawer) -> Unit = {_ -> },
)