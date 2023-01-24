package com.ronjeffries.ship

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.openrndr.math.Vector2

class GameTest {
    @Test
    fun `create game`() {
        val game = Game()
        val asteroid = Asteroid(Vector2(100.0, 100.0), Vector2(50.0, 50.0))
        val ship = Ship(
            position = Vector2(1000.0, 1000.0)
        )
        game.knownObjects.add(asteroid)
        game.knownObjects.add(ship)
        val trans = game.changesDueToInteractions()
        assertThat(trans.removes.size).isEqualTo(0)
        val steps = (1000 - 100) / 50
        for (i in 1..steps * 60) game.tick(1.0 / 60.0)
        val x = asteroid.position.x
        val y = asteroid.position.y
        assertThat(x).isEqualTo(100.0 + steps * 50.0, within(0.1))
        assertThat(y).isEqualTo(100.0 + steps * 50.0, within(0.1))
        val trans2 = game.changesDueToInteractions()
        assertThat(trans2.removes.size).isEqualTo(2)
    }

    @Test
    fun `count interactions`() {
        val objects = SpaceObjectCollection()
        val n = 12
        for (i in 1..n) {
            objects.add(
                Ship(
                    position = Vector2.ZERO
                )
            )
        }
        val pairs = objects.pairsToCheck()
        assertThat(pairs.size).isEqualTo(n*(n-1)/2)
    }

    @Test
    fun `ship goes to center when game starts it`() {
        val game = Game()
        game.insertQuarter(Controls())
        val trans = Transaction()
        game.cycle(0.1)
        game.cycle(3.1)
        val ship = game.knownObjects.ships().first()
        ship.position = Point(100.0, 100.0)
        trans.add(Splat(ship))
        trans.remove(ship)
        assertThat(trans.firstRemove()).isEqualTo(ship)
        game.knownObjects.applyChanges(trans)
        assertThat(ship.position).isNotEqualTo(U.CENTER_OF_UNIVERSE)
        game.cycle(3.2)
        game.cycle(6.2)
        val shipAgain = game.knownObjects.ships().first()
        assertThat(shipAgain.position).isEqualTo(U.CENTER_OF_UNIVERSE)
    }

    @Test
    fun `colliding ship and asteroid splits asteroid, loses ship`() {
        val game = Game()
        val asteroid = Asteroid(Vector2(1000.0, 1000.0))
        val ship = Ship(
            position = Vector2(1000.0, 1000.0)
        )
        game.knownObjects.add(asteroid)
        game.knownObjects.add(ship)
        assertThat(game.knownObjects.spaceObjects().size).isEqualTo(2)
        assertThat(ship).isIn(game.knownObjects.spaceObjects())
        game.processInteractions()
        assertThat(ship).isNotIn(game.knownObjects.spaceObjects())
        assertThat(game.knownObjects.asteroidCount()).isEqualTo(2)
    }

    @Test
    fun `game creation creates ScoreKeeper`() {
        val game = Game()
        val controls = Controls()
        game.insertQuarter(controls)
        val keeper: ScoreKeeper = game.knownObjects.scoreKeeper
        assertThat(keeper.shipCount).isEqualTo(U.SHIPS_PER_QUARTER)
    }

    @Test
    fun `game creates asteroids after a while`() {
        val game = Game()
        val controls = Controls()
        game.createInitialContents(controls)
        assertThat(game.knownObjects.asteroidCount()).isEqualTo(0)
        game.cycle(0.2)
        game.cycle(0.3)
        game.cycle(4.2)
        assertThat(game.knownObjects.asteroidCount()).isEqualTo(4)
    }

    @Test
    fun `game creates asteroids even when quarter comes rapidly`() {
        val game = Game()
        val controls = Controls()
        game.createInitialContents(controls)
        assertThat(game.knownObjects.asteroidCount()).isEqualTo(0)
        game.cycle(0.2)
        game.cycle(0.3)
        game.insertQuarter(controls)
        game.cycle(0.2)
        game.cycle(0.3)
        assertThat(game.knownObjects.asteroidCount()).isEqualTo(0)
        game.cycle(4.2)
        assertThat(game.knownObjects.asteroidCount()).isEqualTo(4)
    }

    @Test
    fun `game can create asteroids directly`() {
        val game = Game()
        game.createInitialContents(Controls())
        val transForFour = Transaction()
        game.makeWave(transForFour)
        assertThat(transForFour.asteroids().size).isEqualTo(4)
        val transForSix = Transaction()
        game.makeWave(transForSix)
        assertThat(transForSix.asteroids().size).isEqualTo(6)
    }

    @Test
    fun `saucer makes it unsafe for ship`() {
        val mix = SpaceObjectCollection()
        val game = Game(mix)
        assertThat(game.canShipEmerge()).isEqualTo(true)
        val saucer = Saucer()
        mix.add(saucer)
        assertThat(game.canShipEmerge()).isEqualTo(false)
    }

    @Test
    fun `asteroid too close makes it unsafe for ship`() {
        val mix = SpaceObjectCollection()
        val game = Game(mix)
        assertThat(game.canShipEmerge()).isEqualTo(true)
        val asteroid = Asteroid(Point(100.0, 100.0))
        mix.add(asteroid)
        assertThat(game.canShipEmerge()).isEqualTo(true)
        val dangerousAsteroid = Asteroid(U.CENTER_OF_UNIVERSE + Point(50.0, 50.0))
        mix.add(dangerousAsteroid)
        assertThat(game.canShipEmerge()).isEqualTo(false)
    }

    @Test
    fun `missile makes it unsafe for ship`() {
        val mix = SpaceObjectCollection()
        val game = Game(mix)
        assertThat(game.canShipEmerge()).isEqualTo(true)
        val missile = Missile(Point(100.0, 100.0))
        mix.add(missile)
        assertThat(game.canShipEmerge()).isEqualTo(false)
    }

    @Test
    fun `how many asteroids per wave`() {
        val game = Game()
        game.insertQuarter(Controls())
        assertThat(game.howMany()).isEqualTo(4)
        assertThat(game.howMany()).isEqualTo(6)
        assertThat(game.howMany()).isEqualTo(8)
        assertThat(game.howMany()).isEqualTo(10)
        assertThat(game.howMany()).isEqualTo(11)
        assertThat(game.howMany()).isEqualTo(11)
    }
}