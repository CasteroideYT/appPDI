package cl.ayacuraespinoza.appcontrolpdi;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class SalidaActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String fechaSalida = "1900-01-01";
    String horaSalida = "00:00:00";
    String siglaCarro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salida);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Trabajo del Layout content_salida.xml

        setContentView(R.layout.content_salida);

        vincularElementos();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_salida) {
            Intent i = new Intent(this, SalidaActivity.class );
            startActivity(i);
        } else if (id == R.id.nav_llegada) {
            Intent i = new Intent(this, LlegadaActivity.class );
        } else if (id == R.id.nav_cerrar) {
            this.finish();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (id == R.id.nav_app) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // Vinculacion de elementos intefaz contento_activity a objetos
    private void vincularElementos() {


        NumberPicker letraSigla = (NumberPicker) findViewById(R.id.nbp_LetraSigla);
        final NumberPicker primerNP = (NumberPicker) findViewById(R.id.nbp_primerDigito);
        NumberPicker segundoNP = (NumberPicker) findViewById(R.id.nbp_segundoDigito);
        NumberPicker tercerNP = (NumberPicker) findViewById(R.id.nbp_tercerDigito);
        NumberPicker cuartoNP = (NumberPicker) findViewById(R.id.nbp_cuartoDigito);
        NumberPicker odometro = (NumberPicker) findViewById(R.id.nbp_Odometro);
        
        Button verificaSigla = (Button) findViewById(R.id.btn_verificarSigla);

        // Establecer valores de NumberPicker
        primerNP.setMinValue(1); primerNP.setMaxValue(9); primerNP.setWrapSelectorWheel(true);
        segundoNP.setMinValue(0); segundoNP.setMaxValue(9); segundoNP.setWrapSelectorWheel(true);

        tercerNP.setMinValue(0); tercerNP.setMaxValue(9); tercerNP.setWrapSelectorWheel(true);

        cuartoNP.setMinValue(0); cuartoNP.setMaxValue(9); cuartoNP.setWrapSelectorWheel(true);

        letraSigla.setMinValue(0); letraSigla.setMaxValue(3);
        letraSigla.setDisplayedValues(new String[]{"A","C","J","F"});letraSigla.setWrapSelectorWheel(true);

    }

    String siglaConsultada;
    int kmRegistrado;
    String estadoCarro;

    private void consultarDatos(String sigla) {

        //Creamos un objeto RequestQueue para efectuar el envío con la librería Volley
        RequestQueue colaSolicitudVolley = Volley.newRequestQueue(this);
        //Ruta al servicio
        String urlServicioAPI ="http://159.203.79.251/app_dev.php/raceCarrosPoliciales"+sigla;

        //Configuración de la solicitud. Observar que se utiliza GET.
        StringRequest cadenaSolicitud = new StringRequest(Request.Method.GET, urlServicioAPI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String respuestaRecibida) {
                        //En caso de éxito en la solicitud. Aquí se gestionan los datos de la respuesta.
                        //En este caso se extrae un valor de la respuesta, convirtiendola primero a un objeto JSON.

                        try {
                            JSONObject respuestaJson = new JSONObject(respuestaRecibida);

                            siglaConsultada = respuestaJson.getString("sigla");
                            String patente = respuestaJson.getString("patente");
                            Toast.makeText(getApplicationContext(),"La Sigla esta registrada a la patente: "+patente,Toast.LENGTH_LONG);
                            kmRegistrado = Integer.parseInt(respuestaJson.getString("kmActual"));
                            Toast.makeText(getApplicationContext(),"El Vehiculo registra "+kmRegistrado+" Km",Toast.LENGTH_LONG);
                            estadoCarro = respuestaJson.getString("estado");


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //En caso de error en la solicitud
                Toast.makeText(getApplicationContext(),"La Sigla no esta registrada a un Vehículo",Toast.LENGTH_LONG);
            }
        });
        //La solicitud se agrega a la cola y es gestionada por Volley.
        colaSolicitudVolley.add(cadenaSolicitud);

    }







}
