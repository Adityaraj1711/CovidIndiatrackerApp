package com.covid.covidtracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import kotlinx.android.synthetic.main.state_list_item.*

class StateWiseDetailsActivity : AppCompatActivity() {
    private lateinit var intents: Intent
    lateinit var state_name: String
    private lateinit var toolbar: Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_state_wise_details)
        intents = intent
        state_name = intents.getStringExtra("state_name")!!

        toolbar = findViewById(R.id.toolbar_state_details)
        setSupportActionBar(toolbar)
        supportActionBar!!.setTitle(state_name)

    }
}