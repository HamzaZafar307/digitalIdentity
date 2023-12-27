package org.example;

import com.github.pwrlabs.pwrj.Block.Block;
import com.github.pwrlabs.pwrj.Transaction.Transaction;
import com.github.pwrlabs.pwrj.Transaction.VmDataTxn;
import com.github.pwrlabs.pwrj.protocol.PWRJ;
import org.bouncycastle.util.encoders.Hex;

//The listener continuously checks for new blocks and prints the messages sent to the VM
public class Listener {

    public static void listen() {
        new Thread() {
            public void run() {
                try {
                    long blockNumber = PWRJ.getLatestBlockNumber();

                    while(true) {
                        long latestBlockNumber = PWRJ.getLatestBlockNumber();

                        if(blockNumber < latestBlockNumber) {
                            Block block = PWRJ.getBlockByNumber(++blockNumber);

                            for(Transaction txn: block.getTransactions()) {
                                if(!(txn instanceof VmDataTxn)) continue;

                                VmDataTxn vmDataTxn = (VmDataTxn) txn;
                                long appId = vmDataTxn.getVmId();

                                if(appId != Main.appId) continue;

                                byte[] data = Hex.decode(vmDataTxn.getData());

                                System.out.println("Message From " + vmDataTxn.getFrom() + ": " + new String(data, "UTF-8"));
                            }
                        }

                        Thread.sleep(10);
                    }
                } catch (Exception e) { e.printStackTrace(); }
            }
        }.start();
    }


}