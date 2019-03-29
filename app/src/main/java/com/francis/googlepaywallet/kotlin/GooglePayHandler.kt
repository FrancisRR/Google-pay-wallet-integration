package com.francis.googlepaywallet.kotlin

import android.app.Activity
import android.content.Intent
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.wallet.*
import org.json.JSONObject


class GooglePayHandler(val activity: Activity) {
    private val TAG = this::class.java.simpleName
    private lateinit var paymentClient: PaymentsClient
    private val LOAD_PAYMENT_DATA_REQUEST_CODE = 42


    init {
        setInstance()
        googlePayReadyFun()
    }

    private fun setInstance() {
        paymentClient = Wallet.getPaymentsClient(
            activity,
            Wallet.WalletOptions.Builder().setEnvironment(WalletConstants.ENVIRONMENT_TEST).build()
        )
    }

    fun googlePayReadyFun() {
        val request = IsReadyToPayRequest.fromJson(GooglePayRequest.getIsReadyToPayRequest.toString())
        val task = paymentClient.isReadyToPay(request)
        task.addOnCompleteListener(OnCompleteListener<Boolean> { task ->
            try {
                val result = task.getResult(ApiException::class.java)!!
                if (result) {
                    // show Google Pay as a payment option
                    googlePayIsReady = true

                }
            } catch (e: ApiException) {
                MainActivity.status.value = "Google pay not ready"
                UiUtils.errorLog(TAG, e.message)
            }
        })

    }

    var googlePayIsReady: Boolean = false


    fun requestPayment() {
        val paymentDataRequestJson = GooglePayRequest.getPaymentDataRequest
        val request = PaymentDataRequest.fromJson(paymentDataRequestJson.toString())
        if (request != null) {
            AutoResolveHelper.resolveTask(
                paymentClient.loadPaymentData(request), activity, LOAD_PAYMENT_DATA_REQUEST_CODE
            )
        }
    }


    internal fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            LOAD_PAYMENT_DATA_REQUEST_CODE -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        val paymentData = PaymentData.getFromIntent(data!!)
                        val json: String? = paymentData?.toJson()
                        UiUtils.errorLog(TAG, json)
                        // if using gateway tokenization, pass this token without modification
                        val paymentMethodData: JSONObject = JSONObject(json).getJSONObject("paymentMethodData")
                        val paymentToken: String =
                            paymentMethodData.getJSONObject("tokenizationData").getString("token")
                        UiUtils.errorLog("TOKEN", paymentToken)
                        MainActivity.status.value = "Payment success"
                    }
                    Activity.RESULT_CANCELED -> {
                        MainActivity.status.value = "Payment cancelled"
                        UiUtils.errorLog(TAG, "RESULT_CANCELED")
                    }
                    AutoResolveHelper.RESULT_ERROR -> {
                        MainActivity.status.value = "Payment failed"
                        UiUtils.errorLog(TAG, "RESULT_ERROR")
                    }
                }
            }
        }
    }


}