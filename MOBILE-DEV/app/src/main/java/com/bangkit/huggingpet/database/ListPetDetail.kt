package com.bangkit.huggingpet.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "pets")
data class ListPetDetail(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "title")
    val title: String? = null,

    @ColumnInfo(name = "image")
    val photoUrl: String? = null,

    ):Parcelable