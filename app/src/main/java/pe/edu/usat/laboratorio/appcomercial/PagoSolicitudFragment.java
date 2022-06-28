package pe.edu.usat.laboratorio.appcomercial;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import pe.edu.usat.laboratorio.appcomercial.logica.Sesion;
import pe.edu.usat.laboratorio.appcomercial.logica.Solicitud;
import pe.edu.usat.laboratorio.appcomercial.logica.TipoCarga;
import pe.edu.usat.laboratorio.appcomercial.util.Gallery;
import pe.edu.usat.laboratorio.appcomercial.util.Helper;

public class PagoSolicitudFragment extends Fragment implements View.OnClickListener {

    ImageView imgViewVoucher;
    EditText textViewNombreEntidad, textViewNumOperacion;
    Spinner spinnerSolicitud;
    Button buttonRegistrarPago;

    public static ArrayList<Solicitud> listaSolicitudes = new ArrayList<>();

    private static final int REQUEST_PICK = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pago_solicitud, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textViewNombreEntidad = view.findViewById(R.id.text_view_nombre_entidad);
        spinnerSolicitud = view.findViewById(R.id.spinner_solicitud);
        buttonRegistrarPago = view.findViewById(R.id.button_registrar_pago);
        textViewNumOperacion = view.findViewById(R.id.text_view_num_operacion);
        imgViewVoucher = view.findViewById(R.id.img_view_voucher);
        imgViewVoucher.setOnClickListener(this);
        imgViewVoucher.setTag("foto_no_es_real");
        buttonRegistrarPago.setOnClickListener(this);

        new SolicitudesPendientes().execute();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.img_view_voucher:
                startActivityForResult(new Intent(Intent.ACTION_PICK).setType("image/*"), REQUEST_PICK );
                break;

            case R.id.button_registrar_pago:
                registrarPago();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_PICK && resultCode == Activity.RESULT_OK) {
            //Capturar la imagen seleccionada en la galería
            Uri rutaImagen = data.getData();
            try{
                Bitmap bitmap = Gallery.rotateImage(this.getActivity(), rutaImagen, Gallery.getOrientation(this.getActivity(), rutaImagen));
                Bitmap bitmapCompress = Gallery.compress(bitmap);
                imgViewVoucher.setImageBitmap(bitmapCompress);
                imgViewVoucher.setTag("foto_real");
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    private void registrarPago() {
        String imgVoucher = imgViewVoucher.getTag().equals("foto_real") ? Helper.imageToBase64(((BitmapDrawable)imgViewVoucher.getDrawable()).getBitmap()) : "NO SE GUARDO IMAGEN";
        String nombreFinanciera = textViewNombreEntidad.getText().toString();
        String numOperacion = textViewNumOperacion.getText().toString();
        String solicitudId = spinnerSolicitud.getSelectedItem().toString().split("\\.")[0];
        String estadoPagoSolicitud = "6";
        String token = Sesion.TOKEN;

        new RegistrarPago(imgVoucher, nombreFinanciera, numOperacion, solicitudId, estadoPagoSolicitud,token).execute();

    }

    private class RegistrarPago extends AsyncTask<Void, Void, Boolean> {

        String imgVoucher, nombreFinanciera, numOperacion, solicitudId, estadoSolicitud, token;

        public RegistrarPago(String imgVoucher, String nombreFinanciera, String numOperacion, String solicitudId, String estadoSolicitud, String token) {
            this.imgVoucher = imgVoucher;
            this.nombreFinanciera = nombreFinanciera;
            this.numOperacion = numOperacion;
            this.solicitudId = solicitudId;
            this.estadoSolicitud = estadoSolicitud;
            this.token = token;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean resultado = false;
            try {
                String URL_WS_Registrar = Helper.BASE_URL_WS + "/pago_servicio/registrarPago";
                HashMap<String, String> parametros = new HashMap<>();

                parametros.put("nombre_financiera", this.nombreFinanciera);
                parametros.put("num_operacion", this.numOperacion);
                parametros.put("voucher", this.imgVoucher);
                parametros.put("estado_pago_id", this.estadoSolicitud);
                parametros.put("solicitud_id", this.solicitudId);
                parametros.put("token", this.token);

                //Realizar la petición al servicio web
                String respuestaJSONRegistro = new Helper().requestHttpPost(URL_WS_Registrar, parametros);

                //Convetir la respuesta del servicio web a objeto json
                JSONObject json = new JSONObject(respuestaJSONRegistro);
                Log.d("PagoSoli", json.toString());
                resultado = json.getBoolean("status");

            }catch (Exception e) {
                e.printStackTrace();
            }
            return resultado;
        }

        @Override
        protected void onPostExecute(Boolean resultado) {
            super.onPostExecute(resultado);

            if (resultado) {
                final AlertDialog.Builder dialog = new AlertDialog.Builder(PagoSolicitudFragment.this.getContext());
                dialog.setTitle("Se registro correctamente");
                dialog.setIcon(R.drawable.ic_question);
                dialog.setMessage("Espera a que la empresa verifique su pago");
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
            }else{
                Toast.makeText(PagoSolicitudFragment.this.getContext(), "No se pudo registrar el pago", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class SolicitudesPendientes extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean resultado = false;
            try {
                String URL_WS_TipoCarga = Helper.BASE_URL_WS + "/solicitud_servicio/listar_solicitud_pendientes_cliente";
                HashMap<String, String> parametros = new HashMap<>();
                parametros.put("token", Sesion.TOKEN);
                parametros.put("cliente_id", Sesion.NUM_DOCUMENTO);

                //Realizar la petición al servicio web
                String listaSolicitudesJSON = new Helper().requestHttpPost(URL_WS_TipoCarga, parametros);

                //Convertir la lista JSON String a JSON Object
                JSONObject jsonObject = new JSONObject(listaSolicitudesJSON);

                if(jsonObject.getBoolean("status")){
                    //Acceder a los datos
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    //Limpiar lista
                    listaSolicitudes.clear();

                    for(int i=0; i<jsonArray.length();i++){
                        JSONObject jsonDatosSolicitud = jsonArray.getJSONObject(i);
                        Solicitud solicitud = new Solicitud();
                        solicitud.setId(jsonDatosSolicitud.getInt("id"));
                        solicitud.setHora_fecha(jsonDatosSolicitud.getString("fecha_hora"));
                        solicitud.setMonto(jsonDatosSolicitud.getDouble("monto"));
                        listaSolicitudes.add(solicitud);
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
                String[] nombreSolicitudes = new String[listaSolicitudes.size()];

                for (int i=0; i<listaSolicitudes.size();i++){
                    Solicitud solicitud1 = listaSolicitudes.get(i);
                    String[] fechaHora = solicitud1.getHora_fecha().split("T");
                    nombreSolicitudes[i] = solicitud1.getId() +". "+fechaHora[0]+" "+fechaHora[1]+" S/."+solicitud1.getMonto();
                }

                //Crear un adaptador para cargar los datos en el spinner
                ArrayAdapter<String> adaptador = new ArrayAdapter<>
                        (
                                PagoSolicitudFragment.this.getContext(),
                                android.R.layout.simple_spinner_dropdown_item,
                                nombreSolicitudes
                        );

                //Asignar el adaptador al spinner
                spinnerSolicitud.setAdapter(adaptador);

            }else{
                Toast.makeText(getContext(), "No se ha podido descargar la lista de solicitudes", Toast.LENGTH_SHORT).show();
            }
        }

    }

}