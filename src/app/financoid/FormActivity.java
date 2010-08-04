package app.financoid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


public class FormActivity extends Activity {
	
	private static final String LOG_TAG = "EmailLauncherActivity";
	static final int FEEDBACK_REQUEST = 0;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.form);
	}
	
	
	public void sendFeedback(View button) {

		
		final EditText nameField = (EditText) findViewById(R.id.EditTextName);
		String name = nameField.getText().toString();
			
		final EditText emailField = (EditText) findViewById(R.id.EditTextEmail);
		String email = emailField.getText().toString();
		
		final EditText feedbackField = (EditText) findViewById(R.id.EditTextFeedbackBody);
		String feedback = feedbackField.getText().toString();
		
		final Spinner feedbackSpinner = (Spinner) findViewById(R.id.SpinnerFeedbackType);
		String feedbackType = feedbackSpinner.getSelectedItem().toString();
		
		final CheckBox responseCheckbox = (CheckBox) findViewById(R.id.CheckBoxResponse);
		boolean bRequiresResponse = responseCheckbox.isChecked();

		// Take the fields and format the message contents
		String subject = formatFeedbackSubject(feedbackType);

		String message = formatFeedbackMessage(feedbackType, name,
			 email, feedback, bRequiresResponse);
		
		// Create the message
		sendFeedbackMessage(subject, message);
	}

	public void sendPlainTextEmail(String subject, String message) {
	    try {
	        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
	
	        String aEmailList[] = { "livathinos.spyros@gmail.com" };
	        //String aEmailCCList[] = { getResources().getString(R.string.email_address_cc) };
	        //String aEmailBCCList[] = { getResources().getString(R.string.email_address_bcc) };
	
	        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, aEmailList);
	        //emailIntent.putExtra(android.content.Intent.EXTRA_CC, aEmailCCList);
	        //emailIntent.putExtra(android.content.Intent.EXTRA_BCC, aEmailBCCList);
	        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
	
	        emailIntent.setType("plain/text");
	        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);
	
	        startActivity(emailIntent);
	        
	    } catch (Exception e) {
	        
	    	Log.e(LOG_TAG, "sendPlainTextEmail() failed to start activity.", e);
	        Toast.makeText(this, "No handler", Toast.LENGTH_LONG).show();
	    
	    }
	}

	
	protected String formatFeedbackSubject(String feedbackType) {
		
		String strFeedbackSubjectFormat = getResources().getString(
				R.string.feedbackmessagesubject_format);

		String strFeedbackSubject = String.format(strFeedbackSubjectFormat, feedbackType);
		
		return strFeedbackSubject;

	}
	
	protected String formatFeedbackMessage(String feedbackType, String name,
			String email, String feedback, boolean bRequiresResponse) {
		
		String strFeedbackFormatMsg = getResources().getString(
				R.string.feedbackmessagebody_format);

		String strRequiresResponse = getResponseString(bRequiresResponse);

		String strFeedbackMsg = String.format(strFeedbackFormatMsg,
				feedbackType, feedback, name, email, strRequiresResponse);
		
		return strFeedbackMsg;

	}
	

	protected String getResponseString(boolean bRequiresResponse)
	{
		if(bRequiresResponse==true)
		{
			return getResources().getString(R.string.feedbackmessagebody_responseyes);
		} else {
			return getResources().getString(R.string.feedbackmessagebody_responseno);
		}
			
	}

	public void sendFeedbackMessage(String subject, String message) {

		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

		String aEmailList[] = { "livathinos.spyros@gmail.com" };
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, aEmailList);

		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);

		emailIntent.setType("plain/text");
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);

		startActivityForResult(emailIntent, FEEDBACK_REQUEST);
		
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if(requestCode == FEEDBACK_REQUEST) {
			
			if(resultCode == RESULT_OK) {
				launchMainScreen();
			} else {
				launchMainScreen();
				Toast.makeText(this, "Your feedback has been sent. Thank you!", Toast.LENGTH_SHORT).show();
			}
			
		}
		
	}
	
	/*	
	 *	FUNCTION: public void launchMainScreen()
	 *
	 *  DESCRIPTION: Gets the user back to the main menu.
	 * 
	 * 		INPUTS: (none)
	 * 		OUTPUTS: (none)
	 * 
	 */
	
	public void launchMainScreen() {
		
		Intent launchFinancoidActivity = new Intent(this, Financoid.class);
		startActivity(launchFinancoidActivity);
	
	}


}
