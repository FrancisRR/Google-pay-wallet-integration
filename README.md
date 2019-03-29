# Google-pay-wallet-integration
A simple example of google pay wallet

Gradle - implementation 'com.google.android.gms:play-services-wallet:16.0.1'

 Add in manifest inside application tag

//<meta-data  //android:name="com.google.android.gms.wallet.api.enabled"  //android:value="true"/>

 val paymentClient = Wallet.getPaymentsClient(
            activity,
            Wallet.WalletOptions.Builder().setEnvironment(WalletConstants.ENVIRONMENT_TEST).build()
        )

Sample wallet request

    /*
    * Move to live need to change three params
    * 1.gateway
    * 2.gatewayMerchantId
    * 3.merchantName
    * */

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





{
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
