package com.example.em_tuntiesimerkki1

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.em_tuntiesimerkki1.databinding.FragmentTempAccessBinding
import org.json.JSONObject
import java.io.UnsupportedEncodingException

class TempAccessFragment : Fragment() {
    // VARIABLES USED BY THE SESSION MANAGEMENT
    val LOGIN_URL = BuildConfig.LOGIN_URL

    // these should be placed in the local properties file and used by BuildConfig
    // JSON_URL should be WITHOUT a trailing slash (/)!
    val JSON_URL_DIRECTUS = BuildConfig.JSON_URL_DIRECTUS

    // Using username + password in Directus
    val userName = BuildConfig.DIRECTUS_NEW_USER_USERNAME
    val password = BuildConfig.DIRECTUS_PASSWORD

    // request queues for requests
    var requestQueue: RequestQueue? = null
    var refreshRequestQueue: RequestQueue? = null

    // state booleans to determine our session state
    var loggedIn: Boolean = false
    var needsRefresh: Boolean = false

    // stored tokens. refresh is used when our session token has expired
    // access token in this case is the same as session token
    var refreshToken = ""
    var accessToken = ""
    var expires = ""

    // change this to match your fragment name
    private var _binding: FragmentTempAccessBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTempAccessBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    // fragment entry point
    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState);

        requestQueue = Volley.newRequestQueue(context)
        refreshRequestQueue = Volley.newRequestQueue(context)

        // start with login
        loginAction()
    }

    // button methods
    private fun loginAction()
    {
        Log.d("ADVTECH", "login")
        Log.d("ADVTECH", "$JSON_URL_DIRECTUS login")
        requestQueue?.add(loginRequest)
    }

    fun refreshLogin() {
        if (needsRefresh) {
            loggedIn = false
            // use this if using refresh logic
            //refreshRequestQueue?.add(loginRefreshRequest)

            // if using refresh logic, comment this line out
            loginAction()
        }
    }

    fun dataAction() {
        if (loggedIn) {
            requestQueue?.add(dataRequest)
        }
    }


    // REQUEST OBJECT 1: LOGIN
    var loginRequest: StringRequest = object : StringRequest(
        Method.POST, LOGIN_URL,
        Response.Listener { response ->

            var responseJSON: JSONObject = JSONObject(response)

            // save the refresh token too if using refresh logic
            //refreshToken = responseJSON.get("refresh_token").toString()
            accessToken = responseJSON.getJSONObject("data").get("access_token").toString()
            refreshToken = responseJSON.getJSONObject("data").get("refresh_token").toString()
            expires = responseJSON.getJSONObject("data").get("expires").toString()

            var expiresText = expires.toInt() / 1000

            loggedIn = true

            // after login's done, get data from API
            dataAction()

            Log.d("ADVTECH", response)

            binding.textViewRefreshToken.text = refreshToken
            binding.textViewTimer.text = expiresText.toString()

            // Note: if you send data to API instead, this might not be needed
        },
        Response.ErrorListener {
            // typically this is a connection error
            Log.d("ADVTECH", it.toString())
        }) {
        @Throws(AuthFailureError::class)
        override fun getHeaders(): Map<String, String> {
            // we have to provide the basic header info
            // + Bearer info => accessToken
            val headers = HashMap<String, String>()
            headers["Accept"] = "application/json"
            // For Directus, the typical approach works:
            headers["Content-Type"] = "application/json; charset=utf-8"
            return headers
        }
        // use this to build the needed JSON-object
        // this approach is used by Directus, IBM Cloud uses the commented version instead
        @Throws(AuthFailureError::class)
        override fun getBody(): ByteArray {
            // this function is only needed when sending data
            var body = ByteArray(0)
            try {
                // on how to create this newData -variable
                var newData = ""

                // a very quick 'n dirty approach to creating the needed JSON body for login
                newData = "{\"email\":\"${userName}\", \"password\": \"${password}\"}"

                // JSON to bytes
                body = newData.toByteArray(Charsets.UTF_8)
            } catch (e: UnsupportedEncodingException) {
                // problems with converting our data into UTF-8 bytes
            }
            return body
        }
    }


    // REQUEST OBJECT 3 : ACTUAL DATA -> FEEDBACK
    private var dataRequest: StringRequest = object : StringRequest(
        Method.GET, JSON_URL_DIRECTUS,
        Response.Listener { response ->
            Log.d("ADVTECH", response)
        },
        Response.ErrorListener {
            // typically this is a connection error
            Log.d("ADVTECH", it.toString())

            if (it is AuthFailureError) {
                Log.d("ADVTECH", "EXPIRED start")

                needsRefresh = true
                loggedIn = false
                refreshLogin()

                Log.d("ADVTECH", "EXPIRED end")
            }
        }) {
        @Throws(AuthFailureError::class)
        override fun getHeaders(): Map<String, String> {
            // we have to provide the basic header info
            // + Bearer info => accessToken
            val headers = HashMap<String, String>()
            //headers["Accept"] = "application/json"
            // headers["Content-Type"] = "application/json; charset=utf-8"
            headers["Authorization"] = "Bearer " + accessToken
            return headers
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}