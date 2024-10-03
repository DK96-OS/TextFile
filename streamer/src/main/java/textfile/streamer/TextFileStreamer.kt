package textfile.streamer

import java.io.File
import java.util.stream.Stream
import kotlin.jvm.optionals.getOrNull

/** Creates Streams for Text File Lines.
 */
class TextFileStreamer(
    /** The File object to stream text from.
     */
    private val file: File,
) {

    /** Operate on a Stream of Strings from the File.
     * @param operator The operation to apply to the Stream.
     * @return Any Result from the operator.
     */
    fun <T> streamLines(
        operator: Stream<String>.() -> T,
    ) : T {
        var result: T
        file.bufferedReader().use {
            result = operator.invoke(it.lines())
        }
        return result
    }

    /** Count the number of Lines in the File.
     * @return The number of lines in the File.
     */
    fun countLines()
        : Long = streamLines { count() }

    /** Read a specific line.
     * @param index The index of the line to read.
     * @return The line as a String, or null.
     */
    fun readLine(
        index: Long,
    ) : String? = streamLines {
        skip(index)
        .findFirst()
        .getOrNull()
    }

    /** Read a specific subset of lines.
     * @param startIndex The index of the first line to read.
     * @param endIndex The index of the last line to read.
     * @return Stream of Strings.
     */
    fun readLines(
        startIndex: Long,
        endIndex: Long
    ) : List<String> = streamLines {
        skip(startIndex)
        .limit(endIndex - startIndex)
        .toList()
    }

    /** Read all Lines from the File.
     */
    fun readAllLines()
        : List<String> = file.readLines()

}