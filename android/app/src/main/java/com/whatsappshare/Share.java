package com.whatsappshare;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import java.io.File;
import java.io.FileOutputStream;

public class Share extends ReactContextBaseJavaModule{
    private static ReactApplicationContext reactContext;

    Share(ReactApplicationContext context) {
        super(context);
        reactContext = context;
    }

    @Override
    public String getName() {
        return "Share";
    }

    @ReactMethod
    public void sharePdfWhatsApp(String base64,String fileName){

        try {
        String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();

        Log.d("ResponseEnv",root);

        File myDir = new File(root);

        if (!myDir.exists()) {
            myDir.mkdirs();
            Log.d("work space ", String.valueOf(myDir.exists()));
        }

        String fname = fileName + ".pdf";
        File file = new File(myDir, fname);
        if (file.exists())
            file.delete();


            FileOutputStream out = new FileOutputStream(file);
            byte[] pdfAsBytes = android.util.Base64.decode(base64, 0);
            out.write(pdfAsBytes);
            out.flush();
            out.close();

            Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
            whatsappIntent.setPackage("com.whatsapp");
            whatsappIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(file.getPath()));
            whatsappIntent.setType("application/pdf");
            whatsappIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            reactContext.startActivity(whatsappIntent);

        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(reactContext,"WhatsApp not installed",Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @ReactMethod
    public void shareJpegWhatsApp(String fileName,String base64 , Callback callback){
        try {
        //   String base64 = "iVBORw0KGgoAAAANSUhEUgAAAOEAAADhCAMAAAAJbSJIAAAAb1BMVEX///8AAABFRUV/f39paWnd3d3n5+fHx8f6+vrX19coKChvb28xMTFgYGD39/e0tLQ9PT2goKBKSkpaWlp4eHiampoWFhaMjIzr6+u5ubmkpKQeHh7y8vIjIyPKysqtra0NDQ2Kioo3NzdUVFR8fHz7LBhHAAAEgUlEQVR4nO2d65qqIBhG1dSyxrKzOmXt6v6vcXsiLRNH4+jzrl+FKN8aR0FAMgwAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAARYm2Fi/sRLZcxt7kSSRbzzBOXAXNH9l+huHwNTRl+8EQhjBUy/BqM+NHTcM5u4NOFDWcMTsoDIUCw0HAUCgwHAQMhVIZ/o7U0N36RUvLX4zUkAsw1B8YcmMWuiwIT10FSTJ0dnOTDdfNil6UHMM1I72CDfU8SjH8x1TQNM8hpTAZhhFjwfQsqmU4+2kPlXCY/xwom+e32+v2qVKGSSPglZfiHHO1dZQ4bricGafQiya/jby/9sUJ0zbdbOlMJ2eSaitl2BxgcvP0/J/3ffQkeT3h97eT9bxltRcnwXBivuPk6atPhoaxqPJdG/+MR7Jp+ZfiGEpQ8fsZGjbJ5jc1btoaLk/ptVjmd8tc2+ahls9DaGcYZJ8XxfVpPPJMd/LIeFo97oHlP/aJMdXc0DwXUkktU3pzqaqJuaW7obnPv4TZx3/FhmXQ2Ftrw0XxLWulh2076224K76l9b5ffGpt8OlquM6/ZDfT8hFpMzLDchQxu2MWt1W3sau2hnlbzS9ri8UzOK/cIW/I5qx0NZzGcUSqB7cK7vKyQwbpRNbOsE7WajsXH+NRGi5qwUUvO2Tob+gF9eCIj/vcrrmhG91JlqKz6VS22G6WdSvqD00Mi1OyrxlOdpOFX2+glU+GuyolzhP0MDycqlBKw9t7nkeRXps1pZOhVaTvaoaN9jUZUKx6WnUyLNpmhkUzJJmqXh6NDMtH2/wZqd1wThwSXyPDo3PZX7wy+UI3rHUYhtN4H0fFxau44UvXWdBhaB4/HUxJw+qO79VSy+uLYmjaHwb3SVtcKcOqR7g2oEKeEWiGZtB8ZYIYthcns1f/9kxyn2FQDdOKZVUfZgpXZBjSby9OxsgMGRy92nGUeMl0bVdBHHY5lGEZ65HeZqJVfLRrfwbKyyIyRtcuzbC/xKKUJmWEtDly8R1n92+FCROsDagwIaAJypqp4NGutH5sLvQZYvLm07geCxxVZ5sIBIb6A0OhuMGmZDvSGbRVL8x59Ibjn8kOwx7AUCgwHAQMhQLDQcBQKDAcBAyFAsNBwFAoMBzEmAyTyfYFP2c+HsPuxd7YR9yXrwz/sAQTh5AHBznAMKaojcPwDysScgi5JzCkAkMYCgGGVLoNr8wD7g1nw49T/8TC1/DDO6jC4WposXtcGQ5Pw6BzWooIeBrSls4QB0dDr/sAIuBnSFk3QyjcDDsW6REHL8N1996C+Mqwfd2iCYdQB8LHkDIHXDhcDGnrDwmHh6GlRE1P4GD4q0ZNT+BgqEhNT2BvqJgge0MFfpXkla9Wnf/QIxyzD/FL6r8csOhL81UphWp6Attff9jJ1vkAU8O7bJtPsDSkvewlD4aG1OUw5cHO8OB0lyaD9iWE+qJaTf+kdRmonqjSadHEvXdH381BXcEUd/o1Xvub6wAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAADGwH+huEh7pHa64AAAAABJRU5ErkJggg==";
        String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();

        Log.d("ResponseEnv",root);

        File myDir = new File(root);

        if (!myDir.exists()) {
            myDir.mkdirs();
            Log.d("work space ", String.valueOf(myDir.exists()));
        }

        String fname = fileName + ".jpeg";
        File file = new File(myDir, fname);
        if (file.exists())
            file.delete();


            FileOutputStream out = new FileOutputStream(file);
            byte[] pdfAsBytes = android.util.Base64.decode(base64, 0);
            out.write(pdfAsBytes);
            out.flush();
            out.close();

            Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
            whatsappIntent.setPackage("com.whatsapp");
            whatsappIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(file.getPath()));
            whatsappIntent.setType("image/jpeg");
            whatsappIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            whatsappIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            reactContext.startActivity(whatsappIntent);
          //  callback.invoke(null,true);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(reactContext,"WhatsApp not installed",Toast.LENGTH_LONG).show();
            callback.invoke(ex.toString(),null);
        } catch (Exception e) {
            e.printStackTrace();
            callback.invoke(e.toString(),null);
        }

    }

    @ReactMethod
    public void sharePngWhatsApp(String fileName,String base64 , Callback callback){
        try {
           // String base64 = "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";
             String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();

            Log.d("ResponseEnv",root);

            File myDir = new File(root);

            if (!myDir.exists()) {
                myDir.mkdirs();
                Log.d("work space ", String.valueOf(myDir.exists()));
            }

            String fname = fileName + ".png";
            File file = new File(myDir, fname);
            if (file.exists())
                file.delete();


            FileOutputStream out = new FileOutputStream(file);
            byte[] pdfAsBytes = android.util.Base64.decode(base64, 0);
            out.write(pdfAsBytes);
            out.flush();
            out.close();

            Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
            whatsappIntent.setPackage("com.whatsapp");
            whatsappIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(file.getPath()));
            whatsappIntent.setType("image/png");
            whatsappIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            whatsappIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            reactContext.startActivity(whatsappIntent);
            //  callback.invoke(null,true);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(reactContext,"WhatsApp not installed",Toast.LENGTH_LONG).show();
            callback.invoke(ex.toString(),null);
        } catch (Exception e) {
            e.printStackTrace();
            callback.invoke(e.toString(),null);
        }

    }


    @ReactMethod
    public void shareTextWhatsApp(String fileName,String base64 , Callback callback){
        try {
           // String base64 = "aGVsbG8gd29yZA0K";
            String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();

            Log.d("ResponseEnv",root);

            File myDir = new File(root);

            if (!myDir.exists()) {
                myDir.mkdirs();
                Log.d("work space ", String.valueOf(myDir.exists()));
            }

            String fname = fileName + ".txt";
            File file = new File(myDir, fname);
            if (file.exists())
                file.delete();


            FileOutputStream out = new FileOutputStream(file);
            byte[] pdfAsBytes = android.util.Base64.decode(base64, 0);
            out.write(pdfAsBytes);
            out.flush();
            out.close();

            Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
            whatsappIntent.setPackage("com.whatsapp");
            whatsappIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(file.getPath()));
            whatsappIntent.setType("application/txt");
            whatsappIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            whatsappIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            reactContext.startActivity(whatsappIntent);
            //  callback.invoke(null,true);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(reactContext,"WhatsApp not installed",Toast.LENGTH_LONG).show();
            callback.invoke(ex.toString(),null);
        } catch (Exception e) {
            e.printStackTrace();
            callback.invoke(e.toString(),null);
        }

    }
}
