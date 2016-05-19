package com.sbg.rpg.image

class ImageReadException(val errorMessage: String = "", val c: Throwable? = null): RuntimeException(errorMessage, c)