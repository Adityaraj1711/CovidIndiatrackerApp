package com.covid.covidtracker

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.covid.covidtracker.Adapter.StateListAdapter
import com.miguelcatalan.materialsearchview.MaterialSearchView
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class StateListActivity : AppCompatActivity() {
    lateinit var toolbar: Toolbar
    lateinit var recyclerView: RecyclerView
    lateinit var stateListAdapter: StateListAdapter
    lateinit var searchView: MaterialSearchView
    lateinit var arrayList: ArrayList<HashMap<String, String>>
    lateinit var progressDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_state_list)
        bindViews()
    }

    private fun bindViews() {
        toolbar = findViewById(R.id.toolbar_state)
        setSupportActionBar(toolbar)
        searchView = findViewById(R.id.search_view)
        supportActionBar!!.setTitle("Select a state..")
        toolbar.setNavigationOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                startActivity(Intent(this@StateListActivity, MainActivity::class.java))
            }
        })
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Loading...")
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.show()
        arrayList = ArrayList()
        recyclerView = findViewById(R.id.state_wise_recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        getStateData()
    }

    private fun getStateData() {
        /* perform network operation and get state data that will be
        passed into adapter which will set them on the recycler view */

        val url = "https://api.covid19india.org/data.json"
        // show shimmer layout and close after getting the response

        val jsonObjectRequest: JsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            object: Response.Listener<JSONObject>{
                override fun onResponse(response: JSONObject?) {
                    try{
                        // store all the statewise results in jsonArray
                        val jsonArray: JSONArray = response!!.getJSONArray("statewise")
                        for(i in 1..jsonArray.length() -1 ){
                            val jsonObject: JSONObject = jsonArray.getJSONObject(i)
                            val stateName = jsonObject.getString("state")
                            val confirmCases = jsonObject.getString("confirmed")
                            val map = HashMap<String, String>()
                            map.put("state_name", stateName)
                            map.put("confirm_cases", confirmCases)
                            arrayList.add(map)
                        }
                        Handler().postDelayed(object : Runnable{
                            override fun run() {

                            }
                        }, 3000)
                        stateListAdapter = StateListAdapter(applicationContext, arrayList)
                        recyclerView.adapter = stateListAdapter
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
                    Toast.makeText(this@StateListActivity, error!!.message.toString(), Toast.LENGTH_SHORT).show()
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
        val requestQueue: RequestQueue = Volley.newRequestQueue(this@StateListActivity)
        requestQueue.add(jsonObjectRequest)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.state_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }
}
