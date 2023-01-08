package com.fleichtweis.mastersenha

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.core.text.toSpannable
import kotlin.random.Random

/*
DIFICULDADE
1 - Fácil
3 - Difícil
 */

class MainActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    // Variáveis de configurações
    var numeroCasas: Int = 4
    var numeroTentativas: Int = 5 //Conforme dificuldade escolhida o número varia. (Fácil = 5; Díficil = 10)
    var numerosDistintos: Boolean = true
    var dificuldade: Int = 1
    var exibeRegrasJogo: Boolean = true

    // Variáveis
    var senha: IntArray = IntArray(numeroCasas)
    var jogando: Boolean = false
    var vitoria: Boolean = false
    var jogoFinalizado: Boolean = false
    var textviewTentativaSelecionado: Int = 0
    var tentativa: Int = 1

    // Componentes da activity
    // Botões
    lateinit var btnNovoAndTesteJogo: Button
    lateinit var btnReset: ImageButton

    // Botões numéricos
    lateinit var btn0: Button
    lateinit var btn1: Button
    lateinit var btn2: Button
    lateinit var btn3: Button
    lateinit var btn4: Button
    lateinit var btn5: Button
    lateinit var btn6: Button
    lateinit var btn7: Button
    lateinit var btn8: Button
    lateinit var btn9: Button

    // TextView Senha
    lateinit var txtSenhaDig1: TextView
    lateinit var txtSenhaDig2: TextView
    lateinit var txtSenhaDig3: TextView
    lateinit var txtSenhaDig4: TextView
    lateinit var txtSenhaDig5: TextView

    // TextView Tentativa
    lateinit var txtTentativaDig1: TextView
    lateinit var txtTentativaDig2: TextView
    lateinit var txtTentativaDig3: TextView
    lateinit var txtTentativaDig4: TextView
    lateinit var txtTentativaDig5: TextView

    // TextView Histórico Tentativas
    lateinit var txtHistorico1: TextView
    lateinit var txtHistorico2: TextView
    lateinit var txtHistorico3: TextView
    lateinit var txtHistorico4: TextView
    lateinit var txtHistorico5: TextView
    lateinit var txtHistorico6: TextView
    lateinit var txtHistorico7: TextView
    lateinit var txtHistorico8: TextView
    lateinit var txtHistorico9: TextView
    lateinit var txtHistorico10: TextView

    // TextView's
    lateinit var txtInfo: TextView
    lateinit var txtTentativas: TextView
    lateinit var txtHistoricoTentativas: TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findIdComponentes()

        btnReset.setOnClickListener {

            if(jogando && tentativa != 1) {
                val builder = AlertDialog.Builder(this)
                builder.setTitle(R.string.dialog_titulo_encerrar_jogo)
                    .setMessage(R.string.dialog_mensagem_encerrar_jogo)
                    .setPositiveButton(R.string.dialog_sim) { _, _ ->
                        fimJogo()
                    }
                    .setNegativeButton(R.string.dialog_nao) { dialog, _ ->
                        dialog.cancel()
                    }

                val dialog: AlertDialog? = builder.create()
                dialog?.show()
            } else{
                //Muda nome e Espera click no botão de novo jogo.
                btnNovoAndTesteJogo.text = getText(R.string.btn_novo_jogo)
                jogando = false
                configuracoesIniciais()
            }
        }


        //Carrega as configurações salvas
        sharedPreferences = getSharedPreferences(getString(R.string.pref_config), Context.MODE_PRIVATE)
        carregaConfiguracoes()

        //Função já chama as configurações iniciais
        componentesQuintoDigito()
        //configuracoesIniciais()

        if (exibeRegrasJogo){
            exibirRegrasJogo(false)
        }

        if (savedInstanceState != null){
            senha = savedInstanceState.getSerializable("senha") as IntArray
            mostrarSenha()
            Log.i("saved", "onCreate $senha")
        }



    }

    private fun findIdComponentes() {
        btnNovoAndTesteJogo = findViewById(R.id.btn_newAndTestGame)
        btnReset = findViewById(R.id.imageBtnReset)

        btn0 = findViewById(R.id.btn0)
        btn1 = findViewById(R.id.btn1)
        btn2 = findViewById(R.id.btn2)
        btn3 = findViewById(R.id.btn3)
        btn4 = findViewById(R.id.btn4)
        btn5 = findViewById(R.id.btn5)
        btn6 = findViewById(R.id.btn6)
        btn7 = findViewById(R.id.btn7)
        btn8 = findViewById(R.id.btn8)
        btn9 = findViewById(R.id.btn9)

        txtSenhaDig1 = findViewById(R.id.txt_senhaDig1)
        txtSenhaDig2 = findViewById(R.id.txt_senhaDig2)
        txtSenhaDig3 = findViewById(R.id.txt_senhaDig3)
        txtSenhaDig4 = findViewById(R.id.txt_senhaDig4)
        txtSenhaDig5 = findViewById(R.id.txt_senhaDig5)

        txtTentativaDig1 = findViewById(R.id.txt_tentativaDig1)
        txtTentativaDig2 = findViewById(R.id.txt_tentativaDig2)
        txtTentativaDig3 = findViewById(R.id.txt_tentativaDig3)
        txtTentativaDig4 = findViewById(R.id.txt_tentativaDig4)
        txtTentativaDig5 = findViewById(R.id.txt_tentativaDig5)

        txtInfo = findViewById(R.id.txt_info)
        txtTentativas = findViewById(R.id.txt_tentativas)
        txtHistoricoTentativas = findViewById(R.id.txt_historicoTentativas)

        txtHistorico1 = findViewById(R.id.txt_historico_1)
        txtHistorico2 = findViewById(R.id.txt_historico_2)
        txtHistorico3 = findViewById(R.id.txt_historico_3)
        txtHistorico4 = findViewById(R.id.txt_historico_4)
        txtHistorico5 = findViewById(R.id.txt_historico_5)
        txtHistorico6 = findViewById(R.id.txt_historico_6)
        txtHistorico7 = findViewById(R.id.txt_historico_7)
        txtHistorico8 = findViewById(R.id.txt_historico_8)
        txtHistorico9 = findViewById(R.id.txt_historico_9)
        txtHistorico10 = findViewById(R.id.txt_historico_10)
    }

    private fun carregaConfiguracoes() {
        numeroCasas = sharedPreferences.getInt(getString(R.string.pref_config_numero_casas), 4)
        numerosDistintos = sharedPreferences.getBoolean(getString(R.string.pref_config_numeros_distintos), true)
        dificuldade = sharedPreferences.getInt(getString(R.string.pref_config_dificuldade), 1)
        exibeRegrasJogo = sharedPreferences.getBoolean(getString(R.string.pref_config_exibe_regras_jogo), true)

        when(dificuldade){
            1 -> numeroTentativas = 5
            3 -> numeroTentativas = 10
            else -> numeroTentativas = 10
        }


    }

    override fun onStart() {
        carregaConfiguracoes()
        componentesQuintoDigito()
        super.onStart()
    }

    //Ação do botão voltar do Android pressionado.
    override fun onBackPressed() {
        if(jogando && tentativa != 1) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle(R.string.dialog_titulo_encerrar_jogo)
                .setMessage(R.string.dialog_mensagem_encerrar_jogo)
                .setPositiveButton(R.string.dialog_sim) { _, _ ->
                    fimJogo()
                    finish()
                }
                .setNegativeButton(R.string.dialog_nao) { dialog, _ ->
                    dialog.cancel()
                }

            val dialog: AlertDialog? = builder.create()
            dialog?.show()
        }else {
            super.onBackPressed()
        }
    }

    //Menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    //Menu - Itens
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.menu_configuracoes){
            if (!jogando){
                val intent = Intent(this, ConfiguracoesActivity::class.java)
                startActivity(intent)
                return true
            } else{
                Toast.makeText(this, R.string.toast_aviso_configuracoes, Toast.LENGTH_LONG).show()
            }
        }
        if (id == R.id.menu_informacoes){
            if (!jogando){
                val intent = Intent(this, InformacoesActivity::class.java)
                startActivity(intent)
                return true
            } else{
                Toast.makeText(this, R.string.toast_aviso_informacoes, Toast.LENGTH_LONG).show()
            }
        }
        if (id == R.id.menu_ajuda){
            exibirRegrasJogo(true)
            return true
            /*if (!jogando){
                exibirRegrasJogo(true)
                return true
            } else{
                Toast.makeText(this, R.string.toast_aviso_informacoes, Toast.LENGTH_LONG).show()
            }*/
        }

        return super.onOptionsItemSelected(item)
    }

    /*
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        //Salvar só se estiver jogando
        outState.putSerializable("senha", senha)
        Log.i("saved", "onSaved $senha")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        *//*for (i in 0 until numeroCasas){
            senha[i] = savedInstanceState.getSerializable("senha")
        }*//*
        senha = savedInstanceState.getSerializable("senha") as IntArray
        mostrarSenha()
        Log.i("saved", "onRestore $senha")
    }
*/

    //Clique em um dos textviews da tentativa de senha
    fun textviewTentativa(view: View){
        componentesTextviewTentativaBorderNormal()
        when(view.id){
            R.id.txt_tentativaDig1 -> {
                txtTentativaDig1.setBackgroundResource(R.drawable.border_selection)
                textviewTentativaSelecionado = 1
            }
            R.id.txt_tentativaDig2 -> {
                txtTentativaDig2.setBackgroundResource(R.drawable.border_selection)
                textviewTentativaSelecionado = 2
            }
            R.id.txt_tentativaDig3 -> {
                txtTentativaDig3.setBackgroundResource(R.drawable.border_selection)
                textviewTentativaSelecionado = 3
            }
            R.id.txt_tentativaDig4 -> {
                txtTentativaDig4.setBackgroundResource(R.drawable.border_selection)
                textviewTentativaSelecionado = 4
            }
            R.id.txt_tentativaDig5 -> {
                txtTentativaDig5.setBackgroundResource(R.drawable.border_selection)
                textviewTentativaSelecionado = 5
            }
            else -> {
                textviewTentativaSelecionado = 0
            }
        }
    }

    fun focoProximoTextviewTentativa(){
        if (numeroCasas == 4){
            when(textviewTentativaSelecionado){
                0 -> textviewTentativa(txtTentativaDig1)
                1 -> textviewTentativa(txtTentativaDig2)
                2 -> textviewTentativa(txtTentativaDig3)
                3 -> textviewTentativa(txtTentativaDig4)
                4 -> textviewTentativa(txtTentativaDig1)
                else -> textviewTentativa(txtTentativaDig1)
            }
        } else{
            when(textviewTentativaSelecionado){
                0 -> textviewTentativa(txtTentativaDig1)
                1 -> textviewTentativa(txtTentativaDig2)
                2 -> textviewTentativa(txtTentativaDig3)
                3 -> textviewTentativa(txtTentativaDig4)
                4 -> textviewTentativa(txtTentativaDig5)
                5 -> textviewTentativa(txtTentativaDig1)
                else -> textviewTentativa(txtTentativaDig1)
            }
        }

    }

    //Clique em um dos botões numéricos
    fun btnNumeros(view: View){
        var textoBotao: String = ""

        textoBotao = when(view.id){
            R.id.btn0 -> "0"
            R.id.btn1 -> "1"
            R.id.btn2 -> "2"
            R.id.btn3 -> "3"
            R.id.btn4 -> "4"
            R.id.btn5 -> "5"
            R.id.btn6 -> "6"
            R.id.btn7 -> "7"
            R.id.btn8 -> "8"
            R.id.btn9 -> "9"
            else -> ""
        }

        when(textviewTentativaSelecionado){
            1 -> txtTentativaDig1.text = textoBotao
            2 -> txtTentativaDig2.text = textoBotao
            3 -> txtTentativaDig3.text = textoBotao
            4 -> txtTentativaDig4.text = textoBotao
            5 -> txtTentativaDig5.text = textoBotao
        }

        focoProximoTextviewTentativa()
    }

    fun btnNewAndTestGame(view: View){
        if (jogando){
            if (tentativa <= numeroTentativas){
                modificaTextoInformacao()
                
                //Verifica se possui todos os números para teste, se não não realiza o teste.
                if(possuiTodosDigitosTentativa()){
                    //Testa Senha
                    val n1 = txtTentativaDig1.text.toString().toInt()
                    val n2 = txtTentativaDig2.text.toString().toInt()
                    val n3 = txtTentativaDig3.text.toString().toInt()
                    val n4 = txtTentativaDig4.text.toString().toInt()

                    var nAcertos: Int = 0
                    if (numeroCasas == 4){
                        nAcertos = conferirSenha(n1, n2, n3, n4)
                    } else{
                        val n5 = txtTentativaDig5.text.toString().toInt()
                        nAcertos = conferirSenha(n1, n2, n3, n4, n5)
                    }

                    //Verifica se acertou todos os dígitos da senha
                    if(nAcertos == numeroCasas){
                        vitoria = true
                        fimJogo()
                    }

                    //Finalizia o jogo caso tenha esgotado o número de tentativas
                    tentativa++
                    if (tentativa > numeroTentativas){
                        fimJogo()
                    }


                    if (!jogoFinalizado){
                        //Limpa campos da tentativa de senha, deixar em branco
                        componentesLimpaTextviewTentativa()

                        //Texto muda para tentativa seguinte: 2, 3, 4, ...
                        componentesMudaTextoNumeroTentativa()

                        //Foco no primeiro campo da tentativa
                        textviewTentativaSelecionado = 0
                        focoProximoTextviewTentativa()
                    }
                } else{
                    txtInfo.text = getText(R.string.txt_infomar_todos_digitos_teste)
                }
            }
        } else{
            novoJogo()
        }
    }

    fun novoJogo(){
        //Reseta parâmetros iniciais
        tentativa = 1
        vitoria = false
        jogoFinalizado = false
        textviewTentativaSelecionado = 0

        //Gera nova senha e mostra senha bloqueada pro usuário.
        gerarSenha()

        //Limpa textos dos textviews da tentativa
        componentesLimpaTextviewTentativa()

        //Limpa textos dos históricos de tentativas
        componentesLimpaTextviewHistoricoTentativa()

        //Muda o texto do número da tentativa
        componentesMudaTextoNumeroTentativa()

        //Ativa componentes da tela.
        componentesHabilitaTextviewTentativa()
        componentesTextviewTentativaVisivel()

        txtTentativas.visibility = View.VISIBLE
        txtHistoricoTentativas.visibility = View.VISIBLE

        componentesHabilitaBotoesNumericos()

        //Troca texto de informações
        modificaTextoInformacao()

        //Foco no primeiro digito, textview tentativa
        //textviewTentativa(txtTentativaDig1)
        focoProximoTextviewTentativa()

        //Espera senha para teste.
        btnNovoAndTesteJogo.text =  getText(R.string.btn_testar_senha)
        jogando = true


        componentesEstadoBotoesConfiguracao()

    }

    private fun modificaTextoInformacao() {
        when(tentativa){
            1, 2 -> txtInfo.text = getText(R.string.txt_frase_efeito1)
            3 -> txtInfo.text = getText(R.string.txt_frase_efeito2)
            4 -> if (dificuldade == 1) txtInfo.text = getText(R.string.txt_frase_efeito4)
            5, 6 -> txtInfo.text = getText(R.string.txt_frase_efeito3)
            7, 8 -> txtInfo.text = getText(R.string.txt_frase_efeito2)
            9 -> txtInfo.text = getText(R.string.txt_frase_efeito4)
            else -> txtInfo.text = getText(R.string.txt_frase_efeito1)
        }
    }

    //FAZER FUNCIONAR PAR 5 CASAS
    fun conferirSenha(n1: Int, n2: Int, n3: Int, n4: Int, n5:Int = -1): Int{
        var acertos: Int = 0
        var quantidadePalpitePertence: Int = 0 //Auxilia para informar no modo difícil
        var tentativaHistorico: String
        var tentativaHistoricoFormatado: SpannableString

        //Texto tentativa para receber formatação de acerto
        if (numeroCasas == 4) {
            tentativaHistorico = "Tentativa $tentativa\n$n1 - $n2 - $n3 - $n4"
        } else{
            tentativaHistorico = "Tentativa $tentativa\n$n1 - $n2 - $n3 - $n4 - $n5"
        }
        tentativaHistoricoFormatado = tentativaHistorico.toSpannable() as SpannableString




        val palpite: IntArray = intArrayOf(n1, n2, n3, n4, n5)
        for (i in 0 until numeroCasas){
            if (senha.contains(palpite[i])){

                //Verifica se está na posição correta e
                //formata string para informar ao usuário sobre o palpite, conforme dificuldade escolhida.
                when(i){
                    0 -> {
                        if (palpite[i] == senha[i]){ //Verifica se está na posição correta.
                            Log.i("VerificaSenha", "Palpite Certo : ${palpite[i]}")
                            acertos++
                            if (dificuldade == 1){
                                tentativaHistoricoFormatado.setSpan(BackgroundColorSpan(getColor(R.color.indigo_900)), 12, 13, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                                tentativaHistoricoFormatado.setSpan(ForegroundColorSpan(getColor(R.color.white)), 12, 13, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                            }
                        } else { //Senão está na posição errada.
                            Log.i("VerificaSenha", "Palpite Pertence : ${palpite[i]}")
                            quantidadePalpitePertence++
                            if (dificuldade == 1){
                                tentativaHistoricoFormatado.setSpan(BackgroundColorSpan(getColor(R.color.amber_500)),12,13,Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                            }
                        }
                    }
                    1 -> {
                        if (palpite[i] == senha[i]){ //Verifica se está na posição correta.
                            Log.i("VerificaSenha", "Palpite Certo : ${palpite[i]}")
                            acertos++
                            if (dificuldade == 1){
                                tentativaHistoricoFormatado.setSpan(BackgroundColorSpan(getColor(R.color.indigo_900)), 16, 17, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                                tentativaHistoricoFormatado.setSpan(ForegroundColorSpan(getColor(R.color.white)), 16, 17, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                            }
                        } else { //Senão está na posição errada.
                            Log.i("VerificaSenha", "Palpite Pertence : ${palpite[i]}")
                            quantidadePalpitePertence++
                            if (dificuldade == 1){
                                tentativaHistoricoFormatado.setSpan(BackgroundColorSpan(getColor(R.color.amber_500)),16,17,Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                            }
                        }
                    }
                    2 -> {
                        if (palpite[i] == senha[i]){ //Verifica se está na posição correta.
                            Log.i("VerificaSenha", "Palpite Certo : ${palpite[i]}")
                            acertos++
                            if (dificuldade == 1){
                                tentativaHistoricoFormatado.setSpan(BackgroundColorSpan(getColor(R.color.indigo_900)), 20, 21, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                                tentativaHistoricoFormatado.setSpan(ForegroundColorSpan(getColor(R.color.white)), 20, 21, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                            }
                        } else { //Senão está na posição errada.
                            Log.i("VerificaSenha", "Palpite Pertence : ${palpite[i]}")
                            quantidadePalpitePertence++
                            if (dificuldade == 1){
                                tentativaHistoricoFormatado.setSpan(BackgroundColorSpan(getColor(R.color.amber_500)),20,21,Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                            }
                        }
                    }
                    3 -> {
                        if (palpite[i] == senha[i]){ //Verifica se está na posição correta.
                            Log.i("VerificaSenha", "Palpite Certo : ${palpite[i]}")
                            acertos++
                            if (dificuldade == 1){
                                tentativaHistoricoFormatado.setSpan(BackgroundColorSpan(getColor(R.color.indigo_900)), 24, 25, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                                tentativaHistoricoFormatado.setSpan(ForegroundColorSpan(getColor(R.color.white)), 24, 25, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                            }
                        } else { //Senão está na posição errada.
                            Log.i("VerificaSenha", "Palpite Pertence : ${palpite[i]}")
                            quantidadePalpitePertence++
                            if (dificuldade == 1){
                                tentativaHistoricoFormatado.setSpan(BackgroundColorSpan(getColor(R.color.amber_500)),24,25,Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                            }
                        }
                    }
                    4 -> {
                        if (palpite[i] == senha[i]){ //Verifica se está na posição correta.
                            Log.i("VerificaSenha", "Palpite Certo : ${palpite[i]}")
                            acertos++
                            if (dificuldade == 1){
                                tentativaHistoricoFormatado.setSpan(BackgroundColorSpan(getColor(R.color.indigo_900)), 28, 29, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                                tentativaHistoricoFormatado.setSpan(ForegroundColorSpan(getColor(R.color.white)), 28, 29, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                            }
                        } else { //Senão está na posição errada.
                            Log.i("VerificaSenha", "Palpite Pertence : ${palpite[i]}")
                            quantidadePalpitePertence++
                            if (dificuldade == 1){
                                tentativaHistoricoFormatado.setSpan(BackgroundColorSpan(getColor(R.color.amber_500)),28,29,Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                            }
                        }
                    }
                }
            } else{ //Palpite não pertence a senha
                if (dificuldade == 1){ //Fácil - Desabilita botão numérico
                    componentesDesabilitaBotoesNumericos(palpite[i])
                }
            }
        }



        //Desativa botões numéricos correspondentes ao palpite da senha.
        //Caso o número esteja na senha correta, o botão é ativado novamente.
        //Caso o número não esteja, o botão segue desativado.
        /*componentesDesabilitaBotoesNumericos(n1)
        componentesDesabilitaBotoesNumericos(n2)
        componentesDesabilitaBotoesNumericos(n3)
        componentesDesabilitaBotoesNumericos(n4)
        if(numeroCasas == 5) componentesDesabilitaBotoesNumericos(n5)*/


        /*for (i in 0 until numeroCasas){
            //Verifica se possui número na senha, em qualquer posição.
            if(senha[i] == n1 || senha[i] == n2 || senha[i] == n3 || senha[i] == n4 || senha[i] == n5){
                Log.i("TESTE_SENHA", "Posição Errada - Número igual : ${senha[i]}")

                //Formata o número correspondente para informar que está na senha, mas na posição incorreta.
                if (senha[i] == n1) {
                    tentativaHistoricoFormatado.setSpan(BackgroundColorSpan(getColor(R.color.purple_200)), 12, 13, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                    componentesHabilitaBotoesNumericos(n1)
                }
                if (senha[i] == n2) {
                    tentativaHistoricoFormatado.setSpan(BackgroundColorSpan(getColor(R.color.purple_200)), 16, 17, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                    componentesHabilitaBotoesNumericos(n2)
                }
                if (senha[i] == n3) {
                    tentativaHistoricoFormatado.setSpan(BackgroundColorSpan(getColor(R.color.purple_200)), 20, 21, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                    componentesHabilitaBotoesNumericos(n3)
                }
                if (senha[i] == n4) {
                    tentativaHistoricoFormatado.setSpan(BackgroundColorSpan(getColor(R.color.purple_200)), 24, 25, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                    componentesHabilitaBotoesNumericos(n4)
                }
                if (senha[i] == n5) {
                    tentativaHistoricoFormatado.setSpan(BackgroundColorSpan(getColor(R.color.purple_200)), 28, 29, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                    componentesHabilitaBotoesNumericos(n5)
                }

                //tentativaHistoricoFormatado.setSpan(ForegroundColorSpan(Color.BLUE), 12, 13, Spanned.SPAN_INCLUSIVE_INCLUSIVE)


                //Verifica se possui número na posição correta.
                if(senha[i] == n1 && i == 0){
                    Log.i("TESTE_SENHA", "Posição Certa - Número igual : ${senha[i]}")
                    tentativaHistoricoFormatado.setSpan(BackgroundColorSpan(Color.CYAN), 12, 13, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                    acertos++
                }
                if(senha[i] == n2 && i == 1){
                    Log.i("TESTE_SENHA", "Posição Certa - Número igual : ${senha[i]}")
                    tentativaHistoricoFormatado.setSpan(BackgroundColorSpan(Color.CYAN), 16, 17, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                    acertos++
                }
                if(senha[i] == n3 && i == 2){
                    Log.i("TESTE_SENHA", "Posição Certa - Número igual : ${senha[i]}")
                    tentativaHistoricoFormatado.setSpan(BackgroundColorSpan(Color.CYAN), 20, 21, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                    acertos++
                }
                if(senha[i] == n4 && i == 3){
                    Log.i("TESTE_SENHA", "Posição Certa - Número igual : ${senha[i]}")
                    tentativaHistoricoFormatado.setSpan(BackgroundColorSpan(Color.CYAN), 24, 25, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                    acertos++
                }
                if(numeroCasas == 5){
                    if(senha[i] == n5 && i == 4){
                        Log.i("TESTE_SENHA", "Posição Certa - Número igual : ${senha[i]}")
                        tentativaHistoricoFormatado.setSpan(BackgroundColorSpan(Color.CYAN), 28, 29, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                        acertos++
                    }
                }

            }
        }*/

        //Termina formatação do histórico da tentativa para modo difícil
        if (dificuldade == 3) {
            tentativaHistorico += " --- $acertos | $quantidadePalpitePertence"
            tentativaHistoricoFormatado = tentativaHistorico.toSpannable() as SpannableString
            if (numeroCasas == 4){
                if (tentativa < 10){
                    //Números e posição corretos.
                    tentativaHistoricoFormatado.setSpan(BackgroundColorSpan(getColor(R.color.indigo_900)), 30, 31, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                    tentativaHistoricoFormatado.setSpan(ForegroundColorSpan(getColor(R.color.white)), 30, 31, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                    //Números corretos, mas posição errada.
                    tentativaHistoricoFormatado.setSpan(BackgroundColorSpan(getColor(R.color.amber_500)),34,35,Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                } else{
                    //Números e posição corretos.
                    tentativaHistoricoFormatado.setSpan(BackgroundColorSpan(getColor(R.color.indigo_900)), 31, 32, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                    tentativaHistoricoFormatado.setSpan(ForegroundColorSpan(getColor(R.color.white)), 31, 32, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                    //Números corretos, mas posição errada.
                    tentativaHistoricoFormatado.setSpan(BackgroundColorSpan(getColor(R.color.amber_500)),35,36,Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                }
            } else { //5 Casas
                if (tentativa < 10){
                    //Números e posição corretos.
                    tentativaHistoricoFormatado.setSpan(BackgroundColorSpan(getColor(R.color.indigo_900)), 34, 35, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                    tentativaHistoricoFormatado.setSpan(ForegroundColorSpan(getColor(R.color.white)), 34, 35, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                    //Números corretos, mas posição errada.
                    tentativaHistoricoFormatado.setSpan(BackgroundColorSpan(getColor(R.color.amber_500)),38,39,Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                } else{
                    //Números e posição corretos.
                    tentativaHistoricoFormatado.setSpan(BackgroundColorSpan(getColor(R.color.indigo_900)), 35, 36, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                    tentativaHistoricoFormatado.setSpan(ForegroundColorSpan(getColor(R.color.white)), 35, 36, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                    //Números corretos, mas posição errada.
                    tentativaHistoricoFormatado.setSpan(BackgroundColorSpan(getColor(R.color.amber_500)),39,40,Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                }
            }
        }

        when(tentativa){
            1 -> txtHistorico1.text = tentativaHistoricoFormatado
            2 -> txtHistorico2.text = tentativaHistoricoFormatado
            3 -> txtHistorico3.text = tentativaHistoricoFormatado
            4 -> txtHistorico4.text = tentativaHistoricoFormatado
            5 -> txtHistorico5.text = tentativaHistoricoFormatado
            6 -> txtHistorico6.text = tentativaHistoricoFormatado
            7 -> txtHistorico7.text = tentativaHistoricoFormatado
            8 -> txtHistorico8.text = tentativaHistoricoFormatado
            9 -> txtHistorico9.text = tentativaHistoricoFormatado
            10 -> txtHistorico10.text = tentativaHistoricoFormatado
            else -> txtInfo.text = getString(R.string.msg_tentativa_texto_formatado)
        }

        return acertos
    }

    fun configuracoesIniciais(){
        //Mostra itens em branco e botões desativados.
        //Apenas botão novo jogo e de configurações do jogo disponíveis.
        //Aqui carrega as estatísticas e informações.
        componentesDesabilitaTextviewTentativa()

        componentesTextviewTentativaInvisivel()

        txtTentativas.visibility = View.INVISIBLE
        txtHistoricoTentativas.visibility = View.INVISIBLE

        componentesDesabilitaBotoesNumericos()
        componentesLimpaTextviewHistoricoTentativa()
        componentesLimpaTextviewTentativa()
        componentesLimpaTextviewSenha()

        btnNovoAndTesteJogo.text = getText(R.string.btn_novo_jogo)
        jogando = false

        componentesEstadoBotoesConfiguracao()

        txtInfo.text = getText(R.string.txt_clique_novo_jogo)

    }

    fun fimJogo(){
        jogoFinalizado = true
        //informa se ganhou ou perdeu.
        //txtInfo.setTypeface(null, Typeface.BOLD)
        //txtInfo.setTextColor(getColor(R.color.indigo_900))
        //txtInfo.setBackgroundColor(getColor(R.color.amber_500))
        if (vitoria){
            txtInfo.text = getText(R.string.txt_vitoria)
        } else {
            txtInfo.text = getText(R.string.txt_derrota)
        }

        //Mostra senha correta.
        mostrarSenha()

        //Desativa componentes e ativa outros.
        componentesTextviewTentativaBorderNormal()
        componentesDesabilitaTextviewTentativa()
        componentesDesabilitaBotoesNumericos()


        //Muda nome e Espera click no botão de novo jogo.
        btnNovoAndTesteJogo.text = getText(R.string.btn_novo_jogo)
        jogando = false


        componentesEstadoBotoesConfiguracao()
    }

    fun gerarSenha(){
        var numero: Int
        var numeroAceito: Boolean = false

        if (numerosDistintos){
            for (i in 0 until numeroCasas){
                numeroAceito = false
                do {
                    numero = proximoNumero()
                    if (!senha.contains(numero)) {
                        senha[i] = numero
                        numeroAceito = true
                    }
                } while (!numeroAceito)
            }
        } else{
            for (i in 0 until numeroCasas){
                senha[i] = proximoNumero()
            }
        }



/*        if(numerosDistintos){
            for (i in 0 until numeroCasas){
                when(i){
                    0 -> senha[i] = proximoNumero()
                    1 -> do {
                        senha[i] = proximoNumero()
                    } while (senha[i] == senha[i-1])
                    2 -> do {
                        senha[i] = proximoNumero()
                    } while(senha[i] == senha[i-1] || senha[i] == senha[i-2])
                    3 -> do {
                        senha[i] = proximoNumero()
                    } while(senha[i] == senha[i-1] || senha[i] == senha[i-2] || senha[i] == senha[i-3])
                    else -> do {
                        senha[i] = proximoNumero()
                    } while(senha[i] == senha[i-1] || senha[i] == senha[i-2] || senha[i] == senha[i-3] || senha[i] == senha[i-4])
                }
            }
        } else{
            for (i in 0 until numeroCasas){
                senha[i] = proximoNumero()
            }
        }*/

        //Deixar a senha escondida
        esconderSenha()
    }

    fun esconderSenha(){
        txtSenhaDig1.setText("*")
        txtSenhaDig2.setText("*")
        txtSenhaDig3.setText("*")
        txtSenhaDig4.setText("*")
        if(numeroCasas == 5){
            txtSenhaDig5.setText("*")
        }
    }

    fun mostrarSenha(){
        txtSenhaDig1.setText(senha[0].toString())
        txtSenhaDig2.setText(senha[1].toString())
        txtSenhaDig3.setText(senha[2].toString())
        txtSenhaDig4.setText(senha[3].toString())
        if(numeroCasas == 5){
            txtSenhaDig5.setText(senha[4].toString())
        }
    }

    private fun exibirRegrasJogo(cliqueNoBotao: Boolean) {
        try {
            val builder = AlertDialog.Builder(this)
                .setView(R.layout.dialog_regras_jogo)


            val dialog: AlertDialog? = builder.create()
            dialog?.show()

            val dialogCheckNaoMostrarNovamente = dialog?.findViewById<CheckBox>(R.id.checkBox_dialog_mostrar_novamente)
            val dialogTxtExemploJogo = dialog?.findViewById<TextView>(R.id.txt_dialog_exemplos_jogo)
            val dialogTxtDificuldadeAtual = dialog?.findViewById<TextView>(R.id.txt_dialog_dificuldade_resultado)
            val dialogBtnFechar = dialog?.findViewById<Button>(R.id.btn_dialog_fechar)

            // Se exibiu o dialog através do clique do botão "Ajuda", esconde o checkbox "Não mostrar novamente".
            // Se exibiu o dialog após iniciar o app, mostra o checkbox "Não mostrar novamente".
            // Caso o usuário tenha marcado o checkbox anteriormente, após iniciar o app, o dialog não será exibido.
            //      Só será possível exibir o dialog através do botão "Ajuda"
            if (cliqueNoBotao){
                dialogCheckNaoMostrarNovamente?.visibility = View.GONE
            } else{
                dialogCheckNaoMostrarNovamente?.visibility = View.VISIBLE
            }

            //Informa qual dificuldade está selecionada nas configurações
            when(dificuldade){
                1 -> dialogTxtDificuldadeAtual?.text = getText(R.string.radiobutton_facil)
                3 -> dialogTxtDificuldadeAtual?.text = getText(R.string.radiobutton_dificil)
                else -> dialogTxtDificuldadeAtual?.text = " ---- "
            }

            // Auxiliar para formatação do texto a ser mostrado.
            /*val textoFormatado: SpannableString
            textoFormatado = formataTextoExemploJogo()
            dialogTxtExemploJogo?.text = textoFormatado*/
            dialogTxtExemploJogo?.text = formataTextoExemploJogo()

            dialogBtnFechar?.setOnClickListener {
                if (dialogCheckNaoMostrarNovamente?.isChecked == true){
                    editor = sharedPreferences.edit()
                    editor.putBoolean(getString(R.string.pref_config_exibe_regras_jogo), false)
                    editor.apply()
                }
                dialog.cancel()
            }

        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    //Exibido no AlertDialog Regras do Jogo
    private fun formataTextoExemploJogo(): SpannableString{
        var textoDicaJogoFormatado: SpannableString = "".toSpannable() as SpannableString

        if (dificuldade == 1){ //Dificuldade Fácil
            textoDicaJogoFormatado = getText(R.string.txt_config_regras_jogo_facil).toSpannable() as SpannableString
            textoDicaJogoFormatado.setSpan(StyleSpan(Typeface.BOLD), 0, 32, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
            textoDicaJogoFormatado.setSpan(BackgroundColorSpan(getColor(R.color.amber_500)), 52, 53, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
            textoDicaJogoFormatado.setSpan(BackgroundColorSpan(getColor(R.color.indigo_900)), 60, 61, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
            textoDicaJogoFormatado.setSpan(ForegroundColorSpan(getColor(R.color.white)), 60, 61, Spanned.SPAN_INCLUSIVE_INCLUSIVE)

            textoDicaJogoFormatado.setSpan(BackgroundColorSpan(getColor(R.color.amber_500)), 64, 65, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
            textoDicaJogoFormatado.setSpan(BackgroundColorSpan(getColor(R.color.indigo_900)), 106, 107, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
            textoDicaJogoFormatado.setSpan(ForegroundColorSpan(getColor(R.color.white)), 106, 107, Spanned.SPAN_INCLUSIVE_INCLUSIVE)

        } else if (dificuldade == 3) { //Dificuldade Difícil
            textoDicaJogoFormatado = getText(R.string.txt_config_regras_jogo_dificil).toSpannable() as SpannableString
            textoDicaJogoFormatado.setSpan(StyleSpan(Typeface.BOLD), 0, 34, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
            textoDicaJogoFormatado.setSpan(BackgroundColorSpan(getColor(R.color.indigo_900)), 68, 69, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
            textoDicaJogoFormatado.setSpan(ForegroundColorSpan(getColor(R.color.white)), 68, 69, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
            textoDicaJogoFormatado.setSpan(BackgroundColorSpan(getColor(R.color.amber_500)), 72, 73, Spanned.SPAN_INCLUSIVE_INCLUSIVE)

            textoDicaJogoFormatado.setSpan(BackgroundColorSpan(getColor(R.color.indigo_900)), 76, 77, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
            textoDicaJogoFormatado.setSpan(ForegroundColorSpan(getColor(R.color.white)), 76, 77, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
            textoDicaJogoFormatado.setSpan(BackgroundColorSpan(getColor(R.color.amber_500)), 128, 129, Spanned.SPAN_INCLUSIVE_INCLUSIVE)

        }

        return textoDicaJogoFormatado
    }

    // Retorna um número aleatório entre 0 e 9
    fun proximoNumero(): Int{
        return Random.nextInt(0, 10)
    }

    fun possuiTodosDigitosTentativa(): Boolean{
        var todosDigitos = true

        if(txtTentativaDig1.text.isNullOrEmpty()) todosDigitos = false
        if(txtTentativaDig2.text.isNullOrEmpty()) todosDigitos = false
        if(txtTentativaDig3.text.isNullOrEmpty()) todosDigitos = false
        if(txtTentativaDig4.text.isNullOrEmpty()) todosDigitos = false
        if (numeroCasas == 5) {
            if (txtTentativaDig5.text.isNullOrEmpty()) todosDigitos = false
        }

        return todosDigitos
    }

    //Quinto dígito visível ou não
    fun componentesQuintoDigito(){
        configuracoesIniciais()

        //Redefine variável senha para 4 ou 5 casas
        senha = IntArray(numeroCasas)

        if(numeroCasas == 4){
            txtSenhaDig5.visibility = View.GONE
            txtTentativaDig5.visibility = View.GONE
        } else{
            txtSenhaDig5.visibility = View.VISIBLE
            if(jogando){
                txtTentativaDig5.visibility = View.VISIBLE
            } else{
                txtTentativaDig5.visibility = View.INVISIBLE
            }

        }
    }

    fun componentesEstadoBotoesConfiguracao(){
        if (jogando){
            btnReset.isEnabled = true
            btnReset.visibility = View.VISIBLE
        } else{
            btnReset.isEnabled = false
            btnReset.visibility = View.INVISIBLE
        }
    }

    fun componentesLimpaTextviewSenha(){
        txtSenhaDig1.text = ""
        txtSenhaDig2.text = ""
        txtSenhaDig3.text = ""
        txtSenhaDig4.text = ""
        txtSenhaDig5.text = ""
    }

    fun componentesMudaTextoNumeroTentativa() {
        when(tentativa){
            1 -> txtTentativas.text = "${getText(R.string.txt_tentativa)} ${getText(R.string.txt_1)} de $numeroTentativas"
            2 -> txtTentativas.text = "${getText(R.string.txt_tentativa)} ${getText(R.string.txt_2)} de $numeroTentativas"
            3 -> txtTentativas.text = "${getText(R.string.txt_tentativa)} ${getText(R.string.txt_3)} de $numeroTentativas"
            4 -> txtTentativas.text = "${getText(R.string.txt_tentativa)} ${getText(R.string.txt_4)} de $numeroTentativas"
            5 -> txtTentativas.text = "${getText(R.string.txt_tentativa)} ${getText(R.string.txt_5)} de $numeroTentativas"
            6 -> txtTentativas.text = "${getText(R.string.txt_tentativa)} ${getText(R.string.txt_6)} de $numeroTentativas"
            7 -> txtTentativas.text = "${getText(R.string.txt_tentativa)} ${getText(R.string.txt_7)} de $numeroTentativas"
            8 -> txtTentativas.text = "${getText(R.string.txt_tentativa)} ${getText(R.string.txt_8)} de $numeroTentativas"
            9 -> txtTentativas.text = "${getText(R.string.txt_tentativa)} ${getText(R.string.txt_9)} de $numeroTentativas"
            10 -> txtTentativas.text = "${getText(R.string.txt_tentativa)} ${getText(R.string.txt_10)} de $numeroTentativas"
            else -> txtTentativas.text = "${getText(R.string.txt_tentativa)} ${getText(R.string.txt_0)} de $numeroTentativas"
        }
    }

    fun componentesLimpaTextviewTentativa() {
        txtTentativaDig1.text = ""
        txtTentativaDig2.text = ""
        txtTentativaDig3.text = ""
        txtTentativaDig4.text = ""
        txtTentativaDig5.text = ""
    }

    fun componentesDesabilitaBotoesNumericos(n: Int = -1) {
        when(n) {
            0 -> btn0.isEnabled = false
            1 -> btn1.isEnabled = false
            2 -> btn2.isEnabled = false
            3 -> btn3.isEnabled = false
            4 -> btn4.isEnabled = false
            5 -> btn5.isEnabled = false
            6 -> btn6.isEnabled = false
            7 -> btn7.isEnabled = false
            8 -> btn8.isEnabled = false
            9 -> btn9.isEnabled = false
            else -> {
                btn0.isEnabled = false
                btn1.isEnabled = false
                btn2.isEnabled = false
                btn3.isEnabled = false
                btn4.isEnabled = false
                btn5.isEnabled = false
                btn6.isEnabled = false
                btn7.isEnabled = false
                btn8.isEnabled = false
                btn9.isEnabled = false
            }
        }
    }

    fun componentesHabilitaBotoesNumericos(n: Int = -1) {
        when(n){
            0 -> btn0.isEnabled = true
            1 -> btn1.isEnabled = true
            2 -> btn2.isEnabled = true
            3 -> btn3.isEnabled = true
            4 -> btn4.isEnabled = true
            5 -> btn5.isEnabled = true
            6 -> btn6.isEnabled = true
            7 -> btn7.isEnabled = true
            8 -> btn8.isEnabled = true
            9 -> btn9.isEnabled = true
            else -> {
                btn0.isEnabled = true
                btn1.isEnabled = true
                btn2.isEnabled = true
                btn3.isEnabled = true
                btn4.isEnabled = true
                btn5.isEnabled = true
                btn6.isEnabled = true
                btn7.isEnabled = true
                btn8.isEnabled = true
                btn9.isEnabled = true
            }
        }
    }

    fun componentesDesabilitaTextviewTentativa(){
        txtTentativaDig1.isEnabled = false
        txtTentativaDig2.isEnabled = false
        txtTentativaDig3.isEnabled = false
        txtTentativaDig4.isEnabled = false
        txtTentativaDig5.isEnabled = false
    }

    fun componentesHabilitaTextviewTentativa(){
        txtTentativaDig1.isEnabled = true
        txtTentativaDig2.isEnabled = true
        txtTentativaDig3.isEnabled = true
        txtTentativaDig4.isEnabled = true
        txtTentativaDig5.isEnabled = true
    }

    // Coloca a borda normal em todos os textviews
    fun componentesTextviewTentativaBorderNormal(){
        txtTentativaDig1.setBackgroundResource(R.drawable.border_normal)
        txtTentativaDig2.setBackgroundResource(R.drawable.border_normal)
        txtTentativaDig3.setBackgroundResource(R.drawable.border_normal)
        txtTentativaDig4.setBackgroundResource(R.drawable.border_normal)
        txtTentativaDig5.setBackgroundResource(R.drawable.border_normal)

        //Reseta textview selecionado para nenhum
        textviewTentativaSelecionado = 0
    }

    fun componentesTextviewTentativaInvisivel(){
        txtTentativaDig1.visibility = View.INVISIBLE
        txtTentativaDig2.visibility = View.INVISIBLE
        txtTentativaDig3.visibility = View.INVISIBLE
        txtTentativaDig4.visibility = View.INVISIBLE
        txtTentativaDig5.visibility = View.INVISIBLE
    }

    fun componentesTextviewTentativaVisivel(){
        txtTentativaDig1.visibility = View.VISIBLE
        txtTentativaDig2.visibility = View.VISIBLE
        txtTentativaDig3.visibility = View.VISIBLE
        txtTentativaDig4.visibility = View.VISIBLE
        if(numeroCasas == 5){
            txtTentativaDig5.visibility = View.VISIBLE
        } else{
            txtTentativaDig5.visibility = View.GONE
        }
    }

    fun componentesLimpaTextviewHistoricoTentativa(){
        txtHistorico1.text = ""
        txtHistorico2.text = ""
        txtHistorico3.text = ""
        txtHistorico4.text = ""
        txtHistorico5.text = ""
        txtHistorico6.text = ""
        txtHistorico7.text = ""
        txtHistorico8.text = ""
        txtHistorico9.text = ""
        txtHistorico10.text = ""
    }

}