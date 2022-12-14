package com.zrapp.warehouse.Fragment.FragOrder;

import static com.zrapp.warehouse.SigninActivity.account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.zrapp.warehouse.Adapter.OrderDetailsAdapter;
import com.zrapp.warehouse.ChangePassActivity;
import com.zrapp.warehouse.DAO.OrderDao;
import com.zrapp.warehouse.DAO.OrderDetailsDao;
import com.zrapp.warehouse.DAO.StaffDAO;
import com.zrapp.warehouse.R;
import com.zrapp.warehouse.RequestActivity;
import com.zrapp.warehouse.SigninActivity;
import com.zrapp.warehouse.databinding.ActivityOrderDetailsBinding;
import com.zrapp.warehouse.model.OrderDetails;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class OrderDetailsActivity extends AppCompatActivity {
    ActivityOrderDetailsBinding binding;
    OrderDetailsDao detailsDao;
    StaffDAO staffDAO;
    OrderDao orderDao;
    ArrayList<OrderDetails> list;
    OrderDetailsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);

        Bundle bundle = getIntent().getExtras();
        String idOrder = bundle.getString("idOrder");
        String idStaff = bundle.getString("idStaff");

        detailsDao = new OrderDetailsDao();
        staffDAO = new StaffDAO();
        orderDao = new OrderDao();

        binding.idOrder.setText(idOrder);
        binding.idStaff.setText(idStaff + " - " + staffDAO.getStaffByID(idStaff).getName());
        list = (ArrayList<OrderDetails>) detailsDao.getAll(idOrder);
        adapter = new OrderDetailsAdapter(OrderDetailsActivity.this, list, R.layout.item_order_details);
        binding.lvOrderD.setAdapter(adapter);

        //T??nh t???ng ti???n ho?? ????n
        int s = 0;
        for (OrderDetails orderDetails : list) {
            if (orderDetails.getOrder().getKindOfOrder().equals("Nh???p"))
                s += orderDetails.getQty() * orderDetails.getProd().getCost_price();
            else s += orderDetails.getQty() * orderDetails.getProd().getPrice();
        }
        Locale locale = new Locale("vi", "VN");
        NumberFormat nf = NumberFormat.getInstance(locale);
        binding.tvTotal.setText(nf.format(s) + "");

        if (account.getPost().equals("Nh??n vi??n")) binding.btnDelete.setVisibility(View.GONE);
        binding.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetailsActivity.this);
                builder.setTitle("X??a ????n h??ng?");
                builder.setMessage("B???n ch???c ch???n mu???n xo?? ????n h??ng?");
                builder.setNegativeButton("X??a", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        detailsDao.deleteOrderDetail(idOrder);
                        Toast.makeText(OrderDetailsActivity.this, "X??a ????n h??ng th??nh c??ng!!!", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                });
                builder.setPositiveButton("H???y X??a", null);
                builder.show();
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        if (account.getPost().equals("Nh??n vi??n")) {
            getMenuInflater().inflate(R.menu.menu_option_staff, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_option_manager, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            case R.id.confirmRequest:
                Intent intentR = new Intent(getApplicationContext(), RequestActivity.class);
                startActivity(intentR);
                break;

            case R.id.changePass:
                Intent intentC = new Intent(getApplicationContext(), ChangePassActivity.class);
                startActivity(intentC);
                break;

            case R.id.logOut:
                logOut();
                break;

            case R.id.exit:
                exit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void logOut() {
        AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetailsActivity.this);
        builder.setTitle("????ng xu???t");
        builder.setMessage("B???n c?? ch???c ch???n mu???n ????ng xu???t?");
        builder.setNegativeButton("C??", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getApplicationContext(), SigninActivity.class);
                startActivity(intent);
            }
        });
        builder.setPositiveButton("Kh??ng", null);
        builder.show();
    }

    public void exit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetailsActivity.this);
        builder.setTitle("Tho??t");
        builder.setMessage("B???n c?? ch???c ch???n mu???n tho??t ch????ng tr??nh?");
        builder.setNegativeButton("C??", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);
            }
        });
        builder.setPositiveButton("Kh??ng", null);
        builder.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}