package com.example.face;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import dmax.dialog.SpotsDialog;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.face.Helper.GraphicOverlay;
import com.example.face.Helper.RectOverlay;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;
import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button btn;
    GraphicOverlay faceD;// needed for the rectangular box used in detecting the faces
    CameraView cam;
    AlertDialog alert;//special dialog box for displaying a message while processing

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn=findViewById(R.id.btn);
        faceD=findViewById(R.id.faceD);
        cam=findViewById(R.id.cam);
        alert=new SpotsDialog.Builder().setContext(this).setMessage("Wait for it").
                setCancelable(false).build();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cam.start();
                cam.captureImage();
                faceD.clear();
            }
        });
        cam.addCameraKitListener(new CameraKitEventListener() {
            @Override
            public void onEvent(CameraKitEvent cameraKitEvent) {

            }

            @Override
            public void onError(CameraKitError cameraKitError) {

            }

            @Override
            public void onImage(CameraKitImage cameraKitImage) {
                alert.show();
                Bitmap img=cameraKitImage.getBitmap();//getting the bitmap from the camera
                img=Bitmap.createScaledBitmap(img,cam.getWidth(),cam.getHeight(),false);//false is for filters
                cam.stop();
                detect(img);//the main method used for detection

            }

            @Override
            public void onVideo(CameraKitVideo cameraKitVideo) {

            }
        });
    }

    private void detect(Bitmap img) {
        FirebaseVisionImage fireVImage=FirebaseVisionImage.fromBitmap(img);//the lib for Computer Vision
        FirebaseVisionFaceDetectorOptions fireFaceOptions=new FirebaseVisionFaceDetectorOptions.
                Builder().build();//builder to build the detector
        FirebaseVisionFaceDetector fireFace= FirebaseVision.getInstance().getVisionFaceDetector(fireFaceOptions);//the detector
        fireFace.detectInImage(fireVImage).addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionFace>>() {
            @Override
            public void onSuccess(List<FirebaseVisionFace> firebaseVisionFaces) {
                getFaceResult(firebaseVisionFaces);//method for collecting the results

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "LOOSER"+e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }
String c;// the string used show the number in a toast

    private void getFaceResult(List<FirebaseVisionFace> firebaseVisionFaces) {//list contains the detected faces
        int count=0;//face counter
        for(FirebaseVisionFace face : firebaseVisionFaces){//for every face in the list
            Rect rect= face.getBoundingBox();
            RectOverlay rectOverlay=new RectOverlay(faceD,rect);//custom class for the charactaristics of the rectangle
            faceD.add(rectOverlay);
            count++;

        }
        c=Integer.toString(count);
        alert.dismiss();
        Toast.makeText(this,c, Toast.LENGTH_LONG).show();

    }

    @Override
    protected void onPause() {
        super.onPause();
        cam.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cam.start();
    }
}
