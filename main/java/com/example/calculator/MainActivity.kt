package com.example.calculator

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder
import java.util.regex.Pattern

class MainActivity : AppCompatActivity() {
    var Tv: EditText? = null//inicia un textView
    var concatenar = ""


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        Tv = findViewById(R.id.numeros)//inicializa el textView con el de la app

        Tv?.isFocusable = true
        Tv?.isClickable = false

        Tv?.showSoftInputOnFocus = false
    }

    fun Calcular(view: View) {//calcula los botones numeros y operaciones
        try {
            var boton = view as Button//se crea una variable boton, que inicia la accion de el boton clickeado
            var textBoton = boton.text.toString()//se obtiene el texto del boton

            if (textBoton != "=")//se concatena en el textView para mostrar los botones en pantalla
                concatenar = Tv?.text.toString() + textBoton

            //se compara el primer caracter para determinar si no es un valor que pueda afectar a el calculo
            if (textBoton == "=") {
                if (concatenar[0] == '+' || concatenar[0] == '-' || concatenar[0] == '*' || concatenar[0] == '/'  || concatenar[0] == '^' || concatenar[0] == '%')
                    Tv?.setText(eliminar0(concatenar))
                Tv?.setText(ExpressionBuilder(Tv?.text.toString()).build().evaluate().toString())
            } else if (textBoton != "C")
                Tv?.setText(concatenar)//sino se agrega al textView
            if (textBoton == "C")
                Tv?.setText("")

            actua(view)
        }catch (e: Exception) {

        }
    }

    fun expresion(expre: String): String {
        var token = Busqueda(expre)
        var resultado = token[0].toDouble()
        for(i in 1 until token.size step 2){
            try {
                val operador = token[i]
                val operando = token[i+1].toDouble()
                when(operador){
                    "%" -> resultado = (resultado*operando)/100
                    else -> throw NumberFormatException("operador: $operador")
                }
            }catch (_: Exception) {
            }
        }
        return "$resultado"
    }

    fun Busqueda(str: String): Array<String> {
        var i = 0
        var numero: String = ""
        var arr: ArrayList<String> = ArrayList()
        while(i < str.length) {
            if (str[i] == '0' || str[i] == '1' || str[i] == '2' || str[i] == '3' || str[i] == '4' || str[i] == '5' || str[i] == '6' || str[i] == '7' || str[i] == '8' || str[i] == '9' || str[i] == '.') {
                numero += str[i]
            }else if (str[i] == '+' || str[i] == '-' || str[i] == '*' || str[i] == '/' || str[i] == '^'){
                arr.add("$numero")
                arr.add(str[i]+"")
                numero = ""
            }
            i++
        }
        arr.add("$numero")
        var A = Array<String>(arr.size) {""}
        for(i in arr.indices)
            A[i] = arr[i]
        return A
    }

    fun eliminar0(str: String): String{//elimina el 0 o otro caracter
        var S: String = ""
        for (i in 1 until str.length) S = str[i].toString()
        return S
    }

    fun borrar(str: String): String{
        var aux: String = ""
        for(i in 0 until str.length-1)
            aux += str[i]
        return aux
    }

    fun atras(View: View){
        var cursor = Tv?.selectionStart
        if(cursor!! > 0){
            val new = Tv?.text?.delete(cursor!!-1,cursor!!)
            Tv?.setText(new)
            Tv?.setSelection(cursor!!-1)
        }
    }

    fun a(View: View){
        var cursor = Tv?.selectionStart
        if(cursor!! > 0){
            Tv?.setSelection(cursor!!-1)
        }
    }

    fun aa(View: View){
        var cursor = Tv?.selectionEnd
        if(cursor!! < Tv?.text!!.length){
            Tv?.setSelection(cursor!!+1)
        }
    }

    fun actua(View: View){
        var cursor = Tv?.selectionEnd
        if(cursor!! < Tv?.text!!.length){
            Tv?.setSelection(cursor!!+Tv?.text!!.length)
        }
    }
}