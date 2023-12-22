package com.bangkit.huggingpet.adapter

import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.huggingpet.R
import com.bangkit.huggingpet.database.ListPetDetail
import com.bangkit.huggingpet.databinding.ItemArticleBinding
import com.bumptech.glide.Glide
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class ArticleListAdapter(
    val id: String?,
    val source: String?,
    val title: String?,
    val description: String?,
    val img: Int,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(source)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeInt(img)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ArticleListAdapter> {
        override fun createFromParcel(parcel: Parcel): ArticleListAdapter {
            return ArticleListAdapter(parcel)
        }

        override fun newArray(size: Int): Array<ArticleListAdapter?> {
            return arrayOfNulls(size)
        }
    }
}