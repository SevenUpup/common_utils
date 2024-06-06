package com.fido.common.common_utils.pop.login_interceptor

/**
 * @author: FiDo
 * @date: 2024/6/5
 * @des:
 */
object LoginInterceptorChain {


    var LOGIN_INTERCEPTOR_IS_LOGIN = ""
    fun isLogin() = LOGIN_INTERCEPTOR_IS_LOGIN.isNotBlank()

    private val _interceptors = ArrayList<ILoginInterceptor>()

    private val loginInterceptor = LoginInterceptor()

    private var index = 0
    fun process(){
        if (_interceptors.size <=1 ) return
        when(index){
            in _interceptors.indices -> {
                val interceptor = _interceptors[index]
                index++
                interceptor.interceptor(this)
            }
            _interceptors.size ->{
                clearAllInterceptor()
            }
        }
    }

    private fun clearAllInterceptor() {
        _interceptors.clear()
        index = 0
    }

    fun addInterceptor(interceptor: ILoginInterceptor):LoginInterceptorChain{
        //默认添加登陆拦截
        if (!_interceptors.contains(loginInterceptor)) {
            _interceptors.add(loginInterceptor)
        }
        if (!_interceptors.contains(interceptor)) {
            _interceptors.add(interceptor)
        }
        return this
    }

    fun loginFinished(){
        if (_interceptors.contains(loginInterceptor) && _interceptors.size > 1) {
            loginInterceptor.loginFinished()
        }
    }

}