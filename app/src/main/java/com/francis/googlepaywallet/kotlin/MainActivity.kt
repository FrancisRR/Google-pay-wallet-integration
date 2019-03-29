package com.francis.googlepaywallet.kotlin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.francis.googlepaywallet.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val TAG = this::class.java.simpleName
    private var googlePayHandler: GooglePayHandler? = null

    companion object {
        internal var status: MutableLiveData<String> = MutableLiveData()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        googlePayHandler = GooglePayHandler(this)

        btGooglePay.setOnClickListener {
            if (googlePayHandler?.googlePayIsReady!!) {
                val req = GooglePayRequest.getPaymentDataRequest.toString()
                UiUtils.errorLog(TAG, req)
                googlePayHandler?.requestPayment()
            } else {
                status.value = "error to show button"
                UiUtils.errorLog(TAG, "error to show button")
            }

        }


        status.observeForever {
            tvStatus.setText("$it")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        googlePayHandler?.onActivityResult(requestCode, resultCode, data)
    }


    //Json Request example


    /*   {
           "apiVersion": 2,
           "apiVersionMinor": 0,
           "allowedPaymentMethods": [
           {
               "type": "CARD",
               "parameters": {
               "allowedAuthMethods": [
               "PAN_ONLY",
               "CRYPTOGRAM_3DS"
               ],
               "allowedCardNetworks": [
               "AMEX",
               "DISCOVER",
               "JCB",
               "MASTERCARD",
               "VISA"
               ]
           },
               "tokenizationSpecification": {
               "type": "PAYMENT_GATEWAY",
               "parameters": {
               "gateway": "example",
               "gatewayMerchantId": "exampleGatewayMerchantId"
           }
           }
           }
           ],
           "transactionInfo": {
           "totalPrice": "1",
           "currencyCode": "INR",
           "totalPriceStatus": "FINAL"
       },
           "merchantInfo": {
           "merchantName": "Example Merchant"
       }
       }
  */

}
