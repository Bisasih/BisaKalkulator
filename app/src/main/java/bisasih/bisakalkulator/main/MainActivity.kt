package bisasih.bisakalkulator.main

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import bisasih.bisakalkulator.R
import kotlinx.android.synthetic.main.activity_main.*
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : AppCompatActivity() {
    // variable untuk mengecek apakah hasil error atau tidak
    var isError = false

    // variable untuk mengecek apakah sebelumnya sudah ada "." atau belum
    var hasPoint = false

    // variable untuk mengecek apakah yang terakhir merupakan angka atau bukan
    var isLastNumber = true

    // variable untuk memberitau apakah [tvMainInput] merupakan hasil perhitungan atau bukan
    var isResult = false

    /*
    * Fungsi yang pertama di panggil ketika activity telah terbuat
    * */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // menambahkan movement scroll pada textView
        tvMainInput.movementMethod = ScrollingMovementMethod()
    }

    /*
    * Fungsi yang dipanggil ketika button di klik
    * Memanggil berbagai fungsi berdasarkan button yang diklik
    * */
    fun onClick(view: View) {
        // merubah class View menjadi class Button
        val button = view as Button
        when (button.id) {
            R.id.btnMainClear -> {
                // ketika button clear di klik
                onClear()
            }
            R.id.btnMainDelete -> {
                // ketika button delete di klik
                onDelete()
                onCalculate()
            }
            R.id.btnMainPlus -> {
                // ketika button operator tambah di klik
                onOperator(button.text.toString())
            }
            R.id.btnMainMinus -> {
                // ketika button operator kurang di klik
                onOperator(button.text.toString())
            }
            R.id.btnMainDivide -> {
                // ketika button operator bagi di klik
                onOperator(button.text.toString())
            }
            R.id.btnMainMultiple -> {
                // ketika button operator kali di klik
                onOperator(button.text.toString())
            }
            R.id.btnMainEqual -> {
                // ketika button operator hasil di klik
                onResult()
            }
            R.id.btnMainPoint -> {
                // ketika button titik di klik
                onPoint()
            }
            else -> {
                // ketika button digit di klik
                onDigit(button.text.toString())
                onCalculate()
            }
        }
    }

    /*
    * Fungsi ketika button hapus di klik
    * digunakan untuk menghapus dan mereset semua inputan dan kondisi
    * */
    fun onClear() {
        // mereset text
        tvMainResult.text = ""
        tvMainInput.text = ""

        // mereset kondisi
        isError = false
        hasPoint = false
        isLastNumber = false
        isResult = false
    }

    /*
    * Fungsi ketika Operator di klik, untuk menambahkan [op] ke dalam [tvMainInput]
    * @param op merupakan text pada button operator
    * */
    fun onOperator(op: String) {
        val text = tvMainInput.text.toString()
        if (text.isNotEmpty() && isLastNumber && !isError) {
            // menambahkan [op] pada [tbMainInput]
            tvMainInput.append(" $op ")

            // mereset flag point dan result, serta memberitahu kalau yang terakhir bukan digit
            isLastNumber = false
            hasPoint = false
            isResult = false
        }
    }

    /*
    * Fungsi ketika digit di klik, akan memasukkan [value] ke [tvMainInput]
    * @property value merupakan text pada button yang di klik
    * */
    fun onDigit(value: String) {
        if (isResult) {
            // mengganti hasil result dengan digit baru
            tvMainInput.text = value
            isResult = false
            isError = false
        } else {
            // menambahkan [value] ke dalam [tvMainInput]
            tvMainInput.append(value)
        }
        isLastNumber = true
    }

    /*
    * Fungsi ketika Point (dot) di klik, akan memasukkan dot desimal "." ke [tvMainInput]
    * */
    fun onPoint() {
        if (!hasPoint && !isResult) {
            if (tvMainInput.text.isEmpty() or !isLastNumber) {
                // ketika text masih kosong menambahkan karakter "0" di depan "."
                tvMainInput.append("0.")
            } else {
                // ketika input text tidak kosong
                tvMainInput.append(".")
            }
            isLastNumber = false
            hasPoint = true
        }
    }

    /*
    * Fungsi ketika tombol delete di klik, akan menghapus karakter terakhir pada [tvMainInput]
    * */
    fun onDelete() {
        val lastText = tvMainInput.text.toString()
        if (lastText.isNotEmpty() && !isError) {
            // menghapus karakter terakhir dari text (lastText[0..N-1])
            tvMainInput.text = lastText.substring(0, lastText.length - 1)
        }
    }

    /*
    * Ketika tombol equal (=) di klik, akan memindahkan text [tvMainResult] ke [tvMainInput]
    * */
    fun onResult() {
        if (tvMainResult.text.isNotEmpty()) {
            isResult = true
            if (tvMainResult.text.equals("Error!")) {
                // memindahkan text "Error!" ke [tvMainInput]
                tvMainInput.text = tvMainResult.text.substring(0, tvMainResult.text.length)
                isError = true
            } else {
                // memindahkan hasil perhitungan ke [tvMainInput] tanpa "= " agar dapat dilakukan perhitungan lanjutan
                tvMainInput.text = tvMainResult.text.substring(2, tvMainResult.text.length) + " "
            }
            tvMainResult.text = ""
        }
    }

    /*
    * Fungsi untuk menghitung seluruh operasi pada [tvMainInput] dan ditampilkan di [tvMainResult]
    * digunakan pada fungsi [onClick] pada button dengan id [btnMainDelete] dan [btnMainNum...]
    * */
    fun onCalculate() {
        val input = tvMainInput.text.toString()
        if (input.isNotEmpty() && isLastNumber && !isError) {
            try {
                // melakukan reformat inputan dengan mengganti "÷" dengan "/" dan "×" dengan "*"
                var newInput = input.replace("÷", "/").replace("×", "*")

                // melakukan perhitungan dengan library ExpressionBuilder
                val expressionBuilder = ExpressionBuilder(newInput).build()
                val result = expressionBuilder.evaluate()

                // memasukkan hasil ke [tvMainResult]
                tvMainResult.text = "= $result"
            } catch (e: ArithmeticException) {
                // ketika terjadi kesalahan perhitungan
                tvMainResult.text = "Error!"
            } catch (e: Exception) {
                // ketika perhitungan tidak dapat di proses ( invalid expression )
                print(e.message)
            }
        }
    }
}
