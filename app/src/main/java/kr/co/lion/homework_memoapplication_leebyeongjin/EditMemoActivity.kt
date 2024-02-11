package kr.co.lion.homework_memoapplication_leebyeongjin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import kr.co.lion.homework_memoapplication_leebyeongjin.databinding.ActivityEditMemoBinding

// EditMemoActivity

// 수정하기 전의 내용을 TextField에 보여준다.
// 수정을 완료하고 상단 오른똑 툴바 아이콘을 누르면 ShowMemoActivity화면으로 돌아가게 한다.
// 이때, 수정된 제목과 내용으로 보여지게 한다.
// 작성 날짜는 수정하지 않는다.

class EditMemoActivity : AppCompatActivity() {

    lateinit var activityEditMemoBinding: ActivityEditMemoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityEditMemoBinding = ActivityEditMemoBinding.inflate(layoutInflater)
        setContentView(activityEditMemoBinding.root)

        // Toolbar 설정
        setToolbar()

        // View 설정
        setView()

        // textField 실시간 유효성 검사
        textFieldValidation()
    }

    // toolbar 설정
    fun setToolbar(){

        activityEditMemoBinding.apply {
            toolbarEditMemo.apply {

                // 제목
                title = "메모 수정"

                // 뒤로 가기
                setNavigationIcon(R.drawable.arrow_back)
                setNavigationOnClickListener {

                    setResult(RESULT_CANCELED)

                    // 현재 Activity 종료
                    finish()
                }

                menuInflater.inflate(R.menu.menu_edit_memo, menu)
                setOnMenuItemClickListener {

                    when(it.itemId){

                        // 수정(메뉴 아이템) 클릭 시
                        R.id.menu_item_EditMemo_Edit -> {

                            // 수정
                            val title = TextFieldEditMemoTitle.text.toString().trim()
                            val content = TextFieldEditMemoContent.text.toString().trim()

                            var errorCnt = 0

                            // 내용 비어 있을 시 오류 알림
                            if (content.isEmpty()){
                                TextInputLayoutEditMemoContent.error = "내용을 입력해주세요(1글자 이상)."
                                errorCnt += 1
                                Util.showSoftInput(TextFieldEditMemoContent, this@EditMemoActivity)
                            } else {
                                TextInputLayoutEditMemoContent.error = null
                            }

                            // 제목 비어 있을 시 오류 알림
                            if (title.isEmpty()){
                                TextInputLayoutEditMemoTitle.error = "제목을 입력해주세요(1글자 이상)."
                                errorCnt += 1
                                Util.showSoftInput(TextFieldEditMemoTitle, this@EditMemoActivity)
                            } else {
                                TextInputLayoutEditMemoTitle.error = null
                            }

                            // 유효성 검사 통과 시 메모 추가
                            if (errorCnt == 0){

                                // Memo 수정
                                val position = intent.getIntExtra("position", 0)
                                Util.memoList[position].title = title
                                Util.memoList[position].content = content

                                setResult(RESULT_OK)

                                // 현재 Activity 종료
                                finish()
                            }
                        }

                    }
                    true
                }
            }
        }
    }

    // View 설정
    fun setView(){

        val position = intent.getIntExtra("position", 0)
        val memo = Util.memoList[position]

        activityEditMemoBinding.apply {

            TextFieldEditMemoTitle.setText(memo.title)
            TextFieldEditMemoContent.setText(memo.content)
        }
    }

    // textField 실시간 유효성 검사
    fun textFieldValidation(){

        activityEditMemoBinding.apply {

            // Memo Title 실시간 유효성 검사
            TextFieldEditMemoTitle.addTextChangedListener(object : TextWatcher {

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                // text가 변경 된 후 입력된 게 없을 시 오류 알림
                override fun afterTextChanged(s: Editable?) {
                    if(s.toString().trim().isEmpty()){
                        TextInputLayoutEditMemoTitle.error = "제목을 입력해주세요(1글자 이상)."
                    } else {
                        TextInputLayoutEditMemoTitle.error = null
                    }
                }
            })

            // Memo Content 실시간 유효성 검사
            TextFieldEditMemoContent.addTextChangedListener(object : TextWatcher {

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                // text가 변경 된 후 입력된 게 없을 시 오류 알림
                override fun afterTextChanged(s: Editable?) {
                    if(s.toString().trim().isEmpty()){
                        TextInputLayoutEditMemoContent.error = "내용을 입력해주세요(1글자 이상)."
                    } else {
                        TextInputLayoutEditMemoContent.error = null
                    }
                }
            })
        }
    }
}