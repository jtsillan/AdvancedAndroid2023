package com.example.em_tuntiesimerkki1

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.em_tuntiesimerkki1.databinding.FragmentWeatherStationBinding
import com.example.em_tuntiesimerkki1.datatypes.weatherstation.WeatherStation
import com.google.gson.GsonBuilder
import com.hivemq.client.mqtt.MqttClient
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient
import com.hivemq.client.mqtt.mqtt3.message.connect.connack.Mqtt3ConnAck
import java.text.SimpleDateFormat
import java.util.*

class WeatherStationFragment : Fragment() {

    // HiveHQ MQTT version 3 -client
    private lateinit var client: Mqtt3AsyncClient

    // change this to match your fragment name
    private var _binding: FragmentWeatherStationBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWeatherStationBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // the binding -object allows you to access views in the layout, textviews etc.

        binding.speedView.visibility = View.GONE
        binding.latestDataTestView.visibility = View.GONE

        // version 3, IBM Cloud, weather station
        client = MqttClient.builder()
            .useMqttVersion3()
            .sslWithDefaultConfig()
            .identifier(BuildConfig.MQTT_CLIENT + UUID.randomUUID().toString())
            .serverHost(BuildConfig.MQTT_BROKER)
            .serverPort(8883)
            .buildAsync()


        client.connectWith()
            .simpleAuth()
            .username(BuildConfig.MQTT_USERNAME)
            .password(BuildConfig.MQTT_PASSWORD.toByteArray())
            .applySimpleAuth()
            .send()
            .whenComplete { connAck: Mqtt3ConnAck?, throwable: Throwable? ->
                if (throwable != null) {
                    Log.d("ADVTECH", "Connection failure.")
                } else {
                    // Setup subscribes or start publishing
                    subscribeToTopic()
                }
            }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        client.disconnect()
    }

    private fun subscribeToTopic()
    {
        val gson = GsonBuilder().setPrettyPrinting().create()

        client.subscribeWith()
            .topicFilter(BuildConfig.MQTT_TOPIC)
            .callback { publish ->

                // this callback runs everytime your code receives new data payload
                var result = String(publish.payloadAsBytes)
                Log.d("ADVTECH", result)

                try {

                    val item: WeatherStation = gson.fromJson(result, WeatherStation::class.java)

                    val temperature = item.d.get1().v.toString()
                    var pressure = item.d.get2().v.toString()

                    var output = "Temperature: $temperature C\nPressure: $pressure hPa"

                    var temperatureValue = item.d.get1().v.toFloat()

                    var timeStamp = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
                    var text = "$timeStamp - Temperature: ${temperatureValue}℃, Humidity: ${item.d.get3().v.toFloat()}%"

                    // Ajetaan ulkoasuun liittyvät asiat UI-säikeessä
                    // älä laita ui-threadiin mitää lisää taukkaa, muuten tulee ongelmia
                    activity?.runOnUiThread(java.lang.Runnable {

                        with(binding) {
                            textViewWeatherData.text = output
                            speedView.speedTo(temperatureValue)
                            latestDataTestView.addData(text)
                            speedView.visibility = View.VISIBLE
                            latestDataTestView.visibility = View.VISIBLE
                            spinKit.visibility = View.GONE
                        }
                    })
                }
                catch (e: java.lang.Exception){
                    Log.d("ADVTECH", "Skipped diagnostics payloads.")
                }


            }
            .send()
            .whenComplete { subAck, throwable ->
                if (throwable != null) {
                    // Handle failure to subscribe
                    Log.d("ADVTECH", "Subscribe failed.")
                } else {
                    // Handle successful subscription, e.g. logging or incrementing a metric
                    Log.d("ADVTECH", "Subscribed!")
                }
            }
    }
}