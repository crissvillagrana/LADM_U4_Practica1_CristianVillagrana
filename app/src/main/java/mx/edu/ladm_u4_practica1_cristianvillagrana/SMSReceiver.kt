package mx.edu.ladm_u4_practica1_cristianvillagrana

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telephony.SmsMessage
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDateTime
import kotlin.coroutines.coroutineContext

class SMSReceiver : BroadcastReceiver(){

    override fun onReceive(context: Context, intent: Intent) {
        val extras = intent.extras
        if(extras!=null){
            var sms = extras.get("pdus") as Array<Any>
            for(msj in sms.indices){
                val format = extras.getString("format")
                var mensaje = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    SmsMessage.createFromPdu(sms[msj] as ByteArray,format)
                }else{
                    SmsMessage.createFromPdu(sms[msj] as ByteArray)
                }
                var numOrigen = mensaje.originatingAddress
                var texto = mensaje.messageBody.toString()

                Toast.makeText(context,"Datos entrantes ${texto} de parte de ${numOrigen}",Toast.LENGTH_LONG).show()

            }
        }
    }


}