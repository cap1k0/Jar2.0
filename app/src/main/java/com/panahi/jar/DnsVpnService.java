package com.panahi.jar;


import android.net.VpnService;
import android.os.ParcelFileDescriptor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class DnsVpnService extends VpnService implements Runnable {

    private Thread thread;
    private ParcelFileDescriptor vpnInterface;
    private static final String DNS_SERVER = "8.8.8.8"; // Example DNS server (Google DNS)

    @Override
    public void onCreate() {
        super.onCreate();
        thread = new Thread(this, "DnsVpnThread");
        thread.start();
    }

    @Override
    public void onDestroy() {
        if (thread != null) {
            thread.interrupt();
        }
        if (vpnInterface != null) {
            try {
                vpnInterface.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onDestroy();
    }

    @Override
    public void run() {
        try {
            Builder builder = new Builder();

            // Configure the VPN interface with the predefined DNS server
            vpnInterface = builder
                    .addAddress("10.0.0.2", 24)
                    .addDnsServer(DNS_SERVER)
                    .establish();

            // The channel is used to send and receive packets
            DatagramChannel tunnel = DatagramChannel.open();
            tunnel.connect(new InetSocketAddress(DNS_SERVER, 53));

            // Allocate the buffer for a single packet
            ByteBuffer packet = ByteBuffer.allocate(32767);

            while (!Thread.interrupted()) {
                // Here you would process the packets
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
