package textfile.linetracker

import androidx.collection.SparseArrayCompat
import androidx.collection.keyIterator
import androidx.collection.set

/** Tracks LineChange information for an external text file editor.
 */
class LineTracker {

    private val changes = SparseArrayCompat<LineChange>()

    /** Count of the number of Lines Changed.
     */
    fun countLineChanges()
        : Int = changes.size()

    /** Determine whether there are any changes present.
     */
    fun hasAnyChanges()
        : Boolean = changes.size() > 0

    /**
     */
    fun getSmallestLineNumberChanged(): Int {
        if (hasAnyChanges())
            return changes.keyAt(0)
        return 0
    }

    /**
     */
    fun getLargestLineNumberChanged(): Int {
        if (hasAnyChanges())
            return changes.keyAt(changes.size() - 1)
        return 0
    }

    /** Obtain a Sequence of Line Numbers with changes.
     * @return A Sequence containing the Line Numbers in ascending order.
     */
    fun getChangedLineNumbers()
        : Sequence<Int> = changes.keyIterator().asSequence()

    /** Obtain a Line Change at the given Line Number.
     * @param lineNumber The Line number to search for.
     * @return The LineChange data, or null if the Line has no tracked changes.
     */
    fun getLineChange(
        lineNumber: Int
    ) : LineChange? = changes[lineNumber]

    /** Clear all changes.
     * @return True if there were any changes present.
     */
    fun clear(): Boolean {
        val result = hasAnyChanges()
        changes.clear()
        return result
    }

    /** Track a New Line.
     */
    fun newLine(
        lineNumber: Int,
        newLine: NewLine,
    ) {
        when (
            val existingLine = changes[lineNumber]
        ) {
            null, is NewLine -> changes[lineNumber] = newLine
            is RemoveLine -> {
                if (newLine.line == existingLine.line) {
                    changes.remove(lineNumber)
                } else {
                    changes[lineNumber] = ModifyLine(
                        initial = existingLine.line,
                        final = newLine.line,
                    )
                }
            }
            is ModifyLine -> changes[lineNumber] = existingLine.copy(
                final = newLine.line
            )
        }
    }

    /** Track a Removed Line.
     */
    fun removeLine(
        lineNumber: Int,
        removedLine: RemoveLine,
    ) {
        when (
            val existingLine = changes[lineNumber]
        ) {
            null, is RemoveLine -> changes[lineNumber] = removedLine
            is NewLine -> changes.remove(lineNumber)
            is ModifyLine -> changes[lineNumber] = removedLine.copy(
                line = existingLine.initial
            )
        }
    }

    /** Track a Modified Line.
     */
    fun modifyLine(
        lineNumber: Int,
        modifyLine: ModifyLine,
    ) {
        changes[lineNumber] = when (
            val existingLine = changes[lineNumber]
        ) {
            null -> modifyLine
            is NewLine -> existingLine.copy(line = modifyLine.final)
            is RemoveLine -> modifyLine.copy(initial = existingLine.line)
            is ModifyLine -> existingLine.copy(final = modifyLine.final)
        }
    }

    /** Obtain the Line Numbers of New Lines.
     */
    fun getNewLineNumbers(): Sequence<Int> {
        return changes.keyIterator()
            .asSequence()
            .filter { changes[it] is NewLine }
    }

    /** Obtain the Line Numbers of Removed Lines.
     */
    fun getRemovedLineNumbers(): Sequence<Int> {
        return changes.keyIterator()
            .asSequence()
            .filter { changes[it] is RemoveLine }
    }

    /** Obtain the Line Numbers of Modified Lines.
     */
    fun getModifiedLineNumbers(): Sequence<Int> {
        return changes.keyIterator()
            .asSequence()
            .filter { changes[it] is ModifyLine }
    }

    /** Clear only New Lines from the Tracker.
     */
    fun clearNewLines() {
        changes.keyIterator()
            .asSequence()
            .filter { changes[it] is NewLine }
            .forEach { changes.remove(it) }
    }

    /** Clear only Removed Lines from the Tracker.
     */
    fun clearRemoveLines() {
        changes.keyIterator()
            .asSequence()
            .filter { changes[it] is RemoveLine }
            .forEach { changes.remove(it) }
    }

    /** Clear only Modified Lines from the Tracker.
     */
    fun clearModifyLines() {
        changes.keyIterator()
            .asSequence()
            .filter { changes[it] is ModifyLine }
            .forEach { changes.remove(it) }
    }

}