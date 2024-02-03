package com.example.wantplant.ui.main.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.wantplant.databinding.ActivityLoginBinding
import com.example.wantplant.ui.main.MainActivity
import com.example.wantplant.data.remote.garden.LoginRetrofitInterfaces
import com.example.wantplant.data.local.LoginResponse
import com.example.wantplant.utils.getRetrofit
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)

        setContentView(binding.root)

        var keyHash = Utility.getKeyHash(this)
        Log.d("해시", keyHash)

        // 카카오톡 로그인 버튼 누를 때
        binding.loginStartBtn.setOnClickListener {
            // 토큰 초기화
            val sharedPref = getSharedPreferences("TOKEN", Context.MODE_PRIVATE)
            with (sharedPref.edit()) {
                remove("accessToken")
                remove("refreshToken")
                apply()
            }

            val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                if (error != null) {
                    Log.e("LOGIN", "카카오계정으로 로그인 실패", error)
                } else if (token != null) {
                    Log.i("LOGIN", "카카오계정으로 로그인 성공 ${token.accessToken}")

                    // 전역 변수에 accessToken 저장
                    val accessToken = token.accessToken

                    // Retrofit 객체 생성
                    val retrofit = getRetrofit()

                    // 인터페이스 생성
                    val service = retrofit.create(LoginRetrofitInterfaces::class.java)

                    // 서버에 로그인 요청을 보냅니다.
                    service.login(accessToken!!).enqueue(object : Callback<LoginResponse> {
                        override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                            if (response.isSuccessful) {
                                val loginResponse = response.body()
                                Log.i("LOGIN", "서버 로그인 성공: accessToken = ${loginResponse?.result?.accessToken}, refreshToken = ${loginResponse?.result?.refreshToken}")

                                // 새로운 토큰을 저장합니다.
                                val sharedPref = getSharedPreferences("TOKEN", Context.MODE_PRIVATE)
                                with (sharedPref.edit()) {
                                    putString("accessToken", loginResponse?.result?.accessToken)
                                    putString("refreshToken", loginResponse?.result?.refreshToken)
                                    apply()
                                }

                                moveToMainActivity()
                            } else {
                                Log.e("LOGIN", "서버 로그인 실패: ${response.errorBody()}")
                            }
                        }

                        override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                            Log.e("LOGIN", "서버 로그인 실패", t)
                        }
                    })
                }
            }

            // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                    if (error != null) {
                        Log.e("LOGIN", "카카오톡으로 로그인 실패", error)

                        // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                        // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                            return@loginWithKakaoTalk
                        }

                        // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                        UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
                    } else if (token != null) {
                        Log.i("LOGIN", "카카오톡으로 로그인 성공 ${token.accessToken}")
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                        finish()
                    }
                }
            } else {
                UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
            }
        }
    }

    private fun moveToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}

