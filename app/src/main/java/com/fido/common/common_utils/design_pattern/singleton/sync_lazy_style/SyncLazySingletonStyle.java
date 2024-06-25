package com.fido.common.common_utils.design_pattern.singleton.sync_lazy_style;

/**
 * @author: FiDo
 * @date: 2024/6/25
 * @des: 由于懒汉式会出现线程不安全的问题，那我们通过加同步锁来保证线程的安全，同时进行双重的判空避免造成不必要的同步开销。
 */
public class SyncLazySingletonStyle {

    private static SyncLazySingletonStyle instance;

    private SyncLazySingletonStyle() {
    }

    public static SyncLazySingletonStyle getInstance(){
        if (instance == null) {
            synchronized (SyncLazySingletonStyle.class) {
                if (instance == null) {
                    instance = new SyncLazySingletonStyle();
                }
            }
        }
        return instance;
    }

}
