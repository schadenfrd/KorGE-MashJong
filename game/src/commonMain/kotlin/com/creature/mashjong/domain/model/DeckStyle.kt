package com.creature.mashjong.domain.model

enum class DeckStyle(val fileName: String) {
    MishMash("atlas_mish_mashjong.png"),
    Classic("atlas_classic_mahjong.png");

    val resourcePath: String get() = "files/$fileName"
}
