package LogIn;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projetdememoire.MainActivity;
import com.example.projetdememoire.R;

public class Registration extends AppCompatActivity {

    EditText username, password, repassword;
    Button signup, signin;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration2);
            username = (EditText) findViewById(R.id.username);
            password = (EditText) findViewById(R.id.password);
            repassword = (EditText) findViewById(R.id.repassword);
            signup = (Button) findViewById(R.id.btnsignup);
            signin = (Button) findViewById(R.id.btnsignin);
            DB = new DBHelper(this);

            signup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String user = username.getText().toString();
                    String pass = password.getText().toString();
                    String repass = repassword.getText().toString();

                    if (user.equals("") || pass.equals("") || repass.equals(""))
                        Toast.makeText(Registration.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                    else {
                        if (pass.equals(repass)) {
                            Boolean checkuser = DB.checkusername(user);
                            if (checkuser == false) {
                                Boolean insert = DB.insertData(user, pass);
                                if (insert == true) {
                                    Toast.makeText(Registration.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(Registration.this, "Registration failed", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(Registration.this, "User already exists! please sign in", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(Registration.this, "Passwords not matching", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
            signin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                }
            });
        }
    }