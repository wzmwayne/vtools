package com.wzmwayne.scene.utils

import android.content.Context
import com.wzmwayne.scene.library.basic.AccessibleServiceState
import com.wzmwayne.scene.library.shell.AccessibilityServiceUtils
import com.wzmwayne.scene.AccessibilityScenceMode

/**
 * Created by Hello on 2018/06/03.
 */

class AccessibleServiceHelper {
    // 场景模式服务是否正在运行
    fun serviceRunning(context: Context): Boolean {
        return AccessibleServiceState().serviceRunning(context, "AccessibilityScenceMode")
    }

    // 停止场景模式服务
    fun stopSceneModeService(context: Context): Boolean {
        return AccessibilityServiceUtils().stopService("${context.packageName}/${AccessibilityScenceMode::class.java.name}")
    }

    fun serviceRunning(context: Context, serviceName: String): Boolean {
        return AccessibleServiceState().serviceRunning(context, serviceName)
    }
}
