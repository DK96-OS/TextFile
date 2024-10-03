package textfile.streamer

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import java.io.BufferedWriter
import java.io.File
import java.io.FileNotFoundException
import java.io.FileWriter

/** Testing TextFileStreamer.
 */
class TextFileStreamerTest {

    private var fileStreamer: TextFileStreamer? = null

    private var nonExistentFile: File = File("DoesNotExist")

    @Before
    fun setUp() {
        // Create a temporary file for testing purposes
        val tempFile = File("testfile.txt")
        BufferedWriter(FileWriter(tempFile)).use { writer ->
            writer.write("First line\nSecond line\nThird line")
        }
        fileStreamer = TextFileStreamer(tempFile)
    }

    @Test
    fun testCountLines() {
        val count = fileStreamer!!.countLines()
        assertEquals(3, count)
    }

    @Test
    fun testCountLines_NonExistentFile_() {
        assertThrows(
            FileNotFoundException::class.java,
        ) {
            val count = TextFileStreamer(nonExistentFile).countLines()
        }
    }

    @Test
    fun testReadLine() {
        // Index is 0-based, so index 1 should be "Second line"
        val line = fileStreamer!!.readLine(1L)
        assertNotNull(line)
        assertEquals("Second line", line)
    }

    @Test
    fun testReadLine_4() {
        val line = fileStreamer!!.readLine(4L)
        assertNull(line)
    }

    @Test
    fun testReadLine_NonExistentFile_ThrowsFileNotFound() {
        assertThrows(
            FileNotFoundException::class.java,
        ) {
            val line = TextFileStreamer(nonExistentFile).readLine(1L)
        }
    }

    @Test
    fun testReadLines_Start0_End0_ReturnsEmptyList() {
        val lines: List<String> = fileStreamer!!.readLines(0L, 0L)
        assertEquals(emptyList<String>(), lines)
    }

    @Test
    fun testReadLines_Start0_End1_ReturnsFirstLine() {
        val lines: List<String> = fileStreamer!!.readLines(0L, 1L)
        val expectedLines: List<String> = mutableListOf("First line")
        assertEquals(expectedLines, lines)
    }

    @Test
    fun testReadLines() {
        val lines: List<String> = fileStreamer!!.readLines(1L, 3L) // Should read from index 1 to 2 (inclusive)
        val expectedLines: List<String> = mutableListOf("Second line", "Third line")
        assertEquals(expectedLines, lines)
    }

    @Test
    fun testReadLines_EndIndexLarger_IgnoresExtraIndex() {
        val lines: List<String> = fileStreamer!!.readLines(1L, 4L)
        val expectedLines: List<String> = mutableListOf("Second line", "Third line")
        assertEquals(expectedLines, lines)
    }

    @Test
    fun testReadLines_NegativeStartValue_ThrowsIllegalArgumentException() {
        assertThrows(
            IllegalArgumentException::class.java
        ) {
            val lines: List<String> = fileStreamer!!.readLines(-2L, 3L)
        }
    }

    @Test
    fun testReadLines_NonExistentFile_ThrowsFileNotFound() {
        assertThrows(
            FileNotFoundException::class.java,
        ) {
            val line = TextFileStreamer(nonExistentFile).readLines(0L, 3L)
        }
    }

}