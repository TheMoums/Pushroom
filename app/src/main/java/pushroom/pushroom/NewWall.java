package pushroom.pushroom;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
public class NewWall extends AppCompatActivity {

    private String push = "My awesome push";
    private String room = "Kitchen";
    Button   button;
    EditText nameTextField;
    EditText roomTextField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_wall);

        button = (Button)findViewById(R.id.new_wall_button);
        nameTextField   = (EditText)findViewById(R.id.new_wall_name);
        roomTextField   = (EditText)findViewById(R.id.new_wall_number);

        button.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View view)
                    {
                        String tempName = nameTextField.getText().toString();
                        String roomName = roomTextField.getText().toString();
                        if ((tempName.compareTo("") != 0) && roomName.compareTo("") != 0){
                            push = tempName;
                            room = roomName;
                            MurManager m = new MurManager(getApplicationContext()); // gestionnaire de la table "user"
                            m.open();
                            long row_id = m.addMur(new Mur(0, push, room));
                            Mur push = m.getMurFromRowId(row_id);
                            m.close();
                            Intent intent = new Intent(getApplicationContext(), PushList.class);
                            intent.putExtra("push", push);
                            startActivity(intent);
                        } else {
                            Context context = getApplicationContext();
                            CharSequence text = getResources().getString(R.string.new_wall_fail);
                            int duration = Toast.LENGTH_LONG;

                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                        }
                    }
                });
    }
}
