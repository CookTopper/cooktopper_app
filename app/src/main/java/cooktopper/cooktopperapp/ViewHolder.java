package cooktopper.cooktopperapp;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public class ViewHolder extends RecyclerView.ViewHolder {

    public View view;

    public ViewHolder(View viewToBeSet){
        super(viewToBeSet);
        view = viewToBeSet;
    }
}