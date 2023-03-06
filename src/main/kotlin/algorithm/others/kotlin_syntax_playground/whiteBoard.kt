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


// *******************************************************************************
data class MenteeRequestField(
    var label: String = "",
    var value: String = ""
)

/**
 * 这个例子是一个template，如果自己写equals()的template
 */
data class LandingMenteeRequestItemData(
    val cardId: String = "",
    val mentorId: String? = null,
    val programId: String? = null,
    val menteeId: String? = null,
    val menteeAvatarUUId: String? = null,
    val menteeName: String? = null,
    val jobName: String? = null,
    val fields: MutableList<MenteeRequestField>? = null,
    val actions: MutableList<MenteeRequestField>? = null
) {
    fun isEqual(other: LandingMenteeRequestItemData): Boolean {
        if (this.fields?.size != other.fields?.size) {
            return false
        }
        // 如何遍历list fields里的元素并且查看是否相等
        this.fields?.let {
            // FIXME: 注意这里的用法！==> list.withIndex()
            for ((index, field) in this.fields.withIndex()) {
                val otherField = other.fields?.get(index)
                if (field.label != otherField?.label || field.value != otherField.value) {
                    return false
                }
            }
        }
        return this.mentorId == other.menteeId &&
            this.programId == other.programId &&
            this.menteeId == other.menteeId &&
            this.menteeAvatarUUId == other.menteeAvatarUUId &&
            this.menteeName == other.menteeName &&
            this.jobName == other.jobName
    }
    
}

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