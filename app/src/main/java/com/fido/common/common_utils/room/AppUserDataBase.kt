package com.fido.common.Room

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
@author FiDo
@description:
@date :2023/6/13 14:56
添加依赖项：在项目的build.gradle文件中添加Room库的依赖项。
创建Entity类：创建一个或多个Entity类来表示数据库中的表。
创建DAO接口：创建一个或多个DAO接口来定义访问数据库的方法。
创建Database对象：创建一个继承自RoomDatabase的抽象类来表示数据库对象，并使用@Database注解指定数据库的配置信息和包含的实体类。
获取DAO实例：使用Database对象的实例方法获取DAO接口的实例。
调用DAO方法：使用DAO接口的实例方法来访问数据库。
链接：https://juejin.cn/post/7203911920637968444
 */

@Database(entities = [User::class], version = 2)
abstract class AppUserDataBase : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {

        private var INSTANCE: AppUserDataBase? = null

        private val MIGRATION_1_2 = object :Migration(1,2){
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE user ADD COLUMN user_sex TEXT NOT NULL DEFAULT ''")
            }
        }

        //需要升级到 version = 3时，为了兼容老版本 需要 addMigrations(MIGRATION_1_2, MIGRATION_2_3)
        //改成 @Database(entities = [User::class], version = 3)
        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE user ADD COLUMN height INTEGER NOT NULL DEFAULT 0")
            }
        }

        fun getInstance(context: Context): AppUserDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppUserDataBase::class.java,
                    "app_user_database"
                )
                    .addMigrations(MIGRATION_1_2)
//                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                    //是否允许在主线程进行查询 (一般用于Debug)
                    .allowMainThreadQueries()
                    //数据库创建和打开后的回调，可以重写其中的方法
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            Log.d("FiDo", "onCreate: app_user_database")
                        }
                    })
                    //数据库升级异常之后的回滚
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

}

val Context.appUserDatabase
    get() = AppUserDataBase.getInstance(this)

val Context.userDao
    get() = appUserDatabase.userDao()