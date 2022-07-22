package com.fleichtweis.mastersenha

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.text.toSpannable
import org.w3c.dom.Text
import kotlin.random.Random

enum class Dificuldade{
    FACIL, //Informa se o número testado está correto e na casa certa. Desativa botões testados que não estão na senha.
    NORMAL,
    DIFICIL //Informa quantos números estão corretos e quantos estão na casa certa, mas não informa qual número é.
}

class MainActivity : AppCompatActivity() {

    // Variáveis de configurações
    var numeroCasas: Int = 4
    var numeroTentativas: Int = 6
    var numerosDistintos: Boolean = true
    var dificuldade: Dificuldade = Dificuldade.FACIL

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
    lateinit var btnConfiguracoes: ImageButton
    lateinit var btnReset: ImageButton
    lateinit var switchNumerosDistintos: Switch
    lateinit var radioBtn4Numeros: RadioButton
    lateinit var radioBtn5Numeros: RadioButton

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
    lateinit var txtNumeroTentativa: TextView
    lateinit var txtHistoricoTentativas: TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnNovoAndTesteJogo = findViewById(R.id.btn_newAndTestGame)
        btnConfiguracoes = findViewById(R.id.imageBtnConfig)

        btnConfiguracoes.visibility = View.INVISIBLE

        btnReset = findViewById(R.id.imageBtnReset)
        radioBtn4Numeros = findViewById(R.id.radioBtn4numeros)
        radioBtn5Numeros = findViewById(R.id.radioBtn5numeros)
        switchNumerosDistintos = findViewById(R.id.switch_numerosDistintos)

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
        txtNumeroTentativa = findViewById(R.id.txt_numTentativa)
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


        switchNumerosDistintos.setOnClickListener {
            numerosDistintos = switchNumerosDistintos.isChecked
        }

        radioBtn4Numeros.setOnClickListener {
            numeroCasas = 4
            componentesQuintoDigito()
        }

        radioBtn5Numeros.setOnClickListener {
            numeroCasas = 5
            componentesQuintoDigito()
        }

        btnReset.setOnClickListener {
            //MOSTRAR MENSAGEM SE REALMENTE DESEJA ENCERRAR O JOGO ATUAL

            fimJogo()
        }

