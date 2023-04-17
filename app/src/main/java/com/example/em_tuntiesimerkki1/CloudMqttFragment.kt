package com.example.em_tuntiesimerkki1

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.em_tuntiesimerkki1.databinding.FragmentCloudMqttBinding
import com.google.gson.GsonBuilder
import com.hivemq.client.mqtt.MqttClient
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient
import com.hivemq.client.mqtt.mqtt3.message.connect.connack.Mqtt3ConnAck
import kotlin.random.Random

class CloudMqttFragment : Fragment() {

    // HiveHQ MQTT version 3 -client
    private lateinit var client: Mqtt3AsyncClient

    // change this to match your fragment name
    private var _binding: FragmentCloudMqttBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCloudMqttBinding.inflate(inflater, container, false)
        val root: View = binding.root


        // version 3, IBM Cloud, weather station
        client = MqttClient.builder()
            .useMqttVersion3()
            .sslWithDefaultConfig()
            .identifier(BuildConfig.HIVE_MQTT_CLIENT)
            .serverHost(BuildConfig.HIVE_MQTT_BROKER)
            .serverPort(8883)
            .buildAsync()

        client.connectWith()
            .simpleAuth()
            .username(BuildConfig.HIVE_MQTT_USERNAME)
            .password(BuildConfig.HIVE_MQTT_PASSWORD.toByteArray())
            .applySimpleAuth()
            .send()
            .whenComplete { connAck: Mqtt3ConnAck?, throwable: Throwable? ->
                if (throwable != null) {
                    Log.d("ADVTECH", "Connection failure.")
                    Log.d("ADVTECH", throwable.message.toString())
                } else {
                    // Setup subscribes or start publishing
                    subscribeToTopic()
                }
            }

        binding.buttonSendMessage.setOnClickListener {
            var randomNumber = Random.nextInt(0, 100)
            var stringPayload = "Hello World! $randomNumber"

            client.publishWith()
                .topic(BuildConfig.HIVE_MQTT_TOPIC)
                .payload(stringPayload.toByteArray())
                .send()
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
            .topicFilter(BuildConfig.HIVE_MQTT_TOPIC)
            .callback { publish ->

                // this callback runs everytime your code receives new data payload
                var result = String(publish.getPayloadAsBytes())
                Log.d("ADVTECH", result)

                try {
                    activity?.runOnUiThread {
                        binding.textViewMessage.text = result
                    }
                }
                catch (e: Exception) {
                    Log.d("ADVTECH", "VIRHE" + e.message.toString())
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