/*
 * %W% %E% Zain-Ul-Abedin
 *
 * Copyright (c) 2017-2018 Miranz Technology. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Miranz
 * technology. You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with Miranz.
 *
 */

package android.wallet.com.wallet.WalletProfile;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.wallet.com.wallet.R;
import android.wallet.com.wallet.web3J.Web3jHandler;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import org.web3j.utils.Convert;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * This class is written for basic functions of Ethereum and web3j integration
 * Because it's handling all the basic function of web3j so that's why we named
 * it as a web3Handler.
 *
 * @version 1.10 24 Aug 2017
 * @author Zain-Ul-Abedin
 */

public class WalletProfile extends AppCompatActivity {

    /** Variables for showing balances in ether */
    TextView etherTextView, ethTextView;
    /** Variable for store big integer */
    BigInteger bigInteger;
    /** ImgageView for showing QRScan*/
    ImageView qrScanImageView;
    /** variables for send and copy address of wallet */
    Button sendButton, copyWalletAddressButton;

    /**
     * onCreate method call when activity start
     *  @param savedInstanceState
     * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /**
         * passing savedInstanceState as parameter
         * @param savedInstanceState
         * */
        super.onCreate(savedInstanceState);
        /**
         * Setting layout for this activity
         * @param R.layout.activity_wallet_profile
         * */
        setContentView(R.layout.activity_wallet_profile);

        /** getting balance from wallet account */
        bigInteger = Web3jHandler.getBalance();

        /** initializing ether text view for displaying ether balance */
        etherTextView = (TextView) findViewById(R.id.etherBalanceTextView);
        /** initializing ether text view for displaying ether balance */
        ethTextView = (TextView) findViewById(R.id.ethBalanceTextView);
        /** initializing QRScan image variable */
        qrScanImageView = (ImageView) findViewById(R.id.imageView);
        /** initializing send button */
        sendButton = (Button) findViewById(R.id.sendButton);
        /** initializing copty wallet address button */
        copyWalletAddressButton = (Button) findViewById(R.id.copyWalletAddress);

        /**
         * setUp to launch QRScan activity by using zxing library
         * @param OnClickListener
         *  */
        final Activity activity = this;
        qrScanImageView.setOnClickListener(new View.OnClickListener() {
            /**
             * onClick override method for setUp on click action
             * @param view
             * */
            @Override
            public void onClick(View view) {
                /** initializing integrator */
                IntentIntegrator intentIntegrator = new IntentIntegrator(activity);
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                intentIntegrator.setPrompt("Scan QR");
                intentIntegrator.setCameraId(0);
                intentIntegrator.setBeepEnabled(true);
                intentIntegrator.setBarcodeImageEnabled(false);
                intentIntegrator.initiateScan();;
            }
        });

        /**
         * onClickListener method
         * @param OnClickListener
         * */
        sendButton.setOnClickListener(new View.OnClickListener() {
            /**
             * override onClick method
             * This method is calling send method for transaction using business logic
             * */
            @Override
            public void onClick(View v) {
                send();
            }
        });

        /**
         * uiThread for setting up current balance
         * @param Runnable
         * */
        this.runOnUiThread(new Runnable() {
            /**
             * run method for setUp
             * balances in ether
             * */
            @Override
            public void run() {

                /** setting balance to edit text*/
                etherTextView.setText("Ether " + Convert.toWei(bigInteger.toString(), Convert.Unit.WEI).divide(new BigDecimal("1000000000000000000")));
                ethTextView.setText("Eth " + Convert.toWei(bigInteger.toString(), Convert.Unit.WEI).divide(new BigDecimal("1000000000000000000")));
            }
        });

        /**
         * setting copy wallet setOnClickListner
         * @param OnClickListener
         * */
        copyWalletAddressButton.setOnClickListener(new View.OnClickListener() {
            /**
             * method for performing specified logic for copying wallet address
             * @param view
             * */
            @Override
            public void onClick(View view) {
                /** copying wallet address to clipboard */
                ((ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE)).setText(Web3jHandler.getWalletAddress());
                Toast.makeText(WalletProfile.this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * this method is start send activity for taking required inputs for transaction
     * */
    void send() {
        /**
         * startActivity method for for starting new activity
         * @param Intent
         * */
        startActivity(new Intent(WalletProfile.this,SendTransactionActivity.class));
    }

    /**
     * onActivity override method for showing results
     * @param requestCode
     * @param resultCode
     * @param data
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        /** initializing IntentResult  for getting results from parameter
         * @param requestCode
         * @param requestCode
         * @param data
         * */
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        /** Condition for is intent result is not null then proceed code */
        if(intentResult!=null){
            /** Condition for if intent contents are null, suppose scanning cancelled  */
            if(intentResult.getContents()==null){
                Toast.makeText(this, "Scanning cancelled", Toast.LENGTH_SHORT).show();
                /** Condition for is intent result is null */
            }else{
                /** Condition for is intentResults contains : symbol then split is into to portions  */
                if(intentResult.getContents().contains(":")){
                    ((ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE)).setText(intentResult.getContents().split(":")[1]);
                    /** Condition for is : symbol not consist */
                } else{
                    ((ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE)).setText(intentResult.getContents());
                }
                Toast.makeText(WalletProfile.this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
            }
        /** if results are empty then calling to default methods from super class */
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
