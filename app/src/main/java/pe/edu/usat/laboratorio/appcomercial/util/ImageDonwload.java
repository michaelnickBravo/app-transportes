package pe.edu.usat.laboratorio.appcomercial.util;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

public class ImageDonwload {

    public static class ImageDownloadTask extends AsyncTask<Void, Void, Bitmap> {

        private ImageView imageView;
        private String imageURL;

        public ImageDownloadTask(ImageView imageView, String imageURL){
            this.imageView = imageView;
            this.imageURL = imageURL;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            // TODO Auto-generated method stub
            String url = imageURL;
            Bitmap bitmap = new Helper().descargarImagen(url);
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            // TODO Auto-generated method stub
            super.onPostExecute(bitmap);
            imageView.setImageBitmap(bitmap);
        }
    }

}
