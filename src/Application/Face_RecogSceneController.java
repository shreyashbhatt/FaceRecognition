package Application;

import javafx.scene.control.Label;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.face.EigenFaceRecognizer;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;
//import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class Face_RecogSceneController {
	private VideoCapture video_Cptur=new VideoCapture();
	Mat roi;
	private CascadeClassifier face_Cascade=new CascadeClassifier();
	private boolean camera_On=false;
	private int absolute_Facesize=0;
	private static int camera_ID=0;
	private String classifier_Path="C:/Users/sunny/Downloads/opencv/build/etc/haarcascades/haarcascade_frontalface_alt.xml";
	private ScheduledExecutorService videoFrame_time;
	private boolean flag=false;
	@FXML
	Label person_Name;
	@FXML
	Button back_Btn;
	@FXML
	Button start_Btn;
	@FXML
	private ImageView img_View;
	@FXML
	private AnchorPane face_RecogScenePane;
	@FXML
	private void back_BtnClicked() {
		try {
			this.camera_On = false;
			this.stopAcquisition();
			FXMLLoader loader=new FXMLLoader(getClass().getResource("First_Scene.fxml"));
			AnchorPane pane=loader.load();
			face_RecogScenePane.getChildren().setAll(pane);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	@FXML
	protected void start_BtnClicked() {
		if (!this.camera_On)
		{
			// start the video capture
			this.video_Cptur.open(camera_ID);
			
			// is the video stream available?
			if (this.video_Cptur.isOpened())
			{
				
				this.camera_On = true;
				this.face_Cascade.load(classifier_Path);
				
				// grab a frame every 33 ms (30 frames/sec)
				Runnable frameGrabber = new Runnable() {
					
					@Override
					public void run()
					{
						// effectively grab and process a single frame
						Mat frame = grabFrame();
						// convert and show the frame
						BufferedImage img;
						try {
							img = Mat2Image(frame);
							Image image_ToShow = SwingFXUtils.toFXImage(img, null);
							updateImageView(img_View, image_ToShow);
						} catch (Exception e) {
							System.err.println("Error in converting Mat to Image");
							e.printStackTrace();
						}
						
						
					}
				};
				
				this.videoFrame_time = Executors.newSingleThreadScheduledExecutor();
				this.videoFrame_time.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);
				
				// update the button content
				this.start_Btn.setText("Stop Camera");
			}
			else
			{
				// log the error
				System.err.println("Impossible to open the camera connection...");
			}
		}
		else
		{
			// the camera is not active at this point
			this.camera_On = false;
			// update again the button content
			this.start_Btn.setText("Start Camera");
			
			// stop the timer
			this.stopAcquisition();
		}
	
	}
	protected Mat grabFrame() {
		// init everything
				Mat frame = new Mat();
				
				// check if the capture is open
				if (this.video_Cptur.isOpened())
				{
					try
					{
						// read the current frame
						this.video_Cptur.read(frame);
						
						// if the frame is not empty, process it
						if (!frame.empty())
						{
							Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2BGRA);
							this.detectAndDisplay(frame);
						}
						
					}
					catch (Exception e)
					{
						// log the error
						System.err.println("Exception during the image elaboration: " + e);
					}
				}
				
				return frame;
	}
	private void detectAndDisplay(Mat frame)
	{
		MatOfRect faces = new MatOfRect();
		Mat grayFrame = new Mat();
		
		// convert the frame in gray scale
		Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY);
		// equalize the frame histogram to improve the result
		Imgproc.equalizeHist(grayFrame, grayFrame);
		
		// compute minimum face size (20% of the frame height, in our case)
		if (this.absolute_Facesize == 0)
		{
			int height = grayFrame.rows();
			if (Math.round(height * 0.2f) > 0)
			{
				this.absolute_Facesize = Math.round(height * 0.2f);
			}
		}
		
		// detect faces
		this.face_Cascade.detectMultiScale(grayFrame, faces, 1.1, 2, 0 | Objdetect.CASCADE_SCALE_IMAGE,
				new Size(this.absolute_Facesize, this.absolute_Facesize), new Size());
				
		// each rectangle in faces is a face: draw them!
		Rect[] facesArray = faces.toArray();
		for (int i = 0; i < facesArray.length; i++) {
			if(flag==false) {
			roi =new Mat(frame,facesArray[i]);
			Thread t1 = new Thread(new MyClass (roi));
			t1.start();
			flag=true;
			}
			Imgproc.rectangle(frame, facesArray[i].tl(), facesArray[i].br(), new Scalar(0, 255, 0), 3);
		}
		
	}

	
	protected void updateImageView(ImageView img_View2, Image imageToShow) {
		Utils.onFXThread(img_View2.imageProperty(), imageToShow);
		
	}
	private void stopAcquisition() {
		if (this.videoFrame_time!=null && !this.videoFrame_time.isShutdown())
		{
			try
			{
				// stop the timer
				this.videoFrame_time.shutdown();
				this.videoFrame_time.awaitTermination(33, TimeUnit.MILLISECONDS);
			}
			catch (InterruptedException e)
			{
				// log any exception
				System.err.println("Exception in stopping the frame capture, trying to release the camera now... " + e);
			}
		}
		
		if (this.video_Cptur.isOpened())
		{
			// release the camera
			this.video_Cptur.release();
		}
		
	}

	protected BufferedImage Mat2Image(Mat frame)throws Exception {        
	    MatOfByte mob=new MatOfByte();
	    Imgcodecs.imencode(".jpg", frame, mob);
	    byte ba[]=mob.toArray();

	    BufferedImage bi=ImageIO.read(new ByteArrayInputStream(ba));
	    return bi;
	}


}
class MyClass implements Runnable {
	Mat roi=new Mat();
	public MyClass(Mat frame) {
		roi=frame;
	}
	
public void run(){
	ArrayList<Mat> img_store=new ArrayList<Mat>();
	ArrayList<String> name=new ArrayList<String>();
	Connection cn=DB_Connection.connectdb();
	try {
		Statement stmt=cn.createStatement();
		ResultSet rs=stmt.executeQuery("Select * from user_database");
		while (rs.next()) {
			//System.out.printf("%s\t%s\n",rs.getString(1),rs.getString(2));
			img_store.add(Img_ToMat(rs.getString(1)));
			name.add(rs.getString(2));
		}
		EigenFaceRecognizer efr=EigenFaceRecognizer.create();
		efr.train(img_store,roi);
		int[] outLabel=new int[1];
		double[] outConf=new double[1];
		efr.predict(roi, outLabel, outConf);
		System.out.println("***Predicted label is "+outLabel[0]+".***");

		System.out.println("***Actual label is "+roi+".***");
		System.out.println("***Confidence value is "+outConf[0]+".***");
		
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	finally {
		System.out.println("array list created ..");
	}
   } 
private Mat Img_ToMat(String path) {
	BufferedImage image;
	Mat frame=new Mat();
	try {
		image = ImageIO.read(new File(path));
		frame=BufferedImage2Mat(image);
		
	} catch (IOException e) {
		e.printStackTrace();
	}
	return frame;
}
private Mat BufferedImage2Mat(BufferedImage image) throws IOException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    ImageIO.write(image, "bmp", byteArrayOutputStream);
    byteArrayOutputStream.flush();
    return Imgcodecs.imdecode(new MatOfByte(byteArrayOutputStream.toByteArray()), Imgcodecs.CV_LOAD_IMAGE_UNCHANGED);
}

}
