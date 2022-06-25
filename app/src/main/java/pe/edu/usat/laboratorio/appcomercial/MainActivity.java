package pe.edu.usat.laboratorio.appcomercial;

import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import de.hdodenhof.circleimageview.CircleImageView;
import pe.edu.usat.laboratorio.appcomercial.logica.Sesion;
import pe.edu.usat.laboratorio.appcomercial.util.Helper;
import pe.edu.usat.laboratorio.appcomercial.util.ImageDonwload;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    CircleImageView foto;
    TextView txtNombre, txtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
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

        //Ejecutar a la clase ConfiguracionTask para acceder al valor del IGV
        /*Configuracion.ConfiguracionTask configuracionTask = new Configuracion.ConfiguracionTask("1");
        configuracionTask.execute();*/

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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {


        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_registrar_solicitud) {
            //Llamar al fragment de registrar solicitudes de carga
            RegistrarSolicitudFragment registrarSolicitudFragment = new RegistrarSolicitudFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.contenedor, registrarSolicitudFragment);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_listar_solicitud) {
            ListaSolicitudesFragment fragment = new ListaSolicitudesFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.contenedor, fragment);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_clientes) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
