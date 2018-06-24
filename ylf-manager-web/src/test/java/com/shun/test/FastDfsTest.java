package com.shun.test;

import java.io.IOException;
import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
/**
* @author czs
* @version 创建时间：2018年5月14日 下午2:26:14
* 用来专门测试图片上传的类 
*/
public class FastDfsTest {
	public static void main(String[] args) throws IOException, MyException {
		// 配置文件和测试图片上传时用的图片存放地址
		String propertiesPath = System.getProperty("user.dir") + "/src/main/resources/conf/FastDfcClient.txt";
		String TestImagePath = System.getProperty("user.dir") + "/src/main/resources/conf/TestImage.png";
		
		System.out.println(propertiesPath);
		System.out.println(TestImagePath);
		

		// 创建一个配置文件。文件名任意。内容就是tracker服务器的地址。
		// 这里就在这个web工程下面建立了一个FastDfsClient.conf内容为tracker_server=192.168.25.133:22122

		// 使用全局对象加载配置文件
		ClientGlobal.init(propertiesPath);

		// 创建一个TrackerClient对象
		TrackerClient trackerClient = new TrackerClient();

		// 通过TrackClient获得一个TrackerServer对象
		TrackerServer trackerServer = trackerClient.getConnection();

		// 创建一个StorageServer的引用，可以使null
		StorageServer storageServer = null;

		// 创建一个StorageClient，参数需要TrackerServer和StrorageServer
		StorageClient storageClient = new StorageClient(trackerServer, storageServer);

		// 创建一个StorageClient上传文件
		String[] upload_file = storageClient.upload_file(TestImagePath, "png", null);
		System.out.println("该图片上传之后的地址为：  192.168.25.133/" + upload_file[0] + "/" + upload_file[1]);
		System.out.println("该图片上传之后的地址为：  47.93.253.48/" + upload_file[0] + "/" + upload_file[1]);
	}
}
