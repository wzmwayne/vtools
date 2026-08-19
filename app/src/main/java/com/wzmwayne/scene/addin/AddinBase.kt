package com.wzmwayne.scene.addin

import android.app.Activity
import com.wzmwayne.scene.common.ui.ProgressBarDialog

/**
 * Created by Hello on 2018/02/20.
 */

open class AddinBase(private var context: Activity) : ProgressBarDialog(context) {
    var title: String? = null
    var desc: String? = null
    var command: String? = null

    open fun run() {
        if (command != null) {
            execShell(command!!)
        }
    }
}
