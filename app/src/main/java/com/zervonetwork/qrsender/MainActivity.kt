package com.zervonetwork.qrsender

import QRCodeScanner
import WSClient
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import androidx.preference.PreferenceManager

class MainActivity : AppCompatActivity() {

    private lateinit var scanButton: Button
    private lateinit var configButton: Button
    private lateinit var scannedContentTextView: TextView
    private lateinit var qrCodeScanner: QRCodeScanner
    private lateinit var wsClient: WSClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val address = sharedPreferences.getString("server_address", "").toString()
        val port = sharedPreferences.getString("server_port", "")?.toInt()

        qrCodeScanner = QRCodeScanner()
        wsClient = port?.let { WSClient(address, it) }!!

        scanButton = findViewById(R.id.scanButton)
        configButton = findViewById(R.id.configButton)
        scannedContentTextView = findViewById(R.id.scannedContent)

        scanButton.setOnClickListener {
            qrCodeScanner.initiateScan(this)
        }

        configButton.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val scannedData = qrCodeScanner.handleActivityResult(requestCode, resultCode, data)

        if (scannedData != null) {
            wsClient.send(scannedData)
            scannedContentTextView.text = "SENT: $scannedData"
        } else {
            scannedContentTextView.text = "Scan Error."
        }
    }
}