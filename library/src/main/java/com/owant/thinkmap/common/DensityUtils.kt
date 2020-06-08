package com.owant.thinkmap.common

import android.content.Context
import android.util.TypedValue

/**
 * 单位转换 工具类
 */
object DensityUtils {
    /**
     * dp转px
     */
    fun dp2px(context: Context, dpVal: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dpVal, context.resources
                .displayMetrics
        ).toInt()
    }

    /**
     * sp转px
     */
    fun sp2px(context: Context, spVal: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP, spVal, context.resources
                .displayMetrics
        ).toInt()
    }

    /**
     * px转dp
     */
    fun px2dp(context: Context, pxVal: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (pxVal / scale).toInt()
    }

    /**
     * px转sp
     */
    fun px2sp(context: Context, pxVal: Float): Float {
        val result = (pxVal / context.resources.displayMetrics.scaledDensity).toInt()
        return result.toFloat()
    }
}