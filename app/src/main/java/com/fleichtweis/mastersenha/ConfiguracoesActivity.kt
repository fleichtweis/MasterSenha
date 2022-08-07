package com.fleichtweis.mastersenha

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.widget.RadioButton
import android.widget.Switch
import android.widget.TextView
import androidx.core.text.toSpannable


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
    lateinit var textExemploDicaJogo: TextView

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
        textExemploDicaJogo = findViewById(R.id.txt_exemplosJogo)


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
            formataTextoExemploDica()
        }

        radioBtnDificuldadeDificil.setOnClickListener {
            editor.putInt("dificuldade", 3)
            editor.apply()
            formataTextoExemploDica()
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
        formataTextoExemploDica()
    }

    private fun formataTextoExemploDica() {
        var textoDicaJogoFormatado: SpannableString = "".toSpannable() as SpannableString

        if (radioBtnDificuldadeFacil.isChecked){
            textoDicaJogoFormatado = getText(R.string.txt_config_regras_jogo_facil).toSpannable() as SpannableString
            textoDicaJogoFormatado.setSpan(StyleSpan(Typeface.BOLD), 0, 32, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
            textoDicaJogoFormatado.setSpan(BackgroundColorSpan(getColor(R.color.amber_500)), 52, 53, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
            textoDicaJogoFormatado.setSpan(BackgroundColorSpan(getColor(R.color.indigo_900)), 60, 61, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
            textoDicaJogoFormatado.setSpan(ForegroundColorSpan(getColor(R.color.white)), 60, 61, Spanned.SPAN_INCLUSIVE_INCLUSIVE)

            textoDicaJogoFormatado.setSpan(BackgroundColorSpan(getColor(R.color.amber_500)), 64, 65, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
            textoDicaJogoFormatado.setSpan(BackgroundColorSpan(getColor(R.color.indigo_900)), 106, 107, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
            textoDicaJogoFormatado.setSpan(ForegroundColorSpan(getColor(R.color.white)), 106, 107, Spanned.SPAN_INCLUSIVE_INCLUSIVE)

        } else if (radioBtnDificuldadeDificil.isChecked) {
            textoDicaJogoFormatado = getText(R.string.txt_config_regras_jogo_dificil).toSpannable() as SpannableString
            textoDicaJogoFormatado.setSpan(StyleSpan(Typeface.BOLD), 0, 34, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
            textoDicaJogoFormatado.setSpan(BackgroundColorSpan(getColor(R.color.indigo_900)), 68, 69, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
            textoDicaJogoFormatado.setSpan(ForegroundColorSpan(getColor(R.color.white)), 68, 69, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
            textoDicaJogoFormatado.setSpan(BackgroundColorSpan(getColor(R.color.amber_500)), 72, 73, Spanned.SPAN_INCLUSIVE_INCLUSIVE)

            textoDicaJogoFormatado.setSpan(BackgroundColorSpan(getColor(R.color.indigo_900)), 76, 77, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
            textoDicaJogoFormatado.setSpan(ForegroundColorSpan(getColor(R.color.white)), 76, 77, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
            textoDicaJogoFormatado.setSpan(BackgroundColorSpan(getColor(R.color.amber_500)), 128, 129, Spanned.SPAN_INCLUSIVE_INCLUSIVE)

        }

        textExemploDicaJogo.text = textoDicaJogoFormatado

    }
}