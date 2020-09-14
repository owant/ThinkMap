package com.owant.thinkmap

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.accessibility.AccessibilityEvent
import androidx.core.view.AccessibilityDelegateCompat
import androidx.core.view.ViewCompat
import androidx.core.view.ViewPropertyAnimatorCompat
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.owant.thinkmap.databinding.FragmentDailogBinding
import kotlinx.android.synthetic.main.fragment_dailog.*

/**
 *  created by Kyle.Zhong on 2020/6/12
 */
open class DialogFragment : BottomSheetDialogFragment() {

    protected lateinit var fragmentDialogBinding: FragmentDailogBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentDialogBinding = DataBindingUtil.inflate<FragmentDailogBinding>(
            inflater,
            R.layout.fragment_dailog,
            container,
            false
        )
        return fragmentDialogBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        fragmentDialogBinding.button3.setOnClickListener { button3.visibility = View.GONE }
        ViewCompat.setAccessibilityDelegate(fragmentDialogBinding.button, MyDelegate())
    }

    inner class MyDelegate : AccessibilityDelegateCompat() {

        override fun onInitializeAccessibilityEvent(host: View?, event: AccessibilityEvent?) {
            super.onInitializeAccessibilityEvent(host, event)
        }

        override fun onRequestSendAccessibilityEvent(
            host: ViewGroup?,
            child: View?,
            event: AccessibilityEvent?
        ): Boolean {
            println(event?.action)
            return super.onRequestSendAccessibilityEvent(host, child, event)
        }

        override fun performAccessibilityAction(host: View?, action: Int, args: Bundle?): Boolean {
            if (host == fragmentDialogBinding.button2) {
                println(action)
            }
            return super.performAccessibilityAction(host, action, args)
        }
    }


}