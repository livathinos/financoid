package app.financoid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Financoid extends Activity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
    /*No menu yet, we just launch the form layout.*/
    public void launchForm(View button) {

        Intent launchFormActivity = new Intent(this, FormActivity.class); 
        startActivity(launchFormActivity);

    }

}