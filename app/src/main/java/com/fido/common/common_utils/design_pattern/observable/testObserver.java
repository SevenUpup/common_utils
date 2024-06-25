package com.fido.common.common_utils.design_pattern.observable;

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
    }

}
