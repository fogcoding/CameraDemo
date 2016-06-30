package andrew.com.myapplication;

import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private Button btnCapture;
    private Button btnExit;
    private Button btnOpenFlashLight;
    private Button btnChange;
    private Button btnVideo;
    private Button btnFurther;
    private Button btnCloser;
    private int changeCount = 1;
    private int openLightCount = 0; //默认闪光灯是关闭的
    private int FocusFlag = 0;      //  默认不自动对焦


    private Camera myCamera;
    private CameraPreview myCameraPreview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCapture = (Button) findViewById(R.id.btnCapture);
        btnExit = (Button) findViewById(R.id.btnExit);
        btnChange = (Button) findViewById(R.id.btnChange);
        btnOpenFlashLight = (Button) findViewById(R.id.btnOpenFlashLight);
        btnVideo = (Button) findViewById(R.id.btnVideo);
        btnFurther = (Button) findViewById(R.id.btnFurther);
        btnCloser = (Button) findViewById(R.id.btnCloser);

        FrameLayout preview = (FrameLayout) findViewById(R.id.flMain);
        myCameraPreview = new CameraPreview(this);
        preview.addView(myCameraPreview);


        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myCameraPreview.takePicture(new Camera.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] data, Camera camera) {
                        //创建文件夹
                        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                        String time = new SimpleDateFormat("yyyyMMdd_hhmmss").format(new Date());
                        String filedir = dir.getPath() + File.separator +"IMAGE_DUAN" + time + ".jpg";
                        File file = new File(filedir);
                        try {
                            FileOutputStream fos = new FileOutputStream(file);
                            fos.write(data);
                            fos.close();
                        } catch (IOException e) {
                            //Log.d(TAG, "Error accessing file: " + e.getMessage());
                        }
                        camera.startPreview();
                    }
                });
            }
        });

        // exit this Applaication.
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myCameraPreview.CamaeraChange(changeCount);
                if (changeCount == 1){
                    changeCount = 0;
                } else if(changeCount == 0) {
                    changeCount = 1;
                } else {
                    changeCount = 0;
                }
            }
        });

        // open FlashLight!
        btnOpenFlashLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (openLightCount == 0) {
                    myCameraPreview.Light(1);
                    openLightCount = 1;
                }else if (openLightCount == 1){
                    myCameraPreview.Light(0);
                    openLightCount = 0;
                } else {
                  openLightCount = 0;
                }

            }
        });

        btnFurther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myCameraPreview.Focus(-1);
            }
        });

        btnCloser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myCameraPreview.Focus(1);
            }
        });

        btnVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myCameraPreview.setFocus();
            }
        });



    }




}

//  预览类的运行方式有问题，是的后续的操作无法进行
//  需要重新构建代码的组成
//2016.05.17
