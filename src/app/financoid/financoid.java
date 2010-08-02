package app.financoid;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class Financoid extends Activity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
    /*
     * FUNCTION: public boolean onCreateOptionsMenu(Menu menu)
     * 
     * DESCRIPTION: Override of native function creating an options menu
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     * 
     * 		INPUTS: Menu menu
     * 		OUTPUTS: boolean true/false
     * 
     */
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    
    
    /*
     * FUNCTION: public boolean onOptionsItemSelected (MenuItem item)
     * 
     * DESCRIPTION: Override of native function listening to selected menu items.
     * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
     * 
     * 		INPUTS: MenuItem item
     * 		OUTPUTS: boolean true/false
     * 
     */
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        	case R.id.menu_item_statistics: Toast.makeText(this, "Statistics go here", Toast.LENGTH_LONG).show();
        						break;
            case R.id.menu_item_feedback: Intent launchFormActivity = new Intent(this, FormActivity.class);
            					startActivity(launchFormActivity);
                                break;
            case R.id.menu_item_settings: Toast.makeText(this, "Settings text here!", Toast.LENGTH_LONG).show();
                                break;
        }
        return true;
    }
    
    /*No menu yet, we just launch the form layout.*/
    public void launchForm(View button) {

        Intent launchFormActivity = new Intent(this, FormActivity.class); 
        startActivity(launchFormActivity);

    }

}