package cn.edu.zhku.b2c.util;

import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

public class AsyncImageLoader {
//	private Map<String,SoftReference<Drawable>> imageCache ;
	private Map<String,SoftReference<Bitmap>> imageCache ;
	private int requiredWidth = 0 ;
	private int requiredHeight = 0 ;
	public AsyncImageLoader(Context context, int requiredWidth, int requiredHeight){//��ߵ���ֵ��dp
		imageCache = new HashMap<String,SoftReference<Bitmap>>() ;
		this.requiredWidth = ScreenUtil.dp2px(context, requiredWidth);
		this.requiredHeight = ScreenUtil.dp2px(context, requiredHeight);
	}
	
	public Bitmap loadBitmap(final String imageUrl, final ImageCallback imageCallback){
		//���ȴӻ����в���
		if(imageCache.containsKey(imageUrl)){
			SoftReference<Bitmap> reference = imageCache.get(imageUrl) ;
			Bitmap bitmap = reference.get() ;
			if(bitmap != null){
				return bitmap ;
			}
		}
		
		//����һ���߳�����ͼƬ
		final Handler handler = new Handler(){
			public void handleMessage(Message message) {  
				imageCallback.imageLoaded((Bitmap) message.obj, imageUrl);  
            }  
		};
		
		new Thread(new Runnable(){
			@Override
			public void run() {
				Bitmap bitMap = loadImageFromUrl(imageUrl);  
                imageCache.put(imageUrl, new SoftReference<Bitmap>(bitMap));  
                Message message = handler.obtainMessage(0, bitMap);  
                handler.sendMessage(message); 
			}
		}).start();
		return null ;
	}
	
	public Bitmap loadImageFromUrl(String imageUrl){
		//��ȡ����ͼƬ
//		 URL m;  
//         InputStream i = null;  
//         try {  
//             m = new URL(imageUrl);  
//             i = (InputStream) m.getContent();  
//         } catch (MalformedURLException e1) {  
//             e1.printStackTrace();  
//         } catch (IOException e) {  
//             e.printStackTrace();  
//         }  
//         Drawable d = Drawable.createFromStream(i, "src");  
//         return d;  
		
		Bitmap bitmap = null;  
        try  
        {  
//            //��ʼ��һ��URL����  
//            URL url = new URL(imageUrl);  
//            //���HTTPConnection�������Ӷ���  
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();  
//            connection.setConnectTimeout(5*1000);  
//            connection.setDoInput(true);  
//            connection.connect();  
//            //�õ�������  
//            InputStream is = connection.getInputStream();  
            
        	//��httpClient����������SKImagedecoder::Factory returned null 
            HttpGet httpRequest = new HttpGet(imageUrl);
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = (HttpResponse) httpclient.execute(httpRequest);
            HttpEntity entity = response.getEntity();
            BufferedHttpEntity bufferedHttpEntity = new BufferedHttpEntity(entity);
            InputStream is = bufferedHttpEntity.getContent();
            
            
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;//true������ͼƬ�����ͼƬ��С
            BitmapFactory.decodeStream(is, null, options);  
            
            options.inSampleSize = calculateInSampleSize(options);
            options.inJustDecodeBounds = false;//����ͼƬ
            is.reset();
            bitmap = BitmapFactory.decodeStream(is, null, options);
            //�ر�������  
            is.close();  
        } catch (Exception e)  
        {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
          
        return bitmap;  
	}
	
	//�������ű���
	public int calculateInSampleSize(BitmapFactory.Options options){
		int originWidth = options.outHeight;
//        int height = options.outWidth;
        double ratio = 1;
        if(originWidth > requiredWidth){
        	ratio = (double)originWidth / requiredWidth ;
        }
		return new BigDecimal(ratio+"").setScale(0,BigDecimal.ROUND_HALF_UP).intValue();
	}
	
	
	public interface ImageCallback{
		public void imageLoaded(Bitmap bitMap, String imageUrl);
	}
}
