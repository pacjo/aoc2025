import kotlin.math.abs
import kotlin.math.sign

const val STARTING_POSITION = 50
const val WHEEL_SIZE = 100

fun main() {
    fun part1(input: List<String>): Int {
        var position = STARTING_POSITION
        var zeroCounter = 0
        for (move in input) {
            val moveAmount = move.substring(1).toInt()
            position = when (move.first()) {
                'L' -> (position - moveAmount)
                'R' -> (position + moveAmount)

                else -> throw IllegalStateException("Undefined input: $move")
            }.mod(WHEEL_SIZE)

            if (position == 0)
                zeroCounter++
        }

        return zeroCounter;
    }

    fun part2(input: List<String>): Int {
        var position = STARTING_POSITION
        var zeroCounter = 0
        for (move in input) {
            val moveAmount = move.substring(1).toInt()
            val smallMoveAmount = moveAmount.rem(WHEEL_SIZE)

            val wrapedClicks = moveAmount / WHEEL_SIZE                  // clicks that left position unchanged
            val startingAdjustment = if (position == 0) -1 else 0       // don't count crossing if we start on zero
            val normalCrossings = when (move.first()) {
                'L' -> {
                    // we will wrap into negatives
                    val crossings = if (smallMoveAmount > position) 1 else 0

                    // update position after calculation
                    position = (position - smallMoveAmount).mod(WHEEL_SIZE)
                    crossings
                }

                'R' -> {
                    // we will overflow
                    val crossings = if (position + smallMoveAmount > WHEEL_SIZE) 1 else 0

                    // update position after calculation
                    position = (position + smallMoveAmount).mod(WHEEL_SIZE)
                    crossings
                }

                else -> throw IllegalStateException("Undefined input: $move")
            }
            val endingAdjustment = if (position == 0) 1 else 0          // but count crossing if we end on zero

            val overflowCrossings = (startingAdjustment + normalCrossings + endingAdjustment).coerceAtLeast(0)
            val crossings = wrapedClicks + overflowCrossings
            zeroCounter += crossings
            // println("\tnew position: $position, crossings: $crossings")
        }

        return zeroCounter
    }

    val testInput = readInput("Day01_test")
    check(part1(testInput) == 3)        // 3 times on zero
    check(part2(testInput) == 6)        // 6 times past zero

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day01")
    println("part1: ${part1(input)}")
    println("part2: ${part2(input)}")
}
