package garcia.andrea.firebaselogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import garcia.andrea.firebaselogin.databinding.ActivitySignUpBinding
import java.util.regex.Pattern

class SignUpActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding:ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
        auth = Firebase.auth

        binding.signUpButton.setOnClickListener {
            val mEmail = binding.emailEditText.text.toString()
            val mPassword = binding.passwordEditText.text.toString()
            val mRepeatPassword = binding.repeatPasswordEditText.text.toString()
            val passwordRegex = Pattern.compile("^" +
                "(?=.*[-@#$%^&+=])" + // Al menos 1 carácter especial
                ".{6,}" + //Al menos 4 carácters
                "$" )

            if (mEmail.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()) {
                Toast.makeText(this, "Ingrese un email valido.",
                    Toast.LENGTH_SHORT).show()
            } else if (mPassword.isEmpty() || !passwordRegex.matcher(mPassword).matches()) {
                Toast.makeText(this, "La contraseña es debil.",
                    Toast.LENGTH_SHORT).show()
            } else if (mPassword != mRepeatPassword) {
                Toast.makeText(this, "Confirma la contraseña.",
                    Toast.LENGTH_SHORT).show()
            } else {
                createAccount(mEmail, mPassword)
            }
        }
        binding.backImageView.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

    }

    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Correo y contraseña registrados.",
                        Toast.LENGTH_SHORT).show()

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("TAG", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Correo o contraseña incorrectos.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }
}