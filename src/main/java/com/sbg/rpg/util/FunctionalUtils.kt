package com.sbg.rpg.util

fun <T1, T2, R> Function2<T1, T2, R>.bindFirst(arg: T1): (T2) -> R {
    return { bindSecond -> this(arg, bindSecond) }
}

fun <T1, T2, R> Function2<T1, T2, R>.bindSecond(arg: T2): (T1) -> R {
    return { bindFirst -> this(bindFirst, arg) }
}

fun <T1, T2, R> compose(f: (T2) -> R, g: (T1) -> T2): (T1) -> R {
    return { x -> f(g(x)) }
}
