package com.mobidoo.sauceclip

import android.app.Activity
import android.content.Context
import android.content.Intent

class SauceClip {
    companion object {
        fun openClipActivity(
            context: Context,
            partnerId: String,
            clipId: String,
            openProductActivity: Boolean = true,
            stageMode: Boolean = false,
            onEnter: (() -> Unit)? = null,
            onMoveExit: ((activity: Activity) -> Unit)? = null,
            onShare: ((message: SauceShareInfo) -> Unit)? = null,
            onMoveProduct: ((message: SauceProductInfo) -> Unit)? = null,
            onMoveCart: ((message: SauceCartInfo) -> Unit)? = null,
        ) {
            val intent = Intent(context, SauceClipActivity::class.java)
            intent.putExtra("partnerId", partnerId)
            intent.putExtra("clipId", clipId)
            intent.putExtra("stageMode", stageMode)
            intent.putExtra("openProductActivity", openProductActivity)

            SauceClipActivity.sauceclipEnter = onEnter
            SauceClipActivity.sauceclipMoveExit = onMoveExit
            SauceClipActivity.sauceclipOnShare = onShare
            SauceClipActivity.sauceclipMoveProduct = onMoveProduct
            SauceClipActivity.sauceclipMoveCart = onMoveCart

            context.startActivity(intent)
        }
    }
}