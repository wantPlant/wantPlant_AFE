package com.example.wantplant.ui.main.profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.wantplant.R
import com.example.wantplant.databinding.FragmentProfileBinding
import com.example.wantplant.ui.main.MainActivity
import com.example.wantplant.ui.main.book.ManualFragment
import com.example.wantplant.ui.main.login.LoginActivity
import com.kakao.sdk.user.UserApiClient


class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater)

        binding.profileHowtouseTv.setOnClickListener {
            (context as MainActivity)
                .supportFragmentManager.beginTransaction().replace(R.id.main_frm, ManualFragment()).commitAllowingStateLoss()
        }

        binding.profileLogoutCl.setOnClickListener {
            UserApiClient.instance.logout { error ->
                if (error != null) {
                    Log.e("PROFILE", "로그아웃 실패. error = $error")
                } else {
                    Log.i("PROFILE", "로그아웃 성공. 다음부터 로그인 필요.")


                    // 로그아웃에 성공하면 LoginActivity로 이동합니다.
                    val intent = Intent(context, LoginActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)

                    // MainActivity를 종료합니다.
                    if (context is Activity) {
                        (context as Activity).finish()
                    }
                }
            }
        }

        return binding.root
    }
}