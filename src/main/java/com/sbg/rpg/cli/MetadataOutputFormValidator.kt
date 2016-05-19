package com.sbg.rpg.cli

import com.beust.jcommander.IParameterValidator
import com.beust.jcommander.ParameterException

class MetadataOutputFormatValidator: IParameterValidator {
    val acceptedOutputFormats = listOf("json", "yml", "yaml", "txt")

    override fun validate(name: String?, value: String?) {
        if (name == null || value == null)
            throw ParameterException("Metadata output format may not be null.")

        val trimmedValue = value.trim().toLowerCase()
        if (!acceptedOutputFormats.contains(trimmedValue))
            throw ParameterException("Accepted metadata output formats:  {json, yaml, txt}, received:  $value")
    }
}