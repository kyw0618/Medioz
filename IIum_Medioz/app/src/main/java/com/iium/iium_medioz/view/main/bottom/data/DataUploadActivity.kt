package com.iium.iium_medioz.view.main.bottom.data

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.KeyEvent.KEYCODE_ENTER
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.iium.iium_medioz.R
import com.iium.iium_medioz.api.APIService
import com.iium.iium_medioz.api.ApiUtils
import com.iium.iium_medioz.databinding.ActivityDataDetyailBinding
import com.iium.iium_medioz.databinding.ActivityDataUploadBinding
import com.iium.iium_medioz.model.upload.CreateMedical
import com.iium.iium_medioz.model.upload.NormalModel
import com.iium.iium_medioz.model.upload.VideoModel
import com.iium.iium_medioz.util.`object`.Constant.DEFAULT_CODE_TRUE
import com.iium.iium_medioz.util.`object`.Constant.ONE_PERMISSION_REQUEST_CODE
import com.iium.iium_medioz.util.`object`.Constant.SECOND_PERMISSION_REQUEST_CODE
import com.iium.iium_medioz.util.`object`.Constant.SEND_CODE_FALSE
import com.iium.iium_medioz.util.`object`.Constant.THRID_PERMISSION_REQUEST_CODE
import com.iium.iium_medioz.util.adapter.upload.MultiImageAdapter
import com.iium.iium_medioz.util.adapter.upload.NormalImgAdapter
import com.iium.iium_medioz.util.adapter.upload.VideoRecyclerAdapter
import com.iium.iium_medioz.util.base.BaseActivity
import com.iium.iium_medioz.util.base.MyApplication.Companion.prefs
import com.iium.iium_medioz.util.file.FileUtil
import com.iium.iium_medioz.util.log.LLog
import com.iium.iium_medioz.util.log.LLog.TAG
import com.iium.iium_medioz.view.main.MainActivity
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okio.utf8Size
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.lang.NullPointerException
import java.time.LocalDate
import java.util.HashMap
import kotlin.concurrent.thread

class DataUploadActivity : BaseActivity() {

    private lateinit var mBinding : ActivityDataUploadBinding
    private lateinit var apiServices: APIService

    private var files4 :MutableList<Uri> = ArrayList()      // ????????? ?????????
    private var files5 :MutableList<Uri> = ArrayList()      // ?????? ?????????
    private var files6 :MutableList<Uri> = ArrayList()      // ?????????

    private val textAdapter = MultiImageAdapter(files4, this)
    private val normalAdapter = NormalImgAdapter(files5, this)
    private val videoAdapter = VideoRecyclerAdapter(files6, this)

