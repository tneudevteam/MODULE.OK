package ua.samosfator.moduleok.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dd.CircularProgressButton;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.splunk.mint.Mint;
import com.splunk.mint.MintLogLevel;

import de.greenrobot.event.EventBus;
import ua.samosfator.moduleok.Auth;
import ua.samosfator.moduleok.R;
import ua.samosfator.moduleok.event.LoginEvent;

public class LoginFragment extends Fragment {
    private CircularProgressButton new_loggin_btn;
    private MaterialEditText login_txt;
    private MaterialEditText password_txt;
    private Button login_button;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        initViews(rootView);
        new_loggin_btn = (CircularProgressButton) rootView.findViewById(R.id.btnWithText);
        new_loggin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String login = login_txt.getText().toString();
                String password = password_txt.getText().toString();

                if (!login.isEmpty() && !password.isEmpty()) {
                    enableInputs(false);
                    doLogin(login, password);
                    Mint.logEvent("log in", MintLogLevel.Info);

                }
                else {

                    if (login.equals("")) {
                        login_txt.setError("Введіть логін");

                    }

                    if (password.isEmpty()) {
                        password_txt.setError("Введіть пароль");

                    }
                }
            }
        });

        return rootView;
    }

    private void doLogin(final String login, final String password) {
        final Auth auth = new Auth();

        new Thread(new Runnable() {
            @Override
            public void run() {
                auth.signIn(login, password);
                if (auth.isSuccess()) {
                    Log.d("LoginFragment#doLogin->auth.isSuccess()", String.valueOf(auth.isSuccess()));
                    EventBus.getDefault().post(new LoginEvent());
                } else {
                    enableInputs(true);
                }
            }
        }).start();
    }

    private void initViews(View rootView) {
        login_txt = (MaterialEditText) rootView.findViewById(R.id.login_editText);
        password_txt = (MaterialEditText) rootView.findViewById(R.id.password_editText);
    }



    private void enableInputs(final boolean bool) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                login_txt.setEnabled(bool);
                password_txt.setEnabled(bool);
                new_loggin_btn.setIndeterminateProgressMode(!bool);
                new_loggin_btn.setProgress(bool ? 0 : 50);
            }
        });
    }
}
