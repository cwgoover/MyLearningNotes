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
      ???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
      ??????????????????????????????????????????????????????????????????????????????????????????????????????Outter$1.class???
      ?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
     */



    /*
     * Java?????????????????? https://www.cnblogs.com/dolphin0520/p/3811445.html
     * Q1: ????????????????????????????????????????????????????????????final?????????
     * A: ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
     *    ?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
     *    Java????????? ??????  ????????????????????????????????????????????????????????????????????????java?????????????????????????????????
     *    ?????????final??????????????????????????????????????????
     *
     *  Q2: ????????????Java?????????????????????
     *  A: ????????????????????????????????????
     * ??????1.????????????????????????????????????????????????????????????????????????????????????????????????????????????(?????????)????????????????????????
     *       ????????????????????????????????????????????????????????????????????????
     * ??????2.????????????????????????????????????????????????????????????????????????????????????
     * ??????3.??????????????????????????????
     * ??????4.????????????????????????
     */
}
