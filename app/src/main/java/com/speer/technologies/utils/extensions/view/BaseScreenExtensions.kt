package com.speer.technologies.utils.extensions.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelStoreOwner
import androidx.viewbinding.ViewBinding
import com.speer.technologies.presentation.base.viewmodel.BaseViewModel
import com.speer.technologies.view.base.BaseActivity
import com.speer.technologies.view.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.getLazyViewModelForClass
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType

/**
 * Inflates ViewBinding of [VB] generic type for an activity using reflection
 */
fun <VB : ViewBinding> BaseActivity<VB, *>.inflateBinding(inflater: LayoutInflater): VB {
    val clazz = this::class.java.findGenericClass<ViewBinding>()
        ?: error("Cannot find view binding generic parameter for $this")

    val method = clazz.findMethod { it.parameterTypes[0] == LayoutInflater::class.java }

    return method.invoke(null, inflater) as VB
}

/**
 * Inflates ViewBinding of [VB] generic type for a fragment using reflection
 */
fun <VB : ViewBinding> BaseFragment<VB, *>.inflateBinding(
    inflater: LayoutInflater,
    parent: ViewGroup?,
    attachToParent: Boolean,
): VB {
    val clazz = this::class.java.findGenericClass<ViewBinding>()
        ?: error("Cannot find view binding generic parameter for $this")

    val method = clazz.findMethod {
        it.parameterTypes.size == 3 &&
                it.parameterTypes[0] == LayoutInflater::class.java &&
                it.parameterTypes[1] == ViewGroup::class.java &&
                it.parameterTypes[2] == Boolean::class.java
    }

    return method.invoke(null, inflater, parent, attachToParent) as VB
}

/**
 * Creates ViewModel of [VM] generic type using reflection
 */
fun <VM : BaseViewModel> ViewModelStoreOwner.createViewModel(): VM {
    val clazz = this::class.java.findGenericClass<BaseViewModel>() as? Class<VM>
        ?: error("Cannot find view model generic parameter for $this")

    return getLazyViewModelForClass(clazz.kotlin, this).value
}

private inline fun <T> Class<T>.findMethod(predicate: (Method) -> Boolean): Method =
    declaredMethods.first(predicate)

/**
 * Tries to find class in class hierarchy with [T] generic type
 */
private inline fun <reified T> Class<*>.findGenericClass(): Class<T>? {
    var clazz: Class<*>? = this

    do {
        val superClass = clazz?.genericSuperclass

        if (superClass is ParameterizedType) {
            superClass.actualTypeArguments.firstOrNull {
                T::class.java.isAssignableFrom((it as Class<*>))
            }?.let {
                return it as Class<T>
            }
        }

        clazz = superClass as Class<*>?

    } while (clazz != null)

    return null
}
