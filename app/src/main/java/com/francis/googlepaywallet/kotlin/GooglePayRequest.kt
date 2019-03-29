package com.francis.googlepaywallet.kotlin

import org.json.JSONArray
import org.json.JSONObject


object GooglePayRequest {

    /*
    * Move to live need to change three params
    * 1.gateway
    * 2.gatewayMerchantId
    * 3.merchantName
    * */


    /*Define your Google Pay API version*/
    val getBaseRequest: JSONObject
        get() = JSONObject()
            .put("apiVersion", 2)
            .put("apiVersionMinor", 0)


    val getTokenizationSpecification: JSONObject
        get() {
            val GATEWAY = JSONObject()
                .put("gateway", "example")  //Need to change
                .put("gatewayMerchantId", "exampleGatewayMerchantId") //need to change
            return JSONObject()
                .put("type", "PAYMENT_GATEWAY")
                .put("parameters", GATEWAY)
        }

    val getAllowedCardNetworks: JSONArray
        get() = JSONArray()
            .put("AMEX")
            .put("DISCOVER")
            .put("JCB")
            .put("MASTERCARD")
            .put("VISA");

    val getAllowedCardAuthMethods: JSONArray
        get() = JSONArray()
            .put("PAN_ONLY")
            .put("CRYPTOGRAM_3DS")

    val getBaseCardPaymentMethod: JSONObject
        get() {
            val cardSetUp = JSONObject()
                .put("allowedAuthMethods", getAllowedCardAuthMethods)
                .put("allowedCardNetworks", getAllowedCardNetworks)
            return JSONObject()
                .put("type", "CARD")
                .put("parameters", cardSetUp)
        }

    val getCardPaymentMethod: JSONObject
        get() = getBaseCardPaymentMethod
            .put("tokenizationSpecification", getTokenizationSpecification)


    val getIsReadyToPayRequest: JSONObject
        get() {
            val obj = getBaseRequest
            return obj.put("allowedPaymentMethods", JSONArray().put(getBaseCardPaymentMethod))
        }


    fun getTransactionInfo(price: String, currencyCode: String): JSONObject {
        return JSONObject()
            .put("totalPrice", price)
            .put("currencyCode", currencyCode)
            .put("totalPriceStatus", "FINAL")
    }

    val getMerchantInfo: JSONObject
        get() = JSONObject().put("merchantName", "Example Merchant") //need to change

    val getPaymentDataRequest: JSONObject
        get() {
            val dd = JSONArray().put(getCardPaymentMethod)
            return getBaseRequest
                .put("allowedPaymentMethods", dd)
                .put("transactionInfo", getTransactionInfo("1", "INR"))
                .put("merchantInfo", getMerchantInfo)
        }


    fun googlePaySingleReqest(price: String, currencyCode: String): JSONObject {
        val allowedCard = JSONArray().put("MASTERCARD").put("VISA")
        val authMethod = JSONArray().put("PAN_ONLY").put("CRYPTOGRAM_3DS")

        val cardAndAuthMethodResult =
            JSONObject().put("allowedAuthMethods", authMethod).put("allowedCardNetworks", allowedCard)

        val gateway = JSONObject().put("gateway", "example").put("gatewayMerchantId", "exampleGatewayMerchantId")
        val gateWayResult = JSONObject().put("type", "PAYMENT_GATEWAY").put("parameters", gateway)

        val cardAndGateWayResult = JSONObject()
            .put("type", "CARD")
            .put("tokenizationSpecification", gateWayResult)
            .put("parameters", cardAndAuthMethodResult)

        val cardAndGateWay = JSONArray().put(cardAndGateWayResult)

        val transactionInfo = JSONObject()
            .put("totalPrice", price)
            .put("currencyCode", currencyCode)
            .put("totalPriceStatus", "FINAL")

        return JSONObject()
            .put("apiVersion", 2)
            .put("apiVersionMinor", 0)
            .put("allowedPaymentMethods", cardAndGateWay)
            .put("merchantInfo", JSONObject().put("merchantName", "Example Merchant"))
            .put("transactionInfo", transactionInfo)
    }


}