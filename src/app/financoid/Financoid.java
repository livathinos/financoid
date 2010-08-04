package app.financoid;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.graphics.Canvas;

public class Financoid extends Activity {

	/*
	 * FUNCTION: public void onCreate(Bundle savedInstanceState)
	 * 
	 * DESCRIPTION: Override of native function. Called when the application
	 * 				is first initialized.
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 * 
	 * 		INPUTS: Bundle savedInstanceState
	 * 		OUTPUTS: (none)
	 * 
	 */
	
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
        
        	case R.id.menu_item_addnew: Intent launchNewTransactionActivity = new Intent(this, NewTransactionActivity.class);
        						startActivity(launchNewTransactionActivity);
        						break;
        	case R.id.menu_item_statistics: Intent launchStatisticsActivity = new Intent(this, StatisticsActivity.class);
        						startActivity(launchStatisticsActivity);
        						break;
            case R.id.menu_item_feedback: Intent launchFormActivity = new Intent(this, FormActivity.class);
            					startActivity(launchFormActivity);
                                break;
            case R.id.menu_item_settings: Intent launchSettingsActivity = new Intent(this, StatisticsActivity.class);
            					startActivity(launchSettingsActivity);
                                break;
        }
        return true;
    }
    
    /*No menu yet, we just launch the form layout.*/
    public void launchForm(View button) {

        Intent launchFormActivity = new Intent(this, FormActivity.class); 
        startActivity(launchFormActivity);

    }
    
    /*
     * FUNCTION: public void onDraw(Canvas canvas)
     * 
     * DESCRIPTION: Override of native function. Draws a canvas
     * 
     * 		INPUTS: Canvas canvas
     * 		OUTPUTS: (none)
     * 
     * 
     */
    
    //public void onDraw(Canvas canvas) {
    	
    //}

}