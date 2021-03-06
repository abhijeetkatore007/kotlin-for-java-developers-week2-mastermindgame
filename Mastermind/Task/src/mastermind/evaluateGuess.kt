package mastermind

data class Evaluation(val positions: Int, val letters: Int)

fun evaluateGuess(secret: String, guess: String): Evaluation {
    //println("secret: $secret \nguess:  $guess")
    val (correctLetterAndPositionsAmount, reducedSecret, reducedGuess) =
            positionsGuessedCorrectly(secret, guess)

    val correctLettersAmount =
            lettersGuessedCorrectly(reducedSecret, reducedGuess)

    return Evaluation(correctLetterAndPositionsAmount, correctLettersAmount)
}

fun lettersGuessedCorrectly(secret: String, guess: String): Int {
    //var noDuplicatesGuess = ""
    //guess.groupBy { letter -> letter }.keys.forEach{ letter -> noDuplicatesGuess+=letter}
    var correctGuesses = 0
    var mutableSecret = secret
    for (ch in guess) {
        if (mutableSecret.contains(ch)) {
            mutableSecret = mutableSecret.replaceFirst(ch.toString(), "", true)
            correctGuesses++
        }
    }
    return correctGuesses
}

fun positionsGuessedCorrectly(secret: String, guess: String): Triple<Int, String, String> {
    val indexesToRemove = identifyIndexesToRemove(guess, secret)
    var (reducedSecret, reducedGuess) =
            reduceStringsRemovingIndexesFrom(secret, guess, indexesToRemove)

    val correctGuesses = indexesToRemove.size
    return Triple(correctGuesses, reducedSecret, reducedGuess)
}

private fun identifyIndexesToRemove(guess: String, secret: String): MutableList<Int> {
    val indexesToRemove = mutableListOf<Int>()
    for (guessedPair in guess.withIndex()) {
        val guessedIndex = guessedPair.index
        val secretLetter = secret.get(guessedIndex)
        if (guessedPair.value == secretLetter) {
            indexesToRemove.add(guessedIndex)
        }
    }
    return indexesToRemove
}

private fun reduceStringsRemovingIndexesFrom(secret: String,
                                             guess: String,
                                             indexesToRemove: MutableList<Int>): Pair<String, String> {
    var currentSecret = secret
    var currentGuess = guess

    //Need to reverse the list to start removing from higher to lower index
    //otherwise it could access an unknown index value
    indexesToRemove.reversed().forEach {
        currentGuess = currentGuess.replaceRange(it, it + 1, "")
        currentSecret = currentSecret.replaceRange(it, it + 1, "")
    }
    return Pair(currentSecret, currentGuess)
}
