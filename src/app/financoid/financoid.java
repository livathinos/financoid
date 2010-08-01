package app.financoid;

import android.app.*;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class financoid extends Activity {
    /** Called when the activity is first created. */
	//private static final int NOTIF_ID = 1234;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        /*Alert dialogue example*/
        
        /*new AlertDialog.Builder(this)
        .setTitle("Popups rule")
        .setMessage("Close me!")
        .setPositiveButton("OK",null)
        .setNeutralButton("Close",null)
        .setNegativeButton("Not OK",null)
        .setIcon(R.drawable.icon)
        .show();*/
        
        /*Toast popup example*/
        
        /*Toast.makeText(this,"Your download has resumed",Toast.LENGTH_LONG).show();*/
        
        /*Notification message example*/
        
        
        /*
        NotificationManager notifManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification note = new Notification(R.drawable.icon, "New E-mail", System.currentTimeMillis());
        
        PendingIntent intent = PendingIntent.getActivity(this,0,new Intent(this, financoid.class),0);
        
        note.setLatestEventInfo(this, "New E-mail","You have one unread message.", intent);
        
        notifManager.notify(NOTIF_ID, note);*/
        
    }
	
	public void sendSimpleEmail(View button) {
		
		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		emailIntent.setType("plain/text");
		startActivity(emailIntent);
		
	}
	
	public void sendPlainTextEmail(View button) {
		
		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		
		String aEmailList[] = { getResources()
				.getString(R.string.email_address) };
		String aEmailCCList[] = { getResources()
				.getString(R.string.email_address_cc) };
		String aEmailBCCList[] = { getResources()
				.getString(R.string.email_address_bcc) };
		
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, aEmailList);
		emailIntent.putExtra(android.content.Intent.EXTRA_CC, aEmailCCList);
		emailIntent.putExtra(android.content.Intent.EXTRA_BCC, aEmailBCCList);
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
				getResources().getString(R.string.email_subject));

		emailIntent.setType("plain/text");
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, getResources()
				.getString(R.string.email_message));

		startActivity(emailIntent);
		
	}
	
	public void sendFeedback(View button) {

		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		emailIntent.setType("plain/text");
		startActivity(Intent.createChooser(emailIntent, getResources().getString(R.string.chooser)));

	}
	
	
}