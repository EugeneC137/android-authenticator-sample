/**
 * Odoo, Open Source Management Solution
 * Copyright (C) 2012-today Odoo SA (<http:www.odoo.com>)
 * <p/>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details
 * <p/>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http:www.gnu.org/licenses/>
 * <p/>
 * Created on 3/2/16 12:16 PM.
 */
package com.odoo.dpr.authenticator;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.odoo.dpr.authenticator.auth.Authenticator;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtUsername, edtPassword;
    private AccountManager accountManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        accountManager = AccountManager.get(this);

        findViewById(R.id.btnLogin).setOnClickListener(this);
        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnLogin) {
            login();
        }
    }

    private void login() {
        edtUsername.setError(null);
        edtPassword.setError(null);
        if (TextUtils.isEmpty(edtUsername.getText())) {
            edtUsername.setError("Username required");
            edtUsername.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(edtPassword.getText())) {
            edtPassword.setError("Password required");
            edtPassword.requestFocus();
            return;
        }

        new AccountCreator(edtUsername.getText().toString().trim(),
                edtPassword.getText().toString().trim()).execute();

    }

    class AccountCreator extends AsyncTask<Void, Void, Boolean> {

        private String mUsername, mPassword;
        private ProgressDialog dialog;
        private String resultMessage;

        public AccountCreator(String username, String password) {
            mUsername = username;
            mPassword = password;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(LoginActivity.this);
            dialog.setTitle("Please wait...");
            dialog.setMessage("Creating account");
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            if (hasAccount(mUsername)) {
                resultMessage = mUsername + " account already exists";
                return false;
            }
            Account account = new Account(mUsername, Authenticator.AUTH_TYPE);
            boolean flag = accountManager.addAccountExplicitly(account, mPassword, new Bundle());
            resultMessage = flag ? mUsername + " account created successfully" :
                    "Unable to create account : " + mUsername;
            return flag;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            dialog.dismiss();
            Toast.makeText(LoginActivity.this, resultMessage, Toast.LENGTH_LONG).show();
            if (result)
                finish();
        }
    }

    private boolean hasAccount(String username) {
        Account[] accounts = accountManager.getAccountsByType(Authenticator.AUTH_TYPE);
        for (Account account : accounts) {
            if (account.name.equals(username)) return true;
        }
        return false;
    }

}
