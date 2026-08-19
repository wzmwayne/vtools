package com.wzmwayne.scene.krscript.model

interface AutoRunTask {
    fun onCompleted(result: Boolean?)
    val key: String?
}
