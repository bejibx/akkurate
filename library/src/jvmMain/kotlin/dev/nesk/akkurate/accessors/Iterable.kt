package dev.nesk.akkurate.accessors

import dev.nesk.akkurate.Validatable

public operator fun <T> Validatable<out Iterable<T>>.iterator(): Iterator<Validatable<T>> = TODO()
public inline fun <T> Validatable<out Iterable<T>>.each(block: Validatable<T>.() -> Unit): Unit = TODO()
