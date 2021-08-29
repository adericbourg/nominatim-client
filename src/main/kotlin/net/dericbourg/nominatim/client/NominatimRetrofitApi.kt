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

        private val httpClient: OkHttpClient by lazy {
            OkHttpClient.Builder().build()
        }

        private val retrofit: Retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build()
        }

        internal val nominatimRetrofitApi: NominatimRetrofitApi by lazy {
            retrofit.create(NominatimRetrofitApi::class.java)
        }
    }
}
