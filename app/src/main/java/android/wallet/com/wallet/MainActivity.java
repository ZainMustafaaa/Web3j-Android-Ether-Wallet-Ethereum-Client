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

package android.wallet.com.wallet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.wallet.com.wallet.WalletProfile.WalletProfile;
import android.wallet.com.wallet.web3J.Web3jHandler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.web3j.crypto.CipherException;
import java.io.IOException;

/**
 * This class is written for basic functions of Ethereum and web3j integration
 * Because it's handling all the basic function of web3j so that's why we named
 * it as a web3Handler.
 *
 * @version 1.10 24 Aug 2017
 * @author Zain-Ul-Abedin
 */

public class MainActivity extends AppCompatActivity {

    /** Variable button for unlock wallet */
    Button unlockButton;
    /** Variable password edit text for password input */
    EditText passwordEditText;
    /** Variable progress dialog for loading */
    ProgressDialog progressDialog;

    /**
     * onCreate method
     * @param savedInstanceState
     * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MultiDex.install(this);
        setContentView(R.layout.activity_main);

        /** setting strict mode thread policy */
        final StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        try {
            /** Web3j Connection */
            Web3jHandler.web3Connection();
        }catch (IOException e){
            Toast.makeText(this, "Connection Error", Toast.LENGTH_SHORT).show();
            finish();
        }

        /** initializing unlockButton */
        unlockButton = (Button) findViewById(R.id.unlockWallet);
        /** initializing password edit text for user input */
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);

        /** method on click action performed for unlock wallet
         * @param OnClickListner
         * */
        unlockButton.setOnClickListener(new View.OnClickListener() {
            /**
             * @override onClick
             * @param view
             * @return void
             * */
            @Override
            public void onClick(View view) {
                /** initializing progress bar for waiting */
                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setTitle("Please wait");
                progressDialog.setMessage("Loading...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                /**
                 * background async task execute
                 * */
                new Background().execute();
            }
        });
    }

    /**
     * Background class extend from
     * AsyncTask @param void, void, void
     * */
    class Background extends AsyncTask<Void, Void, Void>{
        /**
         * @override doInBackground
         * @params voids
         * @return void
         * */
        @Override
        protected Void doInBackground(Void... voids) {
            /** calling unlock wallet method */
            unlock();
            return null;
        }
    }

    /**
     * This method is used to unlock wallet using web3j
     * @exception CipherException
     * @exception IOException
     * */
    void unlock(){
        /**
         * Creating object of Thread
         * @param Runnable
         * */
        new Thread(new Runnable() {
            /**
             * This method in multi thread calling method from web3j for loading credentials file
             * @override run
             * @exception CipherException
             * @exception  IOException
             * */
            @Override
            public void run() {
                try {
                    /**
                     * This method is loading credential file of your wallet from phone external storage
                     * @param password
                     * @throws CipherException
                     * @throws IOException
                     * */
                    Web3jHandler.loadCredentials(passwordEditText.getText().toString());
                    /**
                     * starting wallet profile activity
                     * @param Intent
                     * */
                    startActivity(new Intent(MainActivity.this, WalletProfile.class));
                }catch (CipherException e){
                    Toast.makeText(MainActivity.this, "Invalid Password", Toast.LENGTH_SHORT).show();
                }catch(IOException e){
                    Toast.makeText(MainActivity.this, "Connection Error", Toast.LENGTH_SHORT).show();
                }
                /**
                 * canceling progress dialog
                 * */
                progressDialog.cancel();
            }
        }).start();
    }
}
