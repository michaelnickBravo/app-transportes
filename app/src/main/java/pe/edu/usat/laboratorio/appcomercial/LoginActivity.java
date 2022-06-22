package pe.edu.usat.laboratorio.appcomercial;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import pe.edu.usat.laboratorio.appcomercial.logica.Sesion;
import pe.edu.usat.laboratorio.appcomercial.util.Helper;

import static android.Manifest.permission.READ_CONTACTS;

import org.json.JSONObject;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private Spinner tipoUsuario;

    // Tipos de usuario
    final String [] tiposUsuario = {"cliente", "oficinista", "conductor"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();
        tipoUsuario = findViewById(R.id.tipo_usuario);
        //Definimos el arrayAdaptar para nuestro spinner de tipo de usuario
        ArrayAdapter<String> arrayAdapterTipoUsuario = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tiposUsuario);
        arrayAdapterTipoUsuario.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tipoUsuario.setAdapter(arrayAdapterTipoUsuario);
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button botonRegistrarUsuario = findViewById(R.id.boton_registrar_cliente);
        botonRegistrarUsuario.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegistrarCliente.class);
                startActivity(intent);
                LoginActivity.this.finish();
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String tiUsuario = tipoUsuario.getSelectedItem().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password, tiUsuario);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() >= 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, String> {

        private final String mEmail;
        private final String mPassword;
        private final String tipUsuario;

        UserLoginTask(String email, String password, String tiUsuario) {
            mEmail = email;
            mPassword = password;
            tipUsuario = tiUsuario;
        }

        @Override
        protected String doInBackground(Void... params) {
            //Hacer la llamada (petición) al servicio web de login (inicio sesión)
            String URL_WS_Sesion = Helper.BASE_URL_WS + "/login";
            String resultadoJSON;
            try {
                HashMap<String, String> parametros = new HashMap<>();
                parametros.put("email", mEmail);
                parametros.put("contrasena", Helper.convertPassMd5(mPassword));
                parametros.put("tipo_usuario", tipUsuario);
                resultadoJSON = new Helper().requestHttpPost(URL_WS_Sesion, parametros);
            }catch (Exception e){
                return e.getMessage();
            }
            return resultadoJSON;
        }

        @Override
        protected void onPostExecute(final String resultadoJSON) {
            mAuthTask = null;
            showProgress(false);

            Log.e("RESULTADO WS", resultadoJSON);

            if (resultadoJSON.isEmpty()){
                Toast.makeText(LoginActivity.this, "El servicio web no ha devuelto los datos esperados", Toast.LENGTH_LONG).show();
            }else {
                try {
                    //Leer los datos que devuelve el servicio web en formato JSON

                    //Convertir el resultado del servicio web a JSON Object
                    JSONObject jsonObject = new JSONObject(resultadoJSON);
                    boolean status = jsonObject.getBoolean("status");
                    if (!status){//No ingresa a la app
                        String data = jsonObject.getString("data");
                        mPasswordView.setError(data);
                        mPasswordView.requestFocus();
                    }else{//Ingresa a la app
                        //Capturar los datos del usuario que ha iniciado sesión
                        JSONObject jsonObjectDatosUsuario = jsonObject.getJSONObject("data");

                        //Obtenemos el tipo de usuario para saber que tipo de datos vamos a obtener
                        Sesion.TIPO_USUARIO = jsonObjectDatosUsuario.getString("tipo_usuario");
                        String mensaje = jsonObject.getString("mensaje_bienvenida");

                        //LLamar al activity main
                        if (Objects.equals(Sesion.TIPO_USUARIO, "cliente")) {
                            //Almacenar los datos del usuario en variables estaticas
                            Sesion.DIRECCION = jsonObjectDatosUsuario.getString("direccion");
                            Sesion.EMAIL = jsonObjectDatosUsuario.getString("email");
                            Sesion.ESTADO_ID = jsonObjectDatosUsuario.getInt("estado_id");
                            Sesion.IMG = jsonObjectDatosUsuario.getString("img");
                            Sesion.NOMBRE = jsonObjectDatosUsuario.getString("nombre");
                            Sesion.NUM_DOCUMENTO = jsonObjectDatosUsuario.getString("num_documento");
                            Sesion.TELEFONO = jsonObjectDatosUsuario.getString("telefono");
                            Sesion.TIPO_DOC = jsonObjectDatosUsuario.getString("tipo_doc");
                            Sesion.TOKEN = jsonObjectDatosUsuario.getString("token");
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        }else if (Objects.equals(Sesion.TIPO_USUARIO, "oficinista")) {
                            Sesion.NUM_DOCUMENTO = jsonObjectDatosUsuario.getString("num_documento");
                            Sesion.NOMBRE = jsonObjectDatosUsuario.getString("nombre");
                            Sesion.DIRECCION = jsonObjectDatosUsuario.getString("direccion");
                            Sesion.EMAIL = jsonObjectDatosUsuario.getString("email");
                            Sesion.TELEFONO = jsonObjectDatosUsuario.getString("telefono");
                            Sesion.IMG = jsonObjectDatosUsuario.getString("img");
                            Intent intent = new Intent(LoginActivity.this, MainActivityOficinista.class);
                            startActivity(intent);
                        }else{
                            Sesion.NUM_BREVETE = jsonObjectDatosUsuario.getString("num_brevete");
                            Sesion.NOMBRE = jsonObjectDatosUsuario.getString("nombre");
                            Sesion.EMAIL = jsonObjectDatosUsuario.getString("email");
                            Sesion.IMG = jsonObjectDatosUsuario.getString("img");
                            Intent intent = new Intent(LoginActivity.this, MainActivityConductor.class);
                            startActivity(intent);
                        }

                        Toast.makeText(LoginActivity.this, mensaje, Toast.LENGTH_SHORT).show();

                        //Cerrar activity
                        LoginActivity.this.finish();
                    }
                }catch (Exception e){
                    Toast.makeText(LoginActivity.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

