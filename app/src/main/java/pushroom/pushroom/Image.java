package pushroom.pushroom;

/**
 * Created by dorianeolewicki on 21/03/17.
 */

public class Image {

    private int image_id;
    private int mur_id;
    private int marker_id;
    private String image_path;
    private int image_size;

    public Image(int image_id, int mur_id, int marker_id, String image_path, int image_size) {
        this.image_id = image_id;
        this.mur_id = mur_id;
        this.marker_id = marker_id;
        this.image_path = image_path;
        this.image_size = image_size;
    }

    public int getImageId() {return image_id;}
    public int getMurId() {return mur_id;}
    public int getMarkerId() {return marker_id;}
    public String getImagePath() {return image_path;}
    public int getImageSize() {return image_size;}

    public void setImageId(int image_id) {this.image_id = image_id;}
    public void setMurId(int mur_id) {this.mur_id = mur_id;}
    public void setMarkerId(int marker_id) {this.marker_id = marker_id;}
    public void setImagePath(String image_path) {this.image_path = image_path;}
    public void setImageSize(int image_size) {this.image_size = image_size;}

}
