package com.dXs.permission.helper;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.dXs.lib.permission.IPermission;
import com.dXs.lib.permission.IPermissionCallback;
import com.dXs.lib.permission.PermissionImplementation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private IPermission iPermission = PermissionImplementation.getInstance();
    private View coordinatorLayoutView;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
        coordinatorLayoutView = findViewById(R.id.coordinatorLayout);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Snackbar.make(view, "A simple snackbar :)", Snackbar.LENGTH_INDEFINITE).show();
            }
        });
    }

    public void reqPermission(final View view){
        String permission = Manifest.permission.ACCESS_FINE_LOCATION;
        iPermission.requestPermission(MainActivity.this, permission, 1, new IPermissionCallback() {
            @Override
            public void permissionGranted(int requestCode) {
                Snackbar.make(view, "Location Permission Granted", Snackbar.LENGTH_INDEFINITE).show();
            }

            @Override
            public void permissionDenied(int requestCode, boolean willShowCheckBoxNextTime) {
                if (willShowCheckBoxNextTime) {
                    Snackbar.make(view, "You need to enable location permission for this thing to work.", Snackbar.LENGTH_INDEFINITE).setAction("Open Settings", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            iPermission.openPermissionSettings(MainActivity.this);
                        }
                    }).show();
                } else {
                    Snackbar.make(view, "Location Permission Denied", Snackbar.LENGTH_INDEFINITE).show();

                }
            }
        });
    }

    public void reqPermissions(final View view){
        String[] permissions = new String[]{Manifest.permission.READ_SMS, Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO };
        iPermission.requestPermission(MainActivity.this, permissions, 2, new IPermissionCallback() {
            @Override
            public void permissionGranted(int requestCode) {
                Snackbar.make(view, "Permissions Granted", Snackbar.LENGTH_INDEFINITE).show();
            }

            @Override
            public void permissionDenied(int requestCode, boolean willShowCheckBoxNextTime) {
                if (willShowCheckBoxNextTime) {
                    Snackbar.make(view, "You need to enable these permissions for this thing to work.", Snackbar.LENGTH_INDEFINITE).setAction("Open Settings", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            iPermission.openPermissionSettings(MainActivity.this);
                        }
                    }).show();
                } else {
                    Snackbar.make(view, "Permissions Denied", Snackbar.LENGTH_INDEFINITE).show();
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        List<String> permissions;
        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_all:
                permissions = iPermission.getAllPermissionsList();
                break;
            case R.id.action_granted_list:
                permissions = iPermission.getGrantedPermissionList(getApplicationContext());
                break;
            case R.id.action_settings:
                iPermission.openPermissionSettings(MainActivity.this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
        if(permissions != null) {
            String message = "";
            // if there is no permission granted
            if (permissions.size() == 0) {
                message = "No permissions granted";
            } else {
                for (String permission : permissions) {
                    message = message + permission + System.getProperty("line.separator");
                }
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Permissions: ");
            builder.setMessage(message);
            builder.create().show();
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        iPermission.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


}

