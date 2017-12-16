package cl.ayacuraespinoza.appcontrolpdi;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


public class LoginActivity extends AppCompatActivity {
    String urlApi;
    String urlBase = "http://159.203.79.251/app_dev.php/race_cuentas_usuarios/";
    String urlExtencion = ".json";
    String cargo;
    String contraApi;
    String contrasena;
    String rut;
    // UI references.
    private EditText edtRut;
    private EditText edtContrasena;
    private View mProgressView;
    private View mLoginFormView;
    Button btnLogin;
    boolean conectado = false;
    boolean existe = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        edtRut = (EditText) findViewById(R.id.rut);
        btnLogin = (Button) findViewById(R.id.email_sign_in_button);
        edtContrasena = (EditText) findViewById(R.id.password);
        edtContrasena.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                //Se realiza la accion del login si se apreta el boton siguente en el EditText de contraseña
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    validarEntradas();
                    return true;
                }
                return false;
            }
        });

        btnLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                validarEntradas();
            }
        });
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

    }

    public void buscarDatos(String urlBusqueda) {
        showProgress(true);
        String pass = null;
        RequestQueue colaSolicitudVolley = Volley.newRequestQueue(this);
        //Configuración de la solicitud. Observar que se utiliza GET.
        StringRequest cadenaSolicitud = new StringRequest(Request.Method.GET, urlBusqueda,
                new Response.Listener<String>() {
                    @Override
                    public void  onResponse(String respuestaRecibida) {
                        //En caso de éxito en la solicitud. Aquí se gestionan los datos de la respuesta.
                        //En este caso se extrae un valor de la respuesta, convirtiendola primero a un objeto JSON.
                        try {
                            conectado=true;
                                JSONObject respuestaJson = new JSONObject(respuestaRecibida);
                                Toast.makeText(getApplicationContext(), "llegueeee", Toast.LENGTH_LONG).show();
                                contraApi = respuestaJson.getString("pass");
                                cargo = respuestaJson.getString("perfil");
                            Toast.makeText(getApplicationContext(), contraApi, Toast.LENGTH_LONG).show();
                            existe = true;
                        } catch (JSONException e) {
                            e.printStackTrace();
                            conectado = true;
                            existe= false;

                            Toast.makeText(getApplicationContext(), "Me crachie"+ e, Toast.LENGTH_LONG).show();
                        }
                        compararContrasena(contraApi);
                        Toast.makeText(getApplicationContext(), "..."+contraApi, Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //En caso de error en la solicitud
                //txv_resultado.setText("Se ha producido un error "+error.getMessage());
            }
        });
        //La solicitud se agrega a la cola y es gestionada por Volley.
        colaSolicitudVolley.add(cadenaSolicitud);
    }

    public void compararContrasena(String contraApi) {
        View focusView = null;

        Toast.makeText(getApplicationContext(), contraApi+"uiop", Toast.LENGTH_LONG).show();
        Toast.makeText(getApplicationContext(), contrasena+"asdf", Toast.LENGTH_LONG).show();
        if (conectado && existe) {
            if (contraApi.equals(contrasena)) {
                lanzar();
            } else {
                edtContrasena.setError(getString(R.string.error_incorrect_password));
                focusView = edtContrasena;
                focusView.requestFocus();
            }
        } else if (conectado) {

        } else {
            Toast.makeText(getApplicationContext(), contraApi, Toast.LENGTH_LONG).show();
        }

    }

    public void validarEntradas() {
        View focusView = null;
        boolean error = false;
        String valRut;
        String valPass;
        valRut = edtRut.getText().toString();
        valPass = edtContrasena.getText().toString();
        edtRut.setError(null);
        edtContrasena.setError(null);

        if (TextUtils.isEmpty(valPass) && valPass.length() < 4) {
            edtContrasena.setError(getString(R.string.error_invalid_password));
            focusView = edtContrasena;
            error = true;
        }
        if (TextUtils.isEmpty(valRut)) {
            edtRut.setError(getString(R.string.error_field_required));
            focusView = edtRut;
            error = true;
        }
        if (error) {
            focusView.requestFocus();
        } else {
            rut = valRut;
            contrasena = valPass;
            Toast.makeText(getApplicationContext(), contrasena, Toast.LENGTH_LONG).show();
            urlApi = urlBase + rut + urlExtencion;
            buscarDatos(urlApi);

        }
    }

    public void lanzar() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
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
    }
}