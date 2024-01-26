/*
  Copyright 2023 Kakao Corp.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package com.kakao.sdk.sample.common.k2220

import android.content.Context
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.KakaoJson
import com.kakao.sdk.common.util.PersistentKVStore
import com.kakao.sdk.common.util.SdkLog
import com.kakao.sdk.common.util.SharedPrefsWrapper
import com.kakao.sdk.network.KakaoRetrofitConverterFactory
import com.kakao.sdk.sample.common.k2220.model.CertDemoError
import com.kakao.sdk.sample.common.k2220.model.CertDemoResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CertDemoClient(context: Context) {
    private val demoApi: CertDemoApi
    private val appCache: PersistentKVStore
    val txId: String? get() = appCache.getString(TX_ID_KEY)

    init {
        val preferences = context.getSharedPreferences("default", Context.MODE_PRIVATE)
        val phase = preferences.getString("phase", null)
        val baseUrl = phaseUrl(phase)

        val interceptor: HttpLoggingInterceptor =
            HttpLoggingInterceptor { message -> SdkLog.i("log: $message") }.apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(OkHttpClient.Builder().addInterceptor(interceptor).build())
            .addConverterFactory(KakaoRetrofitConverterFactory())
            .addConverterFactory(GsonConverterFactory.create(KakaoJson.base))
            .build()
        demoApi = retrofit.create(CertDemoApi::class.java)
        appCache = SharedPrefsWrapper(KakaoSdk.applicationContextInfo.sharedPreferences)
    }

    fun saveTxId(txId: String) {
        appCache.putString(TX_ID_KEY, txId).commit()
    }

    fun deleteTxId() {
        appCache.remove(TX_ID_KEY).commit()
    }

    fun demoLogin(publicKey: String, callback: (txId: String?, error: Throwable?) -> Unit) {
        val param = mapOf("public_key" to publicKey)

        demoApi.login(param).enqueue(object : Callback<CertDemoResponse> {
            override fun onResponse(
                call: Call<CertDemoResponse>,
                response: Response<CertDemoResponse>,
            ) {
                if (!response.isSuccessful) {
                    val rawError = response.errorBody()?.string() ?: ""
                    val error =
                        KakaoJson.fromJson<CertDemoError>(rawError, CertDemoError::class.java)
                    callback(null, error)
                    return
                }

                callback(response.body()!!.txId, null)
            }

            override fun onFailure(call: Call<CertDemoResponse>, t: Throwable) {
                callback(null, t)
            }
        })
    }

    fun demoVerify(
        txId: String,
        appUserId: Long,
        callback: (response: CertDemoResponse?, error: Throwable?) -> Unit,
    ) {
        val params = hashMapOf<String, Any>("tx_id" to txId, "app_user_id" to appUserId)

        demoApi.verify(params).enqueue(object : Callback<CertDemoResponse> {
            override fun onResponse(
                call: Call<CertDemoResponse>,
                response: Response<CertDemoResponse>,
            ) {
                if (!response.isSuccessful) {
                    val rawError = response.errorBody()?.string() ?: ""
                    val error =
                        KakaoJson.fromJson<CertDemoError>(rawError, CertDemoError::class.java)
                    callback(null, error)
                    return
                }

                callback(response.body(), null)
            }

            override fun onFailure(call: Call<CertDemoResponse>, t: Throwable) {
                callback(null, t)
            }
        })
    }

    fun demoSign(
        txId: String,
        data: String,
        signature: String,
        callback: (response: CertDemoResponse?, error: Throwable?) -> Unit,
    ) {
        val params = mapOf("tx_id" to txId, "data" to data, "signature" to signature)

        demoApi.sign(params).enqueue(object : Callback<CertDemoResponse> {
            override fun onResponse(
                call: Call<CertDemoResponse>,
                response: Response<CertDemoResponse>,
            ) {
                if (!response.isSuccessful) {
                    val rawError = response.errorBody()?.string() ?: ""
                    val error =
                        KakaoJson.fromJson<CertDemoError>(rawError, CertDemoError::class.java)
                    callback(null, error)
                    return
                }

                callback(response.body(), null)
            }

            override fun onFailure(call: Call<CertDemoResponse>, t: Throwable) {
                callback(null, t)
            }
        })
    }

    private fun phaseUrl(phase: String?): String {
        return when (phase) {
            "cbt" -> "https://cbt-zert-mock.dev.onkakao.net"
            "sandbox" -> "https://zert-mock.sandbox.onkakao.net"
            else -> "https://zert-mock.dev.onkakao.net"
        }
    }

    companion object {
        private var INSTANCE: CertDemoClient? = null

        @JvmStatic
        fun instance(context: Context): CertDemoClient {
            return INSTANCE ?: CertDemoClient(context)
        }

        private const val TX_ID_KEY = "com.kakao.sdk.k2220.txId"
    }
}
