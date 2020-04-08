package edu.uw.tcss450.combinatorpattern;

import static edu.uw.tcss450.combinatorpattern.util.PasswordValidator.*;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.snackbar.Snackbar;
import java.util.Optional;
import java.util.function.Consumer;

import edu.uw.tcss450.combinatorpattern.databinding.ActivityMainBinding;
import edu.uw.tcss450.combinatorpattern.util.PasswordValidator;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mBinding;
    private PasswordValidator mValidator = checkPwdLength(4)
            .and(checkPwdLowerCase().or(checkPwdUpperCase())) //Use of the OR looks for ANY letter
            .and(checkPwdDigit())
            .and(checkPwdSpecialChar())
            .and(checkExcludeWhiteSpace())
            .and(checkPwdDoNotInclude("_(){}[].,\'\"\\"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        mBinding.editPassword.addTextChangedListener(buildPasswordTextWatcher());
    }

    public void onCLick(View theCLickedButton) {
        String username = "abc";
        mValidator.processResult(mValidator
                .apply(mBinding.editPassword.getText().toString())
                .filter(result -> result != ValidationResult.SUCCESS),
                this::register,
                this::handleRegisterError);

        /*
         * NOTE: If you have access to Java 1.9 (Which AS still doesn't support) you can simplify
         * the above return statement to below. The ifPresentOrElse method was introduced to the
         * Optional class in Java 1.9.
         */
//        checkPwdLength()
//                .and(checkPwdDigit())
//                .and(checkPwdLowerCase())
//                .and(checkPwdUpperCase())
//                .and(checkPwdSpecialChar())
//                .apply(binding.editPassword.getText().toString())
//                .filter(result -> result != ValidationResult.SUCCESS)
//                .ifPresentOrElse(this::handleError, this::register);

    }


    private void handleRegisterError(ValidationResult result) {
        mBinding.editPassword.setError(result.name());
    }

    private void register() {
        Snackbar.make(mBinding.getRoot(), "Password Valid", Snackbar.LENGTH_LONG).show();
    }

    /**
     * Returns a TextWatcher that checks for password validation as the user enters the password.
     * This watcher updates an icon on each character typed.
     *
     * @return  a watcher that checks for password validation as the user enters the password
     */
    private TextWatcher buildPasswordTextWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //not used
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //not used
            }

            @Override
            public void afterTextChanged(Editable s) {
                mValidator.processResult(mValidator
                                .apply(mBinding.editPassword.getText().toString())
                                .filter(result -> result != ValidationResult.SUCCESS),
                        this::handleSuccess,
                        this::handleRegisterError);
            }

            /**
             * Handler for an invalid password. Sets the Icon to invalid check.
             * @param result the reason why the password is invalid (ignored)
             */
            private void handleRegisterError(ValidationResult result) {
                mBinding.imagePwdCheck.setImageDrawable(getDrawable(R.drawable.ic_invalid_check));
            }

            /**
             * Handler for a valid password. Sets the Icon to valid check.
             */
            private void handleSuccess() {
                mBinding.imagePwdCheck.setImageDrawable(getDrawable(R.drawable.ic_valid_check));
            }
        };
    }

}
