package de.hennihaus.utils

import com.typesafe.config.ConfigFactory
import de.hennihaus.plugins.ErrorMessage.MISSING_PROPERTY_MESSAGE

fun getHoconFileAsProperties(file: String): Map<String, String> {
    val properties = ConfigFactory.load(file).entrySet().map { it.key }.toSet().flatMap { key ->
        tryGetStringPropertyAsList(key = key, file = file)
            ?: tryGetStringPropertiesAsList(key = key, file = file)
            ?: throw PropertyNotFoundException(key = key)
    }

    return properties.toMap()
}

private fun tryGetStringPropertyAsList(file: String, key: String): List<Pair<String, String>>? {
    val property = runCatching {
        key to ConfigFactory.load(file).getString(key)
    }
    return property.getOrNull()?.let { listOf(it) }
}

private fun tryGetStringPropertiesAsList(file: String, key: String): List<Pair<String, String>>? {
    val properties = runCatching {
        ConfigFactory.load(file).getStringList(key).mapIndexed { index, property ->
            "$key[$index]" to property
        }
    }
    return properties.getOrNull()
}

class PropertyNotFoundException(key: String) : IllegalStateException("$MISSING_PROPERTY_MESSAGE $key")
