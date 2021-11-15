package com.example.bluetoothsample

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.net.MacAddress
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import java.io.OutputStream
import java.lang.Exception
import java.util.*

class MainActivity : AppCompatActivity() {
    private var mBTAdapter: BluetoothAdapter? = null
    private var mBTDevice: BluetoothDevice? = null
    private var mBTSocket: BluetoothSocket? = null
    private var mOutputStream: OutputStream? = null

    private lateinit var sendButton: Button
    private lateinit var finidhButton: Button
    private lateinit var textView: TextView
    private val macAddress = "" // 大文字
    private val uuid = "00001101-0000-1000-8000-00805F9B34FB" // spp



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //
        //      views
        //
        sendButton = findViewById(R.id.sendButton)
        sendButton.setOnClickListener {
            send()
        }

        finidhButton = findViewById(R.id.finishButton)
        finidhButton.setOnClickListener {
            finish()
        }

        textView = findViewById(R.id.textView)

        //
        //      bluetooth
        //
        BTConnect()

    }

    override fun onDestroy() {
        super.onDestroy()

        if (mBTSocket != null) {
            try {
                mBTSocket?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            mBTSocket = null
        }
    }

    private fun BTConnect() {
        mBTAdapter = BluetoothAdapter.getDefaultAdapter()
        mBTDevice = mBTAdapter?.getRemoteDevice(macAddress)

        textView.text = macAddress

        // ソケット設定
        try {
            mBTSocket = mBTDevice?.createRfcommSocketToServiceRecord(UUID.fromString(uuid))
        } catch (e: Exception) {
            e.printStackTrace()
            mBTSocket = null
        }

        // 接続
        if (mBTSocket != null) {
            mBTAdapter?.cancelDiscovery()   // スキャン停止
            try {
                // 相手側がスタンバイ状態でないと失敗する。
                mBTSocket?.connect()
            } catch (e: Exception) {
                mBTSocket?.close()
                mBTSocket = null
                e.printStackTrace()
                return
            }
        }
    }

    private fun send() {
        val byteArray = byteArrayOf("send message".toByte())
        try {
            mOutputStream?.write(byteArray)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}