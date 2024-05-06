package com.fido.common.common_utils.test.asm.apitest.measure_methodtime;

/**
 * @author: FiDo
 * @date: 2024/5/6
 * @des:
 */
public class MeasureMethodTime {

    @MeasureTime
    public void measure(){
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
