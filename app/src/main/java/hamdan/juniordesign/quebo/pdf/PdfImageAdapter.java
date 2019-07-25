package hamdan.juniordesign.quebo.pdf;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import hamdan.juniordesign.quebo.R;

public class PdfImageAdapter extends RecyclerView.Adapter<PdfImageAdapter.ViewHolder> {

    private Context context;
    private ViewHolder viewHolder;
    private List<Bitmap> images = new ArrayList<>();

    PdfImageAdapter(Context ctx, List<Bitmap> list) {
        context = ctx;
        images = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_pdf_image, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.pdfSmallImage.setImageBitmap(images.get(position));
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView pdfSmallImage;

        public ViewHolder(View itemView) {
            super(itemView);
            pdfSmallImage = itemView.findViewById(R.id.pdfSmallImage);
        }
    }
}
