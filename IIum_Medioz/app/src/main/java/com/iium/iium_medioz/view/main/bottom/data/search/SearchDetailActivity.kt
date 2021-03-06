package com.iium.iium_medioz.view.main.bottom.data.search

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.iium.iium_medioz.R
import com.iium.iium_medioz.api.APIService
import com.iium.iium_medioz.api.ApiUtils
import com.iium.iium_medioz.databinding.ActivitySearchBinding
import com.iium.iium_medioz.databinding.ActivitySearchDetailBinding
import com.iium.iium_medioz.model.upload.DeleteModel
import com.iium.iium_medioz.util.`object`.Constant
import com.iium.iium_medioz.util.`object`.Constant.SEARCH_KEYWORD
import com.iium.iium_medioz.util.`object`.Constant.SEARCH_NORMAL
import com.iium.iium_medioz.util.`object`.Constant.SEARCH_TEXTIMG
import com.iium.iium_medioz.util.`object`.Constant.SEARCH_TIME_STAMP
import com.iium.iium_medioz.util.`object`.Constant.SEARCH_TITLE
import com.iium.iium_medioz.util.`object`.Constant.SEARCH_VIDEO
import com.iium.iium_medioz.util.base.BaseActivity
import com.iium.iium_medioz.util.base.MyApplication
import com.iium.iium_medioz.util.log.LLog
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchDetailActivity : BaseActivity() {

    private lateinit var mBinding : ActivitySearchDetailBinding
    private lateinit var apiServices: APIService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_search_detail)
        mBinding.activity = this
        apiServices = ApiUtils.apiService
        mBinding.lifecycleOwner = this
        inStatusBar()
        initView()
    }

    private fun inStatusBar() {
        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        window.statusBarColor = getColor(R.color.main_status )
    }

    private fun initView() {
        val title = intent.getStringExtra(SEARCH_TITLE)
        val keyword = intent.getStringExtra(SEARCH_KEYWORD)
        val timestamp = intent.getStringExtra(SEARCH_TIME_STAMP)
        val textList = intent.getStringExtra(SEARCH_TEXTIMG)
        val normalList = intent.getStringExtra(SEARCH_NORMAL)
        val videoList = intent.getStringExtra(SEARCH_VIDEO)

        val img =  textList?.substring(2)
        val imgtest = img?.substring(0, img.length - 2)
        val tnla = imgtest?.split(",")

        for(i in 0 until tnla?.size!! step(1)) {
            val str_idx = i.toString()
            Thread {
                try {
                    when (str_idx) {
                        "0" -> first(tnla[i].trim())
                        "1" -> second(tnla[i].trim())
                        "2" -> third(tnla[i].trim())
                        "3" -> four(tnla[i].trim())
                        "4" -> five(tnla[i].trim())
                        else -> Log.d(LLog.TAG, "??????")
                    }
                }
                catch (e: Exception) {
                    LLog.d(e.toString())
                }
            }.start()
        }

        val normal =  normalList?.substring(2)
        val normaltest = normal?.substring(0, normal.length - 2)
        val normalstart = normaltest?.split(",")

        for(i in 0 until normalstart?.size!! step(1)) {
            val str_idx = i.toString()
            Thread {
                try {
                    when (str_idx) {
                        "0" -> normal_first(normalstart[i].trim())
                        "1" -> normal_second(normalstart[i].trim())
                        "2" -> normal_third(normalstart[i].trim())
                        "3" -> normal_four(normalstart[i].trim())
                        "4" -> normal_five(normalstart[i].trim())
                        else -> Log.d(LLog.TAG, "??????")
                    }
                }
                catch (e: Exception) {
                    LLog.d(e.toString())
                }
            }.start()
        }

//        val video =  videoList?.substring(2)
//        val videotest = video?.substring(0, img.length - 2)
//        val videostart = videotest?.split(",")
//
//        for(i in 0 until videostart?.size!! step(1)) {
//            val str_idx = i.toString()
//            when (str_idx) {
//                "0" -> video_first(videostart[i].trim())
//                "1" -> video_second(videostart[i].trim())
//                "2" -> video_third(videostart[i].trim())
//                else -> Log.d(TAG,"??????")
//            }
//        }


        mBinding.tvMedicalDetailTitle.text = title.toString()
        mBinding.tvMedicalDetailData.text = timestamp.toString()
        mBinding.tvMyKeyword.text = keyword.toString()

    }

    //////////////////////????????? ????????? ?????? API////////////////////////////
    private fun first(textFirst: String) {
        LLog.e("????????? ????????? ????????? API")
        val vercall: Call<ResponseBody> = apiServices.getImg(textFirst, MyApplication.prefs.newaccesstoken)
        vercall.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    Log.d(LLog.TAG,"????????? ????????? response SUCCESS -> $result")
                    Thread {
                        try {
                            val imgs = result.byteStream()
                            val bit = BitmapFactory.decodeStream(imgs)
                            val bitimage = Bitmap.createScaledBitmap(bit, 210,210,true)
                            mBinding.image1.setImageBitmap(bitimage)
                        }
                        catch (e: Exception) {
                            Log.d(LLog.TAG,"????????? ????????? ????????? ?????? ?????? -> $e")
                        }
                    }.start()
                }
                else {
                    Log.d(LLog.TAG,"????????? ????????? response ERROR -> $result")
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d(LLog.TAG, "????????? ????????? Fail -> ${t.localizedMessage}")
            }
        })
    }

    private fun second(second: String) {
        LLog.e("????????? ????????? ????????? API")
        val vercall: Call<ResponseBody> = apiServices.getImg(second, MyApplication.prefs.newaccesstoken)
        vercall.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    Log.d(LLog.TAG,"????????? ????????? response SUCCESS -> $result")
                    Thread {
                        try {
                            val imgs = result.byteStream()
                            val bit = BitmapFactory.decodeStream(imgs)
                            val bitimage = Bitmap.createScaledBitmap(bit, 210,210,true)
                            mBinding.image2.setImageBitmap(bitimage)
                        }
                        catch (e: Exception) {
                            Log.d(LLog.TAG,"????????? ????????? ????????? ?????? ?????? -> $e")
                        }
                    }.start()
                }
                else {
                    Log.d(LLog.TAG,"????????? ????????? response ERROR -> $result")
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d(LLog.TAG, "????????? ????????? Fail -> ${t.localizedMessage}")
            }
        })
    }

    private fun third(third: String) {
        LLog.e("????????? ????????? ????????? API")
        val vercall: Call<ResponseBody> = apiServices.getImg(third, MyApplication.prefs.newaccesstoken)
        vercall.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    Log.d(LLog.TAG,"????????? ????????? response SUCCESS -> $result")
                    Thread {
                        try {
                            val imgs = result.byteStream()
                            val bit = BitmapFactory.decodeStream(imgs)
                            val bitimage = Bitmap.createScaledBitmap(bit, 210,210,true)
                            mBinding.image3.setImageBitmap(bitimage)
                        }
                        catch (e: Exception) {
                            Log.d(LLog.TAG,"????????? ????????? ????????? ?????? ?????? -> $e")
                        }
                    }.start()
                }
                else {
                    Log.d(LLog.TAG,"????????? ????????? response ERROR -> $result")
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d(LLog.TAG, "????????? ????????? Fail -> ${t.localizedMessage}")
            }
        })
    }

    private fun four(four: String) {
        LLog.e("????????? ????????? ????????? API")
        val vercall: Call<ResponseBody> = apiServices.getImg(four, MyApplication.prefs.newaccesstoken)
        vercall.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    Log.d(LLog.TAG,"????????? ????????? response SUCCESS -> $result")
                    Thread {
                        try {
                            val imgs = result.byteStream()
                            val bit = BitmapFactory.decodeStream(imgs)
                            val bitimage = Bitmap.createScaledBitmap(bit, 210,210,true)
                            mBinding.image4.setImageBitmap(bitimage)
                        }
                        catch (e: Exception) {
                            Log.d(LLog.TAG,"????????? ????????? ????????? ?????? ?????? -> $e")
                        }
                    }.start()
                }
                else {
                    Log.d(LLog.TAG,"????????? ????????? response ERROR -> $result")
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d(LLog.TAG, "????????? ????????? Fail -> ${t.localizedMessage}")
            }
        })
    }

    private fun five(five: String) {
        LLog.e("????????? ???????????? ????????? API")
        val vercall: Call<ResponseBody> = apiServices.getImg(five, MyApplication.prefs.newaccesstoken)
        vercall.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    Log.d(LLog.TAG,"????????? ???????????? response SUCCESS -> $result")
                    Thread {
                        try {
                            val imgs = result.byteStream()
                            val bit = BitmapFactory.decodeStream(imgs)
                            val bitimage = Bitmap.createScaledBitmap(bit, 210,210,true)
                            mBinding.image5.setImageBitmap(bitimage)
                        }
                        catch (e: Exception) {
                            Log.d(LLog.TAG,"????????? ???????????? ????????? ?????? ?????? -> $e")
                        }
                    }.start()
                }
                else {
                    Log.d(LLog.TAG,"????????? ???????????? response ERROR -> $result")
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d(LLog.TAG, "????????? ???????????? Fail -> ${t.localizedMessage}")
            }
        })
    }


    //////////////////////?????? ????????? ?????? API////////////////////////////
    private fun normal_first(nm_first: String) {
        LLog.e("?????? ????????? ????????? API")
        val vercall: Call<ResponseBody> = apiServices.getImg(nm_first, MyApplication.prefs.newaccesstoken)
        vercall.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    Log.d(LLog.TAG,"?????? ????????? response SUCCESS -> $result")
                    Thread {
                        try {
                            val imgs = result.byteStream()
                            val bit = BitmapFactory.decodeStream(imgs)
                            val bitimage = Bitmap.createScaledBitmap(bit, 210,210,true)
                            mBinding.nImage1.setImageBitmap(bitimage)
                        }
                        catch (e: Exception) {
                            Log.d(LLog.TAG,"?????? ????????? ????????? ?????? ?????? -> $e")
                        }
                    }.start()
                }
                else {
                    Log.d(LLog.TAG,"?????? ????????? response ERROR -> $result")
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d(LLog.TAG, "?????? ????????? Fail -> ${t.localizedMessage}")
            }
        })
    }

    private fun normal_second(nm_second: String) {
        LLog.e("?????? ????????? ????????? API")
        val vercall: Call<ResponseBody> = apiServices.getImg(nm_second, MyApplication.prefs.newaccesstoken)
        vercall.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    Log.d(LLog.TAG,"?????? ????????? response SUCCESS -> $result")
                    Thread {
                        try {
                            val imgs = result.byteStream()
                            val bit = BitmapFactory.decodeStream(imgs)
                            val bitimage = Bitmap.createScaledBitmap(bit, 210,210,true)
                            mBinding.nImage2.setImageBitmap(bitimage)
                        }
                        catch (e: Exception) {
                            Log.d(LLog.TAG,"?????? ????????? ????????? ?????? ?????? -> $e")
                        }
                    }.start()
                }
                else {
                    Log.d(LLog.TAG,"?????? ????????? response ERROR -> $result")
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d(LLog.TAG, "?????? ????????? Fail -> ${t.localizedMessage}")
            }
        })
    }

    private fun normal_third(nm_third: String) {
        LLog.e("?????? ????????? ????????? API")
        val vercall: Call<ResponseBody> = apiServices.getImg(nm_third, MyApplication.prefs.newaccesstoken)
        vercall.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    Log.d(LLog.TAG,"?????? ????????? response SUCCESS -> $result")
                    Thread {
                        try {
                            val imgs = result.byteStream()
                            val bit = BitmapFactory.decodeStream(imgs)
                            val bitimage = Bitmap.createScaledBitmap(bit, 210,210,true)
                            mBinding.nImage3.setImageBitmap(bitimage)
                        }
                        catch (e: Exception) {
                            Log.d(LLog.TAG,"?????? ????????? ????????? ?????? ?????? -> $e")
                        }
                    }.start()
                }
                else {
                    Log.d(LLog.TAG,"?????? ????????? response ERROR -> $result")
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d(LLog.TAG, "?????? ????????? Fail -> ${t.localizedMessage}")
            }
        })
    }

    private fun normal_four(nm_four: String) {
        LLog.e("?????? ????????? ????????? API")
        val vercall: Call<ResponseBody> = apiServices.getImg(nm_four, MyApplication.prefs.newaccesstoken)
        vercall.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    Log.d(LLog.TAG,"?????? ????????? response SUCCESS -> $result")
                    Thread {
                        try {
                            val imgs = result.byteStream()
                            val bit = BitmapFactory.decodeStream(imgs)
                            val bitimage = Bitmap.createScaledBitmap(bit, 210,210,true)
                            mBinding.nImage4.setImageBitmap(bitimage)
                        }
                        catch (e: Exception) {
                            Log.d(LLog.TAG,"?????? ????????? ????????? ?????? ?????? -> $e")
                        }
                    }.start()
                }
                else {
                    Log.d(LLog.TAG,"?????? ????????? response ERROR -> $result")
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d(LLog.TAG, "?????? ????????? Fail -> ${t.localizedMessage}")
            }
        })
    }

    private fun normal_five(nm_five: String) {
        LLog.e("?????? ???????????? ????????? API")
        val vercall: Call<ResponseBody> = apiServices.getImg(nm_five, MyApplication.prefs.newaccesstoken)
        vercall.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    Log.d(LLog.TAG,"?????? ???????????? response SUCCESS -> $result")
                    Thread {
                        try {
                            val imgs = result.byteStream()
                            val bit = BitmapFactory.decodeStream(imgs)
                            val bitimage = Bitmap.createScaledBitmap(bit, 210,210,true)
                            mBinding.nImage5.setImageBitmap(bitimage)
                        }
                        catch (e: Exception) {
                            Log.d(LLog.TAG,"?????? ???????????? ????????? ?????? ?????? -> $e")
                        }
                    }.start()
                }
                else {
                    Log.d(LLog.TAG,"?????? ????????????  response ERROR -> $result")
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d(LLog.TAG, "?????? ???????????? Fail -> ${t.localizedMessage}")

            }
        })
    }


    //////////////////////?????? ?????? API////////////////////////////
    private fun video_first(s: String) {

    }

    private fun video_second(s: String) {

    }

    private fun video_third(s: String) {

    }

    fun onPutClick(v: View) {

    }

    ////////////////????????? ?????? API/////////////////////
    fun onDeleteClick(v: View) {
        val id = intent.getStringExtra(Constant.DATA_ID)
        LLog.e("????????? ?????? API")
        val vercall: Call<DeleteModel> = apiServices.getDataDelete(MyApplication.prefs.newaccesstoken,id)
        vercall.enqueue(object : Callback<DeleteModel> {
            override fun onResponse(call: Call<DeleteModel>, response: Response<DeleteModel>) {
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    Log.d(LLog.TAG,"????????? ?????? response SUCCESS -> $result")
                    moveMain()
                }
                else {
                    Log.d(LLog.TAG,"????????? ??????  response ERROR -> $id")
                    serverDialog()
                }
            }
            override fun onFailure(call: Call<DeleteModel>, t: Throwable) {
                Log.d(LLog.TAG, "????????? ?????? Fail -> ${t.localizedMessage}")
                serverDialog()
            }
        })
    }


    fun onBackPressed(v: View) {
        moveSearch()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        moveSearch()
    }
}