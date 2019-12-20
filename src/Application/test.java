package Application;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.face.EigenFaceRecognizer;
import org.opencv.imgcodecs.Imgcodecs;

public class test {

	public static void main(String[] args) {
		Mat frame=new Mat();
		ArrayList<Mat> img_store=new ArrayList<Mat>();
		ArrayList<String> name=new ArrayList<String>();
		Connection cn=DB_Connection.connectdb();
		try {
			Statement stmt=cn.createStatement();
			ResultSet rs=stmt.executeQuery("Select * from user_database");
			while (rs.next()) {
				System.out.printf("%s\t%s\n",rs.getString(1),rs.getString(2));
				img_store.add(Img_ToMat(rs.getString(1)));
				name.add(rs.getString(2));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		frame=img_store.get(img_store.size()-1);
		String value=name.get(name.size()-1);
		EigenFaceRecognizer efr=EigenFaceRecognizer.create();
		efr.train(img_store,frame);
		int[] outLabel=new int[1];
		double[] outConf=new double[1];
		efr.predict(frame, outLabel, outConf);
		System.out.println("***Predicted label is "+outLabel[0]+".***");

		System.out.println("***Actual label is "+frame+".***");
		System.out.println("***Confidence value is "+outConf[0]+".***");
	}
	public static Mat Img_ToMat(String path) {
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
	public static Mat BufferedImage2Mat(BufferedImage image) throws IOException {
	    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	    ImageIO.write(image, "bmp", byteArrayOutputStream);
	    byteArrayOutputStream.flush();
	    return Imgcodecs.imdecode(new MatOfByte(byteArrayOutputStream.toByteArray()), Imgcodecs.CV_LOAD_IMAGE_UNCHANGED);
	}

}
