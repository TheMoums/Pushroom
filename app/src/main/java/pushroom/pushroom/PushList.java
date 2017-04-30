package pushroom.pushroom;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PushList extends AppCompatActivity{
        ListView list;
        CustomAdapterPush adapter; //TODO
        public  PushList CustomListView = null; //TODO
        public ArrayList<Mur> dataArray = new ArrayList<>(); //TODO
        String [] tabOfIp = {"10.120.33.247", "10.120.33.247", "10.120.33.247"};
        int [] tabOfPort = {80, 80 , 80};
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_walls_list, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_ic_plus:
                    Intent intent = new Intent(this, NewWall.class);
                    startActivity(intent);
                    return true;
                default:
                    // If we got here, the user's action was not recognized.
                    // Invoke the superclass to handle it.
                    return super.onOptionsItemSelected(item);

            }
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.walls_list);

            CustomListView = this;

            // Populate the dataArray
            setListData();

            list = (ListView)findViewById( R.id.walls_list_view ); //TODO

            /**************** Create Custom Adapter *********/
            adapter=new CustomAdapterPush( CustomListView, dataArray); //TODO
            list.setAdapter( adapter );
        }

        /** Populate the dataArray **/
        private void setListData()
        {
            MurManager m = new MurManager(getApplicationContext()); // gestionnaire de la table "user"
            m.open();

            Cursor cur = m.getMurs();

            try {
                while (cur.moveToNext()) {
                    Mur mur = new Mur(cur.getInt(0),cur.getString(1),cur.getString(2));
                    dataArray.add(mur);
                }
            } finally {
                cur.close();
            }

            m.close();
        }

        /** Function used by adapter **/
        public void onItemClick(int mPosition, String color)
        {
            Mur tempValues = ( Mur ) dataArray.get(mPosition);
            WifiManager wifi = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if (wifi.isWifiEnabled()){
                if(color.equals("green"))
                    Send("push", "0", tabOfIp[0]);
                else
                    Send("close", "0", tabOfIp[0]);
            }
            else{
                Toast.makeText(getApplicationContext(),R.string.wifiErr,Toast.LENGTH_LONG).show();
            }
            /*Intent intent = new Intent(this, Communication.class);
            intent.putExtra("mur", tempValues);
            intent.putExtra("Ip", tabOfIp[0]);
            intent.putExtra("port", tabOfPort[0]);
            intent.putExtra("action", "push");
            startActivity(intent);*/
        }
        public void onItemLongClick(int mPosition)
        {
            Dialog markerSelection = createDialog(mPosition);//Allow the user to choose what to do with his wall
            markerSelection.show();
        }
        public Dialog createDialog(final int mPosition)
        {
            //Initialize the Alert Dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, android.R.style.Theme_Holo_Light_Dialog));
            //Source of the data in the Dialog
            final Mur tempValues = (Mur) dataArray.get(mPosition);
            CharSequence[] array = {"Check connection", getResources().getString(R.string.wallDelete) + " " + tempValues.getMurName()};
            // Set the dialog title
            builder.setTitle(getResources().getString(R.string.wallChoice)) //
                    // Specify the list array, the items to be selected by default (null for none),
                    // and the listener through which to receive callbacks when items are selected
                    .setSingleChoiceItems(array, 1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    // Set the action buttons
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {//if the user presses "Ok"
                            ListView lw = ((AlertDialog)dialog).getListView();
                            String checkedItem = (String) lw.getAdapter().getItem(lw.getCheckedItemPosition());//Marker chosen by the user
                            if(checkedItem.equals("Check connection")){
                                WifiManager wifi = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                                if (wifi.isWifiEnabled()){
                                    Send("check", "0", tabOfIp[0]);
                                }
                                else{
                                    Toast.makeText(getApplicationContext(),R.string.wifiErr,Toast.LENGTH_LONG).show();
                                }
                            }
                            else{
                                Toast.makeText(getBaseContext(), R.string.wallDeleted, Toast.LENGTH_SHORT).show();
                                Mur mur1 = dataArray.get(mPosition);
                                int murID = mur1.getMurId();
                                ImageManager m = new ImageManager(getApplicationContext());
                                m.open();
                                Cursor cur = m.getImagesFromMurId(murID);
                                cur.close();
                                m.close();
                                MurManager m2 = new MurManager(getApplicationContext());
                                m2.open();
                                m2.supMur(mur1);
                                m2.close();
                                dataArray.remove(mPosition);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {//if the user presses "cancel"
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            return builder.create();
        }
        @Override
        public void onBackPressed() {
            //super.onBackPressed();
            //rien
        }
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    public void Send(String message, String number, String IP) {
            new Create_Part(message, number, IP).execute();
    }
    class Create_Part extends AsyncTask<String, String, String> {
        String instruction;
        String number;
        String IP;
        String response = "Push connection error";
        public  Create_Part(String instruction, String number, String IP)
        {
            this.instruction = instruction;
            this.number = number;
            this.IP = IP;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(PushList.this);
            pDialog.setMessage("Sending instruction to push");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("Name", instruction));
            params.add(new BasicNameValuePair("part_nr", number));
            String path = "http://"+IP+"/db_create.php";
            JSONObject json = jsonParser.makeHttpRequest(path, "POST", params);//TODO adress IP
            try {
                int success = json.getInt("success");
                if(success == 1){
                    response = json.getString("message");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url){
            Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
            pDialog.dismiss();
        }
    }
}