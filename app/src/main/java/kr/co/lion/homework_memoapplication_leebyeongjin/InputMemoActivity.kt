package kr.co.lion.homework_memoapplication_leebyeongjin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import kr.co.lion.homework_memoapplication_leebyeongjin.databinding.ActivityInputMemoBinding

// InputActivity

// 제목과 내용을 입력하고 상단 오른똑 툴바 아이콘을 누르면
// MainActivity의 화면으로 돌아가고 MainActivity의 화면의 RecyclerView의 항목으로 보여지게 한다.
// 이때, 날짜는 현재 날짜를 구해 사용한다.
// 내용은 여러 줄을 입력할 수 있도록 해준다.

class InputMemoActivity : AppCompatActivity() {

    lateinit var activityInputMemoBinding: ActivityInputMemoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityInputMemoBinding = ActivityInputMemoBinding.inflate(layoutInflater)
        setContentView(activityInputMemoBinding.root)

        // Toolbar 설정
        setToolbar()

        // View 설정
        initView()

        // textField 유효성 검사
        textFieldValidation()
    }

    // Toolbar 설정
    fun setToolbar(){

        activityInputMemoBinding.apply {
            toolbarInputMemo.apply {

                // 제목
                title = "메모 작성"

                // 뒤로 가기
                setNavigationIcon(R.drawable.arrow_back)
                setNavigationOnClickListener {

                    setResult(RESULT_CANCELED)

                    // 현재 Activity 종료
                    finish()
                }

                inflateMenu(R.menu.menu_input_memo)
                setOnMenuItemClickListener {

                    when(it.itemId){

                        // 메모 추가(메뉴 아이템) 클릭 시
                        R.id.menu_item_InputMemo_Add -> {

                            val title = TextFieldInputMemoTitle.text.toString().trim()
                            val content = TextFieldInputMemoContent.text.toString().trim()

                            var errorCnt = 0

                            // 내용 비어 있을 시 오류 알림
                            if (content.isEmpty()){
                                TextInputLayoutInputMemoContent.error = "내용을 입력해주세요(1글자 이상)."
                                errorCnt += 1
                                Util.showSoftInput(TextFieldInputMemoContent, this@InputMemoActivity)
                            } else {
                                TextInputLayoutInputMemoContent.error = null
                            }

                            // 제목 비어 있을 시 오류 알림
                            if (title.isEmpty()){
                                TextInputLayoutInputMemoTitle.error = "제목을 입력해주세요(1글자 이상)."
                                errorCnt += 1
                                Util.showSoftInput(TextFieldInputMemoTitle, this@InputMemoActivity)
                            } else {
                                TextInputLayoutInputMemoTitle.error = null
                            }

                            // 유효성 검사 통과 시 메모 추가
                            if (errorCnt == 0){



                                // Memo 객체 생성 및 추가
                                val memo = Memo(title, content)
                                Util.memoList.add(memo)

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
    fun initView(){

        activityInputMemoBinding.apply {

            Util.showSoftInput(TextFieldInputMemoTitle, this@InputMemoActivity)
        }
    }

    // textField 유효성 검사
    fun textFieldValidation(){

        activityInputMemoBinding.apply {

            // Memo Title 유효성 검사
            TextFieldInputMemoTitle.addTextChangedListener(object : TextWatcher{

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                // text가 변경 된 후 입력된 게 없을 시 오류 알림
                override fun afterTextChanged(s: Editable?) {
                    if(s.toString().trim().isEmpty()){
                        TextInputLayoutInputMemoTitle.error = "제목을 입력해주세요(1글자 이상)."
                    } else {
                        TextInputLayoutInputMemoTitle.error = null
                    }
                }
            })

            // Memo Content 유효성 검사
            TextFieldInputMemoContent.addTextChangedListener(object : TextWatcher{

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                // text가 변경 된 후 입력된 게 없을 시 오류 알림
                override fun afterTextChanged(s: Editable?) {
                    if(s.toString().trim().isEmpty()){
                        TextInputLayoutInputMemoContent.error = "내용을 입력해주세요(1글자 이상)."
                    } else {
                        TextInputLayoutInputMemoContent.error = null
                    }
                }
            })
        }
    }
}