package aexp.junit;

import android.app.Activity;
import android.os.Bundle;
import android.test.AndroidTestCase;
import android.test.AndroidTestRunner;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.util.Log;
import junit.framework.TestListener;
import junit.framework.Test;
import junit.framework.AssertionFailedError;

public class JUnit extends Activity {
    static final String LOG_TAG = "junit";
    Thread testRunnerThread = null;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button launcherButton = (Button)findViewById( R.id.launch_button );
        launcherButton.setOnClickListener( new View.OnClickListener() {
            public void onClick( View view ) {
                startTest();
            }
        } );
    }

    private synchronized void startTest() 
    {
        if( ( testRunnerThread != null ) &&
            !testRunnerThread.isAlive() )
            testRunnerThread = null;
        if( testRunnerThread == null ) {
            testRunnerThread = new Thread( new TestRunner( this ) );
            testRunnerThread.start();
        } else
            Toast.makeText(
                        this, 
                        "Test is still running", 
                        Toast.LENGTH_SHORT).show();
    }

}

class TestDisplay implements Runnable 
{
        public enum displayEvent{START_TEST,END_TEST,ERROR,FAILURE,}
        displayEvent ev;
        String testName;
        int testCounter;
        int errorCounter;
        int failureCounter;
        TextView statusText;
        TextView testCounterText;
        TextView errorCounterText;
        TextView failureCounterText;

        public TestDisplay( displayEvent ev,
                        String testName, 
                        int testCounter,
                        int errorCounter,
                        int failureCounter,
                        TextView statusText,
                        TextView testCounterText,
                        TextView errorCounterText,
                        TextView failureCounterText ) 
        {
            this.ev = ev;
            this.testName = testName;
            this.testCounter = testCounter;
            this.errorCounter = errorCounter;
            this.failureCounter = failureCounter;
            this.statusText = statusText;
            this.testCounterText = testCounterText;
            this.errorCounterText = errorCounterText;
            this.failureCounterText = failureCounterText;
        }

        public void run() 
        {
            StringBuffer status = new StringBuffer();
            switch( ev ) {
                case START_TEST:
                    status.append( "Starting" );
                    break;

                case END_TEST:
                    status.append( "Ending" );
                    break;

                case ERROR:
                    status.append( "Error: " );
                    break;

                case FAILURE:
                    status.append( "Failure: " );
                    break;
                
            }
            status.append( ": " );
            status.append( testName );
            statusText.setText( new String( status ) );
            testCounterText.setText( "Tests: "+testCounter );
            errorCounterText.setText( "Errors: "+errorCounter );
            failureCounterText.setText( "Failure: "+failureCounter );
        }
}

class TestRunner implements Runnable,TestListener  
{
        static final String LOG_TAG = "TestRunner";
        int testCounter;
        int errorCounter;
        int failureCounter;
        TextView statusText;
        TextView testCounterText;
        TextView errorCounterText;
        TextView failureCounterText;
        Activity parentActivity;

        public TestRunner( Activity parentActivity ) 
        {
            this.parentActivity = parentActivity;
        }

        public void run() 
        {
            testCounter = 0;
            errorCounter = 0;
            failureCounter = 0;
            statusText = (TextView)parentActivity.
                                    findViewById( R.id.status );
            testCounterText = (TextView)parentActivity.
                                    findViewById( R.id.testCounter );
            errorCounterText = (TextView)parentActivity.
                                    findViewById( R.id.errorCounter );
            failureCounterText = (TextView)parentActivity.
                                    findViewById( R.id.failureCounter );
            Log.d( LOG_TAG, "Test started" );
            AndroidTestRunner testRunner = new AndroidTestRunner();
            testRunner.setTest( new ExampleSuite() );
            testRunner.addTestListener( this );
            testRunner.setContext( parentActivity );
            testRunner.runTest();
            Log.d( LOG_TAG, "Test ended" );
        }

// TestListener
        public void addError(Test test, Throwable t) 
        {
            Log.d( LOG_TAG, "addError: "+test.getClass().getName() );
            Log.d( LOG_TAG, t.getMessage(), t );
            ++errorCounter;
            TestDisplay td = new TestDisplay(
                    TestDisplay.displayEvent.ERROR,
                    test.getClass().getName(),
                    testCounter,
                    errorCounter,
                    failureCounter,
                    statusText,
                    testCounterText,
                    errorCounterText,
                    failureCounterText );
            parentActivity.runOnUiThread( td );
        }

        public void addFailure(Test test, AssertionFailedError t) 
        {
            Log.d( LOG_TAG, "addFailure: "+test.getClass().getName() );
            Log.d( LOG_TAG, t.getMessage(), t );
            ++failureCounter;
            TestDisplay td = new TestDisplay(
                    TestDisplay.displayEvent.FAILURE,
                    test.getClass().getName(),
                    testCounter,
                    errorCounter,
                    failureCounter,
                    statusText,
                    testCounterText,
                    errorCounterText,
                    failureCounterText );
            parentActivity.runOnUiThread( td );
        }

        public void endTest(Test test) 
        {
            Log.d( LOG_TAG, "endTest: "+test.getClass().getName() );
            TestDisplay td = new TestDisplay(
                    TestDisplay.displayEvent.END_TEST,
                    test.getClass().getName(),
                    testCounter,
                    errorCounter,
                    failureCounter,
                    statusText,
                    testCounterText,
                    errorCounterText,
                    failureCounterText );
            parentActivity.runOnUiThread( td );
        }

        public void startTest(Test test)
        {
            Log.d( LOG_TAG, "startTest: "+test.getClass().getName() );
            ++testCounter;
            TestDisplay td = new TestDisplay(
                    TestDisplay.displayEvent.START_TEST,
                    test.getClass().getName(),
                    testCounter,
                    errorCounter,
                    failureCounter,
                    statusText,
                    testCounterText,
                    errorCounterText,
                    failureCounterText );
            parentActivity.runOnUiThread( td );
        }
}
