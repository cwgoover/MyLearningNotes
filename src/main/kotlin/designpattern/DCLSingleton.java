package designpattern;

/**
 * Double-Checked Locking with Singleton:
 * https://www.baeldung.com/java-singleton-double-checked-locking
 *
 * Created by i352072(erica.cao@sap.com) on 12/25/2020
 */
class DCLSingleton {

    static class DraconianSingleton {
        private static DraconianSingleton instance;

        /**
         * Despite this class being thread-safe, we can see that there's a clear performance drawback:
         * each time we want to get the instance of our singleton, we need to acquire a potentially
         * unnecessary lock.
         */
        public static synchronized DraconianSingleton getInstance() {
            if (instance == null) {
                instance = new DraconianSingleton();
            }
            return instance;
        }
    }

    /**
     * To fix that, we could instead start by verifying if we need to create the object
     * in the first place and only in that case we would acquire the lock.
     */
    static class DclSingleton {
        /**
         * One thing to keep in mind with this pattern is that the field needs to be volatile
         * to prevent cache incoherence issues.
         */
        private static volatile DclSingleton instance;

        public static DclSingleton getInstance() {
            if (instance == null) {
                synchronized (DclSingleton.class) {
                     // we want to perform the same check again as soon as we enter the synchronized
                     // block, in order to keep the operation atomic:
                    if (instance == null) {
                        instance = new DclSingleton();
                    }
                }
            }
            return instance;
        }
    }

    /**
     * Even though the double-checked locking can potentially speed things up,
     * it has at least two issues:
     *  1. since it requires the volatile keyword to work properly,
     *      it's not compatible with Java 1.4 and lower versions
     *  2. it's quite verbose and it makes the code difficult to read
     */

    // ************************* Early Initialization *******************************//
    // The easiest way to achieve thread safety is to inline the object creation or to
    // use an equivalent static block. This takes advantage of the fact that static fields
    // and blocks are initialized one after another
    static class EarlyInitSingleton {
        private static final EarlyInitSingleton INSTANCE = new EarlyInitSingleton();

        public static EarlyInitSingleton getInstance() {
            return INSTANCE;
        }
    }


    // ************************* Initialization on Demand *******************************//
    // Additionally, since a class initialization occurs the first time we use one of its
    static class InitOnDemandSingleton {
        private static class InstanceHolder {
            private static final InitOnDemandSingleton INSTANCE = new InitOnDemandSingleton();
        }

        // In this case, the InstanceHolder class will assign the field the first time
        // we access it by invoking getInstance.
        public static InitOnDemandSingleton getInstance() {
            return InstanceHolder.INSTANCE;
        }
    }


    // ********************************* Enum Singleton *******************************//
    // The last solution comes from the Effective Java book (Item 3) by Joshua Block
    // and uses an enum instead of a class. At the time of writing, this is considered to be
    // the most concise and safe way to write a singleton:
    static enum EnumSingleton {
        INSTANCE;

        // other methods...
    }



    /*
      匿名内部类是唯一一种没有构造器的类。正因为其没有构造器，所以匿名内部类的使用范围非常有限，
      大部分匿名内部类用于接口回调。匿名内部类在编译的时候由系统自动起名为Outter$1.class。
      一般来说，匿名内部类用于继承其他类或是实现接口，并不需要增加额外的方法，只是对继承方法的实现或是重写。
     */



    /*
     * Java内部类详解： https://www.cnblogs.com/dolphin0520/p/3811445.html
     * Q1: 为什么局部内部类和匿名内部类只能访问局部final变量？
     * A: 当外部方法执行完毕之后，变量的生命周期就结束了，而此时内部类对象的生命周期很可能还没有结束，
     *    那么在内部类的方法中继续访问外部变量就变成不可能了，但是又要实现这样的效果，怎么办呢？
     *    Java采用了 复制  的手段来解决这个问题。这就要保证数据的一致性，即java编译器就限定必须将变量
     *    限制为final变量，不允许对变量进行更改。
     *
     *  Q2: 为什么在Java中需要内部类？
     *  A: 总结一下主要有以下四点：
     * 　　1.每个内部类都能独立的继承一个接口的实现，所以无论外部类是否已经继承了某个(接口的)实现，对于内部类
     *       都没有影响。内部类使得多继承的解决方案变得完整，
     * 　　2.方便将存在一定逻辑关系的类组织在一起，又可以对外界隐藏。
     * 　　3.方便编写事件驱动程序
     * 　　4.方便编写线程代码
     */
}
