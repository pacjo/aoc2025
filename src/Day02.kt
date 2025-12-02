/**
 * Run [action] for each range in the input file
 */
private fun forEachRange(
    input: List<String>,
    action: (start: Long, end: Long) -> Unit
) {
    for (line in input) {
        val ranges = line.split(",")
        for (range in ranges) {
            val (start, end) = range.split("-").map { it.toLong() }
            action(start, end)
        }
    }
}

fun main() {
    fun part1(input: List<String>): Long {
        var sum = 0L
        forEachRange(input) { start, end ->
            for (id in start..end) {
                val idString = id.toString()

                // we can't check for two equal parts on a odd-length string - skip
                if (idString.length % 2 != 0)
                    continue

                val (left, right) = idString.chunked(idString.length / 2)
                if (left == right)
                    sum += id
            }
        }

        return sum
    }

    fun part2(input: List<String>): Long {
        var sum = 0L
        forEachRange(input) { start, end ->
            // we can't ignore odd-length strings now

            idloop@ for (id in start..end) {
                val idString = id.toString()

                // messy code
                for (substringLength in 1..idString.length / 2) {
                    val chunks = idString.chunked(substringLength)
                    if (chunks.distinct().size == 1 && chunks.size >= 2) {
                        sum += id
                        // println("\t\t$id is correct - adding")
                        continue@idloop
                    }
                }
            }
        }

        return sum
    }

    val testInput = readInput("Day02_test")
    check(part1(testInput) == 1227775554L)        // sum of all invalid IDs
    check(part2(testInput) == 4174379265L)        // sum of all invalid IDs (by new rules)

    // Read the input from the `src/Day02.txt` file.
    val input = readInput("Day02")
    println("part1: ${part1(input)}")
    println("part2: ${part2(input)}")
}
