package com.creature.mashjong.domain.logic

import com.creature.mashjong.domain.model.TilePosition

interface Command {
    fun execute()
    fun undo()
}

class MatchCommand(
    private val game: MahjongGame,
    val tileA: TilePosition,
    val tileB: TilePosition
) : Command {

    override fun execute() {
        game.removeTile(tileA)
        game.removeTile(tileB)
    }

    override fun undo() {
        game.addTile(tileA)
        game.addTile(tileB)
    }
}
