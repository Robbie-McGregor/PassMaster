package com.robbie_mcgregor.passmaster.ui.passphrase_generator;

import kotlin.random.Random


class Passphrase(
    private var length: Int,
    private var separator: String = "-",
    private var insertNumber: Boolean,
    private var capitalize: Boolean,
) {

    var passphrase = ArrayList<String>()

    //    Dictionary array to get random words from
    private val dictionary = arrayOf(
        "ablility",
        "able",
        "about",
        "above",
        "abstract",
        "accept",
        "accord",
        "among",
        "art",
        "angle",
        "add",
        "banana",
        "baby",
        "baboon",
        "barbeque",
        "barista",
        "bag",
        "banker",
        "bandage",
        "banter",
        "bare",
        "core",
        "cradle",
        "candy",
        "cattle",
        "crust",
        "company",
        "cyclone",
        "circle",
        "crumpet",
        "candle",
        "dog",
        "diablo",
        "drive",
        "danger",
        "dialog",
        "demon",
        "dribble",
        "doberman",
        "drag",
        "doom",
        "elephant",
        "eagle",
        "envelope",
        "enigma",
        "enterprise",
        "easy",
        "evil",
        "elvis",
        "email",
        "empty",
        "family",
        "famous",
        "far",
        "frost",
        "frog",
        "frail",
        "flip",
        "face",
        "factory",
        "fake",
        "gravy",
        "garage",
        "gear",
        "general",
        "gentle",
        "giant",
        "gone",
        "gate",
        "global",
        "glow",
        "hammer",
        "heart",
        "hungry",
        "hippo",
        "humor",
        "head",
        "hello",
        "husk",
        "hunting",
        "happy",
        "igloo",
        "into",
        "isle",
        "identical",
        "impact",
        "imitate",
        "illegal",
        "improve",
        "impress",
        "indicate",
        "jail",
        "jewlery",
        "job",
        "joy",
        "journey",
        "joke",
        "judge",
        "juice",
        "jury",
        "jump",
        "keen",
        "kind",
        "kitchen",
        "kiss",
        "knife",
        "knock",
        "knuckle",
        "kingdom",
        "king",
        "kick",
        "lemon",
        "lady",
        "lolly",
        "lamp",
        "language",
        "late",
        "laugh",
        "land",
        "label",
        "lime",
        "money",
        "mighty",
        "minute",
        "mice",
        "men",
        "mean",
        "minor",
        "moon",
        "monkey",
        "monopoly",
        "nice",
        "near",
        "number",
        "noodle",
        "nope",
        "noun",
        "nail",
        "natural",
        "necessary",
        "needle",
        "orange",
        "obsess",
        "occupy",
        "offend",
        "obvious",
        "object",
        "oath",
        "offer",
        "open",
        "often",
        "peace",
        "peaceful",
        "pale",
        "panel",
        "panic",
        "paper",
        "parade",
        "parallel",
        "park",
        "pants",
        "quad",
        "qualify",
        "quantity",
        "quarter",
        "queen",
        "question",
        "quick",
        "quiet",
        "quit",
        "quote",
        "race",
        "radical",
        "radiation",
        "rain",
        "rarely",
        "ratio",
        "reach",
        "rank",
        "rainbow",
        "reaper",
        "sales",
        "salt",
        "satisfaction",
        "sample",
        "sauce",
        "scales",
        "safety",
        "savings",
        "sandwich",
        "saturday",
        "table",
        "tactful",
        "talk",
        "task",
        "tasty",
        "teammate",
        "target",
        "teacher",
        "technical",
        "team",
        "umbrella",
        "umpire",
        "uncle",
        "understand",
        "underground",
        "unique",
        "universal",
        "undo",
        "unit",
        "unite",
        "very",
        "vacation",
        "value",
        "various",
        "verdict",
        "version",
        "vet",
        "video",
        "village",
        "vegetable",
        "wander",
        "willow",
        "waste",
        "way",
        "wax",
        "warm",
        "weak",
        "wallet",
        "water",
        "warmth",
        "xylophone",
        "yellow",
        "yard",
        "yawn",
        "yearly",
        "yesterday",
        "yours",
        "youthful",
        "young",
        "youth",
        "you",
        "zebra",
        "zipper",
        "zero",
        "zoo",
        "zealot"
    )

//    Return passphrase as a string using required separator
    fun getPassphrase(): String {
        return passphrase.joinToString(separator)
    }

//    set the character used to separate words
    fun setSeparator(separator: String){
        this.separator = separator
    }

//    toggle first letter of words as capitalized
    fun setCapitalized(capitalize: Boolean){
        this.capitalize = capitalize
            for (word in passphrase) {
                if (capitalize) {
                passphrase[passphrase.indexOf(word)] = word?.capitalize().toString()
            } else {
                    passphrase[passphrase.indexOf(word)] = word?.lowercase().toString()
                }
        }
    }


    fun setLength(length: Int){
        this.length = length
    }

//    Insert a number at the end of a random word, otherwise strip out any digits
    fun setInsertNumber(insertNumber: Boolean){
        this.insertNumber = insertNumber
        if (insertNumber) {
//            get a random index (word) from the length of the passphrase
            val index = Random.nextInt(0, passphrase.size )
//            get that word and put a random number from 1 - 99 on the end
            val word = passphrase[index]
            passphrase[passphrase.indexOf(word)] = word + Random.nextInt(1, 99)
        } else {
//            Else strip out/remove any numbers from the passphrase
            for (word in passphrase) {
                passphrase[passphrase.indexOf(word)] = word.replace("[\\d.]".toRegex(), "")
            }
        }
    }

//    Generate a new passphrase
    fun generatePassphrase() {
//    clear out the existing passphrase
        passphrase.clear()
//    for length required, put in a random word
        for (i in 1..length) {
            dictionary.randomOrNull()?.let { passphrase.add(it) }
        }
//  Then capitalize/insert number if required
        setCapitalized(capitalize)
        setInsertNumber(insertNumber)
    }
}
