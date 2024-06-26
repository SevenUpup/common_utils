package com.fido.common.Room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
@author FiDo
@description:
@date :2023/6/13 11:34
 */

@Entity(tableName = "user")
data class User(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "user_name") val name: String,
    @ColumnInfo(name = "user_age") val age: Int,
    //room version = 2 新增 user_sex
    @ColumnInfo(name="user_sex") val sex:String,
    //room version = 3 新增 height
//    @ColumnInfo(name="height") val height:Int,
)

