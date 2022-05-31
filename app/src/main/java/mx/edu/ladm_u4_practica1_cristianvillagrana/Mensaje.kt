package mx.edu.ladm_u4_practica1_cristianvillagrana
import com.google.firebase.database.IgnoreExtraProperties
@IgnoreExtraProperties
data class Mensaje(val telefono: String?=null, val mensaje: String?=null, val fechahora: String?=null)
