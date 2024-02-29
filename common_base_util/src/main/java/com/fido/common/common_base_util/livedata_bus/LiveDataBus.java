package com.fido.common.common_base_util.livedata_bus;

import androidx.lifecycle.MutableLiveData;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: FiDo
 * @date: 2024/2/27
 * @des: 消息总线 消息总线通过单例实现，不同的消息通道存放在一个 HashMap中。
 *       订阅 订阅者通过 with 方式获取消息通道，然后调用 observe 订阅这个通道的消息。
 *       发布 发布者通过 with 获取消息通道，然后调用 setValue 或者 postValue 发布消息。
 * 注册订阅
 * LiveDataBus.get().with("key_test", Boolean.class)
 *     .observe(this, new Observer<Boolean>() {
 *         @Override
 *         public void onChanged(@Nullable Boolean aBoolean) {
 *         }
 *  });
 *
 * 发送消息
 * LiveDataBus.get().with("key_test").setValue(true);
 */
public final class LiveDataBus {

    private final Map<String, MutableLiveData<Object>> bus;

    private  LiveDataBus() {
        this.bus = new HashMap<>();
    }

    private static class SingletonHolder{
        private static final LiveDataBus SINGLETON = new LiveDataBus();
    }

    public static LiveDataBus get(){
        return SingletonHolder.SINGLETON;
    }

    public <T>MutableLiveData<T> with(String target,Class<T> clazz){
        if (!bus.containsKey(target)) {
            bus.put(target,new MutableLiveData<>());
        }
        return (MutableLiveData<T>) bus.get(target);
    }

    public MutableLiveData<Object> with(String target){
        return with(target,Object.class);
    }

}
