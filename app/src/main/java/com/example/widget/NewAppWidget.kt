package com.example.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.journeyapps.barcodescanner.BarcodeEncoder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*


/**
 * Implementation of App Widget functionality.
 */
class NewAppWidget : AppWidgetProvider() {

    private val MyOnClick = "myOnClickTag"
    private val MyOnClick_2 = "myOnClickTag_2"
    private val UPDATE_ACTION = "android.appwidget.action.APPWIDGET_UPDATE"
    private var views:RemoteViews? = null
    private val strings = arrayOf("https://www.naver.com",
        "https://www.daum.net", "https://www.kakaocorp.com/page/",
        "https://www.netflix.com/kr/","https://www.disneyplus.com/ko-kr","https://www.cgv.co.kr/")

    companion object{
        private const val TAG="NewAppWidget"
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {

//        val widgetText = context.getString(R.string.appwidget_text)
//
//        var imageView: ImageView? = null
//        var text: String? = "https://www.naver.com"
//
//    views.setImageViewResource(R.id.imageButton_test, R.drawable.example_appwidget_preview)
        println("[SP_DEBUG] WIDGET [onUpdate]")
        println("[SP_DEBUG] START WIDGET 000")

        super.onUpdate(context, appWidgetManager, appWidgetIds)
        var text: String = "https://www.daum.net"
        updateAppWidget(context, appWidgetManager, appWidgetIds[0],text);


        var dataEmail:String = MyApplication.prefs.getString("email", "no email")

        MyApplication.prefs.setString("email", "test@gmail.com")

        println("[SP_DEBUG] [dataEmail]--->:"+dataEmail)
        var t_timer = Timer()
        //타이머 동작 시간 지정 및 작업 내용 지정
        var int_count = 1
        t_timer.schedule(object : TimerTask(){
            override fun run(){
                println("${int_count}")

                var receiveViews = RemoteViews(context.packageName, R.layout.new_app_widget)
                var text: String = "https://www.naver.com"
//                updateAppWidget(context, appWidgetManager, appWidgetIds[0],text);
                println("[SP_DEBUG] [타이머 실행 updateQr]")
//                updateQr(text,context)
                callUpdate(context)
//                Toast.makeText(context, "QR CHANGE", Toast.LENGTH_SHORT).show();
                //해결 방법 1
//                val handler = Handler(Looper.getMainLooper())
//                handler.postDelayed(Runnable { Toast.makeText(context, "Test", Toast.LENGTH_SHORT).show() }, 0)
                //카운트 값 증가
                int_count ++

                //카운트 값이 5가되면 타이머 종료 실시
                if(int_count > 5){
                    println("[SP_DEBUG] [타이머 종료]")
                    t_timer.cancel()
                }
            }
        },5000, 5000) //1초뒤 실행, 1초 마다 반복
        println("[SP_DEBUG] [타이머 실행]")



        updateQr(text,context)

//        callUpdate(context)
        // There may be multiple widgets active, so update all of them
//        for (appWidgetId in appWidgetIds) {
//            updateAppWidget(context, appWidgetManager, appWidgetId)
//        }
    }


    fun callUpdate(context:Context) {
        println("[SP_DEBUG] [callUpdate]")
        // new Intent(“전송하고 싶은 내용”)을 쓰면 된다. 실제 패키지 구조와 상관없이 전송하고 싶은 내용을 적으면 된다.
        val intent = Intent("android.appwidget.action.APPWIDGET_UPDATE")
//        val intent = Intent("APPWIDGET_UPDATE")
        intent.setPackage("com.example.widget")
        context.sendBroadcast(intent)
    }


     fun callApi() {

         println("[SP_DEBUG] callApi")
         val retrofit= Retrofit.Builder()
             .baseUrl("https://book.interpark.com")
             .addConverterFactory(GsonConverterFactory.create()) // Json데이터를 사용자가 정의한 Java 객채로 변환해주는 라이브러리
             .build() //레트로핏 구현체 완성!

         val bookService=retrofit.create(BookService::class.java) //retrofit객체 만듦!

         bookService.getBestSeller("38845BE9BD0EBEDF271A2D5BC770C5BEEBB2D38910F504545CE384C6692DA6D4")
             .enqueue(object: Callback<BestSellerDto> {
                 override fun onFailure(call: Call<BestSellerDto>, t: Throwable) {
                     //todo 실패처리
                     Log.d(TAG,t.toString())
                     println("[SP_DEBUG] onFailure"+t.toString())
                 }

                 override fun onResponse(call: Call<BestSellerDto>, response: Response<BestSellerDto>) {
                     //todo 성공처리
                     println("[SP_DEBUG] response-----0000")
                     if(response.isSuccessful.not()){
                         println("[SP_DEBUG] response-----1111")
                         return
                     }
                     response.body()?.let{
                         //body가 있다면 그안에는 bestSellerDto가 들어있을것
                         Log.d(TAG,it.toString())
                         println("[SP_DEBUG] response"+it.toString())

                         it.books.forEach{ book->
                             Log.d(TAG,book.toString())
                             println("[SP_DEBUG] book"+book.toString())
                         }
                     }
                 }

             })
    }


    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    private fun getPendingSelfIntent(context: Context?, action: String?): PendingIntent? {
        val intent = Intent(context, javaClass)
        intent.action = action
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_MUTABLE)
    }

