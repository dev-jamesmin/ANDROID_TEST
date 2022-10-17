package com.example.widget

import com.google.gson.annotations.SerializedName

data class Book (
    @SerializedName("itemId")val id:Long, //서버에서는 itemId라는 값을 가져와서,  app에선 id라는 값으로
    @SerializedName("title")val title:String,
    @SerializedName("description")val description:String,
    @SerializedName("coverSmallUrl")val coverSmallUrl:String

)

data class BestSellerDto (
    @SerializedName("title") val title:String,
    @SerializedName("item") val books:List<Book>

)

data class SearchBookDto (
    @SerializedName("title") val title:String,
    @SerializedName("item") val books:List<Book>
)