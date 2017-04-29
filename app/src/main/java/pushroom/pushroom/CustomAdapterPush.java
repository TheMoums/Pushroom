package pushroom.pushroom;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;
    public class CustomAdapterPush extends BaseAdapter {
        /** Instance variables **/
        private Activity activity;
        private ArrayList dataArray;
        private static LayoutInflater inflater = null;
        private Mur tempValues = null;
        int i = 0;

        /** CustomAdapter Constructor **/
        public CustomAdapterPush(Activity a, ArrayList d) {
            activity = a;
            dataArray=d;
            inflater = ( LayoutInflater )activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        /** What is the size of Passed Arraylist Size **/
        public int getCount() {
            if(dataArray.size()<=0)
                return 1;
            return dataArray.size();
        }
        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        /** Create a holder Class to contain inflated xml file elements **/
        private static class ViewHolder{
            public TextView name;
            private TextView numberOfPics;
            //public ImageView image;
        }

        /** Depends upon data size called for each row , Create each ListView row **/
        public View getView(int position, View convertView, ViewGroup parent) {
            View vi = convertView;
            ViewHolder holder;

            if(convertView==null){

                // Inflate custom_row_xxx.xml file for each row ( Defined below )
                vi = inflater.inflate(R.layout.custom_row_walls, null);

                // View Holder Object to contain custom_row_xxx.xml file elements
                holder = new ViewHolder();
                holder.name = (TextView) vi.findViewById(R.id.custom_row_walls_name);
                holder.numberOfPics=(TextView)vi.findViewById(R.id.custom_row_walls_number_of_pics);
                //holder.image=(ImageView)vi.findViewById(R.id.image);

                // Set holder with LayoutInflater
                vi.setTag( holder );
            }
            else
                holder=(ViewHolder)vi.getTag();

            if(dataArray.size()<=0)
            {
                holder.name.setText("No Data");
            }
            else
            {
                // Get each Model object from Arraylist
                tempValues=null;
                tempValues = ( Mur ) dataArray.get( position );

                // Set Model values in Holder elements
                holder.name.setText( tempValues.getMurName() );
                holder.numberOfPics.setText( tempValues.getMurName());

                // Set Item Click Listner for LayoutInflater for each row
                vi.setOnClickListener(new OnItemClickListener( position ));
                vi.setOnLongClickListener(new OnItemLongClickListener(position));
            }
            return vi;
        }

        /** Called when Item click in ListView **/
        private class OnItemClickListener implements View.OnClickListener {
            private int mPosition;

            OnItemClickListener(int position){
                mPosition = position;
            }

            @Override
            public void onClick(View arg0) {
                pushroom.pushroom.PushList sct = (pushroom.pushroom.PushList) activity;

                // Call  onItemClick Method inside CustomListViewAndroidExample Class ( See Below )
                sct.onItemClick(mPosition);
            }
        }
        private class OnItemLongClickListener implements View.OnLongClickListener {
            private int mPosition;

            OnItemLongClickListener(int position) {
                mPosition = position;
            }

            @Override
            public boolean onLongClick(View arg0) {
                PushList sct = (PushList) activity;

                // Call  onItemClick Method inside CustomListViewAndroidExample Class ( See Below )
                sct.onItemLongClick(mPosition);
                return true;
            }
        }
    }
