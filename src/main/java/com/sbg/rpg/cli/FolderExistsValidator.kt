package com.sbg.rpg.cli

import com.beust.jcommander.IParameterValidator
import com.beust.jcommander.ParameterException
import java.nio.file.Files
import java.nio.file.Paths

class FolderExistsValidator: IParameterValidator {
    override fun validate(name: String?, value: String?) {
        if (value == null || value.isEmpty())
            throw ParameterException("export-folder value may not be empty")

        val path = Paths.get(value)!!.toAbsolutePath()!!
        if (!Files.exists(path))
            throw ParameterException("Path $path does not exist.")
    }
}