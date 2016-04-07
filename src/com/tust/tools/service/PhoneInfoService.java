package com.tust.tools.service;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.os.SystemClock;

public class PhoneInfoService {
	private String[] commandMax = { "/system/bin/cat","/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq" };
	private String[] commandMin = { "/system/bin/cat","/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_min_freq" };
	private String[] commandCur = { "/system/bin/cat","/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq" };
	public static final int max = 1;
	public static final int min = 0;
	public static final int cur = 2;
	
	public String getMaxCpu(int i){
		String res = "";
		String command[]= null;
		if(i == max){
			command = commandMax;
		}else if(i == min){
			command = commandMin;
		}else if(i == cur){
			command = commandCur;
		}else{
			return "";
		}
		try {
			ProcessBuilder pb = new ProcessBuilder(command);
			Process process = pb.start();
			InputStream is = process.getInputStream();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte buffer[] = new byte[1024];
			int len = 0;
			while((len=is.read(buffer))!=-1){
				baos.write(buffer,0,len);
			}
			byte data[] = baos.toByteArray();
			res = new String(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	// ��ȡCPU����
    public String getCpuName() {
            try {
                FileReader fr = new FileReader("/proc/cpuinfo");
                BufferedReader br = new BufferedReader(fr);
                String text = br.readLine();
                String[] array = text.split(":\\s+", 2);
                return array[1];
            } catch (Exception e) {
                    e.printStackTrace();
            }
            return null;
    }
    //��ȡ�ڴ��С
    public String getTotalMemory() {  
        String str1 = "/proc/meminfo";  
        String str2=""; 
        String memTotal ="";
        String memSheng1 = "";
        String memSheng2 = "";
        int i=0;
        try {  
            FileReader fr = new FileReader(str1);  
            BufferedReader localBufferedReader = new BufferedReader(fr, 8192);  
            while ((str2 = localBufferedReader.readLine()) != null) {  
                i++;
            	 //���   MemTotal:488604 kb  ��str2.substring(0,str2.length()-6);ȡ��488mb
            	System.out.println("str2"+str2); 
            	if(i==1){
            	    memTotal = str2.split(":")[1].substring(0,str2.split(":")[1].length()-6).trim();
            	}else if(i==2){
            	    memSheng1 = str2.split(":")[1].substring(0,str2.split(":")[1].length()-6).trim();
            	    System.out.println("memSheng1"+memSheng1);
            	}else if(i==4){
            	    memSheng2 = str2.split(":")[1].substring(0,str2.split(":")[1].length()-6).trim();
            	    System.out.println("memSheng2"+memSheng2);
            	    int memSheng =Integer.parseInt(memSheng1)+Integer.parseInt(memSheng2);
            	    return "RAM��С��"+memTotal+"MB  ʣ���С��"+memSheng+"MB";
            	}
            }  
        } catch (IOException e) {  
        }  
        return "";
    } 
    
    //Rom��С
    public String[] getRomMemroy() {  
        long[] romInfoLong = new long[2];  
        //Total rom memory  
        romInfoLong[0] = getTotalInternalMemorySize();  
        //Available rom memory  
        File path = Environment.getDataDirectory();  
        StatFs stat = new StatFs(path.getPath());  
        long blockSize = stat.getBlockSize();  
        long availableBlocks = stat.getAvailableBlocks();  
        romInfoLong[1] = blockSize * availableBlocks; 
	    double dTotal = (double)romInfoLong[0]/(double)(1024*1024);
	    String resTotal = String.valueOf(dTotal).substring(0, String.valueOf(dTotal).indexOf(".")-1);
	    double dAvail = (double)romInfoLong[1]/(double)(1024*1024);
	    String resAvail = String.valueOf(dAvail).substring(0, String.valueOf(dAvail).indexOf(".")-1);
	    String[] romInfoString = new String[2]; 
	    dTotal = Double.parseDouble(resTotal)/100;
	    dAvail = Double.parseDouble(resAvail)/100;
	    romInfoString[0] = dTotal+"";
	    romInfoString[1] = dAvail+"";
        return romInfoString;  
    } 

    public long getTotalInternalMemorySize() {  
        File path = Environment.getDataDirectory();  
        StatFs stat = new StatFs(path.getPath());  
        long blockSize = stat.getBlockSize();  
        long totalBlocks = stat.getBlockCount();  
        long total = totalBlocks * blockSize;
        return total;  
    }  
    
    //SD��С
    public String[] getSDCardMemory() {  
        long[] sdCardInfo=new long[2];  
        String[] sdInfoString = new String[2]; 
        String state = Environment.getExternalStorageState();  
        if (Environment.MEDIA_MOUNTED.equals(state)) {  
            File sdcardDir = Environment.getExternalStorageDirectory();  
            StatFs sf = new StatFs(sdcardDir.getPath());  
            long bSize = sf.getBlockSize();  
            long bCount = sf.getBlockCount();  
            long availBlocks = sf.getAvailableBlocks();  
            sdCardInfo[0] = bSize * bCount;//�ܴ�С  
            sdCardInfo[1] = bSize * availBlocks;//���ô�С  
            
            double dTotal = (double)sdCardInfo[0]/(double)(1024*1024);
    	    String resTotal = String.valueOf(dTotal).substring(0, String.valueOf(dTotal).indexOf(".")-1);
    	    double dAvail = (double)sdCardInfo[1]/(double)(1024*1024);
    	    String resAvail = String.valueOf(dAvail).substring(0, String.valueOf(dAvail).indexOf(".")-1);
    	    dTotal = Double.parseDouble(resTotal)/100;
    	    dAvail = Double.parseDouble(resAvail)/100;
    	    sdInfoString[0] = dTotal+"";
    	    sdInfoString[1] = dAvail+"";
        }  
        return sdInfoString;  
    }  

    
//    public BroadcastReceiver batteryReceiver=new BroadcastReceiver(){  
//        @Override  
//        publicvoidonReceive(Context context, Intent intent) {  
//            intlevel = intent.getIntExtra("level",0);  
//            //  level��%���ǵ�ǰ������  
//        }  
//    };  
//    registerReceiver(batteryReceiver,new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    

    //ϵͳ�İ汾��Ϣ
    public String[] getVersion(){  
        String[] version={"null","null","null","null"};  
        String str1 = "/proc/version";  
        String str2;  
        String[] arrayOfString;  
        try {  
            FileReader localFileReader = new FileReader(str1);  
            BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);  
            str2 = localBufferedReader.readLine();  
            arrayOfString = str2.split("\\s+");  
            version[0]=arrayOfString[2];//KernelVersion  
            localBufferedReader.close();  
        } catch (IOException e) {  
        }  
        version[1] = Build.VERSION.RELEASE;//firmware version  android�汾
        version[2]=Build.MODEL;//model  
        version[3]=Build.DISPLAY;//system version  
        return version;  
    }  
    //��ȡ����ʱ�䣬��ȡϵͳʱ��
    public String getTimes() {  
        long ut = SystemClock.elapsedRealtime() / 1000;  
        if (ut == 0) {  
            ut = 1;  
        }  
        int m = (int) ((ut / 60) % 60);  
        int h = (int) ((ut / 3600));  
        return h + "Сʱ" + m +"��";  
    }  


}
