//package com.example.shakeitbaby
//
//import android.os.Bundle
//import android.util.Log
//import android.view.View.OnClickListener
//import android.widget.Button
//import android.widget.LinearLayout
//import android.widget.TextView
//import android.widget.Toast
//import androidx.activity.ComponentActivity
//import androidx.activity.enableEdgeToEdge
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.tooling.preview.Preview
//import com.example.shakeitbaby.ui.theme.ShakeItBabyTheme
//import com.google.android.gms.nearby.Nearby
//import com.google.android.gms.nearby.connection.AdvertisingOptions
//import com.google.android.gms.nearby.connection.ConnectionInfo
//import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback
//import com.google.android.gms.nearby.connection.ConnectionResolution
//import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo
//import com.google.android.gms.nearby.connection.DiscoveryOptions
//import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback
//import com.google.android.gms.nearby.connection.Payload
//import com.google.android.gms.nearby.connection.PayloadCallback
//import com.google.android.gms.nearby.connection.PayloadTransferUpdate
//import com.google.android.gms.nearby.connection.Strategy
//import com.google.android.gms.tasks.OnFailureListener
//import com.google.android.gms.tasks.OnSuccessListener
//import java.nio.charset.Charset
//
//
//class MainActivity : ComponentActivity(R.layout.some_layout) {
//    var shakeListener: ShakeListener? = null
//
//    override fun onPause() {
//        super.onPause()
//        shakeListener?.pause()
//    }
//
//    override fun onResume() {
//        super.onResume()
//        shakeListener?.resume()
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//
//        shakeListener = ShakeListener(this)
//        shakeListener?.setOnShakeListener(object : OnShakeListener {
//            override fun onShake() {
//                Toast.makeText(this@MainActivity, "Shaked", Toast.LENGTH_SHORT)
//                    .show()
//
//            }
//        })
//        val log = findViewById<TextView>(R.id.log)
//        findViewById<Button>(R.id.advertiser).setOnClickListener {
//            val advertisingOptions =
//                AdvertisingOptions.Builder().setStrategy(Strategy.P2P_STAR).build()
//            Nearby.getConnectionsClient(this@MainActivity)
//                .startAdvertising(
//                    "advertiser", "SERVICE_ID", object : ConnectionLifecycleCallback() {
//                        override fun onConnectionInitiated(p0: String, p1: ConnectionInfo) {
//                            Log.d("TestDekAk", "onConnectionInitiated: ")
//                            log.appendWithNewLine("advertiser: Connection initiated")
//                            Nearby.getConnectionsClient(this@MainActivity).acceptConnection(
//                                p0, object : PayloadCallback(){
//                                    override fun onPayloadReceived(p0: String, p1: Payload) {
//
//                                    }
//
//                                    override fun onPayloadTransferUpdate(
//                                        p0: String,
//                                        p1: PayloadTransferUpdate
//                                    ) {
//                                    }
//
//                                }
//                            ).apply { addOnSuccessListener {
//                                log.appendWithNewLine("Success ${it.toString()}")
//                            } }.addOnFailureListener {
//                                log.appendWithNewLine("Failed ${it.toString()}")
//                                it.printStackTrace()
//                            }
//                        }
//
//                        override fun onConnectionResult(p0: String, p1: ConnectionResolution) {
//                            log.appendWithNewLine("Result $p0, $p1")
//
//                        }
//
//                        override fun onDisconnected(p0: String) {
//                            log.appendWithNewLine("Disconnected $p0")
//                            println("Result: Disconnected $p0 ")
//                        }
//
//                    }, advertisingOptions
//                )
//                .addOnSuccessListener { unused: Void? ->
//                    log.appendWithNewLine("Advertising")
//
//                }
//                .addOnFailureListener { e: Exception? ->
//                    log.appendWithNewLine(e.toString())
//                    e?.printStackTrace()
//                }
//        }
//        findViewById<Button>(R.id.discoverer).setOnClickListener {
//            val discoveryOptions =
//                DiscoveryOptions.Builder().setStrategy(Strategy.P2P_STAR).build()
//            Nearby.getConnectionsClient(this)
//                .startDiscovery("SERVICE_ID", object : EndpointDiscoveryCallback() {
//                    override fun onEndpointFound(p0: String, p1: DiscoveredEndpointInfo) {
//                        Nearby.getConnectionsClient(this@MainActivity).stopDiscovery()
//                        println("Discover: Endpoint found $p0 Info ${p1.endpointName + "|" + p1.serviceId + "|" + p1.endpointInfo}")
//                        selectableText(findViewById(R.id.connections), {
//                            Nearby.getConnectionsClient(this@MainActivity)
//                                .requestConnection("Discover", p1.serviceId, object :
//                                    ConnectionLifecycleCallback() {
//                                    override fun onConnectionInitiated(
//                                        p0: String,
//                                        p1: ConnectionInfo
//                                    ) {
//                                        log.appendWithNewLine("Discoverer: Connection initiated")
//                                        Nearby.getConnectionsClient(this@MainActivity).acceptConnection(p0,
//                                            object : PayloadCallback(){
//                                                override fun onPayloadReceived(
//                                                    p0: String,
//                                                    p1: Payload
//                                                ) {
//
//                                                }
//
//                                                override fun onPayloadTransferUpdate(
//                                                    p0: String,
//                                                    p1: PayloadTransferUpdate
//                                                ) {
//                                                }
//
//                                            }
//                                            );
//                                    }
//
//                                    override fun onConnectionResult(
//                                        p0: String,
//                                        p1: ConnectionResolution
//                                    ) {
//                                        log.appendWithNewLine("Discoverer: Connection Result ${p1.status}")
//                                    }
//
//                                    override fun onDisconnected(p0: String) {
//                                        log.appendWithNewLine("Discoverer: Disconnected ${p0}")
//                                    }
//                                })
//                                .addOnSuccessListener({
//                                    log.appendWithNewLine("Connected...")
//                                })
//                                .addOnFailureListener {
//                                    it.printStackTrace()
//                                    log.appendWithNewLine("${it.toString()}: ${it.message}")
//                                };
//                        }).setText("name: ${p1.endpointName}, info: ${p1.endpointInfo}, serviceID: ${p1.serviceId}")
//                    }
//
//                    override fun onEndpointLost(p0: String) {
//                        log.appendWithNewLine("Endpoint Lost $p0")
//                    }
//
//                }, discoveryOptions)
//                .addOnSuccessListener { unused: Void? ->
//                    log.appendWithNewLine("Discovering...")
//                }
//                .addOnFailureListener { e: java.lang.Exception? ->
//                    e?.printStackTrace()
//                    log.appendWithNewLine("Discoverer: $e")
//
//                }
//        }
//
//    }
//
//    private fun selectableText(view: LinearLayout, listener: OnClickListener): TextView {
//        val x = TextView(view.context)
//        x.setOnClickListener(listener)
//        x.setTextColor(resources.getColor(R.color.black))
//        view.addView(x)
//        return x
//    }
//}
//
//private fun TextView.appendWithNewLine(text: String) {
//    append(text)
//    append("\n")
//}
//
//@Composable
//fun Greeting(name: String, modifier: Modifier = Modifier) {
//    Text(
//        text = "Hello $name!",
//        modifier = modifier
//    )
//}
//
//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    ShakeItBabyTheme {
//        Greeting("Android")
//    }
//}