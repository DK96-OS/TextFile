package textfile.linetracker

/** Represents a Change to a Line in a Text File.
 */
sealed interface LineChange


/** A New Line added to the Text File.
 */
data class NewLine(
    /** The Line to insert.
     */
    val line: String,
) : LineChange


/** A Line removed from the Text File.
 */
data class RemoveLine(
    /** The Line before removal.
     */
    val line: String
) : LineChange


/** A Line modified in the Text File.
 */
data class ModifyLine(
    /** The Line before modification.
     */
    val initial: String,
    /** The Line after modification.
     */
    val final: String,
) : LineChange
