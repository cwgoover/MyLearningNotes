package algorithm.others.kotlin_syntax_playground

/**
 * Created by i352072(erica.cao@sap.com) on 02/24/2023
 */

/**
 * 这个例子是为了找到一种方法，是无论调用isHasPhone()和getPhoneNumberList()多少次，
 * phoneNumbers只赋值一次！
 */
data class Phones(
    val phones: MutableList<Phone>
) {
    // One possible way to optimize the code is to use a `lazy-initialized property` to cache
    // the phoneNumbers list:
    // Here, the phoneNumbers property is only computed when it is first accessed, and then cached
    // for later use. This way, if isHasPhone() and getPhoneNumberList() are called multiple times,
    // the phones list will only be filtered and mapped once.
    private val phoneNumbers by lazy {
        // `mapNotNull` is a higher-order function in Kotlin standard library used to transform
        // a collection into another collection by applying a transformation function to each
        // element, and then filtering out the null results. It returns a list of non-null results.
        phones.filter { it.number?.isNotEmpty() == true }.mapNotNull { it.number }
    }
    
    fun isHasPhone(): Boolean = phoneNumbers.isNotEmpty()
    fun getPhoneNumberList() = phoneNumbers
}

data class Phone(
    val number: String?,
    val usage: String?
)

fun main() {
    val phoneList = mutableListOf<Phone>().apply {
        add(Phone(number = "122334", usage = null))
        add(Phone(number = "3344", usage = null))
        add(Phone(number = "56767", usage = null))
    }
    val phones = Phones(phoneList)
    // isHasPhone()调用一次
    if (phones.isHasPhone()) {
        // getPhoneNumberList()又调用一次
        println(phones.getPhoneNumberList())
    }
}