package pe.edu.usat.laboratorio.appcomercial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import pe.edu.usat.laboratorio.appcomercial.util.Helper;

public class SolicitudMapaActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback,  GoogleMap.OnMarkerDragListener, GoogleMap.OnMapClickListener {

    double latitud = -6.7718472, longitud =  -79.839412;
    String direccion;
    GoogleMap mapa;
    Marker marcador;
    FloatingActionButton btnMapaSolicitud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitud_mapa);

        //Ocultar la barra de titulo del activity
        getSupportActionBar().hide();

        //Vincular el fragment que se encuentra en XML con la api de google maps
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapaSolicitud);
        mapFragment.getMapAsync(this);

        //Configurar el botón flotante
        btnMapaSolicitud = findViewById(R.id.btnMapaSolicitud);
        btnMapaSolicitud.setOnClickListener(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mapa = googleMap;
        //mapa.setMapType(mapa.MAP_TYPE_HYBRID);

        //Configurar el mapa para que reconozca (responda) al movimiento del marcador (marker)
        googleMap.setOnMarkerDragListener(this);

        //Configurar el mapa para que reconozca (responda) al evento click
        googleMap.setOnMapClickListener(this);

        //Configurar las características del marcador
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.draggable(true);
        markerOptions.position(new LatLng(this.latitud, this.longitud));
        markerOptions.title("Ubicación");
        markerOptions.snippet("Puede arrastrar el marcador para ubicarse en la dirección deseada");
        marcador = googleMap.addMarker(markerOptions);
        marcador.showInfoWindow();

        //Centrar la camara del mapa según la ubicación del marcador (marker)
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(centrarCamaraMapa())
                .zoom(17)
                .build();

        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        mapa.animateCamera(cameraUpdate);
    }

    private LatLng centrarCamaraMapa(){
        //Centrar la camara del mapa según la posición del marcador
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(new LatLng(this.latitud, this.longitud));
        LatLngBounds bounds = builder.build();
        return bounds.getCenter();
    }

    @Override
    public void onClick(View view) {
        Log.d("RegistarSolici", "Direcc: "+direccion + " "+latitud+" "+longitud);
        switch (view.getId()){
            case R.id.btnMapaSolicitud:
                Bundle parametros = new Bundle();
                parametros.putString("p_direccion", this.direccion);
                parametros.putDouble("p_latitud", this.latitud);
                parametros.putDouble("p_longitud", this.longitud);

                Log.d("RegistarSolici", "Direcc: "+direccion + " "+latitud+" "+longitud);

                //Declarar un intent para enviar los parametros de retorno
                Intent intent = new Intent();
                intent.putExtras(parametros);

                //Indicarle al activity que esta enviando los parametros de manera exitosa
                this.setResult(Activity.RESULT_OK, intent);
                this.finish();
                break;
        }
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        marcador.setPosition(latLng);

        //centrar la camara
        mapa.animateCamera(CameraUpdateFactory.newLatLng(latLng));

        this.latitud = latLng.latitude;
        this.longitud = latLng.longitude;

        //Capturar la dirección, según la latitud y longitud
        //this.direccion = Helper.obtenerDireccionMapa(this, this.latitud, this.longitud);

        Toast.makeText(this, "Dirección: " + this.direccion, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMarkerDragEnd(@NonNull Marker marker) {
        //centrar la camara
        mapa.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));

        //Capturar la latitud y longitud
        this.latitud = marker.getPosition().latitude;
        this.longitud = marker.getPosition().longitude;

        //Capturar la dirección, según la latitud y longitud
        //this.direccion = Helper.obtenerDireccionMapa(this, this.latitud, this.longitud);

        Toast.makeText(this, "Dirección: " + this.direccion, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMarkerDragStart(@NonNull Marker marker) {

    }

    @Override
    public void onMarkerDrag(@NonNull Marker marker) {

    }

}