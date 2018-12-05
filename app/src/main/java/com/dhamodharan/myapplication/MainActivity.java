package com.dhamodharan.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.squareup.picasso.Picasso;
import java.util.Arrays;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {

  LoginButton loginButton;
  CallbackManager callbackManager;
  ImageView imageView;
  TextView txtUsername, txtEmail;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    loginButton = findViewById(R.id.login_button);
    imageView = findViewById(R.id.imageView);
    txtUsername = findViewById(R.id.txtUsername);
    txtEmail = findViewById(R.id.txtEmail);



    loginButton.setReadPermissions(Arrays.asList("email", "public_profile"));
    callbackManager = CallbackManager.Factory.create();

    loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
      @Override
      public void onSuccess(LoginResult loginResult) {

        getUserProfile(AccessToken.getCurrentAccessToken());
      }

      @Override
      public void onCancel() {
        // App code
      }

      @Override
      public void onError(FacebookException exception) {
        // App code
      }
    });

  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    callbackManager.onActivityResult(requestCode, resultCode, data);
    super.onActivityResult(requestCode, resultCode, data);
  }

  private void getUserProfile(AccessToken currentAccessToken) {
    GraphRequest request = GraphRequest.newMeRequest(
        currentAccessToken, new GraphRequest.GraphJSONObjectCallback() {
          @Override
          public void onCompleted(JSONObject object, GraphResponse response) {
            Log.d("TAG", object.toString());
            try {
              String first_name = object.getString("first_name");
              String last_name = object.getString("last_name");
              String email = object.getString("email");
              String id = object.getString("id");
              String image_url = "https://graph.facebook.com/" + id + "/picture?type=normal";

              txtUsername.setText("First Name: " + first_name + "\nLast Name: " + last_name);
              txtEmail.setText(email);
              //Picasso.with(MainActivity.this).load(image_url).into(imageView);

              Picasso.get()
                  .load(image_url)
                  .placeholder(R.mipmap.ic_launcher)
                  .error(R.mipmap.ic_launcher)
                  .into(imageView);

            } catch (JSONException e) {
              e.printStackTrace();
            }

          }
        });

    Bundle parameters = new Bundle();
    parameters.putString("fields", "first_name,last_name,email,id");
    request.setParameters(parameters);
    request.executeAsync();

  }
}
