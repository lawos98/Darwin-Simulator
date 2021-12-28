import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class VectorTest {
    @Test
    fun test_isequal()
    {
        assertEquals(Vector2d(2, 4), (Vector2d(2, 4)))
        assertEquals(Vector2d(3, 5), (Vector2d(3, 5)))
        assertNotEquals(Vector2d(2, 5), (Vector2d(2, 3)))
        assertNotEquals(Vector2d(3, 4), (Vector2d(2, 4)))
    }
    @Test
    fun test_convertToString()
    {
        assertEquals(Vector2d(2, 4).toString(),"(2,4)")
        assertEquals(Vector2d(3, 1).toString(),"(3,1)")
        assertEquals(Vector2d(-1, -1).toString(),"(-1,-1)")
        assertEquals(Vector2d(-5, 5).toString(),"(-5,5)")
    }
    @Test
    fun test_proceeds()
    {
        assertTrue(Vector2d(1, 1).precedes(Vector2d(2, 2)))
        assertTrue(Vector2d(1, 1).precedes(Vector2d(1, 2)))
        assertFalse(Vector2d(5, 1).precedes(Vector2d(3, 3)))
        assertFalse(Vector2d(1, 5).precedes(Vector2d(3, 3)))
    }
    @Test
    fun test_follows()
    {
        assertTrue(Vector2d(3, 3).follows(Vector2d(2, 2)))
        assertTrue(Vector2d(1, 3).follows(Vector2d(1, 2)))
        assertFalse(Vector2d(5, 1).follows(Vector2d(3, 3)))
        assertFalse(Vector2d(1, 5).follows(Vector2d(3, 3)))
    }
    @Test
    fun test_add()
    {
        assertEquals(Vector2d(2, 4) + (Vector2d(4, 4)), (Vector2d(6, 8)))
        assertEquals(Vector2d(3, 3) + (Vector2d(3, 3)), (Vector2d(6, 6)))
        assertEquals(Vector2d(1, 5) + (Vector2d(5, 1)), (Vector2d(6, 6)))
        assertEquals(Vector2d(-1, -1) + (Vector2d(1, 1)), (Vector2d(0, 0)))
    }
    @Test
    fun test_subtract()
    {
        assertEquals(Vector2d(2, 4) -(Vector2d(4, 4)), (Vector2d(-2, 0)))
        assertEquals(Vector2d(3, 3) -(Vector2d(3, 3)), (Vector2d(0, 0)))
        assertEquals(Vector2d(1, 5) -(Vector2d(5, 1)), (Vector2d(-4, 4)))
        assertEquals(Vector2d(-1, -1) -(Vector2d(1, 1)), (Vector2d(-2, -2)))
    }
}