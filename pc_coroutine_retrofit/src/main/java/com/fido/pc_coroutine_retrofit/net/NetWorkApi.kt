package com.fido.pc_coroutine_retrofit.net

import com.fido.pc_coroutine_retrofit.data.HomeData
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * @author: HuTao
 * @date: 2026/5/22
 * @des:
 */
interface NetWorkApi {
    //示例post 请求
    @FormUrlEncoded
    @POST("https://www.wanandroid.com/user/register")
    suspend fun register(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("repassword") repassword: String
    ): String

    // 示例get 请求
    @GET("article/list/0/json")
    suspend fun getHomeList(): String

    // 示例get 请求2
    @GET("article/list/{path}/json")
    suspend fun getHomeList22(@Path("path") page: Int): HomeData

    /************************* 以下只 示例写法，接口调不通，因为找不到那么多 公开接口  全是 Retrofit的用法 来测试 *****************************************************/


//    @FormUrlEncoded
    @Headers("Content-Type: application/x-www-form-urlencoded")  //todo 固定 header
    @POST("https://xxxxxxx")
    fun post1(@Body body: RequestBody): String

    //    @FormUrlEncoded
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @POST("https://xxxxxxx22222")
    fun post12(@Body body: RequestBody, @HeaderMap map: Map<String, String>): String //todo  HeaderMap 多个请求头部自己填写

}