        //Função já chama as configurações iniciais
        componentesQuintoDigito()
        //configuracoesIniciais()



    }


    //Clique em um dos textviews da tentativa de senha
    fun textviewTentativa(view: View){
        componentesTextviewTentativaBorderNormal()
        when(view.id){
            R.id.txt_tentativaDig1 -> {
                txtTentativaDig1.setBackgroundResource(R.drawable.border_selection)
                textviewTentativaSelecionado = 1
            }
            R.id.txt_tentativaDig2 -> {
                //txtTentativaDig2.setBackgroundColor(R.color.marrom)
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
                //textviewTentativaBorderNormal()
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

    }

    fun btnNewAndTestGame(view: View){
        if (jogando){
            if (tentativa <= numeroTentativas){
                txtInfo.text = getText(R.string.txt_frase_efeito1)
                
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

                    //
                    tentativa++
                    if (tentativa > numeroTentativas){
                        fimJogo()
                    }


                    //MODO FACIL = Desativa botões que não estão corretos, que já foi testado e não existe o digito na senha.




                    if (!jogoFinalizado){
                        //Limpa campos da tentativa de senha, deixar em branco
                        componentesLimpaTextviewTentativa()

                        //Texto muda para tentativa seguinte: 2, 3, 4, ...
                        componentesMudaTextoNumeroTentativa()
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
        txtNumeroTentativa.visibility = View.VISIBLE
        txtHistoricoTentativas.visibility = View.VISIBLE

        componentesHabilitaBotoesNumericos()

        //Troca texto de informações
        txtInfo.text = getText(R.string.txt_frase_efeito1)

        //Foco no primeiro digito, textview tentativa


        //Espera senha para teste.
        btnNovoAndTesteJogo.text =  getText(R.string.btn_testar_senha)
        jogando = true


        componentesEstadoBotoesConfiguracao()

    }

    //FAZER FUNCIONAR PAR 5 CASAS
    fun conferirSenha(n1: Int, n2: Int, n3: Int, n4: Int, n5:Int = -1): Int{
        var acertos: Int = 0
        val tentativaHistorico: String
        val tentativaHistoricoFormatado: SpannableString

        //Texto tentativa para receber formatação de acerto
        if (numeroCasas == 4) {
            tentativaHistorico = "Tentativa $tentativa\n$n1 - $n2 - $n3 - $n4"
        } else{
            tentativaHistorico = "Tentativa $tentativa\n$n1 - $n2 - $n3 - $n4 - $n5"
        }
        tentativaHistoricoFormatado = tentativaHistorico.toSpannable() as SpannableString


        //Desativa botões numéricos correspondentes ao palpite da senha.
        //Caso o número esteja na senha correta, o botão é ativado novamente.
        //Caso o número não esteja, o botão segue desativado.
        componentesDesabilitaBotoesNumericos(n1)
        componentesDesabilitaBotoesNumericos(n2)
        componentesDesabilitaBotoesNumericos(n3)
        componentesDesabilitaBotoesNumericos(n4)
        if(numeroCasas == 5) componentesDesabilitaBotoesNumericos(n5)

        for (i in 0 until numeroCasas){
            //Verifica se possui número na senha, em qualquer posição.
            if(senha[i] == n1 || senha[i] == n2 || senha[i] == n3 || senha[i] == n4 || senha[i] == n5){
                Log.i("TESTE_SENHA", "Posição Errada - Número igual : ${senha[i]}")

                //Formata o número correspondente para informar que está na senha, mas na posição incorreta.
                if (senha[i] == n1) {
                    tentativaHistoricoFormatado.setSpan(BackgroundColorSpan(Color.YELLOW), 12, 13, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                    componentesHabilitaBotoesNumericos(n1)
                }
                if (senha[i] == n2) {
                    tentativaHistoricoFormatado.setSpan(BackgroundColorSpan(Color.YELLOW), 16, 17, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                    componentesHabilitaBotoesNumericos(n2)
                }
                if (senha[i] == n3) {
                    tentativaHistoricoFormatado.setSpan(BackgroundColorSpan(Color.YELLOW), 20, 21, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                    componentesHabilitaBotoesNumericos(n3)
                }
                if (senha[i] == n4) {
                    tentativaHistoricoFormatado.setSpan(BackgroundColorSpan(Color.YELLOW), 24, 25, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                    componentesHabilitaBotoesNumericos(n4)
                }
                if (senha[i] == n5) {
                    tentativaHistoricoFormatado.setSpan(BackgroundColorSpan(Color.YELLOW), 28, 29, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
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
            else -> txtInfo.text = "Não conseguimos informar o palpite formatado"
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
        txtNumeroTentativa.visibility = View.INVISIBLE
        txtHistoricoTentativas.visibility = View.INVISIBLE

        componentesDesabilitaBotoesNumericos()
        componentesLimpaTextviewHistoricoTentativa()
        componentesLimpaTextviewTentativa()
        componentesLimpaTextviewSenha()

        componentesEstadoBotoesConfiguracao()

        txtInfo.text = getText(R.string.txt_clique_novo_jogo)

    }

    fun fimJogo(){
        jogoFinalizado = true
        //informa se ganhou ou perdeu.
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
        if(numerosDistintos){
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
        }

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
            btnConfiguracoes.isEnabled = false
            switchNumerosDistintos.isEnabled = false
            radioBtn4Numeros.isEnabled = false
            radioBtn5Numeros.isEnabled = false
        } else{
            btnReset.isEnabled = false
            //btnConfiguracoes.isEnabled = true
            switchNumerosDistintos.isEnabled = true
            radioBtn4Numeros.isEnabled = true
            radioBtn5Numeros.isEnabled = true
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
            1 -> txtNumeroTentativa.text = getText(R.string.txt_1)
            2 -> txtNumeroTentativa.text = getText(R.string.txt_2)
            3 -> txtNumeroTentativa.text = getText(R.string.txt_3)
            4 -> txtNumeroTentativa.text = getText(R.string.txt_4)
            5 -> txtNumeroTentativa.text = getText(R.string.txt_5)
            6 -> txtNumeroTentativa.text = getText(R.string.txt_6)
            7 -> txtNumeroTentativa.text = getText(R.string.txt_7)
            8 -> txtNumeroTentativa.text = getText(R.string.txt_8)
            9 -> txtNumeroTentativa.text = getText(R.string.txt_9)
            10 -> txtNumeroTentativa.text = getText(R.string.txt_10)
            else -> txtNumeroTentativa.text = getText(R.string.txt_0)
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