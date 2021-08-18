package com.example.androidproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.iterator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.*
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var itemList: RecyclerView
    private lateinit var userArrayList: ArrayList<User>
    private lateinit var idArrayList: ArrayList<String>
    private lateinit var myAdapter: MyAdapter
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val newUserButton: View = findViewById(R.id.addNewUserButton)
        //https://www.youtube.com/watch?v=Ly0xwWlUpVM&ab_channel=Foxandroid
        //https://www.youtube.com/watch?v=EoJX7h7lGxM&ab_channel=Foxandroid
        //https://www.youtube.com/watch?v=xgpLYwEmlO0&ab_channel=Stevdza-San
        //https://firebase.google.com/docs/firestore/query-data/get-data
        //https://medium.com/@dev.soni04/custom-checkbox-button-style-fd582020df1a
        // https://stackoverflow.com/questions/20085283/why-am-i-getting-getstring-error-and-how-to-resolve

        itemList = findViewById(R.id.itemList)

        itemList.layoutManager = LinearLayoutManager(this)
        itemList.hasFixedSize()

        userArrayList = arrayListOf()
        idArrayList = arrayListOf()

        myAdapter = MyAdapter(applicationContext, userArrayList)

        itemList.adapter = myAdapter

        eventChangeListener()
        newUserButton.setOnClickListener {
            addUserOnClick()
        }

    }

    private fun eventChangeListener() {

        //Elke activity maakt een nieuwe instance aan, dat is niet goed
        db = FirebaseFirestore.getInstance()
        db.collection("users")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.e("oeps", error.message.toString())
                    return@addSnapshotListener
                }
                // !! of "double bang" is om aan te duiden dat iets Nullable is
                for (dc: DocumentChange in value?.documentChanges!!) {
                    if (dc.type == DocumentChange.Type.ADDED) {
                        userArrayList.add(dc.document.toObject(User::class.java))
                        idArrayList.add(dc.document.id)
                    }
                }
                myAdapter.notifyDataSetChanged()
            }

        myAdapter.setOnItemClickListener(object : MyAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {

                val selectedItemId: String = idArrayList[position]

                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra("firebaseId", selectedItemId)
                startActivity(intent)
            }
        })

    }

    private fun addUserOnClick() {
        val addUserIntent: Intent = Intent(this, EditActivity::class.java)
        startActivity(addUserIntent)
    }
}
