package com.kxxnzstdsw

import com.kxxnzstdsw.utils.Constants
import io.grpc.*

class AuthInterceptor(val startToken: String) : ServerInterceptor {
    // 定义 Metadata Key
    private val tokenKey: Metadata.Key<String> =
        Metadata.Key.of("authorization", Metadata.ASCII_STRING_MARSHALLER)

    override fun <ReqT : Any, RespT : Any> interceptCall(
        call: ServerCall<ReqT, RespT>,
        headers: Metadata,
        next: ServerCallHandler<ReqT, RespT>
    ): ServerCall.Listener<ReqT> {

        val token = headers[tokenKey]
        if (token.isNullOrBlank() || !isValid(token)) {
            call.close(
                Status.UNAUTHENTICATED.withDescription("Invalid token"),
                Metadata()
            )
            // 返回一个空 Listener，直接结束
            return object : ServerCall.Listener<ReqT>() {}
        }

        // 把解析后的用户放到 Context 中，业务实现里可取出
        val ctx = Context.current()
            .withValue(Constants.USER_CTX, parseUser(token))

        return Contexts.interceptCall(ctx, call, headers, next)
    }

    private fun isValid(token: String): Boolean {
        return token == startToken

    }

    private fun parseUser(token: String): String = token
}

