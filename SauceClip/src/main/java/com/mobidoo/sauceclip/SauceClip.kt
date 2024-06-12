package com.mobidoo.sauceclip

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log

class SauceClip {
    companion object {
        /**
         * 클립 액티비티를 열어주는 함수
         *
         * @param context 컨텍스트
         * @param partnerId 파트너 아이디
         * @param clipId 클립 아이디
         * @param curationId 큐레이션 아이디 (옵션)
         * @param openProductActivity 제품 액티비티 열기 여부 (옵션)
         * @param stageMode 스테이지 모드 여부 (옵션)
         * @param onEnter 클립 액티비티 진입 시 실행할 함수 (옵션)
         * @param onMoveExit 클립 액티비티 이동 종료 시 실행할 함수 (옵션)
         * @param onShare 공유 시 실행할 함수 (옵션)
         * @param onMoveProduct 제품 이동 시 실행할 함수 (옵션)
         * @param onMoveCart 카트 이동 시 실행할 함수 (옵션)
         * @param onError 에러 발생 시 실행할 함수 (옵션)
         */
        fun openClipActivity(
            context: Context,
            partnerId: String,
            clipId: String,
            curationId: String? = null,
            openProductActivity: Boolean = true,
            stageMode: Boolean = false,
            onEnter: ((clipActivity: SauceClipActivity) -> Unit)? = null,
            onMoveExit: ((clipActivity: SauceClipActivity) -> Unit)? = null,
            onShare: ((message: SauceShareInfo) -> Unit)? = null,
            onMoveProduct: ((message: SauceProductInfo, clipActivity: SauceClipActivity) -> Unit)? = null,
            onMoveCart: ((clipActivity: SauceClipActivity) -> Unit)? = null,
            onAddCart: ((message: SauceCartInfo, clipActivity: SauceClipActivity) -> Unit)? = null,
            onError: ((message: SauceErrorInfo) -> Unit)? = null,
        ) {
            val intent = Intent(context, SauceClipActivity::class.java)
            intent.putExtra("partnerId", partnerId)
            intent.putExtra("clipId", clipId)
            intent.putExtra("curationId", curationId)
            intent.putExtra("stageMode", stageMode)
            intent.putExtra("openProductActivity", openProductActivity)

            SauceClipActivity.sauceclipEnter = onEnter
            SauceClipActivity.sauceclipMoveExit = onMoveExit
            SauceClipActivity.sauceclipOnShare = onShare
            SauceClipActivity.sauceclipMoveProduct = onMoveProduct
            SauceClipActivity.sauceclipMoveCart = onMoveCart
            SauceClipActivity.sauceclipAddCart = onAddCart
            SauceClipActivity.sauceclipError = onError

            context.startActivity(intent)
        }
    }
}