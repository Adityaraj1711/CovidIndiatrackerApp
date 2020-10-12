package com.covid.covidtracker

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception


class StateWiseDetailsActivity : AppCompatActivity() {
    lateinit var daily_confirm_casesTv: TextView
    lateinit var total_confirm_casesTv: TextView

    lateinit var total_active_casesTv: TextView
    lateinit var daily_recover_casesTv: TextView
    lateinit var total_recover_casesTv: TextView

    lateinit var daily_death_casesTv: TextView
    lateinit var total_death_casesTv: TextView

    lateinit var toolbar: Toolbar
    private lateinit var intents: Intent
    private lateinit var state_name: String

    private lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_state_wise_details)

        bindViews()
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Loading data...")
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.show()
        setOnCLickListener()
        getStateData()
    }

    private fun setOnCLickListener() {

    }

    private fun getStateData() {
        val url = "https://api.covid19india.org/data.json"

        val jsonObjectRequest: JsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            object: Response.Listener<JSONObject>{
                override fun onResponse(response: JSONObject?) {
                    try{
                        // store all the statewise results in jsonArray
                        val jsonArray: JSONArray = response!!.getJSONArray("statewise")
                        // india total cases exists in 0th index
                        for(i in 0..jsonArray.length() - 1){
                            val jsonObject = jsonArray.getJSONObject(i)
                            val state = jsonObject.getString("state")
                            if(state.equals(state_name)){
                                // cases confirmed
                                val total_confirm_cases: String = jsonObject.getString("confirmed")
                                val daily_confirm_cases: String = jsonObject.getString("deltaconfirmed")
                                total_confirm_casesTv.setText(total_confirm_cases)
                                daily_confirm_casesTv.setText("[+$daily_confirm_cases ]")

                                // cases recovered
                                val total_recover_cases: String = jsonObject.getString("recovered")
                                val daily_recover_cases: String = jsonObject.getString("deltarecovered")
                                total_recover_casesTv.setText(total_recover_cases)
                                daily_recover_casesTv.setText("[+$daily_recover_cases ]")

                                // cases active
                                val total_active_cases: String = jsonObject.getString("active")
                                total_active_casesTv.setText(total_active_cases)

                                // cases death
                                val total_death_cases: String = jsonObject.getString("deaths")
                                val daily_death_cases: String = jsonObject.getString("deltadeaths")
                                total_death_casesTv.setText(total_death_cases)
                                daily_death_casesTv.setText("[+$daily_death_cases ]")
                                break
                            }
                        }
                        progressDialog.dismiss()
                    }
                    catch (e: JSONException){
                        e.printStackTrace()
                        progressDialog.dismiss()
                    }
                }
            },
            object: Response.ErrorListener {
                override fun onErrorResponse(error: VolleyError?) {
                    Toast.makeText(this@StateWiseDetailsActivity, error!!.message.toString(), Toast.LENGTH_SHORT).show()
                    progressDialog.dismiss()
                }
            })
        val socketTime = 7000
        val retryPolicy: RetryPolicy = DefaultRetryPolicy(
            socketTime,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        jsonObjectRequest.setRetryPolicy(retryPolicy)
        val requestQueue: RequestQueue = Volley.newRequestQueue(this@StateWiseDetailsActivity)
        requestQueue.add(jsonObjectRequest)
    }

    private fun getStateListActivity() {
        startActivity(Intent(this@StateWiseDetailsActivity, StateListActivity::class.java))
    }

    private fun bindViews() {
        daily_confirm_casesTv = findViewById(R.id.state_daily_confirm_cases)
        total_confirm_casesTv = findViewById(R.id.state_total_confirm_cases)

        total_active_casesTv = findViewById(R.id.state_total_active_cases)

        daily_recover_casesTv = findViewById(R.id.state_daily_recover_cases)
        total_recover_casesTv = findViewById(R.id.state_total_recover_cases)

        daily_death_casesTv = findViewById(R.id.state_daily_death_cases)
        total_death_casesTv = findViewById(R.id.state_total_death_cases)


        toolbar = findViewById(R.id.toolbar_state_details)
        // set toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                startActivity(Intent(this@StateWiseDetailsActivity, StateListActivity::class.java))
            }

        })
        intents = intent
        state_name = intents.getStringExtra("state_name")!!
    }

    // show menu on toolbar, for that override onCreateOptionsMenu and onOptionsItemSelected
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.state_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }
}