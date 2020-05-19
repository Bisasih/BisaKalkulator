package bisasih.bisakalkulator.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import bisasih.bisakalkulator.R
import bisasih.bisakalkulator.main.MainActivity

class SplashActivity : AppCompatActivity() {

    /*
    * Fungsi yang pertama di panggil ketika activity telah terbuat
    * */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // menunggu selama 3 detik, lalu pindah ke activity selanjutnya
        Handler().postDelayed(
            Runnable {
                // pindah ke MainActivity
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)

                // menghapus activity ini
                finish()
            },
            3000
        )
    }
}
