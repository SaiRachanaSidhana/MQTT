package com.example.mqttapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.mqttapp.manager.MQTTConnectionParams
import com.example.mqttapp.manager.MQTTmanager
import com.example.mqttapp.protocols.UIUpdaterInterface
//import kotlinx.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), UIUpdaterInterface {
    var mqttManager:MQTTmanager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        resetUIWithConnection(false)
    }
    fun connect(view: View){

        var ipAddressField=findViewById<TextView>(R.id.ipAddressField)
        var topicField=findViewById<TextView>(R.id.topicField)

        if (!(ipAddressField.text.isNullOrEmpty() && topicField.text.isNullOrEmpty())) {
            //var host = "tcp://" + ipAddressField.text.toString() + ":1883"
            var host = "ssl://coralmqtt.b1automation.com:1885"
            var username="prod-testing"
            var password="prod-testing"
            var topic = topicField.text.toString()
            var connectionParams = MQTTConnectionParams("MQTTSample",host,topic,username,password)
            mqttManager = MQTTmanager(connectionParams,applicationContext,this)
            mqttManager?.connect()
        }else{
            updateStatusViewWith("Please enter all valid fields")
        }

    }

    fun sendMessage(view: View){

        var messageField=findViewById<TextView>(R.id.messageField)

        mqttManager?.publish(messageField.text.toString())

        messageField.setText("")
    }

    override fun resetUIWithConnection(status: Boolean) {

        var ipAddressField=findViewById<TextView>(R.id.ipAddressField)
        var topicField=findViewById<TextView>(R.id.topicField)
        var messageField=findViewById<TextView>(R.id.messageField)
        var connectBtn=findViewById<Button>(R.id.connectBtn)
        var sendBtn=findViewById<Button>(R.id.sendBtn)
        //var statusLabl=findViewById<TextView>(R.id.statusLabl)
        //var messageHistoryView=findViewById<EditText>(R.id.messageHistoryView)

        ipAddressField.isEnabled  = !status
        topicField.isEnabled      = !status
        messageField.isEnabled    = status
        connectBtn.isEnabled      = !status
        sendBtn.isEnabled         = status

        // Update the status label.
        if (status){
            updateStatusViewWith("Connected")
        }else{
            updateStatusViewWith("Disconnected")
        }
    }

    override fun updateStatusViewWith(status: String) {
        var statusLabl=findViewById<TextView>(R.id.statusLabl)
        statusLabl.text = status
    }

    override fun update(message: String) {
        var messageHistoryView=findViewById<EditText>(R.id.messageHistoryView)
        var text = messageHistoryView.text.toString()
        var newText = """
            $text
            $message
            """
        //var newText = text.toString() + "\n" + message +  "\n"
        messageHistoryView.setText(newText)
        messageHistoryView.setSelection(messageHistoryView.text.length)
    }

    override fun onResume() {
        super.onResume()

    }

}