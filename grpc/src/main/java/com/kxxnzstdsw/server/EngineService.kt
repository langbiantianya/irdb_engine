package com.kxxnzstdsw.server

import com.google.protobuf.Empty
import com.kxxnzstdsw.Engine
import com.kxxnzstdsw.grpc.service.EngineServiceGrpcKt
import com.kxxnzstdsw.grpc.service.GetInfoResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.File

class EngineService : EngineServiceGrpcKt.EngineServiceCoroutineImplBase() {
    override suspend fun close(request: Empty): Empty {
        Engine.exit()
        return Empty.getDefaultInstance()
    }

    override suspend fun restart(request: Empty): Empty {
        Engine.restart()
        return Empty.getDefaultInstance()
    }

    override suspend fun getInfo(request: Empty): GetInfoResponse {
        val runtime = Runtime.getRuntime()
        // 计算已使用内存(MB)：总内存 - 空闲内存
        return GetInfoResponse.newBuilder()
            .setTotalRam(runtime.totalMemory().toString()) //byte
            .setFreeMemory(runtime.freeMemory().toString()) //byte
            .setUseRam((runtime.totalMemory() - runtime.freeMemory()).toString()) //byte
            .setSystem(getSystemInfo())
            .setCpu(getCpuInfo())
            .build()
    }

    // 获取系统信息
    private fun getSystemInfo(): String {
        return "${System.getProperty("os.name")} ${System.getProperty("os.version")}"
    }

    // 获取CPU信息
    private fun getCpuInfo(): String {
        val cores = Runtime.getRuntime().availableProcessors()
        val model = runBlocking { getCpuModel() }
        return "$model ($cores cores)"
    }

    // 根据操作系统获取CPU型号
    private suspend fun getCpuModel(): String = withContext(Dispatchers.IO) {
        return@withContext try {
            val osName = System.getProperty("os.name").lowercase()
            when {
                osName.contains("windows") -> getWindowsCpuModel()
                osName.contains("linux") -> getLinuxCpuModel()
                osName.contains("mac") || osName.contains("darwin") -> getMacCpuModel()
                osName.contains("freebsd") -> getFreeBsdCpuModel()
                else -> "Unknown CPU"
            }
        } catch (e: Exception) {
            "Unknown CPU (${e.message?.take(30)})"
        }
    }

    // Windows系统获取CPU型号 (通过wmic命令)
    private fun getWindowsCpuModel(): String {
        val process = Runtime.getRuntime().exec("wmic cpu get name")
        return process.inputStream.bufferedReader().use { reader ->
            reader.readLines()
                .drop(1) // 跳过表头
                .firstOrNull { it.isNotBlank() }
                ?.trim() ?: "Unknown Windows CPU"
        }
    }

    // Linux系统获取CPU型号 (读取/proc/cpuinfo)
    private fun getLinuxCpuModel(): String {
        return File("/proc/cpuinfo").bufferedReader().use { reader ->
            reader.readLines()
                .firstOrNull { it.startsWith("model name") }
                ?.split(":")?.get(1)?.trim() ?: "Unknown Linux CPU"
        }
    }

    // FreeBSD系统获取CPU型号 (通过sysctl命令)
    private fun getFreeBsdCpuModel(): String {
        val process = Runtime.getRuntime().exec("sysctl -n hw.model")  // FreeBSD专用sysctl参数
        return process.inputStream.bufferedReader().use { it.readLine()?.trim() } ?: "Unknown FreeBSD CPU"
    }

    // macOS系统获取CPU型号 (通过sysctl命令)
    private fun getMacCpuModel(): String {
        val process = Runtime.getRuntime().exec("sysctl -n machdep.cpu.brand_string")
        return process.inputStream.bufferedReader().use { it.readLine()?.trim() } ?: "Unknown macOS CPU"
    }
}