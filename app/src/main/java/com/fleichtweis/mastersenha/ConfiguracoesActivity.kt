package com.fleichtweis.mastersenha

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioButton
import android.widget.Switch


/*Dificuldade
    1 == FACIL, //Informa se o número testado está correto e na casa certa. Desativa botões testados que não estão na senha.
    3 == DIFICIL //Informa quantos números estão corretos e quantos estão na casa certa, mas não informa qual número é.
*/

class ConfiguracoesActivity : AppCompatActivity() {


    lateinit var switchNumerosDistintos: Switch
    lateinit var radioBtn4Numeros: RadioButton
    lateinit var radioBtn5Numeros: RadioButton
    lateinit var radioBtnDificuldadeFacil: RadioButton
    lateinit var radioBtnDificuldadeDificil: RadioButton

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuracoes)


        radioBtn4Numeros = findViewById(R.id.radioBtn4numeros)
        radioBtn5Numeros = findViewById(R.id.radioBtn5numeros)
        switchNumerosDistintos = findViewById(R.id.switch_numerosDistintos)
        radioBtnDificuldadeFacil = findViewById(R.id.radioBtn_facil)
        radioBtnDificuldadeDificil = findViewById(R.id.radioBtn_dificil)


        sharedPreferences = getSharedPreferences("ConfiguracoesPref", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()


        //Carrega e inicializa os componentes com as informações salvas sobre as configurações
        inicializaComponentes()


        radioBtn4Numeros.setOnClickListener {
            editor.putInt("numeroCasas", 4)
            editor.apply()
        }

        radioBtn5Numeros.setOnClickListener {
            editor.putInt("numeroCasas", 5)
            editor.apply()
        }

        switchNumerosDistintos.setOnClickListener {
            editor.putBoolean("numerosDistintos", switchNumerosDistintos.isChecked)
            editor.apply()
        }

        radioBtnDificuldadeFacil.setOnClickListener {
            editor.putInt("dificuldade", 1)
            editor.apply()
        }

        radioBtnDificuldadeDificil.setOnClickListener {
            editor.putInt("dificuldade", 3)
            editor.apply()
        }

    }

    private fun inicializaComponentes() {
        //Carrega informações salvas sobre as configurações
        val numeroCasas = sharedPreferences.getInt("numeroCasas", 4)
        val numerosDistintos = sharedPreferences.getBoolean("numerosDistintos", true)
        val dificuldade = sharedPreferences.getInt("dificuldade", 1)

        //Inicializa Componentes conforme preferências salvas
        if (numeroCasas == 4) {
            radioBtn4Numeros.isChecked = true
        } else {
            radioBtn5Numeros.isChecked = true
        }

        switchNumerosDistintos.isChecked = numerosDistintos

        when (dificuldade) {
            1 -> radioBtnDificuldadeFacil.isChecked = true
            3 -> radioBtnDificuldadeDificil.isChecked = true
            else -> radioBtnDificuldadeFacil.isChecked = true
        }
    }
}