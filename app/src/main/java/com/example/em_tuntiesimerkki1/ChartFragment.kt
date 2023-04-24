package com.example.em_tuntiesimerkki1

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.em_tuntiesimerkki1.databinding.FragmentChartBinding
import com.example.em_tuntiesimerkki1.datatypes.weatherstation.WeatherStation
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement
import com.google.gson.GsonBuilder
import com.hivemq.client.mqtt.MqttClient
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient
import com.hivemq.client.mqtt.mqtt3.message.connect.connack.Mqtt3ConnAck
import java.text.SimpleDateFormat
import java.util.*

class ChartFragment : Fragment() {

    // change this to match your fragment name
    private var _binding: FragmentChartBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    // HiveHQ MQTT version 3 -client
    private lateinit var client: Mqtt3AsyncClient

    private val temperatureList = mutableListOf<Double>()

    private val pressureList = mutableListOf<Double>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChartBinding.inflate(inflater, container, false)
        val root: View = binding.root

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

        val aaChartModel : AAChartModel = AAChartModel()
            .chartType(AAChartType.Line)
            .title("MQTT Weather Station")
            .subtitle("Sweden")
            .dataLabelsEnabled(true)
            .series(arrayOf(
                AASeriesElement()
                    .name("Temperature")
                    .data(temperatureList.toTypedArray()),

                AASeriesElement()
                    .name("Pressure")
                    .data(pressureList.toTypedArray())
            )
            )

        // The chart view object calls the instance object of AAChartModel and draws the final graphic
        binding.aaChartView.aa_drawChartWithChartModel(aaChartModel)

        return root
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

                    var item: WeatherStation = gson.fromJson(result, WeatherStation::class.java)

                    var temperature = item.d.get1().v
                    var pressure = item.d.get2().v

                    while (temperatureList.size >= 12) {
                        temperatureList.removeAt(8)
                    }

                    while (pressureList.size >= 12) {
                        pressureList.removeAt(8)
                    }

                    temperatureList.add(temperature)

                    pressureList.add(pressure)

                    var newArray = arrayOf(
                        AASeriesElement()
                            .name("Temperature")
                            .data(temperatureList.toTypedArray()),
                        AASeriesElement()
                            .name("Pressure")
                            .data(pressureList.toTypedArray())
                    )

                    // Run ui related things in UI-tread
                    // Don't put more stuff here, it's gonna cause problems
                    activity?.runOnUiThread(java.lang.Runnable {
                        // Set to chart
                        binding.aaChartView.aa_onlyRefreshTheChartDataWithChartOptionsSeriesArray(newArray, false)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}