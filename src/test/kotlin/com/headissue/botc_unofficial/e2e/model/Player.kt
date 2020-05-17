package com.headissue.botc_unofficial.e2e.model

data class Player(
    val name: String,
    val character: String = "",
    val dead: Boolean = false,
    val canVote: Boolean = true,
    val usedNomination: Boolean = false,
    val nominated: Boolean = false,
    val nominatedBy: String? = null
)