package com.sbg.rpg.util

[deprecated("Use funKTionale")]
fun <T1, T2, R> Function2<T1, T2, R>.bindFirst(arg: T1): (T2) -> R {
    return { (bindSecond: T2) -> this(arg, bindSecond) }
}

[deprecated("Use funKTionale")]
fun <T1, T2, R> Function2<T1, T2, R>.bindSecond(arg: T2): (T1) -> R {
    return { (bindFirst: T1) -> this(bindFirst, arg) }
}

[deprecated("Use funKTionale")]
fun compose<A, B, C>(f: (B) -> C, g: (A) -> B): (A) -> C {
    return { x -> f(g(x)) }
}
