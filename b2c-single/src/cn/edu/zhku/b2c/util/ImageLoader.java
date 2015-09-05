package cn.edu.zhku.b2c.util;

import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

public class ImageLoader {
	private Map<String, SoftReference<Bitmap>> cache = null ;//图片缓存
	public ImageLoader(){
		if(cache == null){
			cache = new HashMap<String, SoftReference<Bitmap>>();
		}
	}
	
	public void loadImage(final String imageUrl, final LoaderCallback callback){
		//先从缓存中检查是否存在
		if(cache.containsKey(imageUrl)){
			SoftReference<Bitmap> reference = cache.get(imageUrl) ;
			Bitmap bitmap = reference.get() ;
			if(bitmap != null){
				callback.async(bitmap, imageUrl);
				return;
			}
		}
		final Handler handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				callback.async((Bitmap)msg.obj, imageUrl);
			}
		};
		
		//异步下载图片
		new Thread(new Runnable(){
			@Override
			public void run() {
				Bitmap bitmap = loadFromNetwork(imageUrl);
				cache.put(imageUrl, new SoftReference<Bitmap>(bitmap));
				Message message = Message.obtain();
				message.obj = bitmap;
				handler.sendMessage(message);
			}
		}).start();
	}
	
	public Bitmap loadFromNetwork(String imageUrl){
		//获取网络图片
		Bitmap bitmap = null;  
       try  
       {  
           HttpGet httpRequest = new HttpGet(imageUrl);
           HttpClient httpclient = new DefaultHttpClient();
           HttpResponse response = (HttpResponse) httpclient.execute(httpRequest);
           HttpEntity entity = response.getEntity();
           BufferedHttpEntity bufferedHttpEntity = new BufferedHttpEntity(entity);
           InputStream is = bufferedHttpEntity.getContent();
           
           bitmap = BitmapFactory.decodeStream(is);
           //关闭输入流  
           is.close();  
       } catch (Exception e)  
       {  
           e.printStackTrace();  
       }  
       return bitmap;  
	}
	
	public void destroyImage(){
		if(cache != null){
			cache = null;
		}
	}
	
	public interface LoaderCallback{
		public void async(Bitmap bitmap, String imageUrl);
	}
}
