import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.locks.LockSupport;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

class ThreadUnsafe {


    ArrayList<String> list = new ArrayList<>();

    public void method1(int loopNumber) {
        for (int i = 0; i < loopNumber; i++) {
// { 临界区, 会产生竞态条件
            method2();
            method3();

// } 临界区
        }
    }

    private void method2() {
        list.add("1");
    }

    private void method3() {
        list.remove(0);
    }
}





public class Test {

    static final int THREAD_NUMBER = 2;
    static final int LOOP_NUMBER = 200;

    public static void main(String[] args) {
//        String s1 = "a";
//        String s2 = "b";
//        String s3 = "a" + "b";
//        String s4 = s1 + s2;
//        String s5 = "ab";
//        String s6 = s4.intern();
//        System.out.println(s3 == s4);
//        System.out.println(s3 == s5);
//        System.out.println(s3 == s6);
//        String x2 = new String("c") + new String("d");
//        String x1 = "cd";
//        x2.intern(); // 问，如果调换了【最后两行代码】的位置呢，如果是jdk1.6呢
//         System.out.println(x1 == x2);

        demo(
                ()->new int[10],
                (array)->array.length,
                (array, index) -> array[index]++,
                array-> System.out.println(Arrays.toString(array))
        );


        ForkJoinPool forkJoinPool=new ForkJoinPool();
    }


    /**
     参数1，提供数组、可以是线程不安全数组或线程安全数组
     参数2，获取数组长度的方法
     参数3，自增方法，回传 array, index
     参数4，打印数组的方法
     */
// supplier 提供者 无中生有 ()->结果
// function 函数 一个参数一个结果 (参数)->结果 , BiFunction (参数1,参数2)->结果
// consumer 消费者 一个参数没结果 (参数)->void, BiConsumer (参数1,参数2)->
    private static <T> void demo(
            Supplier<T> arraySupplier,
            Function<T, Integer> lengthFun,
            BiConsumer<T, Integer> putConsumer,
            Consumer<T> printConsumer ) {
        List<Thread> ts = new ArrayList<>();
        T array = arraySupplier.get();
        int length = lengthFun.apply(array);
        for (int i = 0; i < length; i++) {
// 每个线程对数组作 10000 次操作
            ts.add(new Thread(() -> {
                for (int j = 0; j < 10000; j++) {
                    putConsumer.accept(array, j%length);
                }
            }));
        }
        ts.forEach(t -> t.start()); // 启动所有线程
        ts.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }); // 等所有线程结束
        printConsumer.accept(array);
    }

}


class AddTask3 extends RecursiveTask<Integer> {
    int begin;
    int end;
    public AddTask3(int begin, int end) {
        this.begin = begin;
        this.end = end;
    }
    @Override
    public String toString() {
        return "{" + begin + "," + end + '}';
    }
    @Override
    protected Integer compute() {
// 5, 5
        if (begin == end) {
            log.debug("join() {}", begin);
            return begin;
        }
// 4, 5

        if (end - begin == 1) {
            log.debug("join() {} + {} = {}", begin, end, end + begin);
            return end + begin;
        }
// 1 5
        int mid = (end + begin) / 2; // 3
        AddTask3 t1 = new AddTask3(begin, mid); // 1,3
        t1.fork();
        AddTask3 t2 = new AddTask3(mid + 1, end); // 4,5
        t2.fork();
        log.debug("fork() {} + {} = ?", t1, t2);
        int result = t1.join() + t2.join();
        log.debug("join() {} + {} = {}", t1, t2, result);
        return result;
    }
}