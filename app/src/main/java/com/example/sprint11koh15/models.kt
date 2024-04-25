package com.example.sprint11koh15

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.google.gson.TypeAdapter
import com.google.gson.annotations.SerializedName
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.lang.IllegalStateException
import java.lang.reflect.Type
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class NewsResponse(
    val result: String,
    val data: Data
)

data class Data(
    val title: String,
    val items: List<NewsItem>
)

sealed class NewsItem {

    abstract val id: String
    abstract val title: String
    abstract val type: String
    abstract val created: Date

    data class Sport(
        override val id: String,
        override val title: String,
        override val type: String,
        override val created: Date,
        val specificPropertyForSport: String,
    ) : NewsItem()

    data class Science(
        override val id: String,
        override val title: String,
        override val type: String,
        override val created: Date,
        @SerializedName("specific_property_for_science")
        val specificPropertyForScience: String,
    ) : NewsItem()

    data class Unknown(

        override val id: String,
        override val title: String,
        override val type: String,
        override val created: Date,

    ) : NewsItem()
}


class CustomDateTypeAdapter : TypeAdapter<Date>() {

    // https://ru.wikipedia.org/wiki/ISO_8601
    companion object {
        const val FORMAT_PATTERN = "yyyy-MM-DD'T'hh:mm:ss:SSS"
    }

    private val formatter = SimpleDateFormat(FORMAT_PATTERN, Locale.getDefault())
    override fun write(out: JsonWriter, value: Date) {
        out.value(formatter.format(value))
    }

    override fun read(`in`: JsonReader): Date {
        return formatter.parse(`in`.nextString())
    }

}

class NewsItemAdapter : JsonDeserializer<NewsItem>,JsonSerializer<NewsItem> {

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): NewsItem {
//        {
//            "id": 1,
//            "title": "Спортивная новость",
//            "type": "sport",
//            "created": "2023-01-01T11:12:13:123",
//            "specificPropertyForSport": "Англия - Франция"
//        }
        val type = json.asJsonObject.get("type").asString
        return when (type) {
            "sport" -> context.deserialize(json, NewsItem.Sport::class.java)
            "science" -> context.deserialize(json, NewsItem.Science::class.java)
            else -> context.deserialize(json, NewsItem.Unknown::class.java)
        }
    }

    override fun serialize(
        src: NewsItem?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        TODO("Not yet implemented")
    }

}