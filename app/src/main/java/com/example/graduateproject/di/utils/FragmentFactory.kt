package com.example.graduateproject.di.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import dagger.MapKey
import javax.inject.Inject
import javax.inject.Provider
import kotlin.reflect.KClass

@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@Retention(value = AnnotationRetention.RUNTIME)
@MapKey
internal annotation class FragmentKey(val value: KClass<out Fragment>)

class FragmentFactory @Inject constructor(
    private val creators: Map<Class<out Fragment>, @JvmSuppressWildcards Provider<Fragment>>
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        val fragmentClass = loadFragmentClass(classLoader, className)
        val creator = creators[fragmentClass]
            ?: return createFragmentAsFallback(classLoader, className)

        try {
            return creator.get()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    private fun createFragmentAsFallback(classLoader: ClassLoader, className: String): Fragment {
        return super.instantiate(classLoader, className)
    }
}