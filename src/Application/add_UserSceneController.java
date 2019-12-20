package Application;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
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
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;
import org.opencv.videoio.VideoCapture;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class add_UserSceneController {
	private VideoCapture video_Cptur=new VideoCapture();
	private CascadeClassifier face_Cascade=new CascadeClassifier();
	private String classifier_Path="C:/Users/sunny/Downloads/opencv/build/etc/haarcascades/haarcascade_frontalface_alt.xml";
	private boolean camera_On=false;
	private int absolute_Facesize=0;
	private static int camera_ID=0;
	private ScheduledExecutorService videoFrame_time;
	@FXML
	private Button back;
	@FXML
	protected TextField name;
	@FXML
	Button start_Btn;
	@FXML
	private ImageView img_View;
	@FXML
	private AnchorPane user_AddScene;
	protected String saving_Path="F:/face_recognition_Database/";
	protected int counter=0;
	
	public void initializing_method() {
	}
	@FXML
	private void back_BtnClicked() {
		try {
			this.camera_On = false;
			this.stopAcquisition();
			FXMLLoader loader=new FXMLLoader(getClass().getResource("admin_Scene.fxml"));
			AnchorPane pane=loader.load();
			user_AddScene.getChildren().setAll(pane);
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
				this.videoFrame_time.scheduleAtFixedRate(frameGrabber, 0, 100, TimeUnit.MILLISECONDS);
				
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
							if (!frame.empty())
							{
								
								Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2BGRA);
								// face detection
								
								this.detectAndDisplay(frame);
							}
							
						}
						
					}
					catch (Exception e)
					{
						// log the error
						System.err.println("Exception during the image elaboration: " + e);
						//e.printStackTrace();
					}
				}
				
				return frame;
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
	public void saveImage(Mat frame,String path) {
	 
    // Saving the Image
	 String updated_Path=path+ ++counter +".bmp";
	 java.sql.Connection cn=DB_Connection.connectdb();
		Statement stmt;
		try {
			stmt = cn.createStatement();
			String query="Insert into user_database(image_name,image_path) values("+"'"+name.getText()+"','"+updated_Path+"')";
			stmt.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
    // Saving it again 
    Imgcodecs.imwrite(updated_Path, frame);
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
			Mat roi =new Mat(frame,facesArray[i]);
			if(!name.getText().isEmpty()) {
			//path where frame is to be stored
			String folder_Path=saving_Path+name.getText()+"/";
			//check if folder with name exists
			File check_folder_exists=new File(folder_Path);
			if(!check_folder_exists.exists()) {
				//if folder not exists create one
				check_folder_exists.mkdir();
			}
			if(counter<11) {
			this.saveImage(roi,folder_Path);}
			}
			Imgproc.rectangle(frame, facesArray[i].tl(), facesArray[i].br(), new Scalar(0, 255, 0), 3);
		}
	}

	

}