    private val fileUtil = FileUtil()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_data_upload)
        mBinding.activity = this
        apiServices = ApiUtils.apiService
        mBinding.lifecycleOwner = this
        inStatusBar()
        initView()
        initSecond()
        initThird()
    }

    private fun inStatusBar() {
        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        window.statusBarColor = getColor(R.color.main_status)
    }

    private fun initView() {
        val onlyDate: LocalDate = LocalDate.now()
        mBinding.tvTodayData.text = onlyDate.toString()

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mBinding.textRe.layoutManager = layoutManager
        mBinding.textRe.adapter = textAdapter
        mBinding.textRe.setHasFixedSize(true)
        mBinding.textRe.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL))

        mBinding.etKeyword.addTextChangedListener(object : TextWatcher{

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (mBinding.etKeyword.text.toString() == "") {
                    mBinding.btnKeyword.isEnabled = false
                    mBinding.btnKeyword.setBackgroundColor(R.drawable.color_common_btn)
                    Toast.makeText(this@DataUploadActivity,"???????????? ??????????????????!",Toast.LENGTH_SHORT).show()
                } else if (mBinding.etKeyword.text.toString() != "") {
                        mBinding.btnKeyword.isEnabled = true
                        mBinding.btnKeyword.setBackgroundColor(R.drawable.color_common_btn)
                        mBinding.btnKeyword.setOnClickListener {
                            val textView = TextView(this@DataUploadActivity)
                            textView.text = mBinding.etKeyword.text.toString()
                            mBinding.clAutoTextview.addView(textView)
                        }
                    }
                }
        })
    }

    private fun initSecond() {
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mBinding.normalRe.layoutManager = layoutManager
        mBinding.normalRe.adapter = normalAdapter
        mBinding.normalRe.setHasFixedSize(true)
        mBinding.normalRe.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL))
    }

    private fun initThird() {
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mBinding.videoRe.layoutManager = layoutManager
        mBinding.videoRe.adapter = videoAdapter
        mBinding.videoRe.setHasFixedSize(true)
        mBinding.videoRe.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL))
    }

    fun onBackPressed(v: View) {
        moveMain()
    }

    fun onTextClick(v: View?) {
        getTextPerMission()
    }

    fun onNormalClick(v: View?) {
        getImgPerMission()
    }

    fun onVideoClick(v: View?) {
        geVideoPerMission()
    }

    /////////////////// ?????? ?????? ?????? ///////////////////
    private fun getTextPerMission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
            -> {
                getAlbum()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)
            -> {
                Toast.makeText(this,"?????? ?????? ?????? ????????? ????????????", Toast.LENGTH_SHORT).show()
            }

            else -> requestPermissions(
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),ONE_PERMISSION_REQUEST_CODE
            )
        }
    }


    private fun getImgPerMission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
            -> {
                getNormal()
            }

            shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)
            -> {
                Toast.makeText(this,"?????? ?????? ?????? ????????? ????????????", Toast.LENGTH_SHORT).show()
            }

            else -> requestPermissions(
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),SECOND_PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun geVideoPerMission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
            -> {
                getVideo()
            }

            shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)
            -> {
                Toast.makeText(this,"?????? ?????? ?????? ????????? ????????????", Toast.LENGTH_SHORT).show()
            }

            else -> requestPermissions(
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),THRID_PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            ONE_PERMISSION_REQUEST_CODE -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getAlbum()
            }
            SECOND_PERMISSION_REQUEST_CODE -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getNormal()
            }
            THRID_PERMISSION_REQUEST_CODE -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getVideo()
            }
            else {
                Toast.makeText(this, "???????????? ????????? ????????????.", Toast.LENGTH_SHORT).show()
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }

    }

    private fun getAlbum() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        startActivityForResult(intent, 200)
    }

    private fun getNormal() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        startActivityForResult(intent, 300)
    }

    private fun getVideo() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "video/*"
        startActivityForResult(intent, 400)
    }

    /////////////////// ?????? ?????? ????????????(limit : 5???) ///////////////////
    @SuppressLint("NotifyDataSetChanged")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK && requestCode == 200 ) {
            if(data?.clipData != null) {
                val count = data.clipData!!.itemCount
                if(count > 5) {
                    Toast.makeText(this,"????????? 5????????? ?????? ???????????????.",Toast.LENGTH_SHORT).show()
                    return
                }
                try {
                    for( i in 0 until count) {
                        val imageUri = data.clipData!!.getItemAt(i).uri
                        val imgPath = imageUri.let {
                            fileUtil.getPath(this, it!!)
                        }
                        files4.add(Uri.parse(imgPath))
                        Log.d(TAG,"upload TextIMG -> $imgPath")
                    }
                }
                catch (e : NullPointerException) {
                    e.printStackTrace()
                }
            }
            else {
                val img = data?.data
                img.let {
                    val imageUri : Uri? = img
                    fileUtil.getPath(this, it!!)
                    if(imageUri != null) {
                        files4.add(Uri.parse(img.toString()))
                        Log.d(TAG,"upload TextIMG One -> $img")
                    }
                }
            }
            textAdapter.notifyDataSetChanged()
        }

        if(resultCode == RESULT_OK && requestCode == 300 ) {
            if(data?.clipData != null) {
                val count = data.clipData!!.itemCount
                if(count > 5) {
                    Toast.makeText(this,"????????? 5????????? ?????? ???????????????.",Toast.LENGTH_SHORT).show()
                    return
                }
                try {
                    for( i in 0 until count) {
                        val imageUri = data.clipData!!.getItemAt(i).uri
                        val imgPath = imageUri.let {
                            fileUtil.getPath(this, it!!)
                        }
                        files5.add(Uri.parse(imgPath))
                        Log.d(TAG,"upload IMG -> $imgPath")
                    }
                }
                catch (e : NullPointerException) {
                    e.printStackTrace()
                }
            }
            else {
                val img = data?.data
                img.let {
                    val imageUri : Uri? = img
                    fileUtil.getPath(this, it!!)
                    if(imageUri != null) {
                        files5.add(Uri.parse(img.toString()))
                        Log.d(TAG,"upload IMG One -> $img")
                    }
                }
            }
            normalAdapter.notifyDataSetChanged()
        }

        if(resultCode == RESULT_OK && requestCode == 400 ) {
            if(data?.clipData != null) {
                val count = data.clipData!!.itemCount
                if(count > 3) {
                    Toast.makeText(this,"????????? 3????????? ?????? ???????????????.",Toast.LENGTH_SHORT).show()
                    return
                }
                try {
                    for( i in 0 until count) {
                        val videoUri = data.clipData!!.getItemAt(i).uri
                        val videoPath = videoUri.let {
                            fileUtil.getPath(this, it!!)
                        }
                        files6.add(Uri.parse(videoPath))
                        Log.d(TAG,"upload video -> $videoPath")
                    }
                }
                catch (e : NullPointerException) {
                    e.printStackTrace()
                }
            }
            else {
                val video = data?.data
                video.let {
                    val videoUri : Uri? = video
                    fileUtil.getPath(this, it!!)
                    if(videoUri != null) {
                        files6.add(Uri.parse(video.toString()))
                        Log.d(TAG,"upload video One -> $video")
                    }
                }
            }
            videoAdapter.notifyDataSetChanged()
        }
    }

    fun onDataSendClick(v: View?) {
        val et_title = mBinding.etTitle.text.toString()
        val et_keyword = mBinding.etKeyword.text.toString()
        when {
            et_title.isEmpty() -> {
                mBinding.etTitle.error = "?????????"
                Toast.makeText(this,"????????? ????????? ?????????",Toast.LENGTH_SHORT).show().toString()
            }

            et_keyword.isEmpty() -> {
                mBinding.etKeyword.error = "?????????"
                Toast.makeText(this,"???????????? ????????? ?????????",Toast.LENGTH_SHORT).show().toString()

            }
            else -> {
                callCreateAPI()
            }
        }
    }

    /////////////////// API ?????? ///////////////////
    private fun callCreateAPI() {
        val textimg : MutableList<MultipartBody.Part?> = ArrayList()
        for (uri:Uri in files4) {
            uri.path?.let {
                Log.i("textImg", it)
            }
            textimg.add(prepareFilePart("textImg", uri))
            Log.e("textImg", uri.toString())
        }

        for (imguri:Uri in files5) {
            imguri.path?.let {
                Log.i("Img", it)
            }
            textimg.add(prepareFilePart("Img", imguri))
            Log.e("Img", imguri.toString())
        }

        for (videouri:Uri in files6) {
            videouri.path?.let {
                Log.i("video", it)
            }
            textimg.add(prepareFilePart("video", videouri))
            Log.e("video", videouri.toString())
        }

        val title = mBinding.etTitle.text.toString()
        val keyword =mBinding.etKeyword.text.toString()
        val timestamp = mBinding.tvTodayData.text.toString()
        val requestHashMap : HashMap<String, RequestBody> = HashMap()
        val sendcode = SEND_CODE_FALSE
        val defaultcode = DEFAULT_CODE_TRUE
        val sensitivity = ""

        requestHashMap["title"] = title.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        requestHashMap["keyword"] = keyword.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        requestHashMap["timestamp"] = timestamp.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        requestHashMap["sendcode"] = sendcode.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        requestHashMap["defaultcode"] = defaultcode.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        requestHashMap["sensitivity"] = sensitivity.toRequestBody("multipart/form-data".toMediaTypeOrNull())

        LLog.d("????????? ?????????_????????? API")
        apiServices.getCreate(prefs.myaccesstoken,textimg,requestHashMap).enqueue(object :
            Callback<CreateMedical> {
            override fun onResponse(call: Call<CreateMedical>, response: Response<CreateMedical>) {
                val result = response.body()
                if(response.isSuccessful&& result!= null) {
                    Log.d(TAG,"getCreate API SUCCESS -> $result")
//                    Thread{
//                        try {
//                            normalAPI()
//                        }
//                        catch (e: Exception) {
//                            LLog.e(e.toString())
//                        }
//                    }.start()
                }
                else {
                    Log.d(TAG,"getCreate API ERROR -> ${response.errorBody()}")
                    otherAPI()
                }

            }

            override fun onFailure(call: Call<CreateMedical>, t: Throwable) {
                Log.d(TAG,"getCreate Fail -> $t")
                serverDialog()
            }
        })

    }

    private fun otherAPI() {
        val textimg : MutableList<MultipartBody.Part?> = ArrayList()
        for (uri:Uri in files4) {
            uri.path?.let {
                Log.i("textImg", it)
                Log.d(TAG,"?????? ????????? ????????? : ${it.length}")
            }
            textimg.add(prepareFilePart("textImg", uri))
            Log.e("textImg", uri.toString())
        }

        for (imguri:Uri in files5) {
            imguri.path?.let {
                Log.i("Img", it)
            }
            textimg.add(prepareFilePart("Img", imguri))
            Log.e("Img", imguri.toString())
        }

        for (videouri:Uri in files6) {
            videouri.path?.let {
                Log.i("video", it)
            }
            textimg.add(prepareFilePart("video", videouri))
            Log.e("video", videouri.toString())
        }

        val title = mBinding.etTitle.text.toString()
        val keyword =mBinding.etKeyword.text.toString()
        val timestamp = mBinding.tvTodayData.text.toString()
        val requestHashMap : HashMap<String, RequestBody> = HashMap()
        val sendcode = SEND_CODE_FALSE
        val defaultcode = DEFAULT_CODE_TRUE
        val sensitivity = ""

        requestHashMap["title"] = title.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        requestHashMap["keyword"] = keyword.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        requestHashMap["timestamp"] = timestamp.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        requestHashMap["sendcode"] = sendcode.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        requestHashMap["defaultcode"] = defaultcode.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        requestHashMap["sensitivity"] = sensitivity.toRequestBody("multipart/form-data".toMediaTypeOrNull())

        LLog.d("????????? ?????????_????????? API")
        apiServices.getCreate(prefs.newaccesstoken,textimg, requestHashMap).enqueue(object :
            Callback<CreateMedical> {
            override fun onResponse(call: Call<CreateMedical>, response: Response<CreateMedical>) {
                val result = response.body()
                if(response.isSuccessful&& result!= null) {
                    Log.d(TAG,"getCreate Second API SUCCESS -> $result")
                    Thread{
                        try {
                            moveSave()
                        }
                        catch (e: Exception) {
                            LLog.e(e.toString())
                        }
                    }.start()
                }
                else {
                    Log.d(TAG,"getCreate Second API ERROR -> ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<CreateMedical>, t: Throwable) {
                Log.d(TAG,"getCreate Second Fail -> $t")
                serverDialog()
            }
        })
    }

    private fun prepareFilePart(partName: String, fileUri: Uri): MultipartBody.Part {
        val file = File(fileUri.path!!)
        Log.i("here is error", file.absolutePath.toString())
        val requestFile: RequestBody = file
            .asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(partName, file.name, requestFile)
    }

    private fun videoFilePart(partName: String, fileUri: Uri): MultipartBody.Part {
        val file = File(fileUri.path!!)
        Log.i("here is error", file.absolutePath.toString())
        val requestFile: RequestBody = file
            .asRequestBody("video/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(partName, file.name, requestFile)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        textAdapter.notifyDataSetChanged()
        normalAdapter.notifyDataSetChanged()
        videoAdapter.notifyDataSetChanged()
    }

    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}