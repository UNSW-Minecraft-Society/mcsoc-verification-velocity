package com.mcsoc.verificationvelocity.dataloader

import com.google.gson.Gson
import com.google.gson.GsonBuilder

import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.lang.reflect.Type
import java.nio.file.Files
import java.nio.file.Path

import kotlin.collections.HashMap
import kotlin.collections.Map


internal interface JsonFileLoader <D> {

    fun getGsonParser(): Gson
    

    private fun openFileChecked(file_path: Path): File? {
        val file = file_path.toFile()
        Files.createDirectories(file_path.parent)
        if (file.isDirectory) return null
        file.createNewFile()
        return file
    }

    fun writeToFileFromData(file_path: Path, data_obj: D, data_type: Type) {
        val file = openFileChecked(file_path) ?: return
        FileWriter(file).use{writer  ->
            this.getGsonParser().toJson(data_obj, data_type, writer)
        }
    }
    
    fun readFromFileToData(file_path: Path, data_type: Type): D? {
        val file = openFileChecked(file_path) ?: return null
        FileReader(file).use{reader ->
            return this.getGsonParser().fromJson(reader, data_type)
        }
    }
}