/**
 * @author Laura Martinez (lauracam@andrew.cmu.edu)
 * Last Modified: March 29, 2013
 * 
 * This class acts as the model of the app that will make the HTTPRequest to the app in GAE
 */

package ds.sms2phoneapp;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import android.os.AsyncTask;

public class sms2PhoneApp {
	
	//Member variable of the LandingActivity
	LandingActivity la = null;
	
	/**
     * This is called by the Activity to make an Asynchronous post
     * @param savedInstanceState
     */
	
	public void call(String phoneNum, String messageTxt, LandingActivity la) {
		//assigns the Activity instance and executes the post given the parameters entered in the view.
		this.la = la;
		new AsyncAppEnginePost().execute(phoneNum, messageTxt);
	}
		
	private class AsyncAppEnginePost extends AsyncTask<String, Void, String> {
			 
		protected String doInBackground(String... data) {
			//Get the data that was sent: Phone and message and pass it to the method that will make the HTTPRequest
			return postCall(data);
		}
		
		protected void onPostExecute(String result) {
			//Once the HTTPRequest has been completed, get back to the Activity with the result
        	la.callReady(result);
        }
			
		private String postCall(String... data){
			
			//Another way to make the HTTPRequest Just in case
			
			/*HttpClient httpclient = new DefaultHttpClient();
	        HttpResponse response;
	        String responseString = "";
	        
	        try {
	            response = httpclient.execute(new HttpGet("http://sms2phoneds.appspot.com/sms2phonemodel?msg=" + URLEncoder.encode(data[1], "UTF-8") + "&phone=" + data[0]));
	            StatusLine statusLine = response.getStatusLine();
	            
	            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
	                ByteArrayOutputStream out = new ByteArrayOutputStream();
	                response.getEntity().writeTo(out);
	                out.close();
	                responseString = out.toString();
	            } else{
	                response.getEntity().getContent().close();
	                throw new IOException(statusLine.getReasonPhrase());
	            }
	            
	        } catch (ClientProtocolException e) {
	            //TODO Handle problems..
	        } catch (IOException e) {
	            //TODO Handle problems..
	        }*/
	        
			//Temp vars that will save the result from the post
	        Document doc = null;
	        String result = "";
	        
	        //Make the request and get an XML
			try {
				doc = getRemoteXML("http://sms2phoneds.appspot.com/sms2phoneServlet?msg=" + URLEncoder.encode(data[1], "UTF-8") + "&phone=" + data[0]);
				
				//Parse the XML
				Node n = doc.getFirstChild();
				result = n.getTextContent();
				
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
	        
			//Return the result
	        return result;
		}
		
		//Method that gets the XML from the request
		private Document getRemoteXML(String url) {
			try {
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				InputSource is = new InputSource(url);
				return db.parse(is);
			} catch (Exception e) {
				System.out.print(e);
				return null;
			}
		}
	}
}

