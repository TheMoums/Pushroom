package pushroom.pushroom;

import java.io.Serializable;

        import java.io.Serializable;

/**
 * Created by dorianeolewicki on 21/03/17.
 */

public class Mur implements Serializable {

    private int mur_id;
    private String mur_name;
    private String push_room;

    public Mur(int mur_id, String mur_name, String push_room) {
        this.mur_id = mur_id;
        this.mur_name = mur_name;
        this.push_room = push_room;
    }

    public int getMurId() {return mur_id;}
    public String getMurName() {return mur_name;}
    public String getRoom() {return push_room;}

    public void setMurId(int mur_id) {this.mur_id = mur_id;}
    public void setMurName(String mur_name) {this.mur_name = mur_name;}
    public void setRoom(String push_room) {this.push_room = push_room;}
}
