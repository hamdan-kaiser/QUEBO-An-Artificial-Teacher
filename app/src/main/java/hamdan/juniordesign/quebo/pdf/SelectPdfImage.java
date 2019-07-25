package hamdan.juniordesign.quebo.pdf;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import hamdan.juniordesign.quebo.R;

public class SelectPdfImage extends AppCompatActivity {

    ImageView largePdfImage;
    RecyclerView recyclerView;
    PdfImageAdapter pdfImageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_pdf_image);

        largePdfImage = findViewById(R.id.largePdfImage);
        recyclerView = findViewById(R.id.recyclerView);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(SelectPdfImage.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        pdfImageAdapter = new PdfImageAdapter(SelectPdfImage.this, PdfActivity.images);
        recyclerView.setAdapter(pdfImageAdapter);

    }
}
