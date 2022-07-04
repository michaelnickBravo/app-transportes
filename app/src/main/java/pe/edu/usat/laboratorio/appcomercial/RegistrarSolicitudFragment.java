package pe.edu.usat.laboratorio.appcomercial;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import pe.edu.usat.laboratorio.appcomercial.logica.CategoriaCarga;
import pe.edu.usat.laboratorio.appcomercial.logica.ClaseCarga;
import pe.edu.usat.laboratorio.appcomercial.logica.Sesion;
import pe.edu.usat.laboratorio.appcomercial.logica.TipoCarga;
import pe.edu.usat.laboratorio.appcomercial.util.Helper;
import pe.edu.usat.laboratorio.appcomercial.util.Pickers;

public class RegistrarSolicitudFragment extends Fragment implements View.OnClickListener {

    private EditText editTxtDescripcionCarga,
            editTxtHora,
            editTxtFecha,
            editTextDireccionPartida,
            editTextDireccionLlegada,
            editTxtPeso,
            editTxtMonto;
    private Spinner spinnerClaseCarga, spinnerTipoCarga, spinnerCategoriaCarga;
    private ImageButton imgBtnHora, imgBtnFecha, imgBtnDirPartida, imgBtnDirLlegada;
    private Button btnRegistrarSolicitud;

    private double latitudPartida, longitudPartida, latitudLlega, longitudLlegada, distancia, monto;

    public static final int REQUEST_MAP_PARTIDA = 1;
    public static final int REQUEST_MAP_LLEGADA = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registrar_solicitud, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editTxtDescripcionCarga = view.findViewById(R.id.edit_txt_descripcion_carga);
        editTxtHora = view.findViewById(R.id.edit_txt_hora);
        editTxtFecha = view.findViewById(R.id.edit_txt_fecha);
        editTextDireccionPartida = view.findViewById(R.id.edit_text_direccion_partida);
        editTextDireccionLlegada = view.findViewById(R.id.edit_text_direccion_llegada);
        editTxtPeso = view.findViewById(R.id.edit_txt_peso);
        editTxtMonto = view.findViewById(R.id.edit_txt_monto);
        spinnerClaseCarga = view.findViewById(R.id.spinner_clase_carga);
        spinnerTipoCarga = view.findViewById(R.id.spinner_tipo_carga);
        spinnerCategoriaCarga = view.findViewById(R.id.spinner_categoria_carga);
        imgBtnHora = view.findViewById(R.id.img_btn_hora);
        imgBtnFecha = view.findViewById(R.id.img_btn_fecha);
        imgBtnDirPartida = view.findViewById(R.id.img_btn_dir_partida);
        imgBtnDirLlegada = view.findViewById(R.id.img_btn_dir_llegada);
        btnRegistrarSolicitud = view.findViewById(R.id.btn_registrar_solicitud);

