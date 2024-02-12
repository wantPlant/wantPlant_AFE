package com.example.wantplant.ui.main.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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

        // 앱의 해시키 로그에 출력
        val keyHash = Utility.getKeyHash(this)
        Log.d("KeyHash", keyHash)

        // 로그인 시작 버튼 클릭 리스너 설정
        binding.loginStartBtn.setOnClickListener {
            // 토큰 초기화
            initToken()

            // 카카오톡 로그인 가능 여부에 따른 로그인 방식 선택
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                loginWithKakaoTalk()
            } else {
                loginWithKakaoAccount()
            }
        }
    }

    // 토큰 초기화 함수
    private fun initToken() {
        val sharedPref = getSharedPreferences("TOKEN", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            remove("accessToken")
            remove("refreshToken")
            apply()
        }
    }

    // 카카오톡으로 로그인 함수
    private fun loginWithKakaoTalk() {
        UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
            if (error != null) {
                Log.e("LOGIN", "카카오톡으로 로그인 실패", error)

                // 사용자가 로그인을 취소한 경우, 로그인 취소 처리
                if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                    return@loginWithKakaoTalk
                }

                // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                loginWithKakaoAccount()
            } else if (token != null) {
                Log.i("LOGIN", "카카오톡으로 로그인 성공 ${token.accessToken}")
                moveToMainActivity()
            }
        }
    }

    // 카카오계정으로 로그인 함수
    private fun loginWithKakaoAccount() {
        UserApiClient.instance.loginWithKakaoAccount(this, callback = { token, error ->
            if (error != null) {
                Log.e("LOGIN", "카카오계정으로 로그인 실패", error)
            } else if (token != null) {
                Log.i("LOGIN", "카카오계정으로 로그인 성공 ${token.accessToken}")
                requestServerLogin(token.accessToken)
            }
        })
    }

    // 서버 로그인 요청 함수
    private fun requestServerLogin(accessToken: String?) {
        val retrofit = getRetrofit()
        val service = retrofit.create(LoginRetrofitInterfaces::class.java)

        service.login(accessToken!!).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    Log.i(
                        "LOGIN",
                        "서버 로그인 성공: accessToken = ${loginResponse?.result?.accessToken}, refreshToken = ${loginResponse?.result?.refreshToken}"
                    )

                    // 새로운 토큰을 저장합니다.
                    saveToken(
                        loginResponse?.result?.accessToken,
                        loginResponse?.result?.refreshToken
                    )

                    moveToMainActivity()
                } else {
                    Log.e("LOGIN", "서버 로그인 실패: ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("LOGIN", "서버 로그인 실패: ${t.message}", t)
            }
        })
    }

    // 토큰 저장 함수
    private fun saveToken(accessToken: String?, refreshToken: String?) {
        val sharedPref = getSharedPreferences("TOKEN", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("accessToken", accessToken)
            putString("refreshToken", refreshToken)
            apply()
        }
    }

    // 메인 액티비티로 이동 함수
    private fun moveToMainActivity() {
        Log.i("LOGIN", "메인으로 이동")
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivity(intent)
        finish()
    }
}

