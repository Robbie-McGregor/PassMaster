package com.robbie_mcgregor.passmaster.ui.password_generator;


class Password(
        val length: Int,
        val includeUppercase: Boolean,
        val includeLowercase: Boolean,
        val includeDigits: Boolean,
        val includeSpecialCharacters: Boolean
) {

    //    Character arrays to get random characters from
    private val uppercaseLetters = arrayOf('A'..'Z')[0]
    private val lowercaseLetters = arrayOf('a'..'z')[0]
    private val digits = arrayOf('0','1','2','3','4','5','6','7','8','9')
    private val specialCharacters = arrayOf('!', '@', '#', '$', '%', '^', '&', '*')


    fun generatePassword(): String {
//        Create empty array to store password letters/digits
        val passwordArray = arrayOfNulls<Char>(length)
//        Create empty array to fill with selected/required character types/lists (to then randomly select from)
        val combinedCharacterArray = ArrayList<Char>()

//        Need to ensure at least one of each required type is present in the array
        if (includeUppercase) {
//            For each type required:
//            Find first null/empty index and put in a random character (repeat for each character type)
            passwordArray[passwordArray.indexOf(null)] = uppercaseLetters.randomOrNull()
//            Then add required character array into the larger array of characters to select randomly from
            combinedCharacterArray.addAll(uppercaseLetters)
        }
        if (includeLowercase) {
            passwordArray[passwordArray.indexOf(null)] = lowercaseLetters.randomOrNull()
            combinedCharacterArray.addAll(lowercaseLetters)
        }
        if (includeDigits) {
            passwordArray[passwordArray.indexOf(null)] = digits.randomOrNull()
            combinedCharacterArray.addAll(digits)
        }
        if (includeSpecialCharacters) {
            passwordArray[passwordArray.indexOf(null)] = specialCharacters.randomOrNull()
            combinedCharacterArray.addAll(specialCharacters)
        }

//        Loop through the empty password indices and insert random characters until array is full
        while (passwordArray.indexOf(null) > -1) {
            passwordArray[passwordArray.indexOf(null)] = combinedCharacterArray.randomOrNull()
        }

//        Shuffle Array, then convert to string and return
        passwordArray.shuffle()
        return passwordArray.joinToString("")
    }
}