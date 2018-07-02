package com.cleaner.esaymart.CustomViews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Created by user on 10/23/2017.
 */

public class ImageFactory {

    private static Hashtable<String, Bitmap> hashTable = new Hashtable<String, Bitmap>();

    public static Bitmap getBitmapFromFactory(Context context, int drawableId) {

        Bitmap bitmap = hashTable.get("" + drawableId);

        if (bitmap == null) {

            bitmap = BitmapFactory.decodeResource(context.getResources(), drawableId);
            hashTable.put("" + drawableId, bitmap);
        }

        return bitmap;
    }

    public static void clearHashTable() {

        if (hashTable != null) {

            Enumeration<Bitmap> enumeration = hashTable.elements();
            while (enumeration.hasMoreElements()) {
                enumeration.nextElement().recycle();
            }

            hashTable.clear();
        }

    }

}

