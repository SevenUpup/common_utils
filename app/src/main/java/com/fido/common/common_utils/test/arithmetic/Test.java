package com.fido.common.common_utils.test.arithmetic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Test {

    public static void main(String[] args) {
        testArrangeActivity();
    }

    /**
     * 例题分析 [活动安排问题] 活动安排问题是可以用贪心算法有效求解的一个很好的例子。该问题要求高效地安排一系列争用某一公共资源的活动。贪心算法提供了一个简单、漂亮的方法使得尽可能多的活动能兼容地使用公共资源。
     * 设有n个活动的集合e={1，2，…，n}，其中每个活动都要求使用同一资源，如演讲会场等，而在同一时间内只有一个活动能使用这一资源。每个活动i都有一个要求使用该资源的起始时间si和一个结束时间fi,且si< fi。如果选择了活动i，则它在半开时间区间[si，fi]内占用资源。若区间[si，fi]与区间[sj，fj]不相交，则称活动i与活动j是相容的。也就是说，当si≥fi或sj≥fj时，活动i与活动j相容。活动安排问题就是要在所给的活动集合中选出最大的相容活动子集合。
     * 在下面所给出的解活动安排问题的贪心算法gpeedyselector中，各活动的起始时间和结束时间存储于数组s和f{中且按结束时间的非减序：．f1≤f2≤…≤fn排列。如果所给出的活动未按此序排列，我们可以用o(nlogn)的时间将它重排。
     */
    public static void testArrangeActivity() {
        int[] start = {1, 3, 0, 5, 3, 5, 6, 8, 8, 2, 12};
        int[] end = {4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14};
        List<Integer> results = arrangeActivity(start, end);
        for (int i = 0; i < results.size(); i++) {
            int index = results.get(i);
            System.out.println("开始时间:" + start[index] + ",结束时间:" + end[index]);
        }
    }

    /**
     * 活动安排
     *
     * @param s 开始时间
     * @param e 结束时间
     * @return
     */
    private static List<Integer> arrangeActivity(int[] s, int[] e) {
        int total = s.length;
        int endFlag = e[0];
        List<Integer> results = new ArrayList<>();
        results.add(0);
        for (int i = 0; i < total; i++) {
            if (s[i] > endFlag) {
                results.add(i);
                endFlag = e[i];
            }
        }
        return results;
    }


    /**
     * [找零钱问题]
     * 假如老板要找给我99分钱，他有上面的面值分别为25，10，5，1的硬币数，为了找给我最少的硬币数，那么他是不是该这样找呢，
     * 先看看该找多少个25分的，诶99／25＝3，好像是3个，要是4个的话，我们还得再给老板一个1分的，我不干，那么老板只能给我3个25分的拉，
     * 由于还少给我24，所以还得给我2个10分的和4个1分。
     */
    public static void testGiveMoney() {
        //找零钱
        int[] m = {25, 10, 5, 1};
        int target = 99;
        int[] results = giveMoney(m, target);
        System.out.println(target + "的找钱方案:");
        for (int i = 0; i < results.length; i++) {
            System.out.println(results[i] + "枚" + m[i] + "面值");
        }
    }

    private static int[] giveMoney(int[] m, int target) {
        int k = m.length;
        int[] num = new int[k];
        for (int i = 0; i < k; i++) {
            num[i] = target / m[i];
            target = target % m[i];
        }
        return num;
    }


    public static void getMaxWeightValue(){
        int W = 30;
        int[] goods = {28,20,10};
        int[] values = {28,20,10};

        int[] valuesResult = new int[values.length];
        for (int i = 0; i < values.length; i++) {
            valuesResult[i] = values[i]/goods[i];
        }

        int vMax = Arrays.stream(valuesResult).max().getAsInt();
        int vMin = Arrays.stream(valuesResult).min().getAsInt();
        System.out.println("vMax="+vMax+" vMin="+vMin);
        if (vMax == vMin) {
            //说明价值都是一样的

        } else {

        }

    }

}
