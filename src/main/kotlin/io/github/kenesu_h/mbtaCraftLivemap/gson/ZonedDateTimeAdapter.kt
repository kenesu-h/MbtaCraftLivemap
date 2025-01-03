package io.github.kenesu_h.mbtaCraftLivemap.gson

import com.google.gson.*
import java.lang.reflect.Type
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

// Credits go to the following: https://mkyong.com/java/gson-supports-java-8-date-time-types/
class ZonedDateTimeAdapter : JsonSerializer<ZonedDateTime>, JsonDeserializer<ZonedDateTime> {
    private val formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME

    @Override
    override fun serialize(
        dateTime: ZonedDateTime,
        type: Type,
        jsonSerializationContext: JsonSerializationContext
    ): JsonElement {
        return JsonPrimitive(dateTime.format(formatter))
    }

    @Override
    override fun deserialize(
        jsonElement: JsonElement,
        type: Type,
        jsonDeserializationContext: JsonDeserializationContext
    ): ZonedDateTime {
        return ZonedDateTime.parse(jsonElement.asJsonPrimitive.asString, formatter)
    }
}