    override fun onReceive(context: Context?, intent: Intent) {
        super.onReceive(context, intent)
        var remoteViews: RemoteViews


    println("[SP_DEBUG] START WIDGET onReceive")
        println("[SP_DEBUG] START WIDGET onReceive-intent.action::::"+intent.action)

    if (MyOnClick == intent.action) {
        //your onClick action is here
        println("[SP_DEBUG] onReceive==views::::"+views)
        println("[SP_DEBUG] onReceive==context::::"+context)
        println("[SP_DEBUG] START WIDGET MyOnClick===!!!!!")
        var text: String = "https://www.naver.com"
//        var text: String = "https://community.sparkplus.co/"
//        if (views != null) {

        val appWidgetManager = AppWidgetManager
            .getInstance(context)

        if (context != null) {
            val thisAppWidget = ComponentName(
                context.packageName, javaClass.name
            )

            var text: String = "https://www.naver.com"
//            updateAppWidget(context, appWidgetManager, appWidgetManager.getAppWidgetIds(thisAppWidget)[0],text);
            println("[SP_DEBUG] onReceive==receiveViews::::")
            updateQr(text,context)
            Toast.makeText(context, "QR CHANGE", Toast.LENGTH_SHORT).show();
        }
//            views!!.setImageViewResource(R.id.imageButton_test, R.drawable.example_appwidget_preview)
//        receiveViews?.let { updateQr(it,text) }
//        }


//        views!!.setImageViewResource(R.id.imageButton_test, R.drawable.example_appwidget_preview)
    }else if (MyOnClick_2 == intent.action){
        println("[SP_DEBUG] START WIDGET onReceive---ACTION---22")
        var text: String = "https://www.naver.com"
//            updateAppWidget(context, appWidgetManager, appWidgetManager.getAppWidgetIds(thisAppWidget)[0],text);
        println("[SP_DEBUG] onReceive==receiveViews::::")
        if (context != null) {
            updateQr(text,context)
        }
        Toast.makeText(context, "BTN_CLICK", Toast.LENGTH_SHORT).show();
        println("[SP_DEBUG] START WIDGET onReceive---ACTION---333")
    }else if (UPDATE_ACTION == intent.action){
        println("[SP_DEBUG] START WIDGET onReceive---UPDATE_ACTION---00")
        Toast.makeText(context, "UPDATE_ACTION", Toast.LENGTH_SHORT).show();

    }
  }

    private fun updateQr( targetText: String,context:Context) {

        var receiveViews = RemoteViews(context.packageName, R.layout.new_app_widget)
        println("[SP_DEBUG] updateQr")
//        println("[SP_DEBUG] updateQr")
        val random = Random()
        val num = random.nextInt(6)

        val multiFormatWriter = MultiFormatWriter()
        try {
            val bitMatrix: BitMatrix =
                multiFormatWriter.encode(strings[num], BarcodeFormat.QR_CODE, 400, 400)
            val barcodeEncoder = BarcodeEncoder()
            val bitmap: Bitmap = barcodeEncoder.createBitmap(bitMatrix)
            println("[SP_DEBUG] updateQr set IMAGE----000")
            println("[SP_DEBUG] updateQr==views-00000::::"+receiveViews)
            println("[SP_DEBUG] updateQr==context-11111==>::"+context)
//            Toast.makeText(context, "QR CHANGE", Toast.LENGTH_SHORT).show();
            println("[SP_DEBUG] updateQr set IMAGE----1111")
            receiveViews.setImageViewBitmap(R.id.imageButton_test, bitmap)
            println("[SP_DEBUG] updateQr set IMAGE----22222")
        } catch (e: Exception) {
        }
        val appWidgetManager = AppWidgetManager
            .getInstance(context)

        val thisAppWidget = ComponentName(
            context.packageName, javaClass.name
        )

        println("[SP_DEBUG] updateQr set IMAGE-getAppWidgetIds----"+appWidgetManager.getAppWidgetIds(thisAppWidget)[0])
//        println("[SP_DEBUG] updateQr set IMAGE")
        appWidgetManager.updateAppWidget(appWidgetManager.getAppWidgetIds(thisAppWidget)[0], receiveViews)
        println("[SP_DEBUG] updateQr set IMAGE-----333")
    }


    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        text: String
    ) {

        views = RemoteViews(context.packageName, R.layout.new_app_widget)
//     val MyOnClick = "myOnClickTag"
////     var views = RemoteViews(context.packageName, R.layout.new_app_widget)
//
//    val widgetText = context.getString(R.string.appwidget_text)
        // Construct the RemoteViews object
//        val views = RemoteViews(context.packageName, R.layout.new_app_widget)
//
//    var imageView: ImageView? = null
//        var text: String = "https://www.daum.net"
//        var text: String = "https://community.sparkplus.co/"
//
////    views.setImageViewResource(R.id.imageButton_test, R.drawable.example_appwidget_preview)
//

        println("[SP_DEBUG] START WIDGET")


        views!!.setOnClickPendingIntent(R.id.imageButton_test,
            getPendingSelfIntent(context, MyOnClick))

        views!!.setOnClickPendingIntent(R.id.imageButton_test_2,
            getPendingSelfIntent(context, MyOnClick_2))

//    println("[SP_DEBUG] START WIDGET 2222")
//    val intent = Intent(context, MainActivity::class.java)
//
//    //PendingIntent.FLAG_MUTABLE PendingIntent.FLAG_IMMUTABLE
//    val pe = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_MUTABLE)
//    views.setOnClickPendingIntent(R.id.imageButton_test, pe)
//
//    views.setOnClickPendingIntent(R.id.imageButton_test,
//        getPendingSelfIntent(context, MyOnClick));

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

}



