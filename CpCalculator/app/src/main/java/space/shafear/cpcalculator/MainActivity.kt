package space.shafear.cpcalculator

import algorithm.MyPokemon
import android.annotation.TargetApi
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import space.shafear.cpcalculator.algorithm.Pokedex
import space.shafear.cpcalculator.algorithm.getPokemonByNumber

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        displayNotification()
    }

    private fun displayNotification() {
        val mBuilder = Notification.Builder(this)
                .setContentText(getString(R.string.notification_content))
                .setContentTitle(getString(R.string.notification_title))
        val resultIntent = Intent(this, NotificationActivity::class.java)
        if(android.os.Build.VERSION.SDK_INT >= 21) {
            mBuilder.setSmallIcon(R.mipmap.cp_icon_notificationbar)
            mBuilder.setColor(ContextCompat.getColor(this, R.color.colorAccent))
        } else {
            mBuilder.setSmallIcon(R.mipmap.cp_calculator_icon)
        }
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val pi = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        mBuilder.setContentIntent(pi)
        mBuilder.setOngoing(true)
        val mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mNotificationManager.notify(0, mBuilder.build())
    }

    fun closeDescription(view: View){
        this.finish()
    }
}
