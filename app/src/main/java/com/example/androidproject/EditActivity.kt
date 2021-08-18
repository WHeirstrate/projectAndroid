package com.example.androidproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

class EditActivity : AppCompatActivity() {

    lateinit var db: FirebaseFirestore
    lateinit var firebaseId: String
    lateinit var selectedUser: User
    private var createNewUser: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_activity)

        val saveButton: View = findViewById(R.id.saveUserButton)
        val intent = intent
        createNewUser = intent.getBooleanExtra("createNewUser", true)
        if (!createNewUser) {
            firebaseId = intent.getStringExtra("firebaseId").toString()
            getSingleUser()
        }
        saveButton.setOnClickListener {
            saveOnButtonClick()
        }
    }

    private fun getSingleUser() {
        lateinit var user: User

        db = FirebaseFirestore.getInstance()
        db.collection("users").document(firebaseId).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    user = document.toObject(User::class.java)!!
                    addDataToView(user)
                } else {
                    Log.e("oeps", "no doc found")
                }
            }.addOnFailureListener { exception ->
                Log.e("oeps", "get failed with", exception)
            }
    }

    private fun addDataToView(user: User) {
        val firstName: TextView = findViewById(R.id.editUserFirstName)
        val lastName: TextView = findViewById(R.id.editUserLastName)
        val street: TextView = findViewById(R.id.editUserStreet)
        val streetNr: TextView = findViewById(R.id.editUserStreetNr)
        val postal: TextView = findViewById(R.id.editUserPostal)
        val city: TextView = findViewById(R.id.editUserCity)

        firstName.text = user.firstName
        lastName.text = user.lastName
        street.text = user.street
        streetNr.text = user.streetNr.toString()
        postal.text = user.postalCode.toString()
        city.text = user.city

        selectedUser = user
    }

    private fun saveOnButtonClick() {

        val newUserCity = findViewById<EditText>(R.id.editUserCity).text.toString()
        val firstName = findViewById<EditText>(R.id.editUserFirstName).text.toString()
        val lastName = findViewById<EditText>(R.id.editUserLastName).text.toString()
        val postal = findViewById<EditText>(R.id.editUserPostal).text.toString()
        val street = findViewById<EditText>(R.id.editUserStreet).text.toString()
        val streetNr = findViewById<EditText>(R.id.editUserStreetNr).text.toString()

        val newUser = User(
            city = newUserCity,
            firstName = firstName,
            lastName = lastName,
            postalCode = postal,
            street = street,
            streetNr = streetNr
        )
        Log.d("oops", "$newUser")
        db = FirebaseFirestore.getInstance()
        if (!createNewUser) {
            db.collection("users").document(firebaseId)
                .set(newUser)
                .addOnSuccessListener { docRef ->
                    Log.d("oops", " succes $docRef")
                    Toast.makeText(this@EditActivity, R.string.succesEdit, Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { exception ->
                    Log.e("oops", "Error adding document", exception)
                    Toast.makeText(this@EditActivity, R.string.failureEdit, Toast.LENGTH_SHORT).show()

                }
        } else {
            db.collection("users")
                .add(newUser)
                .addOnSuccessListener { docRef ->
                    Log.d("oops", " succes $docRef")
                    Toast.makeText(this@EditActivity, R.string.succesAdd, Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { exception ->
                    Log.e("oops", "Error adding document", exception)
                    Toast.makeText(this@EditActivity, R.string.failureEdit, Toast.LENGTH_SHORT).show()
                }
        }
    }
}