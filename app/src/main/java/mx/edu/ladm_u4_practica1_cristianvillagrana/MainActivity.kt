package mx.edu.ladm_u4_practica1_cristianvillagrana

import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.telephony.SmsManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import androidx.core.app.ActivityCompat
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firestore.*
import mx.edu.ladm_u4_practica1_cristianvillagrana.databinding.ActivityMainBinding
import java.time.LocalDateTime
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    val permiso = 1
    val permisorecibir = 10
    var aidis = ArrayList<String>()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTitle("Almacen de SMS")

        //Aquí lo chido--------------------------------------------------------------------------------------------------------------------------
        val query = FirebaseDatabase.getInstance().getReference().child("mensajeria")
        val listener = object : ValueEventListener{
            override fun onDataChange(snap: DataSnapshot) {
                var datos = ArrayList<String>()
                aidis.clear()


                for(data in snap.children!!){
                    val id = data.key
                    aidis.add(id!!)
                    val telefono = data.getValue<Mensaje>()!!.telefono
                    val msj = data.getValue<Mensaje>()!!.mensaje
                    val hora = data.getValue<Mensaje>()!!.fechahora
                    datos.add("Número: ${telefono}, Mensaje: ${msj}, Hora: ${hora} \n")
                }

                
                lista()
            }

            override fun onCancelled(error: DatabaseError) {}
        }
        query.addValueEventListener(listener)


        //--------------------------------------------------------------------------------------------------------------------------------------
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.RECEIVE_SMS)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,arrayOf(android.Manifest.permission.RECEIVE_SMS),permiso)
        }
        binding.btnEnviar.setOnClickListener {
            if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.SEND_SMS)!=PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,arrayOf(android.Manifest.permission.SEND_SMS),permisorecibir)
            }else{
                enviarMensaje()
            }
        }
    }//onCreate

    private fun lista() {
        binding.listaMensajes.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==permiso){
            enviarMensaje()
        }
        if(requestCode==permisorecibir){
            Toast.makeText(this,"Se puede recibir", Toast.LENGTH_LONG).show()
        }
    }//onRequestPermissionsResult




    @RequiresApi(Build.VERSION_CODES.O)
    fun enviarMensaje(){
        SmsManager.getDefault().sendTextMessage(binding.numero.text.toString(),null,binding.mensaje.text.toString(),null,null)
        Toast.makeText(this,"Mensaje enviado", Toast.LENGTH_LONG).show()
        val baseRemota = FirebaseDatabase.getInstance().reference
        baseRemota.child("mensajeria").push().setValue(binding.numero.text.toString()+", "+LocalDateTime.now()+", "+binding.mensaje.text.toString()+"")

    }
}