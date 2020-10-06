package com.covid.covidtracker

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


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    lateinit var daily_confirm_casesTv: TextView
    lateinit var total_confirm_casesTv: TextView

    lateinit var total_active_casesTv: TextView
    lateinit var daily_recover_casesTv: TextView
    lateinit var total_recover_casesTv: TextView

    lateinit var daily_death_casesTv: TextView
    lateinit var total_death_casesTv: TextView

    lateinit var updateDateAndTimeTv: TextView

    lateinit var refresh_btn: ImageView

    lateinit var card1: CardView
    lateinit var card2:CardView
    lateinit var card3:CardView
    lateinit var card4:CardView

    lateinit var toolbar: Toolbar

    lateinit var shimmer_homepage: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindViews()
        setOnCLickListener()
        getData()
    }

    private fun setOnCLickListener() {
        card1.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                getStateListActivity()
            }
        })
        card2.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                getStateListActivity()
            }
        })
        card3.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                getStateListActivity()
            }
        })
        card4.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                getStateListActivity()
            }
        })

        refresh_btn.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                shimmer_homepage = findViewById(R.id.shimmer_homepage)
                shimmer_homepage.visibility = View.VISIBLE
                getData()
                Handler().postDelayed(object : Runnable{
                    override fun run() {
                        Toast.makeText(this@MainActivity, "Refreshed", Toast.LENGTH_SHORT).show()
                    }
                }, 3000)
            }
        })

    }

    private fun getData() {
        val url = "https://api.covid19india.org/data.json"
        // show shimmer layout and close after getting the response
        shimmer_homepage.visibility = View.VISIBLE
        val jsonObjectRequest: JsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            object: Response.Listener<JSONObject>{
                override fun onResponse(response: JSONObject?) {
//                    Toast.makeText(this@MainActivity, response.toString(), Toast.LENGTH_SHORT).show()
                    try{
                        // store all the statewise results in jsonArray
                        val jsonArray: JSONArray = response!!.getJSONArray("statewise")
                        // india total cases exists in 0th index
                        val jsonObject: JSONObject = jsonArray.getJSONObject(0)

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

                        val updateTime: String = jsonObject.getString("lastupdatedtime")
                        updateDateAndTimeTv.setText(updateTime)

                        Handler().postDelayed(object : Runnable{
                            override fun run() {
                                shimmer_homepage.visibility = View.INVISIBLE
                            }
                        }, 3000)

                    }
                    catch (e: JSONException){
                        e.printStackTrace()
                        shimmer_homepage.visibility = View.INVISIBLE
                    }
                }
            },
            object: Response.ErrorListener {
                override fun onErrorResponse(error: VolleyError?) {
                    Toast.makeText(this@MainActivity, error!!.message.toString(), Toast.LENGTH_SHORT).show()
                    shimmer_homepage.visibility = View.INVISIBLE
                }
        })
        val socketTime = 7000
        val retryPolicy: RetryPolicy = DefaultRetryPolicy(
            socketTime,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        jsonObjectRequest.setRetryPolicy(retryPolicy)
        val requestQueue: RequestQueue = Volley.newRequestQueue(this@MainActivity)
        requestQueue.add(jsonObjectRequest)


    }

    private fun getStateListActivity() {
        startActivity(Intent(this@MainActivity, StateListActivity::class.java))
    }

    private fun bindViews() {
        daily_confirm_casesTv = findViewById(R.id.daily_confirm)
        total_confirm_casesTv = findViewById(R.id.total_confirm_cases)

        total_active_casesTv = findViewById(R.id.total_active_cases)

        daily_recover_casesTv = findViewById(R.id.daily_recover_cases)
        total_recover_casesTv = findViewById(R.id.total_recover_cases)

        daily_death_casesTv = findViewById(R.id.daily_death_cases)
        total_death_casesTv = findViewById(R.id.total_death_cases)
        updateDateAndTimeTv = findViewById(R.id.update)

        refresh_btn = findViewById(R.id.refresh_btn)

        card1 = findViewById(R.id.card1)
        card2 = findViewById(R.id.card2)
        card3 = findViewById(R.id.card3)
        card4 = findViewById(R.id.card4)

        toolbar = findViewById(R.id.toolbar)
        // set toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        shimmer_homepage = findViewById(R.id.shimmer_homepage)

    }

    // show menu on toolbar, for that override onCreateOptionsMenu and onOptionsItemSelected
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }
}