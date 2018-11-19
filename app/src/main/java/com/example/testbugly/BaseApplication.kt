package com.example.testbugly

import android.app.Application
import android.content.Context
import android.content.Intent
import android.support.multidex.MultiDex
import com.tencent.bugly.Bugly
import com.tencent.bugly.beta.Beta
import com.tencent.bugly.beta.interfaces.BetaPatchListener

@Suppress("unused")
class BaseApplication : Application(), BetaPatchListener {

    override fun onCreate() {
        super.onCreate()
        /**Bugly管理平台：https://bugly.qq.com/v2/ */
        Bugly.init(this, "795287b50d", false)
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(base)
        /**安装tinker*/
        Beta.installTinker()
        /**设置监听器，补丁包应用成功后杀进程，重启app*/
        Beta.betaPatchListener = this
    }

    /**Bugly BetaPatchListener*/
    override fun onApplySuccess(p0: String?) {
        /**补丁包应用成功回调，在这里杀进程，重启app，完成热更新。
        否则需要等待用户下次自己主动杀进程重启后才能完成更新*/
        restartApp()
    }

    override fun onPatchReceived(p0: String?) {
    }

    override fun onApplyFailure(p0: String?) {
    }

    override fun onDownloadReceived(p0: Long, p1: Long) {
    }

    override fun onDownloadSuccess(p0: String?) {
    }

    override fun onDownloadFailure(p0: String?) {
    }

    override fun onPatchRollback() {
    }

    /**
     * 杀进程，重启app
     */
    private fun restartApp() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        android.os.Process.killProcess(android.os.Process.myPid())
    }
}