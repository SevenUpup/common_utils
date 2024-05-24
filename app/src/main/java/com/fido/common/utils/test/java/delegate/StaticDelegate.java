package com.fido.common.common_utils.test.java.delegate;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: FiDo
 * @date: 2024/2/19
 * @des:  静态代理
 */
public class StaticDelegate {

    private static int mFlag;

    public static int addFlag(int flags){
        return mFlag |= flags;
    }

    public static int setFlags(int flags){
        mFlag = flags;
        return mFlag;
    }

    public static void main(String[] args) {
        RentHouse baozupo = new Baozupo();
        RentHouse zhongjie = new Intermediary(baozupo);
        zhongjie.rentHouse();

        String params = TestStaticClass.number;
        System.out.println("main count="+params);

        List<? super RentHouse> list = new ArrayList<>();
        //测试用，只是为了验证可以add
//        RentHouse obj = (RentHouse) new Object();
//        list.add(obj);
        list.add(baozupo);
        list.add(zhongjie);

        Fruit f = new Fruit("");
        Object t = f.getT();
        System.out.println("t=" + t);

        Fruit<Integer> f2 = new Fruit<Integer>(123);
        Integer t1 = f2.getT();
        System.out.println("t1=" + t1);


//        System.out.println("addFlag one= " + addFlag(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK));
//        System.out.println("addFlag two= " + addFlag(Intent.FLAG_ACTIVITY_NEW_TASK));
//        System.out.println("setFlags = " + setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK));
//        System.out.println("setFlags = " + setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

        System.out.println(addFlag(1|2));
        System.out.println(addFlag(12));
        System.out.println(setFlags(66));
        System.out.println(setFlags(55));
    }

    static class Fruit<T>{
        public Fruit(T t) {
            this.t = t;
        }

        private T t;

        public T getT(){
            return this.t;
        }
    }

    //租房
    interface RentHouse {
        void rentHouse();
    }

    //包租婆，让中介帮忙租房
    static class Baozupo implements RentHouse {

        @Override
        public void rentHouse() {
            System.out.println("包租婆的房租出去咯");
        }
    }

    //中介--代理实现类
    static class Intermediary implements RentHouse {

        private RentHouse rentHouse;

        public Intermediary(RentHouse rentHouse) {
            this.rentHouse = rentHouse;
        }

        //前置处理
        public void before(){
            System.out.println("中介租房之前，我可以帮你把房租出去，但是租出后我要拿80%");
        }

        //后置处理
        public void after(){
            System.out.println("中介租房之后，给钱");
        }

        @Override
        public void rentHouse() {
            before();
            rentHouse.rentHouse();
            after();

        }
    }

    //房客，找中介租房
    class Lodger implements RentHouse {

        @Override
        public void rentHouse() {
            System.out.println("小胖子想要租房");
        }
    }

}
