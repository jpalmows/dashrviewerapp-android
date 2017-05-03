package dashr.app.com.dashrviewapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.bluetooth.le.BluetoothLeScanner;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements BluetoothAdapter.LeScanCallback {
    private LinearLayout laneLayout, lane1Layout, lane2Layout, lane3Layout;
    private EditText lane1idTextField, lane2idTextField, lane3idTextField;
    private LinearLayout.LayoutParams parameter;
    private TableRow row1, row2, row3, row4;
    private TextView time1Lane1, time2Lane1, time1Lane2, time2Lane2, time1Lane3, time2Lane3, lane1drillText, lane2drillText, lane3drillText;
    Button advertiseButton;
    ImageButton menuButton;
    private BluetoothAdapter btAdapter;
    private BluetoothLeAdvertiser bleAdvertiser;
    private AdvertiseSettings.Builder settingsBuilder;
    private AdvertiseData.Builder dataBuilder;
    private BluetoothLeAdvertiser advertiser;
    private AdvertiseSettings settings;
    private AdvertiseData data;
    private AdvertiseCallback advertisingCallback;
    private BluetoothAdapter mBluetoothAdapter2;
    Handler handler = new Handler();
    Handler handler2 = new Handler();
    Handler handler3 = new Handler();
    Handler handler4 = new Handler();
    Handler handler5 = new Handler();

    private float currentlaneLayoutWeightSum, newlaneLayoutWeightSum;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private int laneNumber = 0;
    private ListView mDrawerList;
    private String[] mPlanetTitles;


    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();


    private BluetoothAdapter mBluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/digital-7.ttf");
        Typeface custom_font2 = Typeface.createFromAsset(getAssets(), "fonts/SFCollegiate.ttf");


        // handler5.postDelayed(check, 0);

        row1 = (TableRow) findViewById(R.id.row1);
        row2 = (TableRow) findViewById(R.id.row2);
        row3 = (TableRow) findViewById(R.id.row3);
        row4 = (TableRow) findViewById(R.id.row4);

        time1Lane1 = (TextView) findViewById(R.id.time1lane1);
        time1Lane1.setTypeface(custom_font);
        // time1Lane1.setTextSize((float) timeView1.getWidth());


        time2Lane1 = (TextView) findViewById(R.id.time2lane1);
        time2Lane1.setTypeface(custom_font);

        time1Lane2 = (TextView) findViewById(R.id.time1lane2);
        time1Lane2.setTypeface(custom_font);

        time2Lane2 = (TextView) findViewById(R.id.time2lane2);
        time2Lane2.setTypeface(custom_font);

        time1Lane3 = (TextView) findViewById(R.id.time1lane3);
        time1Lane3.setTypeface(custom_font);

        time2Lane3 = (TextView) findViewById(R.id.time2lane3);
        time2Lane3.setTypeface(custom_font);

        lane1drillText = (TextView) findViewById(R.id.lane1drillText);
        lane1drillText.setTypeface(custom_font2);

        lane2drillText = (TextView) findViewById(R.id.lane2drillText);
        lane2drillText.setTypeface(custom_font2);

        lane3drillText = (TextView) findViewById(R.id.lane3drillText);
        lane3drillText.setTypeface(custom_font2);

        lane1idTextField = (EditText) findViewById(R.id.lane1idTextField);
        lane2idTextField = (EditText) findViewById(R.id.lane2idTextField);
        lane3idTextField = (EditText) findViewById(R.id.lane3idTextField);


        lane1idTextField.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(3)});
        lane2idTextField.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(3)});
        lane3idTextField.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(3)});


        BluetoothManager manager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        mBluetoothAdapter = manager.getAdapter();

        advertiser = mBluetoothAdapter.getBluetoothLeAdvertiser();

        BluetoothManager manager2 = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        mBluetoothAdapter2 = manager2.getAdapter();
        mBluetoothAdapter2.startLeScan(this);


        mPlanetTitles = getResources().getStringArray(R.array.planets_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);


        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mPlanetTitles));
        // Set the list's click listener

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // drawerListItem = (TextView) findViewbyId(R.id.drawer)

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);


        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {


            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    /* Called whenever we call invalidateOptionsMenu() */
//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        // If the nav drawer is open, hide action items related to the content view
//       // boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
//      //  menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
//        return super.onPrepareOptionsMenu(menu);
//    }


    String hex = "";
    char[] arr;

    private String texttoShow;
    private String time1View1Hex, time2View1Hex, currentDrillHex;
    private String deviceName;
    private Long timeDelay = 0L;

    @Override
    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
        long SystemTime = SystemClock.uptimeMillis();

        hex = bytesToHex(scanRecord);
        System.out.println(hex);

        arr = hex.toCharArray();

        String deviceidHex = hex.substring(14, 20);
        String deviceidASCII = hexToASCII(deviceidHex);
        System.out.println("lane 1 text field: " + lane1idTextField.getText().toString());
        System.out.println("device ascii: "+deviceidASCII);

        if (lane1idTextField.getText().toString().equals(deviceidASCII) || lane2idTextField.getText().toString().equals(deviceidASCII) || lane3idTextField.getText().toString().equals(deviceidASCII) ) {

            timeDelay = SystemClock.uptimeMillis();
            currentDrillHex = hex.substring(20, 22);
            time1View1Hex = hex.substring(24, 28);
            time2View1Hex = hex.substring(28, 32);
            deviceName = device.getName();

            if (lane1idTextField.getText().toString().equals(deviceidASCII)) {
                laneNumber = 1;
            }

            else if (lane2idTextField.getText().toString().equals(deviceidASCII)) {
                laneNumber = 2;
            }
            else {
                laneNumber = 3;
            }

            handler4.postDelayed(displayData, 0);


        }

    }


    @Override
    protected void onPause() {
        super.onPause();

        mBluetoothAdapter2.stopLeScan(this);


    }

    protected void onResume() {
        super.onResume();


        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            //Bluetooth is disabled
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBtIntent);
            finish();
            return;
        }

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "No LE Support.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        mBluetoothAdapter2.startLeScan(this);

        setSizes();
        changeWeightSums(1, 0);

    }


    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static int hex2decimal(String s) {
        String digits = "0123456789ABCDEF";
        s = s.toUpperCase();
        int val = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            int d = digits.indexOf(c);
            val = 16 * val + d;
        }
        return val;
    }

    private static String asciiToHex(String asciiValue) {
        char[] chars = asciiValue.toCharArray();
        StringBuffer hex = new StringBuffer();
        for (int i = 0; i < chars.length; i++) {
            hex.append(Integer.toHexString((int) chars[i]));
        }
        return hex.toString();
    }

    private static String hexToASCII(String hexValue) {
        StringBuilder output = new StringBuilder("");
        for (int i = 0; i < hexValue.length(); i += 2) {
            String str = hexValue.substring(i, i + 2);
            output.append((char) Integer.parseInt(str, 16));
        }
        return output.toString();
    }

    public Runnable displayData = new Runnable() {
        //
        public void run() {

            int currentDrillDec = hex2decimal(currentDrillHex);
            System.out.println("drill int: " +currentDrillDec);
            int time1View1Dec = hex2decimal(time1View1Hex);
            int time2View1Dec = hex2decimal(time2View1Hex);

            float time1View1Float = (float) time1View1Dec / 100;
            float time2View1Float = (float) time2View1Dec / 100;


            if (laneNumber == 1) {
                time1Lane1.setText(String.format("%.2f", time1View1Float));
                time2Lane1.setText(String.format("%.2f", time2View1Float));

                switch (currentDrillDec) {

                    case 0:
                        lane1drillText.setText("Dash");

                        break;

                    case 1:
                        lane1drillText.setText("Pro-Agility");

                        break;

                    case 2:
                        lane1drillText.setText("Dash w/ Split");

                        break;

                    case 3:
                        lane1drillText.setText("Flying 40");


                        break;

                    case 4:
                        lane1drillText.setText("Lap");


                        break;
                    default:
                        ; //do nothing
                        ;

                }
            }
            else if (laneNumber == 2) {
                time1Lane2.setText(String.format("%.2f", time1View1Float));
                time2Lane2.setText(String.format("%.2f", time2View1Float));

                switch (currentDrillDec) {

                    case 0:
                        lane2drillText.setText("Dash");

                        break;

                    case 1:
                        lane2drillText.setText("Pro-Agility");

                        break;

                    case 2:
                        lane2drillText.setText("Dash w/ Split");

                        break;

                    case 3:
                        lane2drillText.setText("Flying 40");


                        break;

                    case 4:
                        lane2drillText.setText("Lap");


                        break;
                    default:
                        ; //do nothing
                        ;

                }
            }

             else {
                time1Lane3.setText(String.format("%.2f", time1View1Float));
                time2Lane3.setText(String.format("%.2f", time2View1Float));

                switch (currentDrillDec) {

                    case 0:
                        lane3drillText.setText("Dash");

                        break;

                    case 1:
                        lane3drillText.setText("Pro-Agility");

                        break;

                    case 2:
                        lane3drillText.setText("Dash w/ Split");

                        break;

                    case 3:
                        lane3drillText.setText("Flying 40");


                        break;

                    case 4:
                        lane3drillText.setText("Lap");


                        break;
                    default:
                        ; //do nothing
                        ;

                }
            }

            handler4.removeCallbacks(this, 0);


        }
    };

    public Runnable check = new Runnable() {
        //
        public void run() {
            if ((SystemClock.uptimeMillis() - timeDelay) > 1000) {
            }
            handler5.postDelayed(this, 1000);


        }
    };

    private void setSizes() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int dens = dm.densityDpi;
        float wi = (float) width / (float) dens;
        int sizeToScale = Math.round(wi) / 3;
        lane1drillText.setTextSize(sizeToScale * 15);
        lane2drillText.setTextSize(sizeToScale * 15);
        lane3drillText.setTextSize(sizeToScale * 15);
        lane1idTextField.setTextSize(sizeToScale * 15);
        lane2idTextField.setTextSize(sizeToScale * 15);
        lane3idTextField.setTextSize(sizeToScale * 15);
        time1Lane1.setTextSize(sizeToScale * 75);
        time2Lane1.setTextSize(sizeToScale * 75);
        time1Lane2.setTextSize(sizeToScale * 75);
        time2Lane2.setTextSize(sizeToScale * 75);
        time1Lane3.setTextSize(sizeToScale * 75);
        time2Lane3.setTextSize(sizeToScale * 75);

        // parameter =  (LinearLayout.LayoutParams) lane2idTextField.getLayoutParams();
        //  parameter.setMargins(sizeToScale, 0, 0, 0); // left, top, right, bottom


        // lane2idTextField.setLayoutParams(parameter);
        //  lane2idTextField.setLayoutParams(params);
        //  lane3idTextField.setLayoutParams(params);

    }


    private void changeWeightSums(int WeightSum, int OldWeightSum) {

        switch (WeightSum) {
            case 1:



                lane2drillText.setVisibility(View.GONE);
                lane3drillText.setVisibility(View.GONE);

                time1Lane2.setVisibility(View.GONE);
                time2Lane2.setVisibility(View.GONE);

                time1Lane3.setVisibility(View.GONE);
                time2Lane3.setVisibility(View.GONE);

                lane2idTextField.setVisibility(View.GONE);
                lane3idTextField.setVisibility(View.GONE);

                break;

            case 2:

                lane2drillText.setVisibility(View.VISIBLE);
                lane3drillText.setVisibility(View.GONE);

                time1Lane2.setVisibility(View.VISIBLE);
                time2Lane2.setVisibility(View.VISIBLE);

                time1Lane3.setVisibility(View.GONE);
                time2Lane3.setVisibility(View.GONE);

                lane2idTextField.setVisibility(View.VISIBLE);
                lane3idTextField.setVisibility(View.GONE);

                break;

            case 3:


                lane2drillText.setVisibility(View.VISIBLE);
                lane3drillText.setVisibility(View.VISIBLE);

                time1Lane2.setVisibility(View.VISIBLE);
                time2Lane2.setVisibility(View.VISIBLE);

                time1Lane3.setVisibility(View.VISIBLE);
                time2Lane3.setVisibility(View.VISIBLE);

                lane2idTextField.setVisibility(View.VISIBLE);
                lane3idTextField.setVisibility(View.VISIBLE);

                break;

            default: WeightSum = OldWeightSum;
                ;

        }
        row1.setWeightSum(WeightSum);
        row2.setWeightSum(WeightSum);
        row3.setWeightSum(WeightSum);
        row4.setWeightSum(WeightSum);


    }


    public void menuOpen(View view) {

        mDrawerLayout.openDrawer(Gravity.LEFT);
    }


    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {


        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(mDrawerList);
        System.out.println("position: " + position);

        switch (position) {

            case 0:
                currentlaneLayoutWeightSum = row1.getWeightSum();
                newlaneLayoutWeightSum = currentlaneLayoutWeightSum + 1;
                System.out.println("weight sums: " + (int) newlaneLayoutWeightSum + " " + (int) currentlaneLayoutWeightSum);
                changeWeightSums((int) newlaneLayoutWeightSum, (int) currentlaneLayoutWeightSum);

                break;

            case 1:
                currentlaneLayoutWeightSum = row1.getWeightSum();
                newlaneLayoutWeightSum = currentlaneLayoutWeightSum - 1;
                changeWeightSums((int) newlaneLayoutWeightSum, (int) currentlaneLayoutWeightSum);

                break;

            default:
                ;


        }


    }
}



