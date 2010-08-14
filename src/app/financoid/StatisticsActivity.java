package app.financoid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class StatisticsActivity extends Activity {
	
	private IChart[] mCharts = new IChart[] {new BudgetPieChart()};
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
        setContentView(R.layout.statistics_overview);
        
        Intent intent = null;
        
		intent = mCharts[0].execute(this);
        startActivity(intent);
        /*Intent intent = null;
        intent = new Intent(this, BudgetPieChart.class);
        
        startActivity(intent);*/
    }
	
}
