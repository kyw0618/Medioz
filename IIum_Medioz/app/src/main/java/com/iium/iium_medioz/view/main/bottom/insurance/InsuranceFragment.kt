package com.iium.iium_medioz.view.main.bottom.insurance

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.iium.iium_medioz.R
import com.iium.iium_medioz.api.APIService
import com.iium.iium_medioz.api.ApiUtils
import com.iium.iium_medioz.databinding.FragmentInsuranceBinding
import com.iium.iium_medioz.model.document.DocumentList
import com.iium.iium_medioz.model.document.DocumentListModel
import com.iium.iium_medioz.model.recycler.MedicalData
import com.iium.iium_medioz.util.adapter.document.DocumentAdapter
import com.iium.iium_medioz.util.base.MyApplication
import com.iium.iium_medioz.util.base.MyApplication.Companion.prefs
import com.iium.iium_medioz.util.log.LLog
import com.iium.iium_medioz.view.main.bottom.data.search.SearchActivity
import com.iium.iium_medioz.view.main.bottom.insurance.affiliated.HospitalActivity
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InsuranceFragment : Fragment() {

    private lateinit var mBinding : FragmentInsuranceBinding
    private lateinit var apiServices: APIService
    private var readapter: DocumentAdapter?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_insurance, container, false)
        apiServices = ApiUtils.apiService
        mBinding.fragment = this
        initView()
        return mBinding.root
    }

    fun onTestClick(v: View) {
        val intent = Intent(activity, HospitalActivity::class.java)
        startActivity(intent)
    }

    private fun initView() {
        LLog.e("???????????? ?????? API")
        val vercall: Call<DocumentListModel> = apiServices.getDocument(prefs.newaccesstoken)
        vercall.enqueue(object : Callback<DocumentListModel> {
            override fun onResponse(call: Call<DocumentListModel>, response: Response<DocumentListModel>) {
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    Log.d(LLog.TAG,"???????????? ?????? SUCCESS -> $result")
                    if (result.documentList.isEmpty()) {
                        mBinding.documentRe.visibility = View.GONE
                        mBinding.tvDataDoNot.visibility = View.VISIBLE
                    } else {
                        mBinding.documentRe.visibility = View.VISIBLE
                        mBinding.tvDataDoNot.visibility = View.GONE
                        setAdapter(result.documentList)
                    }
                }
                else {
                    Log.d(LLog.TAG,"???????????? ?????? response ERROR -> $result")
                }
            }
            override fun onFailure(call: Call<DocumentListModel>, t: Throwable) {
                Log.d(LLog.TAG, "???????????? ?????? Fail -> $t")
            }
        })
    }

    private fun setAdapter(documentList: List<DocumentList>) {
        val adapter = DocumentAdapter(documentList, context!!)
        val rv = mBinding.documentRe
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(context)
        rv.setHasFixedSize(true)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        readapter?.notifyDataSetChanged()
    }

}