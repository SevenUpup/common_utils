package com.fido.common.common_utils.test.asm.apitest.measure_methodtime;

/**
 * @author: FiDo
 * @date: 2024/5/6
 * @des:  查看字节码用的
 */
public class MeasureMethodTime2 {

    public int test(int a,boolean b){
        return 10;
    }

    @MeasureTime
    public void measure(){
        long var1 = System.currentTimeMillis();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            RuntimeException var10000 = new RuntimeException(e);
            long var4 = System.currentTimeMillis();
            System.out.println(var4 - var1);
            throw var10000;
        }
        long var6 = System.currentTimeMillis();
        System.out.println("used time:" + (var6 - var1));
    }

}
