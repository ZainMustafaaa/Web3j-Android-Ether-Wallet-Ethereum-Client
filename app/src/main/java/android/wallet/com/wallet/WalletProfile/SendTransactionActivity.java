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

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.wallet.com.wallet.R;
import android.wallet.com.wallet.web3J.Web3jHandler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionTimeoutException;
import java.io.IOException;

/**
 * This class is written for basic functions of Ethereum and web3j integration
 * Because it's handling all the basic function of web3j so that's why we named
 * it as a web3Handler.
 *
 * @version 1.10 24 Aug 2017
 * @author Zain-Ul-Abedin
 */

public class SendTransactionActivity extends AppCompatActivity {

    /** variables for input address and ether's for transaction */
    EditText addressEditText, ethEditText;
    /** variable for send button */
    Button sendButton;
    /** textView variables for output of transaction hashes and subDetails */
    TextView transactionHashAddressTextView, subDetailsTextView;

    /**
     * @override onCreate method calls when activity start
     * @param savedInstanceState
     * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /** passing savedInstanceState to super onCreate method
         * @param savedInstanceState
         * */
        super.onCreate(savedInstanceState);
        /** set content layout
         * @param R.layout.activity_send_transaction
         * */
        setContentView(R.layout.activity_send_transaction);

        /** initializing address and ether editText for input from user */
        addressEditText = (EditText) findViewById(R.id.toAddressEditText);
        ethEditText = (EditText) findViewById(R.id.ethEditText);

        /** initializing send button for transaction */
        sendButton = (Button) findViewById(R.id.sendButton);

        /** textView for transaction details */
        transactionHashAddressTextView = (TextView) findViewById(R.id.transactionHashTextView);
        subDetailsTextView = (TextView) findViewById(R.id.subDetailsTextView);

        /** setUp send on click button for transaction */
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendTransaction();
            }
        });
    }

    /**
     * sendTransaction method for calling libary functions
     * to transact amount of ether from one to another wallet
     * @exception InterruptedException
     * @exception IOException
     * @exception TransactionTimeoutException
     * */
    void sendTransaction(){

        /** condition for valid address input */
        if(addressEditText.length()==0) {
            Toast.makeText(this, "Please Provide Address", Toast.LENGTH_SHORT).show();
            return;
        }

        /** condition for valid transaction amount */
        if(ethEditText.length()==0){
            Toast.makeText(this, "Please Provide Balance", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            /**
             * Calling transaction method from Web3jHandler for transferring amount from wallet to another wallet
             * @param address
             * @param etherAmount
             * @throws org.web3j.crypto.CipherException
             * @throws TransactionTimeoutException
             * @throws IOException
             * @return TransactionReceipt after transaction
             *  */
            TransactionReceipt transactionReceipt = Web3jHandler.transaction(addressEditText.getText().toString(), Double.parseDouble(ethEditText.getText().toString()));
            /** output for transaction hash */
            transactionHashAddressTextView.setText(transactionReceipt.getTransactionHash());
            /** sub details including gas used and block hash */
            subDetailsTextView.setText("Gas Used: " + transactionReceipt.getGasUsed() + "\n" +
                "Block Hash: " + transactionReceipt.getBlockHash());

            /** toast for showing success message */
            Toast.makeText(this, "Successfully Transfer", Toast.LENGTH_SHORT).show();
        } catch (InterruptedException e) {
            Toast.makeText(this, "Interruption occurred during transaction", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (IOException e) {
            Toast.makeText(this, "Please check your connection", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (TransactionTimeoutException e) {
            Toast.makeText(this, "Transaction Time out", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }
}
