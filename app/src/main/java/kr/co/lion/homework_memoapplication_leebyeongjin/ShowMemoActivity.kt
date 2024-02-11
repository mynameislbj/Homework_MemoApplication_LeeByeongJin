package kr.co.lion.homework_memoapplication_leebyeongjin

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kr.co.lion.homework_memoapplication_leebyeongjin.databinding.ActivityShowMemoBinding

// ShowActivity

// MainActivity화면의 RecyclerView에서 선택한 항목의 메모 내용이 보여지게 한다.
// 메모의 내용은 TextField를 통해 보여주고 입력은 불가능하게 한다.
// 툴바에는 수정과 삭제가 있으며 아이콘은 자유롭게 해준다.
// 수정을 누르면 EditMemoActvity화면으로 이동하고
// 삭제를 누르면 현재 메모에 대해 삭제 처리를 하고 MainActivity화면으로 돌아간다.


class ShowMemoActivity : AppCompatActivity() {

    lateinit var activityShowMemoBinding: ActivityShowMemoBinding

    // EditMemoActivity 런처
    lateinit var editMemoActivityLauncher : ActivityResultLauncher<Intent>

    // 수정 여부
    var isEdit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityShowMemoBinding = ActivityShowMemoBinding.inflate(layoutInflater)
        setContentView(activityShowMemoBinding.root)

        setLauncher()

        setToolbar()

        setView()
    }

    // 런처 설정
    fun setLauncher(){

        val contract_EditMemo = ActivityResultContracts.StartActivityForResult()
        editMemoActivityLauncher = registerForActivityResult(contract_EditMemo){

            when(it.resultCode){

                // 메모 수정했을 시
                RESULT_OK -> {isEdit = true}

                // 뒤로가기 했을 시(메모 수정X)
                RESULT_CANCELED -> {isEdit = false}
            }
        }
    }

    // toolbar 설정
    fun setToolbar(){

        activityShowMemoBinding.apply {

            toolbarShowMemo.apply {

                // 제목
                title = "메모 보기"

                // 뒤로 가기
                setNavigationIcon(R.drawable.arrow_back)
                setNavigationOnClickListener {

                    setResult(RESULT_CANCELED)

                    // 현재 Activity 종료
                    finish()
                }

                inflateMenu(R.menu.menu_show_memo)
                setOnMenuItemClickListener {

                    when(it.itemId){

                        // 수정(메뉴 아이템) 클릭 시
                        R.id.menu_item_ShowMemo_Edit -> {

                            // EditMemoActivity 이동
                            val editMemoIntent = Intent(this@ShowMemoActivity, EditMemoActivity::class.java)
                            val position = intent.getIntExtra("position", 0)
                            editMemoIntent.putExtra("position", position)
                            editMemoActivityLauncher.launch(editMemoIntent)
                        }

                        // 삭제(메뉴 아이템) 클릭 시
                        R.id.menu_item_ShowMemo_Delete -> {

                            // 삭제 다이얼로그
                            val deleteDialog = MaterialAlertDialogBuilder(this@ShowMemoActivity).apply {

                                // 제목
                                setTitle("메모 삭제")

                                // 메시지
                                setMessage("메모를 삭제하시겠습니까?")

                                // 취소 버튼
                                setNegativeButton("취소", null)

                                // 삭제 버튼
                                setPositiveButton("삭제"){ dialogInterface: DialogInterface, i: Int ->

                                    val position = intent.getIntExtra("position", 0)

                                    // Memo 삭제
                                    Util.memoList.removeAt(position)

                                    setResult(RESULT_OK)

                                    // 현재 Activity 종료
                                    finish()
                                }
                            }

                            deleteDialog.show()
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

        activityShowMemoBinding.apply {

            TextFieldShowMemoTitle.setText(memo.title)
            TextFieldShowMemoDate.setText(memo.date)
            TextFieldShowMemoContent.setText(memo.content)
        }
    }

    override fun onResume() {
        super.onResume()

        // EditMemoActivity에서 수정했을 시
        if(isEdit == true){

            setView()

            // 메모 수정 Snackbar 알림
            Snackbar.make(activityShowMemoBinding.root, "메모가 수정되었습니다.", Snackbar.LENGTH_SHORT).show()
        }
    }
}