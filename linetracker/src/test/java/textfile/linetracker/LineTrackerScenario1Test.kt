package textfile.linetracker

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/** Testing LineChangeTracker Scenario 1.
 *  This Test Scenario includes:
 *  - 1 New Line at LineNumber 5
 *  - 1 Removed Line at LineNumber 3
 *  - 1 Modified Line at LineNumber 1
 */
class LineTrackerScenario1Test {

    lateinit var mInstance: LineTracker

    val changedLineNumbers = listOf(1, 3, 5)
    val newLine = NewLine("")
    val removeLine = RemoveLine("")
    val modifyLine = ModifyLine("", "")

    @Before
    fun testSetup() {
        mInstance = LineTracker()
        mInstance.newLine(5, newLine)
        mInstance.removeLine(3, removeLine)
        mInstance.modifyLine(1, modifyLine)
    }

    @Test
    fun testCountLineChanges_Returns3() {
        assertEquals(3, mInstance.countLineChanges())
    }

    @Test
    fun testHasAnyChanges_ReturnsTrue() {
        assertTrue(mInstance.hasAnyChanges())
    }

    @Test
    fun testGetSmallestLineNumberChanged_Returns10() {
        assertEquals(1, mInstance.getSmallestLineNumberChanged())
    }

    @Test
    fun testGetLargestLineNumberChanged_Returns5() {
        assertEquals(5, mInstance.getLargestLineNumberChanged())
    }

    @Test
    fun testGetChangedLineNumbers_ReturnsEmpty() {
        assertEquals(
            listOf(1, 3, 5),
            mInstance.getChangedLineNumbers().toList()
        )
    }

    @Test
    fun testGetLineChange_NonChangeLine_ReturnsNull() {
        for (lineNumber in 0 until 10)
            if (lineNumber !in changedLineNumbers)
                assertNull(mInstance.getLineChange(lineNumber))
    }

    @Test
    fun testGetLineChange_ChangedLine1_ReturnsLineChange() {
        assertEquals(
            modifyLine, mInstance.getLineChange(1)
        )
    }

    @Test
    fun testGetLineChange_ChangedLine3_ReturnsLineChange() {
        assertEquals(
            removeLine, mInstance.getLineChange(3)
        )
    }

    @Test
    fun testGetLineChange_ChangedLine5_ReturnsLineChange() {
        assertEquals(
            newLine, mInstance.getLineChange(5)
        )
    }

    @Test
    fun testClear_ReturnsTrue() {
        assertTrue(mInstance.clear())
    }

    @Test
    fun testNewLine_Line1_() {
        val testInput = NewLine("")
        mInstance.newLine(1, testInput)
        //
    }

    @Test
    fun testNewLine_Line3_SameAsOriginal_ClearsRemoveLine() {
        val testInput = NewLine(removeLine.line)
        mInstance.newLine(3, testInput)
        //
        assertNull(mInstance.getLineChange(3))
    }

    @Test
    fun testNewLine_Line3_DifferentFromOriginal_SetsModifyLine() {
        val differentLine = "different"
        val testInput = NewLine(differentLine)
        mInstance.newLine(3, testInput)
        //
        assertEquals(
            ModifyLine(removeLine.line, differentLine),
            mInstance.getLineChange(3)
        )
    }

    @Test
    fun testNewLine_Line5_SameAsOriginal_Noop() {
        val testInput = NewLine(newLine.line)
        mInstance.newLine(5, testInput)
        //
        assertEquals(
            newLine,
            mInstance.getLineChange(5)
        )
    }

    @Test
    fun testNewLine_Line5_DifferentFromOriginal_UpdatesNewLine() {
        val differentLine = "different"
        val testInput = NewLine(differentLine)
        mInstance.newLine(5, testInput)
        //
        assertEquals(
            NewLine(differentLine),
            mInstance.getLineChange(5)
        )
    }

    @Test
    fun testRemoveLine_Line1_() {
        val testInput = RemoveLine(modifyLine.initial)
        mInstance.removeLine(1, testInput)
        //
        assertEquals(
            testInput,
            mInstance.getLineChange(1)
        )
    }

    @Test
    fun testRemoveLine_Line3_SetsRemoveLine() {
        val testInput = RemoveLine("")
        mInstance.removeLine(3, testInput)
        //
        assertEquals(
            testInput,
            mInstance.getLineChange(3)
        )
    }

    @Test
    fun testRemoveLine_Line5_ClearsNewLine() {
        val testInput = RemoveLine("")
        mInstance.removeLine(5, testInput)
        //
        assertEquals(2, mInstance.countLineChanges())
        assertNull(mInstance.getLineChange(5))
    }

    @Test
    fun testModifyLine_Line1_() {
        val testInput = ModifyLine("", "")
        mInstance.modifyLine(1, testInput)
        //
    }

    @Test
    fun testModifyLine_Line3_SameAsOriginal_Noop() {
        val testInput = ModifyLine(modifyLine.initial, modifyLine.final)
        mInstance.modifyLine(3, testInput)
        //
        assertEquals(
            modifyLine,
            mInstance.getLineChange(3)
        )
    }

    @Test
    fun testModifyLine_Line3_DifferentFromOriginal_SetsModifyLine() {
        val differentFinal = "different"
        val testInput = ModifyLine(modifyLine.initial, differentFinal)
        mInstance.modifyLine(3, testInput)
        //
        assertEquals(
            testInput,
            mInstance.getLineChange(3)
        )
    }

    @Test
    fun testModifyLine_Line5_SameAsOriginal_Noop() {
        val testInput = ModifyLine("", newLine.line)
        mInstance.modifyLine(5, testInput)
        //
        assertEquals(
            newLine,
            mInstance.getLineChange(5)
        )
    }

    @Test
    fun testModifyLine_Line5_DifferentFromOriginal_SetsNewLine() {
        val differentLine = "different"
        val testInput = ModifyLine(newLine.line, differentLine)
        mInstance.modifyLine(5, testInput)
        //
        assertEquals(
            NewLine(differentLine),
            mInstance.getLineChange(5)
        )
    }

    @Test
    fun testClearNewLines_() {
        mInstance.clearNewLines()
        //
        assertEquals(2, mInstance.countLineChanges())
        assertEquals(0, mInstance.getNewLineNumbers().count())
    }

    @Test
    fun testClearRemovedLines_() {
        mInstance.clearRemovedLines()
        //
        assertEquals(2, mInstance.countLineChanges())
        assertEquals(0, mInstance.getRemovedLineNumbers().count())
    }

    @Test
    fun testClearModifiedLines_() {
        mInstance.clearModifiedLines()
        //
        assertEquals(2, mInstance.countLineChanges())
        assertEquals(0, mInstance.getModifiedLineNumbers().count())
    }

}