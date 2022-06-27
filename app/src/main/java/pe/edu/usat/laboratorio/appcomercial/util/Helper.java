package pe.edu.usat.laboratorio.appcomercial.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Spinner;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import pe.edu.usat.laboratorio.appcomercial.R;

public class Helper {
    public static final String APP_NAME = "Comercial: ServiciosWeb";
    //public static final String BASE_URL_WS = "http://10.0.2.2:3007";
    public static final String BASE_URL_WS = "http://192.168.0.8:81";

    public String requestHttpPost(String requestURL, HashMap<String, String> postDataParams) throws JSONException {

        URL url;
        String response = "";
        try {
            url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.addRequestProperty("Accept-Charset", "UTF-8");
            conn.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.addRequestProperty("Accept", "*/*");
            conn.addRequestProperty("User-Agent", "***SDK/1.0");
            conn.setRequestProperty("Cache-Control", "no-cache");

            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(false);

            OutputStream os = conn.getOutputStream();

            if (postDataParams != null){
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));
                writer.flush();
                writer.close();
            }

            os.close();
            int responseCode=conn.getResponseCode();

            //System.out.println("RRRR: " + responseCode);
            Log.e("http status", String.valueOf(responseCode));

            String line;
            BufferedReader br;
            if (responseCode == HttpURLConnection.HTTP_OK) {
                br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "iso-8859-1"), 1024);
            }else{
                br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "iso-8859-1"), 1024);
            }

            while ((line = br.readLine()) != null) {
                response += line;
            }

            //Log.e("Respuesta", response);

        }catch (FileNotFoundException e2){
            JSONObject rpta = new JSONObject();
            rpta.put("status", false);
            rpta.put("data", e2.getMessage());
            return rpta.toString();
            //e2.printStackTrace();
        } catch (Exception e) {
            JSONObject rpta = new JSONObject();
            rpta.put("status", false);
            rpta.put("data", e.getMessage());
            return rpta.toString();
            //e.printStackTrace();
        }

        return response;
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException{
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }

    public static Bitmap base64ToImage(String image){
        try{
            String imageDataBytes = image.substring(image.indexOf(",") + 1);
            InputStream stream = new ByteArrayInputStream(Base64.decode(imageDataBytes.getBytes(), Base64.DEFAULT));
            Bitmap bitmap = BitmapFactory.decodeStream(stream);
            Log.v("Base 64", "Convietiendo de base 64 a imágen --> ok");
            return bitmap;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Bitmap descargarImagenURL(String url) {
        HttpURLConnection urlConnection = null;
        try {
            URL uri = new URL(url);
            urlConnection = (HttpURLConnection) uri.openConnection();

            int statusCode = urlConnection.getResponseCode();
            if (statusCode != 200) {
                return null;
            }

            InputStream inputStream = urlConnection.getInputStream();
            if (inputStream != null) {
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
            }
        } catch (Exception e) {
            e.printStackTrace();
            urlConnection.disconnect();
            Log.w("ImageDownloader", "Error downloading image from " + url);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null;
    }

    public Bitmap descargarImagen (String imageHttpAddress){
        URL imageUrl = null;
        Bitmap imagen = null;
        try{
            imageUrl = new URL(imageHttpAddress);
            HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
            conn.connect();
            imagen = BitmapFactory.decodeStream(conn.getInputStream());
        }catch(IOException ex){
            ex.printStackTrace();
        }

        return imagen;
    }

    public String obtenerFechaActual(){
        Date fecha = new Date();
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd-MM-yyyy");
        return formatoFecha.format(fecha);
    }

    public static String formatearNumero(double numero){
        DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
        simbolos.setDecimalSeparator('.');
        simbolos.setGroupingSeparator(',');

        DecimalFormat formato = new DecimalFormat("###,###.00", simbolos);

        return formato.format(numero);

    }

    public static String formatearNumero4(double numero){
        DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
        simbolos.setDecimalSeparator('.');
        simbolos.setGroupingSeparator(',');

        DecimalFormat formato = new DecimalFormat("###,###.0000", simbolos);

        return formato.format(numero);

    }

    public boolean isOnline(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable()) {

                return checkHttpConnection();
                //return true;
            }
        }
        return false;
    }

    public boolean checkHttpConnection() {
        String link = "http://www.google.com.pe";
        URL url = null;
        try {
            url = new URL(link);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.connect();

            int resCode = conn.getResponseCode();

            if (resCode == 200){
                return true;
            }

            //System.out.println("Response code:===========" + resCode);

        } catch (MalformedURLException e) {
            return false;

            //e.printStackTrace();
        }catch (IOException e2){
            //System.out.println(e2.);
            return false;
        }

        return false;

    }

    public static String convertPassMd5(String pass) {
        String password = null;
        MessageDigest mdEnc;
        try {
            mdEnc = MessageDigest.getInstance("MD5");
            mdEnc.update(pass.getBytes(), 0, pass.length());
            pass = new BigInteger(1, mdEnc.digest()).toString(16);
            while (pass.length() < 32) {
                pass = "0" + pass;
            }
            password = pass;
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }
        return password;
    }


    public static void configurarAlmacenamientoCache(Context app){
        //Configurar la librería para guaradar en el cache
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder( app );
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app
        ImageLoader.getInstance().init(config.build());
    }

    public static DisplayImageOptions configurarOpcionesDescargaImagenes(){
        /*Configurar la descarga de las fotos de los productos*/
        DisplayImageOptions options;
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_stub)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(new FadeInBitmapDisplayer(1500))
                .build();
        /*Configurar la descarga de las fotos de los productos*/

        return options;
    }





    public static void habilitarDirectivasInternetX(){
        /*Si la versión del SO es mayor a la versión de GINGERBARD, entonces habilita una politica especial para conectarse a internet*/
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        /*Si la versión del SO es mayor a la versión de GINGERBARD, entonces habilita una politica especial para conectarse a internet*/
    }

    public static void asignarColorSwipeLayout(SwipeRefreshLayout swipeContenedor){
        swipeContenedor.setColorScheme
                (
                        android.R.color.holo_red_light,
                        android.R.color.holo_green_light,
                        android.R.color.holo_orange_light,
                        android.R.color.holo_blue_bright
                );
    }


    /*


    public static boolean mensajeConfirmacion(Context context, String title, String message) {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message mesg) {
                throw new RuntimeException();
            }
        };

        // make a text input dialog and show it
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle(title);
        alert.setMessage(message);
        alert.setIcon(R.drawable.question);
        alert.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                mResult = true;
                handler.sendMessage(handler.obtainMessage());
            }
        });
        alert.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                mResult = false;
                handler.sendMessage(handler.obtainMessage());
            }
        });
        alert.show();

        // loop till a runtime exception is triggered.
        try {
            Looper.loop();
        }
        catch(RuntimeException e2) {

        }

        return mResult;
    }
    */

    private static boolean mResult;

    public static boolean mensajeInformacion(Context context, String title, String message) {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message mesg) {
                throw new RuntimeException();
            }
        };

        // make a text input dialog and show it
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle(title);
        alert.setMessage(message);
        alert.setIcon(R.drawable.ic_info);
        alert.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                mResult = true;
                handler.sendMessage(handler.obtainMessage());
            }
        });

        alert.show();

        // loop till a runtime exception is triggered.
        try {
            Looper.loop();
        }
        catch(RuntimeException e2) {

        }

        return mResult;
    }


    public static boolean mensajeError(Context context, String title, String message) {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message mesg) {
                throw new RuntimeException();
            }
        };

        // make a text input dialog and show it
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle(title);
        alert.setMessage(message);
        alert.setIcon(R.drawable.ic_error);
        alert.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                mResult = true;
                handler.sendMessage(handler.obtainMessage());
            }
        });

        alert.show();

        // loop till a runtime exception is triggered.
        try {
            Looper.loop();
        }
        catch(RuntimeException e2) {

        }

        return mResult;
    }


    public static void selectedItemSpinner(Spinner spinner, String itemSelection){
        int position = 0;
        for (int i=0; i<spinner.getCount(); i++){
            if (spinner.getItemAtPosition(i).equals(itemSelection)){
                position = i;
                break;
            }
        }
        spinner.setSelection(position);
    }

    public static ArrayList<String> getJSONListValues(JSONObject params) throws Exception {
        ArrayList<String> list = new ArrayList<String>();
        Iterator<String> itr = params.keys();

        while(itr.hasNext()){
            String key= itr.next();
            Object value = params.get(key);
            list.add(value.toString());
            //System.out.println("a"+key);
        }
        return list;
    }


    public static void ocultarTeclado(Activity activity){
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public static void mostarTeclado(Activity activity){
        InputMethodManager imm = (InputMethodManager)
                (activity).getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }


    public static void habilitarDirectivaVerificaConeccionInternet(){

        //Habilitar la directiva para verificar si el telefono tiene conexión a internet

        /*  Se hace la habilitación de esta directa, siempre y cuando el SO del telefono
            sea mayor a la versión de GINGERBARD
         */
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

    public static String imageToBase64(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String imageDecode = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return imageDecode;
    }



}