package com.jaron.fsconnectparent;

import android.content.Context;
import android.os.Environment;

import java.io.IOException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

/**
 * 应用程序配置类
 * 用于保存用户相关信息及设置
 */
public class AppConfig {
    private final static String APP_CONFIG = "config";
    public static final String KEY_LOAD_IMAGE = "KEY_LOAD_IMAGE";
    public static final String KEY_NOTIFICATION_DISABLE_WHEN_EXIT = "KEY_NOTIFICATION_DISABLE_WHEN_EXIT";
    public static final String KEY_CHECK_UPDATE = "KEY_CHECK_UPDATE";
    //public static final String KEY_DOUBLE_CLICK_EXIT = "KEY_DOUBLE_CLICK_EXIT";

    // 默认存放图片的路径
    public final static String DEFAULT_SAVE_IMAGE_PATH = Environment
            .getExternalStorageDirectory()//获取外部存储
            + File.separator//名称分隔符"/"或"\"
            + "ShanXue"
            + File.separator + "img" + File.separator;

    // 默认存放视频下载的路径
    public final static String DEFAULT_SAVE_FILE_PATH = Environment
            .getExternalStorageDirectory()
            + File.separator
            + "ShanXue"
            + File.separator + "video" + File.separator;

    private Context mContext;
    private static AppConfig appConfig;

    public static AppConfig getAppConfig(Context context) {
        if (appConfig == null) {
            appConfig = new AppConfig();
            appConfig.mContext = context;
        }
        return appConfig;
    }//单例模式

    public String get(String key) {
        Properties props = get();//主要用于读取Java的配置文件.在Java中，其配置文件常为.properties文件，格式为文本文件，文件的内容的格式是“键=值”的格式，文本注释信息可以用"#"来注释。
        return (props != null) ? props.getProperty(key) : null;//用指定的键在此属性列表中搜索属性。也就是通过参数 key ，得到 key 所对应的 value。
    }

    public Properties get() {
        FileInputStream fis = null;
        Properties props = new Properties();
        try {
            // 读取app_config目录下的config
            File dirConf = mContext.getDir(APP_CONFIG, Context.MODE_PRIVATE);//该函数主要用于得到一个文件夹的句柄，并通过该句柄创建和访问外文件夹。
            fis = new FileInputStream(dirConf.getPath()//此抽象路径名的字符串形式
                    + File.separator
                    + APP_CONFIG);

            props.load(fis);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return props;
    }

    private void setProps(Properties p) {
        FileOutputStream fos = null;
        try {
            // 把config建在(自定义)app_config的目录下
            File dirConf = mContext.getDir(APP_CONFIG, Context.MODE_PRIVATE);
            File conf = new File(dirConf, APP_CONFIG);//则配置文件的路径为：data/data/包名/app_+APP_CONFIG /APP_CONFIG ,其中包名后面的app_是为调用时，系统自己加上的、
            fos = new FileOutputStream(conf);

            p.store(fos, null);
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void set(String key, String value) {
        Properties props = get();
        props.setProperty(key, value);
        setProps(props);
    }

    public void remove(String... key) {
        Properties props = get();
        for (String k : key)
            props.remove(k);
        setProps(props);

        //R.string.releaseUrl
        //BuildConfig.API_SERVER_URL

        //BuildConfig.API_SERVER_URL
    }

}
