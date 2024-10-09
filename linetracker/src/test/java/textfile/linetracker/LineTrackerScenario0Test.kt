package textfile.linetracker

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

/** Testing LineChangeTracker.
 */
class LineTrackerScenario0Test {

    lateinit var mInstance: LineTracker

    @Before
    fun testSetup() {
        mInstance = LineTracker()
    }

    @Test
    fun testCountLineChanges_Returns0() {
        assertEquals(0, mInstance.countLineChanges())
    }

    @Test
    fun testHasAnyChanges_ReturnsFalse() {
        assertFalse(mInstance.hasAnyChanges())
    }

    @Test
    fun testGetSmallestLineNumberChanged_Returns0() {
        assertEquals(0, mInstance.getSmallestLineNumberChanged())
    }

    @Test
    fun testGetLargestLineNumberChanged_Returns0() {
        assertEquals(0, mInstance.getLargestLineNumberChanged())
    }

    @Test
    fun testGetChangedLineNumbers_ReturnsEmpty() {
        assertEquals(
            emptyList<Int>(),
            mInstance.getChangedLineNumbers().toList()
        )
    }

    @Test
    fun testGetLineChange_ReturnsNull() {
        for (lineNumber in 0 until 10)
            assertNull(mInstance.getLineChange(lineNumber))
    }

    @Test
    fun testClear_ReturnsFalse() {
        assertFalse(mInstance.clear())
    }

}