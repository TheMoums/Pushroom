package pushroom.pushroom;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
/**
 * Created by dorianeolewicki on 21/03/17.
 */

public class MurManager {
    Context contextglobal;

    private static final String TABLE_NAME = "Mur";
    public static final String KEY_MUR_ID="mur_id";
    public static final String KEY_MUR_NAME="mur_name";
    public static final String KEY_PUSH_ROOM="push_room";
    public static final String CREATE_TABLE_MUR = "CREATE TABLE "+TABLE_NAME+
            " ("+
            " "+KEY_MUR_ID+" INTEGER not null primary key," +
            " "+KEY_MUR_NAME+" TEXT not null," +
            " "+KEY_PUSH_ROOM+" TEXT not null" +
            ");";
    private MySQLite maBaseSQLite; // notre gestionnaire du fichier SQLite
    private SQLiteDatabase db;

    // Constructeur
    public MurManager(Context context)
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

    public long addMur(Mur mur) {
        ContentValues values = new ContentValues();
        values.put(KEY_MUR_NAME, mur.getMurName());
        values.put(KEY_PUSH_ROOM, mur.getRoom());

        return db.insert(TABLE_NAME,null,values);
    }

    public int modMur(Mur mur) {
        ContentValues values = new ContentValues();
        values.put(KEY_MUR_NAME, mur.getMurName());
        values.put(KEY_PUSH_ROOM, mur.getRoom());

        String where = KEY_MUR_ID+" = ?";
        String[] whereArgs = {mur.getMurId()+""};

        return db.update(TABLE_NAME, values, where, whereArgs);
    }

    public int supMur(Mur mur) {
        String where = KEY_MUR_ID+" = ?";
        String[] whereArgs = {mur.getMurId()+""};

        return db.delete(TABLE_NAME, where, whereArgs);
    }

    public Mur getMur(long mur_id) {
        Mur a = new Mur(0, "", "");

        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE "+KEY_MUR_ID+"="+mur_id, null);
        if(c.moveToFirst()) {
            a.setMurId(c.getInt(c.getColumnIndex(KEY_MUR_ID)));
            a.setMurName(c.getString(c.getColumnIndex(KEY_MUR_NAME)));
            a.setRoom(c.getString(c.getColumnIndex(KEY_PUSH_ROOM)));
            c.close();
        }

        return a;
    }

    public Mur getMurFromRowId(long row_id) {
        Mur a = new Mur(0, "", "");

        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE rowid = "+row_id, null);
        if(c.moveToFirst()) {
            a.setMurId(c.getInt(c.getColumnIndex(KEY_MUR_ID)));
            a.setMurName(c.getString(c.getColumnIndex(KEY_MUR_NAME)));
            a.setRoom(c.getString(c.getColumnIndex(KEY_PUSH_ROOM)));
            c.close();
        }

        return a;
    }

    public Cursor getMurs() {
        // sélection de tous les enregistrements de la table
        return db.rawQuery("SELECT * FROM "+TABLE_NAME, null);
    }

    public boolean canCreateMur(Mur mur) {
        int murId = mur.getMurId();
        String murName = mur.getMurName();
        String pushRoom = mur.getRoom();

        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE "+KEY_MUR_NAME+"="+murName, null);
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

