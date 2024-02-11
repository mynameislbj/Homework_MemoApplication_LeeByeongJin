package kr.co.lion.homework_memoapplication_leebyeongjin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kr.co.lion.homework_memoapplication_leebyeongjin.databinding.ActivityMainBinding
import kr.co.lion.homework_memoapplication_leebyeongjin.databinding.RowmainmemoBinding

// Memo App

// MainActivity

// 상단 툴바의 + 를 누르면 InputMemoActivity의 화면 이동
// RecyclerView의 항목은 메모의 제목과 작성 날짜를 보여준다.
// RecyclerView의 항목을 누르면 ShowMemoActivity의 화면 이동

class MainActivity : AppCompatActivity() {

    lateinit var activityMainBinding: ActivityMainBinding

    // InputMemoActivity 런처
    lateinit var inputMemoActivityLauncher : ActivityResultLauncher<Intent>

    // ShowMemoActivity 런처
    lateinit var showMemoActivityLauncher : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        // Launcher 설정
        setLauncher()

        // Toolbar 설정
        setToolbar()

        // View 설정
        setView()
    }

    // Launcher 설정
    fun setLauncher(){

        // InputMemoActivity 런처
        val contract_InputMemo = ActivityResultContracts.StartActivityForResult()
        inputMemoActivityLauncher = registerForActivityResult(contract_InputMemo){

            when(it.resultCode){

                // InputMemoActivity에서 Memo를 등록했을 시
                RESULT_OK -> {

                    // 메모 추가 Snackbar 알림
                    Snackbar.make(activityMainBinding.root, "메모가 등록되었습니다.", Snackbar.LENGTH_SHORT).show()
                }

                // 뒤로가기 했을 시(메모 등록 X)
                RESULT_CANCELED -> {}
            }
        }

        // ShowMemoActivity 런처
        val contract_ShowMemo = ActivityResultContracts.StartActivityForResult()
        showMemoActivityLauncher = registerForActivityResult(contract_ShowMemo){

            when(it.resultCode){

                // ShowMemoActivity에서 Memo를 삭제했을 시
                RESULT_OK -> {

                    // 메모 삭제 Snackbar 알림
                    Snackbar.make(activityMainBinding.root, "메모가 삭제되었습니다.", Snackbar.LENGTH_SHORT).show()
                }

                // 뒤로가기 했을 시
                RESULT_CANCELED -> {}
            }
        }
    }

    // Toolbar 설정
    fun setToolbar(){

        activityMainBinding.apply {

            toolbarMain.apply {

                // 제목
                title = "메모 관리"

                inflateMenu(R.menu.menu_main)
                setOnMenuItemClickListener {

                    when(it.itemId){

                        // 메모 작성(메뉴 아이템) 클릭 시
                        R.id.menu_item_Main_EditNote -> {

                            // InputMemoActivity 이동
                            val inputMemoIntent = Intent(this@MainActivity, InputMemoActivity::class.java)
                            inputMemoActivityLauncher.launch(inputMemoIntent)
                        }
                    }
                    true
                }
            }
        }
    }

    // View 설정
    fun setView(){

        activityMainBinding.apply {

            recyclerViewMain.apply {

                adapter = RecyclerViewAdapter()

                layoutManager = LinearLayoutManager(this@MainActivity)
            }
        }
    }

    // RecyclerViewAdapter
    inner class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolderClass>(){

        inner class ViewHolderClass(rowMainMemoBinding: RowmainmemoBinding) : RecyclerView.ViewHolder(rowMainMemoBinding.root){

            var rowMainMemoBinding : RowmainmemoBinding

            init {

                this.rowMainMemoBinding = rowMainMemoBinding

                // 범위
                this.rowMainMemoBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                )
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {

            val rowMainMemoBinding = RowmainmemoBinding.inflate(layoutInflater)
            val viewHolderClass = ViewHolderClass(rowMainMemoBinding)

            return viewHolderClass
        }

        override fun getItemCount(): Int {
            return Util.memoList.size
        }

        override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {

            val memo = Util.memoList[position]

            holder.rowMainMemoBinding.apply {

                textViewMemoTitle.text = memo.title
                textViewMemoTime.text = memo.date

                // Item 클릭 시
                CardViewMain.setOnClickListener {

                    // ShowMemoActivity 실행
                    val showMemoIntent = Intent(this@MainActivity, ShowMemoActivity::class.java)
                    showMemoIntent.putExtra("position", position)
                    showMemoActivityLauncher.launch(showMemoIntent)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        // recyclerView 갱신
        activityMainBinding.recyclerViewMain.adapter?.notifyDataSetChanged()
    }
}