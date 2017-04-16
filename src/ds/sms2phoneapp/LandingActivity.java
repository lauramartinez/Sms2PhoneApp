/**
 * @author Laura Martinez (lauracam@andrew.cmu.edu)
 * Last Modified: March 29, 2013
 * 
 * This Activity acts as the controller of the main screen in the app
 */

package ds.sms2phoneapp;

import ds.sms2phoneapp.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class LandingActivity extends Activity {
	
	/**
     * This method executes as soon as the app is launched
     * @param savedInstanceState
     */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Get the main view
		setContentView(R.layout.landing);
		
		//Create an instance of itself
		final LandingActivity la = this;
		
		//Get the button from the layout
		Button submitButton = (Button)findViewById(R.id.submit);
		
		//Set a listener to the submit button
		submitButton.setOnClickListener(new OnClickListener(){
			public void onClick(View viewParam) {
				
				//Get the values the user entered in the textboxes for both the phone number and the message
				String phoneNumber = ((EditText)findViewById(R.id.phoneNumberTxt)).getText().toString();
				String message = ((EditText)findViewById(R.id.messageTxt)).getText().toString();
				
				//Create an instance of the class that will communicate with the GAE app
				sms2PhoneApp sp = new sms2PhoneApp();
				
				//Request the call method
        		sp.call(phoneNumber, message, la);
			}
		});
	}
	
	/**
     * The method that is called when the GAE app has responded
     * @param result string with the result of the httprequest
     */
	
	public void callReady(String result) {

		//Create an alert and set its title
		AlertDialog.Builder builder = new AlertDialog.Builder(LandingActivity .this);
		builder.setTitle("Sms2Phone Call");
		
		//Depending on the result, show either a success or fail message in the Alert
		if (result.equals("success")){
			builder.setMessage("Your message has been successfully sent. The phone is ringing!");
		}
		else
		{
			builder.setMessage("Uh oh, something went wrong. Please try again!");
		}
		// Set the listener for the OK button in the Alert
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				//close the alert
				dialog.dismiss();
			}
		});
		
		//Show the Alert to the user
		AlertDialog dialog = builder.create();
		dialog.show();
	}
}
