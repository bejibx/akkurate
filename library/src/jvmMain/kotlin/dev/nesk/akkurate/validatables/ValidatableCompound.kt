package dev.nesk.akkurate.validatables

/*
    This way of managing validation compounds doesn't allow to write `foo and bar {}` without parentheses around the compound: `(foo and bar) {}`
    This is due to Kotlin's precedence, if you write without parentheses, its like writing `foo and (bar {})`. We could solve this by applying the
    constraints first to the last validatable, which returns the lambda and applies to the previous validatable or compound. However this brings
    many issues:
        - since the lambda must be returned, the invoke operator can no longer be inline
        - without inline functions, the lambda can no longer contain suspending code
        - validatables are not validated in the same order they are written
        - compounds of multiple types are not supported because precedence applies the lambda to the last validatable, and not to the compound

    To ease development, the first release will make parentheses mandatory.
 */
public class ValidatableCompound<T>(public val validatables: Set<Validatable<T>>) {
    public infix fun and(other: Validatable<T>): ValidatableCompound<T> = ValidatableCompound(validatables + setOf(other))
    public infix fun and(other: ValidatableCompound<T>): ValidatableCompound<T> = ValidatableCompound(validatables + other.validatables)

    // TODO: Convert to extension function (breaking change) once JetBrains fixes imports: https://youtrack.jetbrains.com/issue/KTIJ-22147
    public inline operator fun invoke(block: Validatable<T>.() -> Unit): Unit = validatables.forEach { it.block() }
}

public infix fun <T> Validatable<T>.and(other: Validatable<T>): ValidatableCompound<T> = ValidatableCompound(setOf(this, other))
public infix fun <T> Validatable<T>.and(other: ValidatableCompound<T>): ValidatableCompound<T> = ValidatableCompound(setOf(this) + other.validatables)

// TODO: find a solution to improve type combinations
//infix fun <T1, T2> ValidatableCompound<T1>.and(other: Validatable<T2>): ValidatableCompound<Any?> = TODO()
//infix fun <T1, T2: Any?> ValidatableCompound<T1>.and(other: ValidatableCompound<T2>): ValidatableCompound<Any?> = TODO()
//infix fun <T1, T2: Any?> Validatable<T1>.and(other: Validatable<T2>): ValidatableCompound<Any?> = TODO()