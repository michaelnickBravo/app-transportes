package pe.edu.usat.laboratorio.appcomercial;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import de.hdodenhof.circleimageview.CircleImageView;
import pe.edu.usat.laboratorio.appcomercial.clientes;
import pe.edu.usat.laboratorio.appcomercial.logica.Sesion;
import pe.edu.usat.laboratorio.appcomercial.util.Helper;

public class MainActivityOficinista extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    CircleImageView foto;
    TextView txtNombre, txtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_oficinista);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        setTitle("Oficinista");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_oficinista);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_oficinista);
        navigationView.setNavigationItemSelectedListener(this);

        //Referenciar a la cabecera del menú
        View cabeceraMenu = navigationView.getHeaderView(0);
        foto = cabeceraMenu.findViewById(R.id.imagenUsuario);
        txtNombre = cabeceraMenu.findViewById(R.id.nombreUsuario);
        txtEmail = cabeceraMenu.findViewById(R.id.loginUsuario);

        //Mostrar los datos del usuario: Nombre y su email
        txtNombre.setText(Sesion.NOMBRE);
        txtEmail.setText(Sesion.EMAIL);

        /*
        //Mostrar la foto del usuario que ha iniciado sesión (descargar desde el servicio web)
        ImageDonwload.ImageDownloadTask imageDownloadTask = new ImageDonwload.ImageDownloadTask(foto, "url foto");
        imageDownloadTask.execute();*/

        foto.setImageBitmap(Helper.base64ToImage(Sesion.IMG));

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_oficinista);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            this.logout();
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("UsuarioJson");
        editor.apply();
        this.finish();
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_clientes) {
            clientes fragment = new clientes();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.contenedor, fragment);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_listado_antendidas) {
            SolicitudesServicioCargasAtendidasConductor fragment = new SolicitudesServicioCargasAtendidasConductor();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.contenedor, fragment);
            fragmentTransaction.commit();
        }else if (id == R.id.nav_conductores_vehiculos){
            VehiculosConductoresFrag fragment = new VehiculosConductoresFrag();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.contenedor, fragment);
            fragmentTransaction.commit();
        }else if (id == R.id.nav_tarifas) {
            Intent intent = new Intent(this, Tarifas.class);
            this.startActivity(intent);
        }else if (id == R.id.nav_solictudes) {
            SolicitudesTransporteCarga fragment = new SolicitudesTransporteCarga();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.contenedor, fragment);
            fragmentTransaction.commit();

        } else if (id == R.id.nav_reportar_ubicacion) {

        } else if (id == R.id.nav_salir) {
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_oficinista);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}