package com.kxxnzstdsw

fun ByteArray.toBase64(): String {
    return java.util.Base64.getEncoder().encodeToString(this)
}
