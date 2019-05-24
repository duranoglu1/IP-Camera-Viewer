package com.example.eklemeislemi;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvException;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;


public class MainActivity extends AppCompatActivity {

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i("Exception", "OpenCV loaded successfully");
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        Button captureButton = findViewById(R.id.button);
        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                captureImage();
            }
        });
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d("Exception", "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, mLoaderCallback);
        } else {
            Log.d("Exception", "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }


    // captures an image from camera and displays in the ImageView in the app
    public void captureImage() {
        Mat imageArray = new Mat();
        VideoCapture videoDevice = new VideoCapture();
        // verilen adresteki yayını yakalar
        //videoDevice.open("http://192.168.1.8");
         videoDevice.open("http://1jfiegbqnbxcq:Duranoglu1@192.168.1.8");
        // Kullanıcı girişi gerektiren yayınlar için aşağıdaki gibi düzenlemeniz gerekmekte
        //videoDevice.open("http://<username:password>@<ip_address>/video.cgi?.mjpg")
        if (videoDevice.isOpened()) {
            // video aygıtından bir kare oku ve mat nesnesine yerleştir
            videoDevice.read(imageArray);
            videoDevice.release();

            // let's put the camera image to ImageView to display in the app
            ImageView imageView = findViewById(R.id.imageView);
            imageView.setImageBitmap(convertMatToBitMap(imageArray));

            Log.d("D1", "Kameradan goruntu alindi");

        } else {
            Log.d("D1", "Goruntu almada hata");
            Log.d("Exception", "IP kamera aygıtına bağlanılamadı.");
        }
    }

    private static Bitmap convertMatToBitMap(Mat input){
        Bitmap bmp = null;
        Mat rgb = new Mat();
        Imgproc.cvtColor(input, rgb, Imgproc.COLOR_BGR2RGB);

        try {
            bmp = Bitmap.createBitmap(rgb.cols(), rgb.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(rgb, bmp);
        }
        catch (CvException e){
            Log.d("Exception",e.getMessage());
        }
        return bmp;
    }

//    static {
//        System.loadLibrary("opencv");
//        System.load("/Users/duranoglu1/Downloads/OpenCV-android-sdk/sdk/native/libs/x86_64/libopencv_java4.so");
//    }

//    public static void main(String[] args) {
//        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
//        Mat imageArray = new Mat();
//        VideoCapture videoDevice = new VideoCapture();
//        // verilen adresteki yayını yakalar
//        videoDevice.open("http://192.168.1.51/mjpg/stream.cgi");
//        // Kullanıcı girişi gerektiren yayınlar için aşağıdaki gibi düzenlemeniz gerekmekte
//        //videoDevice.open("http://<username:password>@<ip_address>/video.cgi?.mjpg")
//        if (videoDevice.isOpened()) {
//            // video aygıtından bir kare oku ve mat nesnesine yerleştir
//            videoDevice.read(imageArray);
//            videoDevice.release();
//        } else {
//            System.out.println("IP kamera aygıtına bağlanılamadı.");
//        }
//    }
}
