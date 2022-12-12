package com.example.sportscommunity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.example.sportscommunity.Adapter.WriteShopAdapter
import com.example.sportscommunity.databinding.WriteShopFragmentBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class WriteShopFragment : Fragment() {

    private var mBinding: WriteShopFragmentBinding? = null
    private val binding get() = mBinding!!
    lateinit var callback: OnBackPressedCallback
    private var shopImage = mutableListOf<Uri>()
    private var imageText = mutableListOf<String>()
    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>
    private var flag = 0
    private var area = ""
    private var categoryType = 0
    private var title = ""
    private var content = ""
    private var nickname = ""
    private var writeTime = ""
    private var price = ""
    private var image = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = WriteShopFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mainActivity = activity as MainActivity
        mainActivity.hideBottomNavigationView(true)
        val writeShopAdapter =
            WriteShopAdapter(requireContext().applicationContext, shopImage, imageText)
        val imageUri = "drawable://" + R.drawable.add_image_background

        shopImage.add(Uri.parse(imageUri))
        shopImage.add(Uri.parse(imageUri))
        shopImage.add(Uri.parse(imageUri))
        shopImage.add(Uri.parse(imageUri))
        shopImage.add(Uri.parse(imageUri))

        imageText.add("대표사진")
        imageText.add("두번째 사진")
        imageText.add("세번째 사진")
        imageText.add("네번째 사진")
        imageText.add("다섯번째 사진")

        binding.productListview.adapter = writeShopAdapter
        binding.productListview.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        writeShopAdapter.setItemClickListener(object : WriteShopAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                flag = position
                imagePickerLauncher.launch(
                    Intent(Intent.ACTION_PICK).apply {
                        this.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        this.action = Intent.ACTION_GET_CONTENT
                    }
                )
            }
        })

        imagePickerLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                it.data?.data?.let { uri ->
                    val imageUri: Uri? = it.data?.data

                    image = uri.toString()

                    if (imageUri != null) {

                        shopImage[flag] = imageUri
                        binding.productListview.adapter = writeShopAdapter
                        writeShopAdapter.notifyDataSetChanged()
                    }
                }
            }

        val categoryAdapter =
            ArrayAdapter.createFromResource(
                requireContext(),
                R.array.sportsList,
                R.layout.spinner_dropdown_item
            )
        val areaAdapter =
            ArrayAdapter.createFromResource(
                requireContext(),
                R.array.areaList,
                R.layout.spinner_dropdown_item
            )

        categoryAdapter.setDropDownViewResource(R.layout.spinner_item_style)
        areaAdapter.setDropDownViewResource(R.layout.spinner_item_style)

        binding.run {
            categorySpinner.adapter = categoryAdapter
            areaSpinner.adapter = areaAdapter

            areaSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    when (position) {
                        0 -> detailAreaSpinner(R.array.noSelect)
                        1 -> detailAreaSpinner(R.array.seoulItem)
                        2 -> detailAreaSpinner(R.array.gyungiList)
                        3 -> detailAreaSpinner(R.array.incheonList)
                        4 -> detailAreaSpinner(R.array.daeJeonList)
                        5 -> detailAreaSpinner(R.array.daeGuList)
                        6 -> detailAreaSpinner(R.array.ulsanList)
                        7 -> detailAreaSpinner(R.array.jeonnamList)
                        8 -> detailAreaSpinner(R.array.jeonbukList)
                        9 -> detailAreaSpinner(R.array.chungnamList)
                        10 -> detailAreaSpinner(R.array.chungbukList)
                        11 -> detailAreaSpinner(R.array.jejuList)
                        12 -> detailAreaSpinner(R.array.sejongList)
                        13 -> detailAreaSpinner(R.array.gwangjuList)
                        14 -> detailAreaSpinner(R.array.busanList)
                        15 -> detailAreaSpinner(R.array.gyungbukList)
                        16 -> detailAreaSpinner(R.array.gyungnamList)
                        17 -> detailAreaSpinner(R.array.gangwonList)
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }
            saveBtn.setOnClickListener {
                checkBlank()
                shopRetrofit()
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val builder = AlertDialog.Builder(requireContext()).setTitle("맺음")
                    .setMessage("작성을 취소하시겠습니까?\n확인시 작성중이던 글은 삭제됩니다.")
                    .setPositiveButton("확인") { dialog, which ->

                        requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container_view, SportsShopFragment())
                            .commit()
                    }.setNegativeButton("취소") { dialog, which ->
                        dialog.dismiss()
                    }
                builder.show()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }

    private fun detailAreaSpinner(item: Int) {
        binding.run {
            val adapter = ArrayAdapter.createFromResource(
                requireContext(),
                item,
                R.layout.spinner_dropdown_item
            )
            adapter.setDropDownViewResource(R.layout.spinner_item_style)
            areaSpinnerTwo.adapter = adapter
        }
    }

    private fun checkBlank() {
        binding.run {
            if (categorySpinner.selectedItem.toString() == "선택") {
                Toast.makeText(requireContext(), "카테고리를 입력해주세요", Toast.LENGTH_SHORT)
                    .show()
            } else if (areaSpinner.selectedItem.toString() == "시/도") {
                Toast.makeText(requireContext(), "지역을 입력해주세요", Toast.LENGTH_SHORT).show()
            } else if (contentTitleEdit.text.isNullOrBlank()) {
                Toast.makeText(requireContext(), "제목을 입력해주세요", Toast.LENGTH_SHORT).show()
            } else if (contentTextEdit.text.isNullOrBlank()) {
                Toast.makeText(requireContext(), "내용을 입력해주세요", Toast.LENGTH_SHORT)
                    .show()
            } else if (priceEdit.text.isNullOrBlank()) {
                Toast.makeText(requireContext(), "가격을 입력해주세요", Toast.LENGTH_SHORT).show()
            } else {
                Log.d("saveBtnClick", "success")
            }
        }
    }

    private fun shopRetrofit() {

        val sp = requireActivity().getSharedPreferences("userId", Context.MODE_PRIVATE)
        val id: Int = sp.getInt("id", 0)

        when (binding.categorySpinner.selectedItem) {
            "구기종목" -> categoryType = 1
            "레저" -> categoryType = 2
            "해양 스포츠" -> categoryType = 3
            "생활 스포츠" -> categoryType = 4
            "동계 스포츠" -> categoryType = 5
            "E-스포츠" -> categoryType = 6
        }

        area = if (binding.areaCheckBox.isChecked) {
            "상관없음"
        } else {
            binding.areaSpinner.selectedItem.toString() +
                    binding.areaSpinnerTwo.selectedItem.toString()
        }
        title = binding.contentTitleEdit.text.toString()
        content = binding.contentTextEdit.text.toString()
        price = binding.priceEdit.text.toString()

        val currentDate = LocalDateTime.now()
        val formatter = DateTimeFormatter.ISO_DATE
        val formatted = currentDate.format(formatter)
        Log.d("currentDate", formatted.toString())

        val formatterTwo = DateTimeFormatter.ofPattern("HH:mm:ss")
        val formattedTime = currentDate.format(formatterTwo)
        Log.d("currentTime", formattedTime.toString())

        writeTime = "$formatted $formattedTime"
        Log.d("currentDateTime", writeTime)

        nickname = sp.getString("nickname","none").toString()

        val writing = HashMap<String, Any>()
        writing["id"] = categoryType
        writing["local"] = area
        writing["title"] = title
        writing["description"] = content
        writing["nickname"] = nickname
        writing["writedate"] = writeTime
        writing["usedimage"] = image
        writing["price"] = price
        writing["userid"] = 3

        val retrofitService = Retrofits.postShop()
        val call: Call<WriteShop> = retrofitService.postContent(writing)

        call.enqueue(object : Callback<WriteShop> {
            override fun onResponse(
                call: Call<WriteShop>,
                response: Response<WriteShop>
            ) {
                try {
                    if (response.isSuccessful) {
                        Log.e("userInfoPost", "success")
                        Log.d("성공:", "${response.body()}")
                    }
                } catch (e: Exception) {
                    Log.e("userInfoPost", response.body().toString())
                    Log.e("userInfoPost", response.message().toString())
                }
            }

            override fun onFailure(call: Call<WriteShop>, t: Throwable) {
                Log.e("userInfoPost", t.message.toString())
            }
        })
    }
}