//package com.example.wantplant.ui.main.login
//
//import android.content.ContentValues.TAG
//import android.content.Intent
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.util.Log
//import com.example.wantplant.R
//import com.example.wantplant.databinding.ActivityLoginBinding
//import com.example.wantplant.databinding.ActivityMainBinding
//import com.example.wantplant.databinding.FragmentLandingBinding
//import com.example.wantplant.ui.main.MainActivity
//import com.example.wantplant.ui.main.book.BookFragment
//import com.example.wantplant.ui.main.book.LandingFragment
//import com.example.wantplant.ui.main.book.LandingPageFragment
//import com.example.wantplant.ui.main.garden.GardenFragment
//import com.example.wantplant.ui.main.profile.ProfileFragment
//import com.example.wantplant.ui.main.selectgarden.SelectGardenActivity
//import com.example.wantplant.ui.main.water.month.WaterMonthFragment
//import com.kakao.sdk.auth.model.OAuthToken
//import com.kakao.sdk.common.model.AuthErrorCause
//import com.kakao.sdk.common.model.ClientError
//import com.kakao.sdk.common.model.ClientErrorCause
//import com.kakao.sdk.common.util.Utility
//import com.kakao.sdk.user.UserApiClient
//
//class LoginActivity : AppCompatActivity() {
//
//    private lateinit var binding: ActivityLoginBinding
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        binding = ActivityLoginBinding.inflate(layoutInflater)
//
//        setContentView(binding.root)
//
//        var keyHash = Utility.getKeyHash(this)
//        Log.d("해시", keyHash)
//
//        // 카카오톡 로그인 버튼 누를 때
//        binding.loginStartBtn.setOnClickListener {
//            moveToMainActivity()
//        }
//    }
//
//    private fun moveToMainActivity() {
//        val intent = Intent(this, MainActivity::class.java)
//        startActivity(intent)
//        finish()
//    }
//}

package com.example.wantplant.ui.main.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.wantplant.databinding.ActivityLoginBinding
import com.example.wantplant.ui.main.MainActivity
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient

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
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
//            val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
//                if (error != null) {
//                    Log.e("LOGIN", "카카오계정으로 로그인 실패", error)
//                } else if (token != null) {
//                    Log.i("LOGIN", "카카오계정으로 로그인 성공 ${token.accessToken}")
//                    moveToMainActivity()
//                }
//            }
//
//            // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
//            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
//                UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
//                    if (error != null) {
//                        Log.e("LOGIN", "카카오톡으로 로그인 실패", error)
//
//                        // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
//                        // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
//                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
//                            return@loginWithKakaoTalk
//                        }
//
//                        // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
//                        UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
//                    } else if (token != null) {
//                        Log.i("LOGIN", "카카오톡으로 로그인 성공 ${token.accessToken}")
//                        val intent = Intent(this, MainActivity::class.java)
//                        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
//                        finish()
//                    }
//                }
//            } else {
//                UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
//            }
        }
    }

    private fun moveToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
