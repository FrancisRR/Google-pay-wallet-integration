# Google-pay-wallet-integration
A simple example of google pay wallet

 /*google wallet*/
 implementation 'com.google.android.gms:play-services-wallet:16.0.1'

 Add in manifest inside application tag

 <meta-data
          android:name="com.google.android.gms.wallet.api.enabled"
          android:value="true"/>


Sample wallet request

    /*
    * Move to live need to change three params
    * 1.gateway
    * 2.gatewayMerchantId
    * 3.merchantName
    * */


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
