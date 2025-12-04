@file:Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")

// we need the key to depend on the number of batteries
data class CacheKey(
    val batteries: List<Integer>,
    val batteriesLeft: Int
)

val valuesCache = mutableMapOf<CacheKey, Long>()

private fun getMaxJoltage(batteries: List<Integer>, batteriesLeft: Int): Long? {
    if (batteries.isEmpty() || batteriesLeft == 0)
        return null

    val key = CacheKey(batteries, batteriesLeft)
    return valuesCache.getOrPut(key) {
        batteries.maxOf { first ->
            val rest = getMaxJoltage(
                batteries = batteries.subList(
                    fromIndex = batteries.indexOfFirst {
                        // referential equality allows for differentiating one number object from another
                        @Suppress("IDENTITY_SENSITIVE_OPERATIONS_WITH_VALUE_TYPE")     // this is exactly what we want
                        first === it
                    } + 1,
                    toIndex = batteries.size
                ),
                batteriesLeft = batteriesLeft - 1
            )

            // this is awful... but works
            rest?.let { "$first$rest".toLong() } ?: first.toLong()
        }
    }
}

private fun getJoltagesFromLine(line: String): List<Integer> {
    return line.map {
        // we do this, since we need to distingish between
        // one number and another with the same value

        @Suppress("DEPRECATION")
        Integer(it.digitToInt())
    }
}

fun main() {
    fun part(input: List<String>, numberOfBatteries: Int): Long {
        var sum = 0L
        for (line in input) {
            print("checking line: $line ")
            val joltages = getJoltagesFromLine(line)
            val max = getMaxJoltage(joltages, numberOfBatteries)!!
            println(" - max: $max")
            sum += max
        }

        return sum
    }

    val testInput = readInput("Day03_test")
    check(part(testInput, 2) == 357L)                 // sum of 98, 89, 78, 92
    check(part(testInput, 12) == 3121910778619L)      // sum of 987654321111, 811111111119, 434234234278, 888911112111

    // Read the input from the `src/Day03.txt` file.
    val input = readInput("Day03")
    println("part1: ${part(input, 2)}")
    println("part2: ${part(input, 12)}")
}
