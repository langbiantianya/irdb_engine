package com.kxxnzstdsw.server

import com.google.protobuf.Empty
import com.kxxnzstdsw.SessionManager
import com.kxxnzstdsw.grpc.service.CreateSessionRequest
import com.kxxnzstdsw.grpc.service.GetSessionInfoRequest
import com.kxxnzstdsw.grpc.service.GetSessionInfoResponse
import com.kxxnzstdsw.grpc.service.SessionServiceGrpcKt

class SessionService : SessionServiceGrpcKt.SessionServiceCoroutineImplBase() {
    override suspend fun createSession(request: CreateSessionRequest): Empty {
        SessionManager.createSession(request.name, request.uri)
        return Empty.getDefaultInstance()
    }

    override suspend fun getSessionInfo(request: GetSessionInfoRequest): GetSessionInfoResponse {
        val sessionInfo = SessionManager.getSessionInfo(request.name)
        return GetSessionInfoResponse.newBuilder()
            .setVersion(sessionInfo?.version)
            .setDriverName(sessionInfo?.driverName)
            .build()
    }
}