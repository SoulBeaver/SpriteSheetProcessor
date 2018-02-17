/*
 *  Copyright 2016 Christian Broomfield
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.sbg.rpg.cli

import com.beust.jcommander.IParameterValidator
import com.beust.jcommander.ParameterException

class MetadataOutputFormatValidator: IParameterValidator {
    private val acceptedOutputFormats = listOf("json", "yml", "yaml", "txt")

    override fun validate(name: String?, value: String?) {
        if (name == null || value == null)
            throw ParameterException("Metadata output format may not be null.")

        val trimmedValue = value.trim().toLowerCase()
        if (!acceptedOutputFormats.contains(trimmedValue))
            throw ParameterException("Accepted metadata output formats:  {json, yaml, txt}, received:  $value")
    }
}