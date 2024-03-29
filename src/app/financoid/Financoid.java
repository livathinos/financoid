package app.financoid;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;
import android.app.TabActivity;

public class Financoid extends TabActivity {
	
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
        
        //Removes the title bar from the application top
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        //setContentView(R.layout.main_tabs);
        
        TabHost tabHost = getTabHost();
        tabHost.setBackgroundColor(Color.BLACK);
        //tabHost.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_white));
        
        LayoutInflater.from(this).inflate(R.layout.main_tabs, tabHost.getTabContentView(), true);
        
        tabHost.addTab(tabHost.newTabSpec("tab1")
        		.setIndicator("Overview",getResources().getDrawable(R.drawable.notes))
        		.setContent(new Intent(this, OverviewActivity.class)));
        tabHost.addTab(tabHost.newTabSpec("tab2")
                .setIndicator("Transactions", getResources().getDrawable(R.drawable.tasks))
        		.setContent(new Intent(this, LatestActivity.class)));
        tabHost.addTab(tabHost.newTabSpec("tab3")
        		.setIndicator("Statistics", getResources().getDrawable(R.drawable.calculator))
        		.setContent(new Intent(this, StatisticsActivity.class)));
        /*tabHost.addTab(tabHost.newTabSpec("tab4")
        		.setIndicator("Budgets", getResources().getDrawable(R.drawable.tasks))
        		.setContent(new Intent(this, BudgetsActivity.class)));*/
        
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
        
        	case R.id.menu_item_addnew: launchNewTransaction();
        								break;
        								
        	case R.id.menu_item_account: launchAccountManager();
        									break;
        									
        	case R.id.menu_item_export: launchExport();
        								break;
        									
            case R.id.menu_item_feedback: launchForm();
                                		  break;
                                		  
            case R.id.menu_item_settings: launchSettings();
                                		  break;
                                		  
        }
        return true;
    }
    
    /*
     * [DEPRECATED] FUNCTION: public void launchForm(View button)
     * 
     * DESCRIPTION: Works at the press of a button, launching the feedback form.
     * 		
     * 		INPUTS: View button
     * 		OUTPUTS: (none)
     * 
     * 		NEW VERSION: protected void launchForm()
     * 
     * 		TODO: Remove function in future version of the application.
     * 								
     */
    public void launchForm(View button) {

        Intent launchFormActivity = new Intent(this, FormActivity.class); 
        startActivity(launchFormActivity);

    }
    
    /*
     * FUNCTION: protected void launchNewTransaction()
     * 
     * DESCRIPTION: Launches the new transaction activity screen.
     * 
     * 		INPUTS: (none)
     * 		OUTPUTS: (none)
     * 
     * 
     */
    protected void launchNewTransaction() {
    	
    	Intent launchNewTransactionActivity = new Intent(this, NewTransactionActivity.class);
    	startActivity(launchNewTransactionActivity);
    	
    }
    
    /*
     * FUNCTION: protected void launchStatistics()
     * 
     * DESCRIPTION: Launches the statistics activity screen.
     * 
     * 		INPUTS: (none)
     * 		OUTPUTS: (none)
     * 
     * 
     */    
    protected void launchAccountManager() {
    	
    	Intent launchAccountActivity = new Intent(this, AccountActivity.class);
    	startActivity(launchAccountActivity);
    	
    }
    
    /*
     * FUNCTION: protected void launchNewTransaction()
     * 
     * DESCRIPTION: Launches the new transaction activity screen.
     * 
     * 		INPUTS: (none)
     * 		OUTPUTS: (none)
     * 
     * 
     */
    protected void launchExport() {
    	
    	Intent launchExportActivity = new Intent(this, ExportActivity.class);
    	startActivity(launchExportActivity);
    	
    }
    
    /*
     * FUNCTION: protected void launchForm()
     * 
     * DESCRIPTION: Launches the feedback form activity screen.
     * 
     * 		INPUTS: (none)
     * 		OUTPUTS: (none)
     * 
     * 
     */
    protected void launchForm() {
    	
    	Intent launchFormActivity = new Intent(this, FormActivity.class);
    	startActivity(launchFormActivity);
    	
    }
    
    /*
     * FUNCTION: protected void launchSettings()
     * 
     * DESCRIPTION: Launches the settings activity screen.
     * 
     * 		INPUTS: (none)
     * 		OUTPUTS: (none)
     * 
     * 
     */
    protected void launchSettings() {
    	
    	Intent launchSettings = new Intent(this, SettingsActivity.class);
    	startActivity(launchSettings);
    	
    }
    

}