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

package android.wallet.com.wallet.web3J;

import android.os.Environment;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionTimeoutException;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


/**
 * This class is written for basic functions of Ethereum and web3j integration
 * Because it's handling all the basic function of web3j so that's why we named
 * it as a web3Handler.
 *
 * @version 1.10 24 Aug 2017
 * @author Zain-Ul-Abedin
 */

public class Web3jHandler {

    /** String variable 'network' is used for selecting which network you want to use */
    private static Web3j web3;
    /** Web3j variable 'web3' is used to implement all the functions, exist in Web3j Library */
    private static Credentials credentials;
    /** Credentials variable 'credentials' is used to implement all the functions, exist in Credentials Library */
    private static TransactionReceipt transactionReceipt;

    /**
     * web3Connection function is used to create the connection with the end-client node. // I uses Infura API.
     * @return condition
     */
    public static boolean web3Connection() throws IOException {
        web3 = Web3jFactory.build(new HttpService("https://rinkeby.infura.io/Lc2vdbhIswp6iQDRcmSa"));
        return  web3 != null;
    }

    /**
     * loadCredentials function is used to load the UTC-JSON file from a particular path.
     * @param password  is used to access your UTC-JSON file.
     * @throws IOException
     * @throws CipherException
     */
    public static void loadCredentials(String password) throws IOException, CipherException {
        credentials = WalletUtils.loadCredentials(password, Environment.getExternalStorageDirectory().getAbsolutePath() + "/UTC--2017-08-21T11-49-30.013Z--8c17ea160c092ae854f81580396ba570d9e62e24.json");
    }

    /**
     * transaction function is used to send funds from your address to another Ethereum address.
     * @param address   is a TO address or a address where you want to transfer funds.
     * @param ethBalance   is a amount you want to send.
     * @throws TransactionTimeoutException
     * @throws IOException
     * @throws InterruptedException
     * @return
     */
    public static TransactionReceipt transaction(String address, double ethBalance) throws InterruptedException, IOException, TransactionTimeoutException {
        return transactionReceipt = Transfer.sendFunds( web3, credentials, address, BigDecimal.valueOf(ethBalance), Convert.Unit.ETHER);
    }


    /**
     * getBalance function is used to get Balance and it returns the BigInteger value.
     * @throws InterruptedException
     * @throws ExecutionException
     * @return
     */
    public static BigInteger getBalance(){
        Future<EthGetBalance> ethGetBalanceFuture = web3.ethGetBalance(credentials.getAddress(), DefaultBlockParameterName.LATEST).sendAsync();
        try {
            return ethGetBalanceFuture.get().getBalance();
        }catch (Exception e){
            return BigInteger.ONE;
        }
    }

    /**
     * This function is returning wallet address from credentials
     * @return address in String
     * */
    public static String getWalletAddress(){
        return credentials.getAddress();
    }

}
