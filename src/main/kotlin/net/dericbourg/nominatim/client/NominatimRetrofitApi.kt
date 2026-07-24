package net.dericbourg.nominatim.client

import net.dericbourg.nominatim.api.Place
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.QueryMap


internal interface NominatimRetrofitApi {

    @GET("/search")
    fun search(@QueryMap queryMap: Map<String, String>): Call<List<Place>>

    companion object {

        private const val BaseUrl = "https://nominatim.openstreetmap.org"

        internal fun create(userAgent: String, baseUrl: String = BaseUrl): NominatimRetrofitApi {
            val httpClient = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    chain.proceed(
                        chain.request().newBuilder()
                            .header("User-Agent", userAgent)
                            .build()
                    )
                }
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build()

            return retrofit.create(NominatimRetrofitApi::class.java)
        }
    }
}
