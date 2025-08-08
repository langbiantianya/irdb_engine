package com.kxxnzstdsw.utils

import io.grpc.Context

object Constants {
    val USER_CTX: Context.Key<String> = Context.key("token")
}