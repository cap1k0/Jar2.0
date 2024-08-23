package com.panahi.jar;

import android.content.Intent;
import android.net.VpnService;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final int VPN_REQUEST_CODE = 0x0F;
    private boolean isVpnActive = false;

    private Button btnToggleVpn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnToggleVpn = findViewById(R.id.btnToggleVpn);

        btnToggleVpn.setOnClickListener(v -> {
            if (!isVpnActive) {
                startVpn();
            } else {
                stopVpn();
            }
        });
    }

    private void startVpn() {
        Intent intent = VpnService.prepare(this);
        if (intent != null) {
            startActivityForResult(intent, VPN_REQUEST_CODE);
        } else {
            onActivityResult(VPN_REQUEST_CODE, RESULT_OK, null);
        }
    }

    private void stopVpn() {
        Intent intent = new Intent(this, DnsVpnService.class);
        stopService(intent);
        btnToggleVpn.setText("Start VPN");
        isVpnActive = false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == VPN_REQUEST_CODE && resultCode == RESULT_OK) {
            // Start the VPN service with the predefined DNS server
            Intent intent = new Intent(this, DnsVpnService.class);
            startService(intent);

            btnToggleVpn.setText("Stop VPN");
            isVpnActive = true;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
