package home.mva.blog.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import home.mva.blog.R;
import home.mva.blog.posts.PostsActivity;

public class PasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private final String PASSWORD = "password";

    //For recovery
    private final String MASTER_PASSWORD = "66";

    private EditText mPasswordEditText;

    private Button mEnterButton;

    private SharedPreferences mSharedPreferences;

    private boolean mIsFirstStart = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password_activity);


        mPasswordEditText = findViewById(R.id.password_text);

        mEnterButton = findViewById(R.id.confirm_button);

        mSharedPreferences = getPreferences(MODE_PRIVATE);
        if (mSharedPreferences.getAll().isEmpty()) {
            setupFirstStart();
        }
        mEnterButton.setOnClickListener(this);
    }

    private void setupFirstStart() {
        mIsFirstStart = true;
        mPasswordEditText.setHint(R.string.create_password);
        mEnterButton.setText(R.string.button_create);
    }

    @Override
    public void onClick(View v) {

        if (mPasswordEditText.getText().toString().isEmpty()) {
            Snackbar.make(v, getString(R.string.empty_password), Snackbar.LENGTH_LONG).show();
            return;
        }

        Editor editor = mSharedPreferences.edit();

        if (mPasswordEditText.getText().toString().equals("66")) {
            editor.clear();
            editor.apply();
            Snackbar.make(v, getString(R.string.password_cleared), Snackbar.LENGTH_LONG).show();
            setupFirstStart();
            return;
        }

        if (mIsFirstStart) {
            editor.putString(PASSWORD, mPasswordEditText.getText().toString());
            editor.apply();
            startApp();
        } else {
            String password = mSharedPreferences.getString(PASSWORD, "");
            if (mPasswordEditText.getText().toString().equals(password)) {
                startApp();
            } else {
                Snackbar.make(v, getString(R.string.invalid_password), Snackbar.LENGTH_LONG).show();
            }
        }
    }

    private void startApp() {
        Intent intent = new Intent(this, PostsActivity.class);
        startActivity(intent);
        finish();
    }
}
