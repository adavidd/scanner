package com.shapira.collectorscanner.utility;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class BarcodeString {
    public String getBarcodeFromIntent(Context context, Intent intent) {
        byte[] barocode =null;
        String _barcode =null;

        if(barocode ==null ||!(barocode.length>0)){
            Bundle extras = intent.getExtras();
            if(extras!=null){
//                        ArrayList<Parcelable> list = intent.getParcelableArrayListExtra("nMap");
                _barcode=    extras.getString("barcode");
            }
        }
        if(_barcode == null){
            try {
                barocode = intent.getByteArrayExtra("barocode");
            }catch(Error e){

            }
            int barocodelen = intent.getIntExtra("length", 0);
            if(barocodelen >0 ) {
                byte temp = intent.getByteExtra("barcodeType", (byte) 0);
                android.util.Log.i("debug", "----codetype--" + temp);
                _barcode = new String(barocode, 0, barocodelen);
            }

        }
        if(_barcode==null ){
            Toast.makeText(context,"שגיאה בקריאת ברקוד",Toast.LENGTH_SHORT).show();
            return null;
        }
        return _barcode;
    }

}
