package pushroom.pushroom;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by dorianeolewicki on 21/03/17.
 */
public class ImageManager {
    Context contextglobal;

    private static final String TABLE_NAME = "Image";
    public static final String KEY_IMAGE_ID="image_id";
    public static final String KEY_MUR_ID="mur_id";
    public static final String KEY_MARKER_ID="marker_id";
    public static final String KEY_IMAGE_PATH="image_path";
    public static final String KEY_IMAGE_SIZE="image_size";
    public static final String CREATE_TABLE_IMAGE = "CREATE TABLE "+TABLE_NAME+
            " ("+
            " "+KEY_IMAGE_ID+" INTEGER not null primary key," +
            " "+KEY_MUR_ID+" INTEGER not null," +
            " "+KEY_MARKER_ID+" INTEGER not null," +
            " "+KEY_IMAGE_PATH+" TEXT not null," +
            " "+KEY_IMAGE_SIZE+" INTEGER not null," +
            //" unique("+KEY_MUR_ID+", "+KEY_MARKER_ID+", "+KEY_IMAGE_PATH+") " +
            " unique("+KEY_MUR_ID+", "+KEY_MARKER_ID+") " +
            ");";
    private MySQLite maBaseSQLite; // notre gestionnaire du fichier SQLite
    private SQLiteDatabase db;

    // Constructeur
    public ImageManager(Context context)
    {
        contextglobal = context;
        maBaseSQLite = MySQLite.getInstance(context);
    }

    public void open()
    {
        //on ouvre la table en lecture/écriture
        db = maBaseSQLite.getWritableDatabase();
    }

    public void close()
    {
        //on ferme l'accès à la BDD
        db.close();
    }

    public long addImage(Image image) {
        ContentValues values = new ContentValues();
        values.put(KEY_MUR_ID, image.getMurId());
        values.put(KEY_MARKER_ID, image.getMarkerId());
        values.put(KEY_IMAGE_PATH, image.getImagePath());
        values.put(KEY_IMAGE_SIZE, image.getImageSize());

        return db.insert(TABLE_NAME,null,values);
    }

    public int modImage(Image image) {
        ContentValues values = new ContentValues();
        values.put(KEY_MUR_ID, image.getMurId());
        values.put(KEY_MARKER_ID, image.getMarkerId());
        values.put(KEY_IMAGE_PATH, image.getImagePath());
        Log.e("ModImage :", image.getImageSize()+"");
        values.put(KEY_IMAGE_SIZE, image.getImageSize());

        String where = KEY_IMAGE_ID+" = ?";
        String[] whereArgs = {image.getImageId()+""};

        return db.update(TABLE_NAME, values, where, whereArgs);
    }

    public int supImage(Image image) {
        String where = KEY_IMAGE_ID+" = ?";
        String[] whereArgs = {image.getImageId()+""};

        return db.delete(TABLE_NAME, where, whereArgs);
    }

    public Image getImage(long image_id) {
        Image a = new Image(0, 0, 0, null, 0);

        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE "+KEY_MUR_ID+"='"+image_id+"'", null);
        if(c.moveToFirst()) {
            a.setImageId(c.getInt(c.getColumnIndex(KEY_IMAGE_ID)));
            a.setMurId(c.getInt(c.getColumnIndex(KEY_MUR_ID)));
            a.setMarkerId(c.getInt(c.getColumnIndex(KEY_MARKER_ID)));
            a.setImagePath(c.getString(c.getColumnIndex(KEY_IMAGE_PATH)));
            a.setImageSize(c.getInt(c.getColumnIndex(KEY_IMAGE_SIZE)));
            c.close();
        }

        return a;
    }

    public String getImagePath(long mur_id, long marker_id) {
        String ret = null;
        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE "+KEY_MUR_ID+"='"+mur_id+"' AND " + KEY_MARKER_ID + "='" + marker_id +"'", null);
        if(c.moveToFirst()) {
            ret = c.getString(c.getColumnIndex(KEY_IMAGE_PATH));
            c.close();
        }

        return ret;
    }

    public Cursor getImages() {
        // sélection de tous les enregistrements de la table
        return db.rawQuery("SELECT * FROM "+TABLE_NAME, null);
    }

    public Cursor getImagesFromMurId(int mur_id) {
        // sélection de tous les enregistrements de la table
        return db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE "+KEY_MUR_ID+"='"+mur_id+"'", null);
    }

    public boolean canCreateImage(Image image) {
        int imageId = image.getImageId();
        int murId = image.getMurId();
        int markerId = image.getMarkerId();
        String imagePath = image.getImagePath();
        int imageSize = image.getImageSize();

        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE "+KEY_MUR_ID+"='"+murId+
                "' AND " + KEY_MARKER_ID + "='" + markerId +"'", null);
        if (c.moveToFirst()) {
            c.close();
            return false;
        }
        else {
            c.close();
            return true;
        }
    }
}
