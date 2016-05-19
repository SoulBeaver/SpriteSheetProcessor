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