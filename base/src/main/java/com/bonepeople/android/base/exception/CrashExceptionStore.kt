package com.bonepeople.android.base.exception

import java.lang.ref.WeakReference
import java.util.Collections

internal object CrashExceptionStore {
    private val handledExceptions = Collections.synchronizedSet(mutableSetOf<IdentityWeakReference<Throwable>>())

    fun shouldHandle(exception: Throwable): Boolean {
        handledExceptions.removeAll { it.get() == null }
        return handledExceptions.add(IdentityWeakReference(exception))
    }

    private class IdentityWeakReference<T : Any>(value: T) : WeakReference<T>(value) {
        private val identityHashCode = System.identityHashCode(value)

        override fun hashCode(): Int {
            return identityHashCode
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is IdentityWeakReference<*>) return false
            return get() === other.get()
        }
    }
}