        btnRegistrarSolicitud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarSolicitud();
                Log.d("Regis", "en el switch");
            }
        });

        //Cargar los spinner con datos
        new ClaseCargaTask().execute();
        new TipoCargaTask().execute();
        new CategoriaCargaTask().execute();

        //onClicks
        imgBtnHora.setOnClickListener(this);
        imgBtnFecha.setOnClickListener(this);
        imgBtnDirLlegada.setOnClickListener(this);
        imgBtnDirPartida.setOnClickListener(this);

        /*
        Calcular en linea recta
        Location locationA = new Location("punto A");
        locationA.setLatitude(-6.769746);
        locationA.setLongitude(-79.860107);

        Location locationB = new Location("punto B");
        locationB.setLatitude(-6.769522);
        locationB.setLongitude(-79.862146);

        Location.distanceBetween();
        float distanceKm = locationA.distanceTo(locationB)/1000;
        float distanceKmRedonde = Math.round(distanceKm * 100.0f)/100.0f;

        Log.d("RegistrarSolicitudFragment", String.valueOf(distanceKm));
        Log.d("RegistrarSolicitudFragment", String.valueOf(distanceKmRedonde) + " "+ "km");*/
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.img_btn_fecha:
                Pickers.obtenerFecha(getContext(), editTxtFecha);
                break;

            case R.id.img_btn_hora:
                Pickers.obtenerHora(getContext(), editTxtHora);
                break;

            case R.id.img_btn_dir_partida:
                Intent intent = new Intent(RegistrarSolicitudFragment.this.getActivity(), SolicitudMapaActivity.class);
                startActivityForResult(intent, REQUEST_MAP_PARTIDA);
                break;

            case R.id.img_btn_dir_llegada:
                Intent intent1 = new Intent(RegistrarSolicitudFragment.this.getActivity(), SolicitudMapaActivity.class);
                startActivityForResult(intent1, REQUEST_MAP_LLEGADA);
                break;
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("Regist", "Ingrese a onActivity, requestCode: "+ requestCode+ " ");
        if (requestCode == REQUEST_MAP_PARTIDA && resultCode == Activity.RESULT_OK) {
            Log.d("Regist", "Ingrese  ala sentencia");
            Bundle parametros = data.getExtras();
            //Mostrar la dirección
            editTextDireccionPartida.setText(parametros.getString("p_direccion"));

            //Almacenar la latitud y longitud
            this.latitudPartida = parametros.getDouble("p_latitud");
            this.longitudPartida = parametros.getDouble("p_longitud");
        }else if (requestCode == REQUEST_MAP_LLEGADA && resultCode == Activity.RESULT_OK) {
            Log.d("Regist", "Ingrese  ala sentencia");
            Bundle parametros = data.getExtras();
            //Mostrar la dirección
            editTextDireccionLlegada.setText(parametros.getString("p_direccion"));

            //Almacenar la latitud y longitud
            this.latitudLlega = parametros.getDouble("p_latitud");
            this.longitudLlegada = parametros.getDouble("p_longitud");
            DistanciaTask distanciaTask = new DistanciaTask(String.valueOf(this.latitudPartida), String.valueOf(this.longitudPartida), String.valueOf(this.latitudLlega), String.valueOf(this.longitudLlegada), Sesion.TOKEN);
            distanciaTask.execute();
        }

    }

    private void registrarSolicitud() {
        Log.d("Regist", "registarSolicitud()");
        final String descripcionCarga = editTxtDescripcionCarga.getText().toString();
        final String claseCargaId = ClaseCarga.buscarClaseCargaId(spinnerClaseCarga.getSelectedItem().toString());
        final String tipoCargaId = TipoCarga.buscarTipoCargaId(spinnerTipoCarga.getSelectedItem().toString());
        final String categoriaCargaId = CategoriaCarga.buscarCategoriaCargaId(spinnerCategoriaCarga.getSelectedItem().toString());
        final String pesoCarga = editTxtPeso.getText().toString();
        final String fechaHoraPartida = Helper.formatearDMA_to_AMD(editTxtFecha.getText().toString())+" "+editTxtHora.getText().toString().split(" ")[0];
        final String direccionPartida = editTextDireccionPartida.getText().toString();
        final String direccionLlegada = editTextDireccionLlegada.getText().toString();
        RegistrarSolicitudFragment.this.monto = Math.round(((Double.parseDouble(pesoCarga)/1000d)*Sesion.TARIFA*this.distancia)*100d)/100d;
        final String monto = String.valueOf(this.monto);
        final String numDocCliente = Sesion.NUM_DOCUMENTO;
        final String tarifaId = String.valueOf(Sesion.TARIFA_ID);
        final String token = Sesion.TOKEN;

        RegistrarSolicitudCargaTask registrarSolicitudCargaTask = new RegistrarSolicitudCargaTask(descripcionCarga, claseCargaId, tipoCargaId, categoriaCargaId, pesoCarga, fechaHoraPartida, direccionPartida, direccionLlegada, monto, numDocCliente, tarifaId, token);
        registrarSolicitudCargaTask.execute();
        Log.d("Regist", "se ejecuta la tarea de registro");
    }

    private class ClaseCargaTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean resultado = false;
            try {
                String URL_WS_ClaseCarga = Helper.BASE_URL_WS + "/clasecarga/listar";
                HashMap<String, String> parametros = new HashMap<>();
                parametros.put("token", Sesion.TOKEN);

                //Realizar la petición al servicio web
                String listaClientesJSON = new Helper().requestHttpPost(URL_WS_ClaseCarga, parametros);

                //Convertir la lista clientes JSON String a JSON Object
                JSONObject jsonObject = new JSONObject(listaClientesJSON);

                if(jsonObject.getBoolean("status")){
                    //Acceder a las clases de carga
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    //Limpiar lista
                    ClaseCarga.listaClaseCarga.clear();

                    for(int i=0; i<jsonArray.length();i++){
                        JSONObject jsonDatosClaseCarga = jsonArray.getJSONObject(i);
                        ClaseCarga claseCarga = new ClaseCarga();
                        claseCarga.setId(jsonDatosClaseCarga.getInt("id"));
                        claseCarga.setNombre(jsonDatosClaseCarga.getString("nombre"));
                        ClaseCarga.listaClaseCarga.add(claseCarga);
                    }
                    resultado=true;
                }
            }catch (Exception e){
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            return resultado;
        }

        @Override
        protected void onPostExecute(Boolean resultado) {
            super.onPostExecute(resultado);
            if (resultado){
                //Declarar un Array para almacenar solo los nombres de los clientes
                String nombreClaseCarga[] = new String[ClaseCarga.listaClaseCarga.size()];

                for (int i=0; i<ClaseCarga.listaClaseCarga.size();i++){
                    nombreClaseCarga[i] = ClaseCarga.listaClaseCarga.get(i).getNombre();
                }

                //Crear un adaptador para cargar los datos en el spinner
                ArrayAdapter<String> adaptador = new ArrayAdapter<>
                        (
                                getContext(),
                                android.R.layout.simple_spinner_dropdown_item,
                                nombreClaseCarga
                        );

                //Asignar el adaptador al spinner
                spinnerClaseCarga.setAdapter(adaptador);

            }else{
                Toast.makeText(getContext(), "No se ha podido descargar la lista de clases de carga", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class TipoCargaTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean resultado = false;
            try {
                String URL_WS_TipoCarga = Helper.BASE_URL_WS + "/tipocarga/listar";
                HashMap<String, String> parametros = new HashMap<>();
                parametros.put("token", Sesion.TOKEN);

                //Realizar la petición al servicio web
                String listaClientesJSON = new Helper().requestHttpPost(URL_WS_TipoCarga, parametros);

                //Convertir la lista JSON String a JSON Object
                JSONObject jsonObject = new JSONObject(listaClientesJSON);

                if(jsonObject.getBoolean("status")){
                    //Acceder a los datos
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    //Limpiar lista
                    TipoCarga.listaTipoCarga.clear();

                    for(int i=0; i<jsonArray.length();i++){
                        JSONObject jsonDatosClaseCarga = jsonArray.getJSONObject(i);
                        TipoCarga tipoCarga = new TipoCarga();
                        tipoCarga.setId(jsonDatosClaseCarga.getInt("id"));
                        tipoCarga.setNombre(jsonDatosClaseCarga.getString("nombre"));
                        TipoCarga.listaTipoCarga.add(tipoCarga);
                    }
                    resultado=true;
                }
            }catch (Exception e){
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            return resultado;
        }

        @Override
        protected void onPostExecute(Boolean resultado) {
            super.onPostExecute(resultado);
            if (resultado){
                //Declarar un Array para almacenar solo los nombres de los clientes
                String nombreTipoCarga[] = new String[TipoCarga.listaTipoCarga.size()];

                for (int i=0; i<TipoCarga.listaTipoCarga.size();i++){
                    nombreTipoCarga[i] = TipoCarga.listaTipoCarga.get(i).getNombre();
                }

                //Crear un adaptador para cargar los datos en el spinner
                ArrayAdapter<String> adaptador = new ArrayAdapter<>
                        (
                                getContext(),
                                android.R.layout.simple_spinner_dropdown_item,
                                nombreTipoCarga
                        );

                //Asignar el adaptador al spinner
                spinnerTipoCarga.setAdapter(adaptador);

            }else{
                Toast.makeText(getContext(), "No se ha podido descargar la lista de tipos de carga", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class CategoriaCargaTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean resultado = false;
            try {
                String URL_WS_CategoriaCarga = Helper.BASE_URL_WS + "/categoriacarga/listar";
                HashMap<String, String> parametros = new HashMap<>();
                parametros.put("token", Sesion.TOKEN);

                //Realizar la petición al servicio web
                String listaClientesJSON = new Helper().requestHttpPost(URL_WS_CategoriaCarga, parametros);

                //Convertir la lista JSON String a JSON Object
                JSONObject jsonObject = new JSONObject(listaClientesJSON);

                if(jsonObject.getBoolean("status")){
                    //Acceder a los datos
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    //Limpiar lista
                    CategoriaCarga.listaCategoriaCarga.clear();

                    for(int i=0; i<jsonArray.length();i++){
                        JSONObject jsonDatosCategoriaCarga = jsonArray.getJSONObject(i);
                        CategoriaCarga categoriaCarga = new CategoriaCarga();
                        categoriaCarga.setId(jsonDatosCategoriaCarga.getInt("id"));
                        categoriaCarga.setNombre(jsonDatosCategoriaCarga.getString("nombre"));
                        CategoriaCarga.listaCategoriaCarga.add(categoriaCarga);
                    }
                    resultado=true;
                }
            }catch (Exception e){
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            return resultado;
        }

        @Override
        protected void onPostExecute(Boolean resultado) {
            super.onPostExecute(resultado);
            if (resultado){
                //Declarar un Array para almacenar solo los nombres de las categorias de carga
                String[] nombreCategoriaCarga = new String[CategoriaCarga.listaCategoriaCarga.size()];

                for (int i=0; i<CategoriaCarga.listaCategoriaCarga.size();i++){
                    nombreCategoriaCarga[i] = CategoriaCarga.listaCategoriaCarga.get(i).getNombre();
                }

                //Crear un adaptador para cargar los datos en el spinner
                ArrayAdapter<String> adaptador = new ArrayAdapter<>
                        (
                                getContext(),
                                android.R.layout.simple_spinner_dropdown_item,
                                nombreCategoriaCarga
                        );

                //Asignar el adaptador al spinner
                spinnerCategoriaCarga.setAdapter(adaptador);

            }else{
                Toast.makeText(getContext(), "No se ha podido descargar la lista de categorias de carga", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class DistanciaTask extends AsyncTask<Void, Void, Boolean> {
        private String latitudPartida;
        private String longitudPartida;
        private String latitudLlega;
        private String longitudLlegada;
        private String token;

        public DistanciaTask(String latitudPartida, String longitudPartida, String latitudLlega, String longitudLlegada, String token) {
            this.latitudPartida = latitudPartida;
            this.longitudPartida = longitudPartida;
            this.latitudLlega = latitudLlega;
            this.longitudLlegada = longitudLlegada;
            this.token = token;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean resultado=false;
            try {
                String URL_WS_Distancia = Helper.BASE_URL_WS + "/ubicacion/obtener_distancia_recorrido";
                HashMap<String, String> parametros = new HashMap<>();

                parametros.put("token", this.token);
                parametros.put("latitud_partida", this.latitudPartida);
                parametros.put("longitud_partida", this.longitudPartida);
                parametros.put("latitud_llegada", this.latitudLlega);
                parametros.put("longitud_llegada", this.longitudLlegada);

                //Realizar la petición al servicio web
                String respuestaJSONVenta = new Helper().requestHttpPost(URL_WS_Distancia, parametros);

                //Convetir la respuesta del servicio web a objeto json
                JSONObject json = new JSONObject(respuestaJSONVenta);

                if (json.getBoolean("status")) {
                    RegistrarSolicitudFragment.this.distancia = json.getDouble("data");
                    resultado = true;
                }else {
                    RegistrarSolicitudFragment.this.distancia = 0.0d;
                }

            }catch(Exception e) {
                e.printStackTrace();
            }
            return resultado;
        }

        @Override
        protected void onPostExecute(Boolean resultado) {
            super.onPostExecute(resultado);
            if (resultado) Toast.makeText(getContext(), "Se calculo la distancia: "+RegistrarSolicitudFragment.this.distancia+" Km", Toast.LENGTH_SHORT).show();
        }
    }

    private class RegistrarSolicitudCargaTask extends AsyncTask<Void, Void, Boolean>{
        private String descripcionCarga;
        private String claseCargaId;
        private String tipoCargaId;
        private String categoriaCargaId;
        private String pesoCarga;
        private String fechaHoraPartida;
        private String direccionPartida;
        private String direccionLlegada;
        private String monto;
        private String numDocCliente;
        private String tarifaId;
        private String token;

        public RegistrarSolicitudCargaTask(String descripcionCarga, String claseCargaId, String tipoCargaId, String categoriaCargaId, String pesoCarga, String fechaHoraPartida, String direccionPartida, String direccionLlegada, String monto, String numDocCliente, String tarifaId, String token) {
            this.descripcionCarga = descripcionCarga;
            this.claseCargaId = claseCargaId;
            this.tipoCargaId = tipoCargaId;
            this.categoriaCargaId = categoriaCargaId;
            this.pesoCarga = pesoCarga;
            this.fechaHoraPartida = fechaHoraPartida;
            this.direccionPartida = direccionPartida;
            this.direccionLlegada = direccionLlegada;
            this.monto = monto;
            this.numDocCliente = numDocCliente;
            this.tarifaId = tarifaId;
            this.token = token;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Log.d("Regist", "doInBackground");
            boolean resultado=false;
            try {
                String URL_WS_Venta = Helper.BASE_URL_WS + "/solicitud_servicio/registrar";
                HashMap<String, String> parametros = new HashMap<>();

                parametros.put("descripcion_carga", this.descripcionCarga);
                parametros.put("clase_carga_id", this.claseCargaId);
                parametros.put("tipo_carga_id", this.tipoCargaId);
                parametros.put("categoria_carga_id", this.categoriaCargaId);
                parametros.put("peso_carga", this.pesoCarga);
                parametros.put("fecha_hora_partida", this.fechaHoraPartida);
                parametros.put("direccion_partida", this.direccionPartida);
                parametros.put("direccion_llegada", this.direccionLlegada);
                parametros.put("monto", this.monto);
                parametros.put("num_doc_cliente", this.numDocCliente);
                parametros.put("tarifa_id", this.tarifaId);
                parametros.put("token", this.token);

                //Realizar la petición al servicio web
                String respuestaJSONVenta = new Helper().requestHttpPost(URL_WS_Venta, parametros);

                //Convetir la respuesta del servicio web a objeto json
                JSONObject json = new JSONObject(respuestaJSONVenta);

                Log.d("Regis", json.getString("data"));

                resultado = json.getBoolean("status");

            }catch (Exception e){
                e.printStackTrace();
            }
            return resultado;
        }

        @Override
        protected void onPostExecute(Boolean resultado) {
            super.onPostExecute(resultado);

            if (resultado) {
                Helper.mensajeInformacion(getContext(), "Solicitud", "Se registro correctamente su solicitud \n Monto total: "+ RegistrarSolicitudFragment.this.monto);
                editTxtDescripcionCarga.setText("");
                editTxtHora.setText("");
                editTxtFecha.setText("");
                editTextDireccionPartida.setText("");
                editTextDireccionLlegada.setText("");
                editTxtPeso.setText("");
                editTxtMonto.setText("");
            }else {
                Helper.mensajeError(getContext(), "Solicitud", "No se puedo registrar su solicitud");
            }
        }
    }

}