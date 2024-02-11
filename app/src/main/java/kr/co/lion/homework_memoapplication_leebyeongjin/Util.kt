package kr.co.lion.homework_memoapplication_leebyeongjin

import android.content.Context
import android.os.SystemClock
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.concurrent.thread

class Util {

    companion object{

        // Memo List
        val memoList = arrayListOf<Memo>()

        // 현재 날짜와 시간 가져오기(대한민국 기준)
        fun getDate() : String{

            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA)
            dateFormat.timeZone = TimeZone.getTimeZone("Asia/Seoul")
            return dateFormat.format(Date())
        }

        // 포커스를 주고 키보드를 올려주는 메서드
        fun showSoftInput(view: View, context: Context){

            // 포커스를 준다.
            view.requestFocus()

            thread {

                SystemClock.sleep(500)
                val inputMethodManager = context.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.showSoftInput(view, 0)
            }
        }
    }
}