package com.example.shakeitbaby

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.os.Build
import android.os.Bundle
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.isVisible
import com.example.shakeitbaby.ui.theme.ShakeItBabyTheme
import com.google.android.gms.nearby.connection.ConnectionInfo
import com.google.android.gms.nearby.connection.Payload
import com.google.android.gms.nearby.connection.Strategy
import com.google.android.gms.nearby.connection.Strategy.P2P_CLUSTER
import com.google.android.gms.nearby.connection.Strategy.P2P_STAR
import java.nio.charset.Charset


class MainActivity : ConnectionsActivity() {
    var shakeListener: ShakeListener? = null

    override fun onPause() {
        super.onPause()
        shakeListener?.pause()
    }
    private var nameIS: String = ""

    @SuppressLint("MissingPermission")
    override fun getName(): String {
        val myDevice = BluetoothAdapter.getDefaultAdapter()
        val deviceName = myDevice.name
        return deviceName
    }

    override fun getServiceId(): String {
        return "ServiceID"
    }

    override fun getStrategy(): Strategy = P2P_CLUSTER

    override fun onResume() {
        super.onResume()
        shakeListener?.resume()
    }
    lateinit var log: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.some_layout)

        shakeListener = ShakeListener(this)
        shakeListener?.setOnShakeListener(object : OnShakeListener {
            override fun onShake() {
                Toast.makeText(this@MainActivity, "Shaked", Toast.LENGTH_SHORT)
                    .show()

            }
        })
        val sendMessage = findViewById<Button>(R.id.sendMessage).apply {
            setOnClickListener {
            }
        }
        log = findViewById<TextView>(R.id.log)

        findViewById<Button>(R.id.advertiser).setOnClickListener {
            nameIS = "AdvertiserDevice"
            startAdvertising()
            sendMessage.isVisible = true
        }
        findViewById<Button>(R.id.discoverer).setOnClickListener {
            nameIS = "DiscovererDevice"
            startDiscovering()
            sendMessage.isVisible = false

        }

    }

    override fun onAdvertisingStarted() {
        super.onAdvertisingStarted()
        log.appendWithNewLine("Advertising Started")
    }

    override fun onDiscoveryStarted() {
        super.onDiscoveryStarted()
        log.appendWithNewLine("Discovery Started")
    }

    override fun onAdvertisingFailed() {
        super.onAdvertisingFailed()
        log.appendWithNewLine("Advertising Failed")
    }

    override fun onDiscoveryFailed(){
        super.onDiscoveryFailed()
        log.appendWithNewLine("Discovery failed")
    }

    override fun onEndpointDiscovered(endpoint: Endpoint?) {
        super.onEndpointDiscovered(endpoint)
        log.appendWithNewLine("Endpoint discovered")
        selectableText(findViewById(R.id.connections)) {
            connectToEndpoint(endpoint)
        }.setText(endpoint.toString())
    }

    override fun onConnectionInitiated(endpoint: Endpoint?, connectionInfo: ConnectionInfo?) {
        super.onConnectionInitiated(endpoint, connectionInfo)
        if (connectionInfo?.isIncomingConnection == true){
            selectableText(findViewById(R.id.connections)) {
                acceptConnection(endpoint,  {
                    send(Payload.fromBytes("Name: DekanAko, phone: 7728607381".toByteArray()))
                })
            }.setText("${endpoint.toString()} with code ${connectionInfo?.authenticationDigits} want to connect. tap me to connect")
        } else {
            acceptConnection(endpoint, {  })

            selectableText(findViewById(R.id.connections)) {
            }.setText("Waiting for ${endpoint?.name} the reciver to verify the connection ${connectionInfo?.authenticationDigits}")
        }

    }

    override fun onEndpointConnected(endpoint: Endpoint?) {
        super.onEndpointConnected(endpoint)
        log.appendWithNewLine("succesfully conencted to $endpoint")
    }

    override fun onReceive(endpoint: Endpoint?, payload: Payload?) {
        log.appendWithNewLine("Message received" + payload?.asBytes()?.toString(Charset.defaultCharset()))
    }

    override fun onStop() {
        super.onStop()
        stopAdvertising()
        stopDiscovering()
    }
    private fun selectableText(view: LinearLayout, listener: OnClickListener): TextView {
        val x = TextView(view.context)
        x.setOnClickListener(listener)
        x.setTextColor(resources.getColor(R.color.black))
        view.addView(x)
        return x
    }
}

private fun TextView.appendWithNewLine(text: String) {
    append(text)
    append("\n")
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ShakeItBabyTheme {
        Greeting("Android")
    }
}