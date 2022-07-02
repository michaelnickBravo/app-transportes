package pe.edu.usat.laboratorio.appcomercial;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import pe.edu.usat.laboratorio.appcomercial.adaptador.AdaptadorListaClientes;
import pe.edu.usat.laboratorio.appcomercial.adaptador.AdaptadorListaSolicitudes;
import pe.edu.usat.laboratorio.appcomercial.logica.Cliente;
import pe.edu.usat.laboratorio.appcomercial.logica.EstadoCliente;
import pe.edu.usat.laboratorio.appcomercial.logica.Sesion;
import pe.edu.usat.laboratorio.appcomercial.logica.Solicitud;
import pe.edu.usat.laboratorio.appcomercial.logica.Vehiculo;
import pe.edu.usat.laboratorio.appcomercial.util.Helper;


public class SolicitudesTransporteCarga extends Fragment{

    RecyclerView recyclerViewSolicitudes;
    ArrayList<Solicitud> listaSolicitudesWS;
    AdaptadorListaSolicitudes adaptadorListaSolicitudes;
    ProgressDialog dialog;
    SearchView txtbusquedaV;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_solicitudes_transporte_carga, container, false);
        //Mostrar un titulo para el fragment
        this.getActivity().setTitle("LISTADO DE SOLICITUDES DE TRANSPORTE DE CARGA");



        return view;
    }


    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        //Identificar la opción en la que el usuario hizo clic
        switch (item.getItemId()){
            case 1: //
                int pos = adaptadorListaSolicitudes.posicionItemSeleccionadoRecyclerView;
                Intent intent = new Intent(getActivity(), DialogGestionestadosSolicitud.class);
                Bundle extras = new Bundle();

                Solicitud datos = listaSolicitudesWS.get(pos);
                String id=String.valueOf(datos.getId());
                String dni=datos.getNum_doc_cliente();
                String cliente=datos.getCliente();
                String clase=datos.getClase();
                String direccionP=datos.getDireccionPartida();
                String fecha=datos.getFecaPartida();
                String direccionL=datos.getDireccionLlegada();
                String tipo=datos.getTipo();

                extras.putString("id", id);
                extras.putString("dni", dni);
                extras.putString("cliente", cliente);
                extras.putString("direccionP", direccionP);
                extras.putString("fecha", fecha);
                extras.putString("direccionL", direccionL);
                extras.putString("clase", clase);
                extras.putString("tipo", tipo);

                intent.putExtras(extras);
                startActivity(intent);
                break;
            case 2: //
                int pos1 = adaptadorListaSolicitudes.posicionItemSeleccionadoRecyclerView;
                Intent intent1 = new Intent(getActivity(), dialog_gestion_asignacion_Vehiculo_conductor.class);
                Bundle extras1 = new Bundle();
                Log.e("proror","gwddskjsm,fsk");

                Solicitud datos1 = listaSolicitudesWS.get(pos1);
                String id1=String.valueOf(datos1.getId());
                String dni1=datos1.getNum_doc_cliente();
                String cliente1=datos1.getCliente();
                String clase1=datos1.getClase();
                String direccionP1=datos1.getDireccionPartida();
                String fecha1=datos1.getFecaPartida();
                String direccionL1=datos1.getDireccionLlegada();
                String tipo1=datos1.getTipo();

                extras1.putString("id", id1);
                extras1.putString("dni", dni1);
                extras1.putString("cliente", cliente1);
                extras1.putString("direccionP", direccionP1);
                extras1.putString("fecha", fecha1);
                extras1.putString("direccionL", direccionL1);
                extras1.putString("clase", clase1);
                extras1.putString("tipo", tipo1);

                intent1.putExtras(extras1);
                startActivity(intent1);
                break;

        }
        return true;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerViewSolicitudes = view.findViewById(R.id.recycler_view_solicitudes_carga);
        recyclerViewSolicitudes.setHasFixedSize(true);
        recyclerViewSolicitudes.setLayoutManager(new LinearLayoutManager(this.getContext()));
        this.listarSolicitudes();
    }

    private void listarSolicitudes() {

        //Mostrar un cuadro de dialogo indicando que se esta descargando la lista de solicitudes.
        dialog = new ProgressDialog(this.getContext());
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Descargando la lista de solicitudes, por favor espere...");
        dialog.setCancelable(false);
        dialog.show();

        //Inicializar el ArrayList.
        listaSolicitudesWS = new ArrayList<>();

        //Inicializar el adaptador.
        adaptadorListaSolicitudes = new AdaptadorListaSolicitudes(this.getContext());
        recyclerViewSolicitudes.setAdapter(adaptadorListaSolicitudes);
        //Ejecutar la clase CatalogoProductoTask
        new SolicitudesTransporteCarga.ListarSolicitudesTask().execute();

    }

    private class ListarSolicitudesTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean resultado = false;
            try {
                String URL_WS_ListaSolicitudes = Helper.BASE_URL_WS + "/solicitud_servicio/listar_todas_solicitud";
                HashMap<String, String> parametros = new HashMap<>();
                //parametros.put("token", Sesion.TOKEN);

                //Realizar la petición (request) del servicio web
                String resultadoJSON = new Helper().requestHttpPost(URL_WS_ListaSolicitudes, parametros);

                JSONObject jsonListaSolicitudes = new JSONObject(resultadoJSON);
                if (jsonListaSolicitudes.getBoolean("status")) {
                    //Acceder a los datos de la lista de solicitudes
                    JSONArray jsonArraySolicitudes = jsonListaSolicitudes.getJSONArray("data");

                    //Limpia el almacen de datos
                    listaSolicitudesWS.clear();

                    for (int i = 0; i < jsonArraySolicitudes.length(); i++) {
                        //Capturar los datos de cada solicitud
                        JSONObject jsonSolicitud = jsonArraySolicitudes.getJSONObject(i);

                        //Instanciar un objeto de la clase Solicitud para guardar los datos
                        Solicitud solicitud = new Solicitud();
                        solicitud.setCategoria(jsonSolicitud.getString("categoria"));
                        solicitud.setClase(jsonSolicitud.getString("clase"));
                        solicitud.setDescripcionCarga(jsonSolicitud.getString("descripcion_carga"));
                        solicitud.setDireccionLlegada(jsonSolicitud.getString("direccion_llegada"));
                        solicitud.setDireccionPartida(jsonSolicitud.getString("direccion_partida"));
                        String[] fechaHoraPartida = jsonSolicitud.getString("fecha_hora_partida").split("T");
                        solicitud.setFecaPartida(fechaHoraPartida[0]);
                        solicitud.setHoraPartida(fechaHoraPartida[1]);
                        solicitud.setId(jsonSolicitud.getInt("id"));
                        solicitud.setMonto(jsonSolicitud.getDouble("monto"));
                        solicitud.setPesoCarga(jsonSolicitud.getDouble("peso_carga"));
                        solicitud.setTarifa(jsonSolicitud.getDouble("precio_tn_kg"));
                        solicitud.setTipo(jsonSolicitud.getString("tipo"));
                        solicitud.setNum_doc_cliente(jsonSolicitud.getString("num_doc_cliente"));
                        solicitud.setCliente(jsonSolicitud.getString("cliente"));

                        //Almacenar el objeto solicitud en el almacen de datos
                        listaSolicitudesWS.add(solicitud);
                    }

                    //Retornar un valor verdadero
                    resultado = true;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return resultado;
        }

        @Override
        protected void onPostExecute(Boolean resultado) {
            super.onPostExecute(resultado);
            if (resultado) {
                //Se ha descargado la lista de solicitudes
                //Enviar los datos almacenados en el ArrayList al adaptador
                adaptadorListaSolicitudes.cargarListaSolicitudes(listaSolicitudesWS);
                //Cerrar el cuadro de dialogo de descarga.
                dialog.dismiss();
            }
        }
    }

}