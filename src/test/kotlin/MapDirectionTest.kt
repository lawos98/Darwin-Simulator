import org.junit.Test
import kotlin.test.assertEquals


class MapDirectionTest {
    @Test
    fun test_next()
    {
        assertEquals(MapDirection.North.next(), MapDirection.NorthEast)
        assertEquals(MapDirection.South.next(), MapDirection.SouthWest)
        assertEquals(MapDirection.West.next(), MapDirection.NorthWest)
        assertEquals(MapDirection.East.next(), MapDirection.SouthEast)
    }
    @Test
    fun test_previous()
    {
        assertEquals(MapDirection.North.previous(), MapDirection.NorthWest)
        assertEquals(MapDirection.South.previous(), MapDirection.SouthEast)
        assertEquals(MapDirection.West.previous(), MapDirection.SouthWest)
        assertEquals(MapDirection.East.previous(), MapDirection.NorthEast)
    }
    @Test
    fun test_toVectors()
    {
        assertEquals(MapDirection.North.toUnitVector(),Vector2d(0,1))
        assertEquals(MapDirection.NorthEast.toUnitVector(),Vector2d(1,1))
        assertEquals(MapDirection.East.toUnitVector(),Vector2d(1,0))
        assertEquals(MapDirection.SouthEast.toUnitVector(),Vector2d(1,-1))
        assertEquals(MapDirection.South.toUnitVector(),Vector2d(0,-1))
        assertEquals(MapDirection.SouthWest.toUnitVector(),Vector2d(-1,-1))
        assertEquals(MapDirection.West.toUnitVector(),Vector2d(-1,0))
        assertEquals(MapDirection.NorthWest.toUnitVector(),Vector2d(-1,1))

    }
}