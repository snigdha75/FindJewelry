package com.example.findjewelry.activity.admin;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.findjewelry.R;
import com.example.findjewelry.adapters.OrderItemsAdapter;
import com.example.findjewelry.models.Order;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminOrderDetailsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView user, address, payment, status;
    private Button acceptOrder;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_order_details);

        user = findViewById(R.id.user);
        address = findViewById(R.id.address);
        payment = findViewById(R.id.payment);
        status = findViewById(R.id.status);
        recyclerView = findViewById(R.id.orderItems);
        acceptOrder = findViewById(R.id.acceptOrder);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        firestore = FirebaseFirestore.getInstance();

        Order order = (Order) getIntent().getSerializableExtra("order");

        if (order != null) {
            user.setText("Name: " + order.getName() + "\nPhone: " + order.getPhone());
            address.setText("Address: " + order.getAddress() + ", " + order.getCity());
            payment.setText("Payment: " + order.getPaymentMethod() + "\nNotes: " + order.getNotes());
            status.setText("Status: " + order.getStatus());

            recyclerView.setAdapter(new OrderItemsAdapter(this, order.getItems()));

            if (!order.getStatus().equals("Pending")) {
                acceptOrder.setEnabled(false);
                acceptOrder.setText("Order Accepted");
            }

            ImageView back = findViewById(R.id.back);
            back.setOnClickListener(v -> {
                finish();
            });

            acceptOrder.setOnClickListener(v -> {
                firestore.collection("orders")
                        .document(order.getUserId())
                        .collection("userOrders")
                        .document(order.getOrderId())
                        .update("status", "Accepted")
                        .addOnSuccessListener(unused -> {
                            Toast.makeText(this, "Order Accepted", Toast.LENGTH_SHORT).show();
                            status.setText("Status: Accepted");
                            acceptOrder.setEnabled(false);
                            acceptOrder.setText("Order Accepted");
                            startActivity(new Intent(this, AdminOrderActivity.class));
                            finish();
                        })
                        .addOnFailureListener(e ->
                                Toast.makeText(this, "Failed to update status", Toast.LENGTH_SHORT).show()
                        );
            });
        }
    }
}
