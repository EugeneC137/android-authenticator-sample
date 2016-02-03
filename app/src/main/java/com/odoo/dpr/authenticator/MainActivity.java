package com.odoo.dpr.authenticator;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    AccountManager accountManager;
    String authType = "com.odoo.myapp.auth";
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.accounts);
        findViewById(R.id.newAccount).setOnClickListener(this);
    }

    private void showAccounts() {
        accountManager = AccountManager.get(this);
        Account[] accounts = accountManager.getAccountsByType(authType);
        String text = "";
        if (accounts.length > 0) {
            for (Account account : accounts) {
                text += account.name + "\n";
            }
            textView.setText(text);
        } else {
            textView.setText("No Accounts found for auth type : " + authType);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        showAccounts();
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(this, LoginActivity.class));
    }
}
