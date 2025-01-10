package com.fido.common.common_utils.design_pattern.observable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: FiDo
 * @date: 2024/6/24
 * @des:
 */
public class testObserver {

    public static void main(String[] args) {

        Observable<String> observable = new Observable<>();

        Observer<String> observer1 = new Observer<String>() {
            @Override
            public void onUpdate(String data) {
                System.out.println("观察者1："+data.toString());
            }
        };

        Observer<String> observer2 = new Observer<String>() {
            @Override
            public void onUpdate(String data) {
                System.out.println("观察者1："+data.toString());
            }
        };


        observable.register(observer1);
        observable.register(observer2);

        observable.notifyObserver("666");


        observable.unRegister(observer1);
        observable.notifyObserver("777");


        observer11 = (observable1, s) -> {
            System.out.println("observer11 onChanged "+s);
            observable1.unRegister(observer11);
        };
        Observable1<String> observable1 = new Observable1<>();
        observable1.register(observer11);
        observable1.notify("666");

    }
    static Observer1<String> observer11 = null;

}

//======================================= 测试===============================
//定义一个被观察者
class Observable1<T>{

    List<Observer1<T>> list = new ArrayList<>();

    public void register(Observer1<T> observer1){
        list.add(observer1);
    }

    public void unRegister(Observer1<T> observer1){
        list.remove(observer1);
    }

    public void notify(T t){
        for (Observer1<T> tObserver1 : list) {
            tObserver1.onChanged(this,t);
        }
    }

}

//定义一个观察者
interface Observer1<T>{
    void onChanged(Observable1<T> observable1,T t);
}
