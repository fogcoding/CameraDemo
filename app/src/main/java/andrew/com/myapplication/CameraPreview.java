package andrew.com.myapplication;

/**
 * Created by Andrew on 2016/5/9.
 */

import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * 相机图片预览类
 *
 * @author
 */
public class CameraPreview extends SurfaceView implements
        SurfaceHolder.Callback {

    private SurfaceHolder mHolder;
    private Camera pCamera;
    private int values = 5;   //相机的初始焦距值

    public Camera getCamera(){
        return pCamera;
    }

    public void CamaeraChange(int a){
        pCamera.stopPreview();
        pCamera.release();
        pCamera = null;
        pCamera = Camerainit(a);
        try {
            pCamera.setPreviewDisplay(mHolder);
            pCamera.setDisplayOrientation(90);
            pCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public CameraPreview(Context context) {
        super(context);
        //pCamera = camera;
        pCamera = Camerainit(0);

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        try {
            pCamera.setPreviewDisplay(holder);
            pCamera.setDisplayOrientation(90);
            //pCamera.startPreview();

        } catch (IOException e) {
            //Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // empty. Take care of releasing the Camera preview in your activity.
        pCamera.stopPreview();
        pCamera.release();
        pCamera = null;

    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (mHolder.getSurface() == null){
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            pCamera.stopPreview();
        } catch (Exception e){
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here

        // start preview with new settings
        try {
            pCamera.setPreviewDisplay(mHolder);
            pCamera.startPreview();
        } catch (Exception e){
            //Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }

    public void takePicture(Camera.PictureCallback imageCallback) {
        pCamera.takePicture(null, null, imageCallback);
    }

    // Camera open
    public Camera Camerainit(int a){
        Camera initCamera = Camera.open(a);
        Camera.Parameters parameters = initCamera.getParameters();
        parameters.setZoom(values);
        initCamera.setParameters(parameters);
        return initCamera;
    }

    // 闪光灯控制函数
    public void Light(int a){
        if (a == 0){
            pCamera.stopPreview();
            Camera.Parameters parameters = pCamera.getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            pCamera.setParameters(parameters);
            pCamera.startPreview();

        }
        if (a == 1){
            pCamera.stopPreview();
            Camera.Parameters parameters = pCamera.getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            pCamera.setParameters(parameters);
            pCamera.startPreview();

        }
    }

    //焦距调节
    public void Focus(int flag){
        Camera.Parameters parameters = pCamera.getParameters();
        int maxZoom = parameters.getMaxZoom();
        if(flag == -1 && values >= 1){
            pCamera.stopPreview();
            values = values - 1;
            parameters.setZoom(values);
            pCamera.setParameters(parameters);
            pCamera.startPreview();
        }else if (flag == 1 && values <= 9){
            pCamera.stopPreview();
            values = values + 1;
            parameters.setZoom(values);
            pCamera.setParameters(parameters);
            pCamera.startPreview();
        } else {
          return ;
        }
    }

    public void setFocus(){
        pCamera.stopPreview();
        Camera.Parameters parameters = pCamera.getParameters();
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        pCamera.setParameters(parameters);
        pCamera.startPreview();
    }


}

