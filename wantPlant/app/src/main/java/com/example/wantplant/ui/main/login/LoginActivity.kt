package com.example.wantplant.ui.main.login

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.wantplant.R
import com.example.wantplant.databinding.ActivityLoginBinding
import com.example.wantplant.databinding.ActivityMainBinding
import com.example.wantplant.databinding.FragmentLandingBinding
import com.example.wantplant.ui.main.MainActivity
import com.example.wantplant.ui.main.book.BookFragment
import com.example.wantplant.ui.main.book.LandingFragment
import com.example.wantplant.ui.main.book.LandingPageFragment
import com.example.wantplant.ui.main.garden.GardenFragment
import com.example.wantplant.ui.main.profile.ProfileFragment
import com.example.wantplant.ui.main.water.month.WaterMonthFragment
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)

        setContentView(binding.root)

        // 카카오톡 로그인 버튼 누를 때
        binding.loginStartBtn.setOnClickListener {
            val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                if (error != null) {
                    when {
                        error.toString() == AuthErrorCause.AccessDenied.toString() -> {
                            Log.d("[카카오로그인]", "접근이 거부 됨(동의 취소)")
                        }

                        error.toString() == AuthErrorCause.InvalidClient.toString() -> {
                            Log.d("[카카오로그인]", "유효하지 않은 앱")
                        }

                        error.toString() == AuthErrorCause.InvalidGrant.toString() -> {
                            Log.d("[카카오로그인]", "인증 수단이 유효하지 않아 인증할 수 없는 상태")
                        }

                        error.toString() == AuthErrorCause.InvalidRequest.toString() -> {
                            Log.d("[카카오로그인]", "요청 파라미터 오류")
                        }

                        error.toString() == AuthErrorCause.InvalidScope.toString() -> {
                            Log.d("[카카오로그인]", "유효하지 않은 scope ID")
                        }

                        error.toString() == AuthErrorCause.Misconfigured.toString() -> {
                            Log.d("[카카오로그인]", "설정이 올바르지 않음(android key hash)")
                        }

                        error.toString() == AuthErrorCause.ServerError.toString() -> {
                            Log.d("[카카오로그인]", "서버 내부 에러")
                        }

                        error.toString() == AuthErrorCause.Unauthorized.toString() -> {
                            Log.d("[카카오로그인]", "앱이 요청 권한이 없음")
                        }

                        else -> { // Unknown
                            Log.d("[카카오로그인]", "기타 에러")
                        }
                    }
                } else if (token != null) {
                    Log.i(TAG, "카카오계정으로 로그인 성공 ${token.accessToken}")
//                            UserApiClient.instance.me { user, error ->
//                                nickname.text = "닉네임: ${user?.kakaoAccount?.profile?.nickname}"
//                            }
                    moveToMainActivity()
                } else {
                    Log.d("카카오로그인", "토큰==null error==null")
                }
            }

            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                UserApiClient.instance.loginWithKakaoTalk(this, callback = callback)
            } else {
                UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
            }


            setContentView(binding.root)
        }
    }

    private fun moveToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}