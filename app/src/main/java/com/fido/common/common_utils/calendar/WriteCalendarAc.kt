package com.fido.common.common_utils.calendar

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fido.common.common_base_ui.base.viewbinding.binding
import com.fido.common.common_base_ui.ext.permission.extRequestPermission
import com.fido.common.common_base_ui.util.throttleClick
import com.fido.common.common_base_util.ext.toDateMills
import com.fido.common.common_base_util.ext.toast
import com.fido.common.common_base_util.util.calendar.CalendarUtils
import com.fido.common.common_utils.databinding.AcWriteCalendarBinding

/**
@author FiDo
@description:
@date :2023/8/23 17:26
 */
class WriteCalendarAc:AppCompatActivity() {

    val binding:AcWriteCalendarBinding by binding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val remindStart = "2023-08-24 15:25:00".toDateMills()
        val remindEnd = "2023-08-25 14:20:00".toDateMills()
        val beforeRemindMuint = 5 // 提前5分钟提醒
        binding.btWrite.throttleClick {
            extRequestPermission(listOf(android.Manifest.permission.READ_CALENDAR,android.Manifest.permission.WRITE_CALENDAR),"不给权限玩不了"){
                if (it) {
                    CalendarUtils.addCalendarEventRemind(this@WriteCalendarAc,"今晚打老虎","去哪打啊，带我吗？",remindStart,remindStart,beforeRemindMuint,object :CalendarUtils.onCalendarRemindListener{
                        override fun onFailed(error_code: CalendarUtils.onCalendarRemindListener.Status?) {
                            toast(error_code?.name)
                        }

                        override fun onSuccess() {
                            toast("Ok,今晚打老虎")
                        }

                    })
                }
            }
        }

        binding.btDelete.throttleClick {
            extRequestPermission(listOf(android.Manifest.permission.READ_CALENDAR,android.Manifest.permission.WRITE_CALENDAR),"不给权限玩不了"){
                CalendarUtils.deleteCalendarEventRemind(this@WriteCalendarAc,"今晚打老虎","去哪打啊，带我吗？",remindStart,object :CalendarUtils.onCalendarRemindListener{
                    override fun onFailed(error_code: CalendarUtils.onCalendarRemindListener.Status?) {
                        toast(error_code?.name)
                    }

                    override fun onSuccess() {
                        toast("delet success")
                    }

                })
            }
        }
    }

}