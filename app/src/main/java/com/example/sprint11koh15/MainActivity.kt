package com.example.sprint11koh15

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.Date

/*

 */
class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "SPRINT_11"
    }

    private val adapter = NewsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val uri: Uri = Uri.parse("https://myserver.com:5051/api/v1/path?text=android&take=1#last")

        Log.d(TAG, "uri.scheme ${uri.scheme}")
        Log.d(TAG, "uri.host ${uri.host}")
        Log.d(TAG, "uri.authority ${uri.authority}")
        Log.d(TAG, "uri.pathSegments ${uri.pathSegments}")
        Log.d(TAG, "uri.lastPathSegment ${uri.lastPathSegment}")
        Log.d(TAG, "uri.queryParameterNames ${uri.queryParameterNames}")
        Log.d(TAG, "uri.getQueryParameter(\"text\") ${uri.getQueryParameter("text")}")
        Log.d(TAG, "uri.fragment ${uri.fragment}")


        val itemsRv: RecyclerView = findViewById(R.id.items)
        itemsRv.adapter = adapter

        val retrofit = Retrofit.Builder()
            .baseUrl("https://raw.githubusercontent.com/avanisimov/sprint-11-koh-15/")
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .setDateFormat("yyyy-MM-DD'T'hh:mm:ss:SSS")
                        .registerTypeAdapter(Date::class.java, CustomDateTypeAdapter())
                        .registerTypeAdapter(NewsItem::class.java, NewsItemAdapter())
                        .create()
                )
            )
            .build()
        val serverApi = retrofit.create(Sprint11ServerApi::class.java)

        itemsRv.postDelayed({
            serverApi.getNews1().enqueue(object : Callback<NewsResponse> {
                override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                    Log.i(TAG, "onResponse: ${response.body()}")
                    adapter.items = response.body()?.data?.items ?: emptyList()
                }

                override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                    Log.e(TAG, "onFailure: $t")
                }

            })
        }, 5000)
    }

    private fun programLayout() {

        val container: FrameLayout = findViewById(R.id.container)

        val textView = TextView(this)
        textView.text = "my text view"
        textView.layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT,
            Gravity.END
        ).apply {

        }
        textView.setPadding(
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16f, resources.displayMetrics).toInt(),
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16f, resources.displayMetrics).toInt(),
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16f, resources.displayMetrics).toInt(),
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16f, resources.displayMetrics).toInt(),
        )
        textView.textSize = 45f
        container.addView(textView)

        val items = (1..5).toList()

        container.removeView(textView)

        container.removeAllViews()
        val generateViewId = View.generateViewId()
        items.forEachIndexed { index, item ->
            val textView = TextView(this)
            textView.id = generateViewId
            textView.text = "my $item"
            textView.setBackgroundColor(resources.getColor(R.color.black, theme))
            textView.layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT,
                Gravity.END
            ).apply {

            }
            if (index != 0){

            }
            textView.setPadding(
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16f, resources.displayMetrics).toInt(),
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16f, resources.displayMetrics).toInt(),
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16f, resources.displayMetrics).toInt(),
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16f, resources.displayMetrics).toInt(),
            )
            textView.textSize = 45f
            container.addView(textView)
        }
        container.findViewById<TextView>(generateViewId)

        container.children
            .filterIsInstance<TextView>()
            .forEach {  }

        val v_layout = LayoutInflater.from(this)
            .inflate(R.layout.v_layout, container, false)
        container.addView(v_layout)


    }



}

// https://raw.githubusercontent.com/avanisimov/sprint-11-koh-15/main/jsons/news_1.json

interface Sprint11ServerApi {


    @GET("main/jsons/news_2.json")
    fun getNews1(): Call<NewsResponse>
}
