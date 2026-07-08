package com.mcsoc.verificationvelocity.dataloader

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.google.gson.reflect.TypeToken

import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.lang.reflect.Type
import java.nio.file.Files
import java.nio.file.Path

import kotlin.collections.HashMap
import kotlin.collections.Map


internal interface LoadedFileData: JsonSerializer<LoadedFileData>, JsonDeserializer<LoadedFileData> {
    fun toJson(): JsonObject

    override fun serialize(src: LoadedFileData?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement? {
        return src?.toJson()
    }
}

internal interface JsonFileLoader <D : LoadedFileData> {
    fun getGsonParser(): Gson
    
    fun getFileDataType(): Type
    fun getDefaultFileData(): D
    
    fun saveConfigData(file_path: Path, config_data: D) {
        writeToFileFromData(file_path, config_data, getFileDataType())
    }
    
    fun loadConfigData(file_path: Path): D {
        return readFromFileToData(file_path, getFileDataType()) ?: getDefaultFileData()
    }
    
    
    private fun openFileChecked(file_path: Path): File? {
        val file = file_path.toFile()
        Files.createDirectories(file_path.parent)
        if (file.isDirectory) return null
        file.createNewFile()
        return file
    }

    private fun writeToFileFromData(file_path: Path, data_obj: D, data_type: Type) {
        val file = openFileChecked(file_path) ?: return
        FileWriter(file).use{writer  ->
            this.getGsonParser().toJson(data_obj, data_type, writer)
        }
    }
    
    private fun readFromFileToData(file_path: Path, data_type: Type): D? {
        val file = openFileChecked(file_path) ?: return null
        FileReader(file).use{reader ->
            return this.getGsonParser().fromJson(reader, data_type)
        }
    }
}