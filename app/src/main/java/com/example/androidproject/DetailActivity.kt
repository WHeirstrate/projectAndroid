package com.example.androidproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

class DetailActivity : AppCompatActivity() {

    lateinit var db: FirebaseFirestore
    lateinit var firebaseId: String
    lateinit var selectedUser: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_activity)
        val editUserButton = findViewById<Button>(R.id.editUserButton)

        val intent = intent
        firebaseId = intent.getStringExtra("firebaseId").toString()
        getSingleUser()

        editUserButton.setOnClickListener {
            editOnButtonClick()
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
        val firstName: TextView = findViewById(R.id.detailUserFirstName)
        val lastName: TextView = findViewById(R.id.detailUserLastName)
        val street: TextView = findViewById(R.id.detailUserStreet)
        val streetNr: TextView = findViewById(R.id.detailUserStreetNr)
        val postal: TextView = findViewById(R.id.detailUserPostal)
        val city: TextView = findViewById(R.id.detailUserCity)

        firstName.text = user.firstName
        lastName.text = user.lastName
        street.text = user.street
        streetNr.text = user.streetNr.toString()
        postal.text = user.postalCode.toString()
        city.text = user.city

        selectedUser = user
    }

    private fun editOnButtonClick() {
        val newIntent: Intent = Intent(this@DetailActivity, EditActivity::class.java)
        newIntent.putExtra("firebaseId", firebaseId)
        newIntent.putExtra("createNewUser", false)
        startActivity(newIntent)
    }
}