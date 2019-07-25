package hamdan.juniordesign.quebo.pdf;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import hamdan.juniordesign.quebo.R;

public class PdfAdapter extends ArrayAdapter<File> {

    public ArrayList<File> all_pdf;
    private Context context;
    private ViewHolder viewHolder;

    PdfAdapter(Context ctx, ArrayList<File> allPdf) {
        super(ctx, R.layout.adapter_pdf, allPdf);
        context = ctx;
        all_pdf = allPdf;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        if (all_pdf.size() > 0) {
            return all_pdf.size();
        } else {
            return 1;
        }
    }

    @NonNull
    @Override
    public View getView(final int position, View view, @NonNull ViewGroup parent) {

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.adapter_pdf, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.pdfName = view.findViewById(R.id.pdfName);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.pdfName.setText(all_pdf.get(position).getName());
        return view;

    }

    public class ViewHolder {
        TextView pdfName;
    }
}
