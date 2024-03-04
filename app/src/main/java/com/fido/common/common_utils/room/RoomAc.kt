package com.fido.common.common_utils.room

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fido.common.common_base_ui.base.viewbinding.binding
import com.fido.common.common_base_ui.ext.bindData
import com.fido.common.common_base_ui.ext.edittext.throttleSearch
import com.fido.common.common_base_ui.ext.vertical
import com.fido.common.common_base_util.ext.click
import com.fido.common.common_base_util.ext.loge
import com.fido.common.common_base_util.ext.toast
import com.fido.common.common_utils.R
import com.fido.common.common_utils.databinding.AcRoomBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import kotlin.random.Random

/**
@author FiDo
@description: 数据库Room Test
@date :2023/6/13 11:29
 */
class RoomAc:AppCompatActivity() {

    val binding:AcRoomBinding by binding()

    private fun search(it: CharSequence?) = flow<String> {
        loge("search=>${it}")
        emit(it.toString())
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*binding.input2.textChanges()
            .filterNot { it.isNullOrBlank() }
//            .distinctUntilChanged()   //去重操作符，distinctUntilChangedBy的简化版，相同的值将被丢弃
            .debounce(300)
            .flatMapLatest {//前面的看时间间隔进行结合 , 中间的可能跳过某些元素 , 不要中间值 , 只重视最新的数据
                search(it)
            }
            .onEach {
                toast("textChanges=${it}")
            }
            .launchIn(lifecycleScope)*/

        binding.input2.throttleSearch(lifecycle,300,{searchKey ->
            runBlocking {//模拟网络请求延迟
                delay(500)
                User(0,searchKey.toString(),18,"1")
            }
        },{
            toast(it.name)
            binding.input.setText(it.name)
        })

        /*lifecycleScope.launch {
            binding.input.onTextChangedFlow()
                .debounce(200)
                .filter { it.isNotEmpty() }
                .distinctUntilChanged()
                .catch { print(it.message) }
                .flowOn(Dispatchers.IO)
                .collect{
                    // 网络请求获取搜索结果
                    toast(it.toString())
                }
        }*/

        binding.add.click {
            userDao.insertUser(getRandomUser())
            updateRv()
        }

        binding.delete.click {
            userDao.deleteUser(userDao.getAllUser().last())
            updateRv()
        }

        binding.query.click {
            userDao.getUserById(userDao.getAllUser().shuffled().take(1)[0].id)?.let {
                updateRv(mutableListOf(it))
            }
        }

        binding.update.click {
            val allUsers = userDao.getAllUser()
            if (allUsers.isEmpty()) return@click
            val shuffledId = allUsers.shuffled().take(1)[0].id  //取list 一个随机元素
            userDao.updateUser(User(shuffledId,"shuffledIdUser${shuffledId}",Random.nextInt(66),"1"))

            updateRv()
        }

        binding.mRv.vertical()
        binding.getAll.click {
            updateRv()
        }

    }

    private fun getRandomUser(): User {
        val id = Random.nextInt(100000)
        return User(id,"randomUser${id}",Random.nextInt(60),"0")
    }

    private fun updateRv(users:List<User>?=null) {
        binding.mRv.bindData(users?:userDao.getAllUser(), R.layout.item_rv_text){holder, position, item ->
            holder.setText(R.id.mTitle,"name=${item?.name} age=${item?.age} sex=${item?.sex}")
            holder.setGone(R.id.bt_up,true)
            holder.setGone(R.id.bt_down,true)
        }
    }

}