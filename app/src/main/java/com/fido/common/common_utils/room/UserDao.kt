package com.fido.common.Room

import androidx.room.*

/**
@author FiDo
@description:
@date :2023/6/13 11:36
 */

@Dao
interface UserDao {

    @Query("SELECT * FROM user")
    fun getAllUser():List<User>

    @Query("SELECT * FROM user WHERE id=:uId")
    fun getUserById(uId:Int):User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User)

    @Delete
    fun deleteUser(user: User)

    @Update
    fun updateUser(user: User)



}