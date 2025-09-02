package com.demo.testing.target

class TargetClass {
    companion object {
        val EMPTY_STRING_ARRAY = arrayOf<String>()
    }

    fun substringBetween(str: String?, open: String?, close: String?): Array<String>? {
        if (str == null || open?.isEmpty() == true || close?.isEmpty() == true) {
            return null
        }

        val strLen = str.length
        if (strLen == 0) {
            return EMPTY_STRING_ARRAY
        }

        val closeLen = close!!.length
        val openLen = open!!.length
        val list = mutableListOf<String>()
        var pos = 0

        while (pos < strLen - closeLen) {
            var start = str.indexOf(open, pos)

            if (start < 0) {
               break
            }

            start += openLen
            var end = str.indexOf(close, pos)
            if (end < 0) {
                break
            }

            list.add(str.substring(start, end))
            pos = end + closeLen
        }

        if (list.isEmpty()) {
            return null
        }

        return list.toTypedArray()
    }
}