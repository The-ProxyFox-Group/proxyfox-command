package dev.proxyfox.command

import dev.proxyfox.command.menu.CommandButton
import dev.proxyfox.command.menu.CommandScreen
import dev.proxyfox.command.node.CommandNode
import kotlin.math.min

public typealias Executor<T> = suspend CommandContext<T>.() -> Boolean

public typealias NodeActionParam<T, C, V> = suspend CommandNode<T, C>.(ParamGetter<T, V>) -> Unit

public typealias NodeAction<T, C> = suspend CommandNode<T, C>.() -> Unit

public typealias ParamGetter<T, V> = suspend CommandContext<T>.() -> V

public typealias ScreenBuilder<T, C> = CommandScreen<T, C>.(ctx: C) -> Unit
public typealias ButtonBuilder<T, C> = CommandButton<T, C>.(ctx: C) -> Unit

/**
 * Levenshtein Distance algorithm
 * Credits: https://gist.github.com/ademar111190/34d3de41308389a0d0d8
 * */
public fun String.levenshtein(other: String): Int {
    if(this == other) { return 0 }
    if(isEmpty()) { return other.length }
    if(other.isEmpty()) { return length }

    val lhsLength = length + 1
    val rhsLength = other.length + 1

    var cost = Array(lhsLength) { it }
    var newCost = Array(lhsLength) { 0 }

    for (i in 1 until rhsLength) {
        newCost[0] = i

        for (j in 1 until lhsLength) {
            val match = if(this[j - 1] == other[i - 1]) 0 else 1

            val costReplace = cost[j - 1] + match
            val costInsert = cost[j] + 1
            val costDelete = newCost[j - 1] + 1

            newCost[j] = min(min(costInsert, costDelete), costReplace)
        }

        val swap = cost
        cost = newCost
        newCost = swap
    }

    return cost[lhsLength - 1]
}