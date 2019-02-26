package id.or.mozilla.androidrealtimechat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.client.Firebase;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    EditText etUsername, etPassword;
    Button bRegister, bLogin;
    String user, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUsername = (EditText)findViewById(R.id.etUsername);
        etPassword = (EditText)findViewById(R.id.etPassword);
        bRegister = (Button)findViewById(R.id.bRegister);
        bLogin = (Button)findViewById(R.id.bLogin);

        Firebase.setAndroidContext(this);

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = etUsername.getText().toString();
                pass = etPassword.getText().toString();

                if(user.equals("")){
                    etUsername.setError("can't be blank");
                }
                else if(pass.equals("")){
                    etPassword.setError("can't be blank");
                }
                else if(!user.matches("[A-Za-z0-9]+")){
                    etUsername.setError("only alphabet or number allowed");
                }
                else if(user.length()<5){
                    etUsername.setError("at least 5 characters long");
                }
                else if(pass.length()<5){
                    etPassword.setError("at least 5 characters long");
                }
                else {
                    final ProgressDialog pd = new ProgressDialog(RegisterActivity.this);
                    pd.setMessage("Loading...");
                    pd.show();
                    /*Catatan:
                    https://androidchatapp-12579.firebaseio.com adalah link dari realtime database firebase.
                    Ganti sesuai dengan link databasemu.
                    */
                    String url = "https://androidchatapp-12579.firebaseio.com/users.json";

                    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
                        @Override
                        public void onResponse(String s) {
                            Firebase reference = new Firebase("https://androidchatapp-12579.firebaseio.com/users");

                            if(s.equals("null")) {
                                reference.child(user).child("etPassword").setValue(pass);
                                Toast.makeText(RegisterActivity.this, "registration successful", Toast.LENGTH_LONG).show();
                            }
                            else {
                                try {
                                    JSONObject obj = new JSONObject(s);

                                    if (!obj.has(user)) {
                                        reference.child(user).child("etPassword").setValue(pass);
                                        Toast.makeText(RegisterActivity.this, "registration successful", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(RegisterActivity.this, "etUsername already exists", Toast.LENGTH_LONG).show();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            pd.dismiss();
                        }

                    },new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            System.out.println("" + volleyError );
                            pd.dismiss();
                        }
                    });

                    RequestQueue rQueue = Volley.newRequestQueue(RegisterActivity.this);
                    rQueue.add(request);
                }
            }
        });
    }

}
