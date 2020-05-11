package com.headissue.botc_unofficial.e2e.model

data class Player(
    val name: String,
    val dead: Boolean = false,
    val canVote: Boolean = true
) {
}