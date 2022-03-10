package com.alibabajavacodingguidelines;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Demo implements Serializable {

    private static final long serialVersionUID = -5010275166682123126L;

    /**
     * 接口过时，需要加@Deprecated，并且说明新接口
     * */
    @Deprecated
    public void method(){}

    public static void main(String[] args) {
        /**
         * 所有的相同类型的包装类对象之间值的比较，全部使用 equals 方法比较。
         * 说明：对于 Integer var = ? 在-128 至 127 范围内的赋值，Integer 对象是在IntegerCache.cache 产生，
         * 会复用已有对象，这个区间内的 Integer 值可以直接使用==进行判断，但是这个区间之外的所有数据，都会在堆上产生，并不会复用已有对象，这是一个大坑，
         * 推荐使用 equals 方法进行判断。
         * */
        Integer a = 1;
        Integer b = 2;
        if (a.equals(b)) {
        }


        /**
         * 循环体内用StringBuilder进行字符串链接，避免资源浪费
         * */
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < 10 ; i++){
            stringBuilder.append("a");
        }
        System.out.println(stringBuilder);


        /**
         * ArrayList的subList结果不可强转成ArrayList，否则会抛出ClassCastException
         * 异常，即 java.util.RandomAccessSubList cannot be cast to java.util.ArrayList.
         * 说明：subList 返回的是 ArrayList 的内部类 SubList，并不是 ArrayList ，而是
         * ArrayList 的一个视图，对于 SubList 子列表的所有操作最终会反映到原列表上。
         *
         * 在 subList 场景中，高度注意对原集合元素个数的修改，会导致子列表的遍历、增加、
         * 删除均会产生 ConcurrentModificationException 异常。
         * */
        List<Integer> integerList = new ArrayList<>();
        integerList.add(1);
        integerList.add(2);
        integerList.add(3);

        List<Integer> subList = integerList.subList(0,2);
        subList.set(0,4);

        System.out.println(integerList);
        //print [4,2,3]

        /**
         * 使用集合转数组的方法，必须使用集合的 toArray(T[] array)，传入的是类型完全一样的数组，大小就是 list.size()。
         * */
        int size = integerList.size();
        Integer[] myIntegerArray = integerList.toArray(new Integer[size]);

        /**
         * 使用工具类 Arrays.asList()把数组转换成集合时，不能使用其修改集合相关的方
         * 法，它的 add/remove/clear 方法会抛出 UnsupportedOperationException 异常。
         * 说明：asList 的返回对象是一个 Arrays 内部类，并没有实现集合的修改方法。Arrays.asList
         * 体现的是适配器模式，只是转换接口，后台的数据仍是数组。
         * */
        List<Integer> myIntegerList = Arrays.asList(myIntegerArray);
        //运行时异常
        //myIntegerList.add(1);

        //修改原数组，生成的list也会变化
        myIntegerArray[0] = 100;
        System.out.println(myIntegerList);


        /**
         * 在 JDK7 版本及以上，Comparator 要满足如下三个条件，不然 Arrays.sort，Collections.sort 会报 IllegalArgumentException 异常。
         * 说明：三个条件如下
         * 1） x，y 的比较结果和 y，x 的比较结果相反。
         * 2） x>y，y>z，则 x>z。
         * 3） x=y，则 x，z 比较结果和 y，z 比较结果相同。
         * */
        List<Student> studentList = new ArrayList<>();
        studentList.add(new Student(10,"小明"));
        studentList.add(new Student(15,"小花"));
        studentList.add(new Student(10,"小马"));

        Collections.sort(studentList, (o1, o2) -> {
            if (o1.getAge().equals(o2.getAge())) return 0;
            else return o1.getAge() > o2.getAge() ? 1 : -1;
        });

        System.out.println(studentList);

        /**
         * 如果是 JDK8 的应用，可以使用 Instant 代替 Date，LocalDateTime 代替 Calendar，
         * DateTimeFormatter 代替 SimpleDateFormat，官方给出的解释：simple beautiful strong
         * immutable thread-safe。
         * */
        //UTC时间线的原点
        System.out.println(Instant.EPOCH);
        //UTC时间线最大值
        System.out.println(Instant.MAX);
        //当前时间
        Instant now = Instant.now();
        //距离原点有多少秒
        now.getEpochSecond();
        //当前时间的纳秒数
        now.getNano();
        //距离原点有多少毫秒
        now.toEpochMilli();

        //构造距离原点一秒的时间点
        Instant instantOne = Instant.ofEpochSecond(1L);
        //Instant instantOne = Instant.ofEpochSecond(-1L);

        //构造距离原点5秒+500纳秒的时间点
        Instant instantTwo = Instant.ofEpochSecond(5L, 500L);

        //字符串构造时间点
        Instant datetime1 = Instant.parse("2022-03-10T15:00:30.00Z");
        Instant datetime2 = Instant.parse("2022-03-10T15:00:30.00Z");

        //时间比较
        datetime1.compareTo(datetime2);
        datetime1.isAfter(datetime2);

        //一天后
        Instant datetime = Instant.now().plus(1, ChronoUnit.DAYS);
        //一天前
        datetime.minus(1, ChronoUnit.DAYS);
        //5小时30分钟后
        datetime.plus(Duration.ofHours(5).plusMinutes(30));

        //计算两个Instant的差值
        long minutes = ChronoUnit.MINUTES.between(datetime, Instant.now());
        System.out.println(minutes);

        //2022-03-10
        LocalDate localDate = LocalDate.now();
        System.out.println(localDate);

        //15:34:16.980
        LocalTime localTime = LocalTime.now();
        System.out.println(localTime);

        //2022-03-10T17:10:44.854
        LocalDateTime localDateTimeNow = LocalDateTime.now();
        LocalDateTime localDateTimeFuture = LocalDateTime.now().plus(1, ChronoUnit.DAYS);

        //当前时间加1小时30分钟
        System.out.println(localDateTimeNow.plus(Duration.ofHours(1).plusMinutes(30)));
        //当前时间加1星期15天
        System.out.println(localDateTimeNow.plus(Period.ofWeeks(1).plusDays(15)));
        //localDateTime 转换为 localDate
        System.out.println(localDateTimeNow.toLocalDate());

        //计算差值demo1
        long minutesTwo = ChronoUnit.MINUTES.between(localDateTimeNow, localDateTimeFuture);
        System.out.println(minutesTwo);
        //计算差值demo2
        Duration duration = Duration.between(localDateTimeNow, localDateTimeFuture);
        long days = duration.toDays();
        System.out.println(days);
        //计算差值demo3
        long minutesThree = localDateTimeNow.until(localDateTimeFuture, ChronoUnit.MINUTES);
        System.out.println(minutesThree);

        Random random = new Random();
        for(int i = 0; i < 20; i++){
            System.out.print(random.nextInt(2));
        }



    }

    /**
     * 1） 只要重写 equals，就必须重写 hashCode。
     * 2） 因为 Set 存储的是不重复的对象，依据 hashCode 和 equals 进行判断，所以 Set 存储的对象必须重写这两个方法。
     * 3） 如果自定义对象做为 Map 的键，那么必须重写 hashCode 和 equals。
     * 说明：String 重写了 hashCode 和 equals 方法，所以我们可以非常愉快地使用 String 对象作为 key 来使用。
     * */
    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    static class Student{
        private Integer age;
        private String name;

        @Override
        public String toString() {
            return "年龄：" + age + ", 姓名：" + name;
        }
    }

}
