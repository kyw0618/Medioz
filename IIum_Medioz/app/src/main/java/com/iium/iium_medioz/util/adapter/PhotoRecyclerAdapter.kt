package com.iium.iium_medioz.util.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iium.iium_medioz.R


class PhotoRecyclerAdapter internal constructor(list: ArrayList<Uri>?, context: Context?) :
    RecyclerView.Adapter<PhotoRecyclerAdapter.ViewHolder>() {
    private var mData: ArrayList<Uri>? = null
    private var mContext: Context? = null

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    inner class ViewHolder internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var image: ImageView = itemView.findViewById(R.id.image)

    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    // LayoutInflater - XML에 정의된 Resource(자원) 들을 View의 형태로 반환.
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val context: Context = parent.context
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater // context에서 LayoutInflater 객체를 얻는다.
        val view: View = inflater.inflate(
            R.layout.view_photo_item,
            parent,
            false
        ) // 리사이클러뷰에 들어갈 아이템뷰의 레이아웃을 inflate.
        return ViewHolder(view)
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val image_uri: Uri = mData!![position]
        mContext?.let {
            Glide.with(it)
                .load(image_uri)
                .into(holder.image)
        }

    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    override fun getItemCount(): Int {
        return mData!!.count()
    }

    // 생성자에서 데이터 리스트 객체, Context를 전달받음.
    init {
        mData = list
        mContext = context
    }
}