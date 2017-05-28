package agendamentobarbearia.com.br.agendamentobarbearia.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import agendamentobarbearia.com.br.agendamentobarbearia.R;

/**
 * Created by Marcello on 25/05/2017.
 */

public class ViewPagerAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;

    private Integer [] images = {R.drawable.person, R.drawable.person};
    private Bitmap[] imagesBitmap = null;

    public ImageView imageView;

    public ViewPagerAdapter(Context context){
        this.context = context;
    }

    @Override
    public int getCount() {
        if(imagesBitmap == null) {
            return images.length;
        } else {
            return imagesBitmap.length;
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.custom_viewpager, null);
        imageView = (ImageView) view.findViewById(R.id.fotoImageView);

        if(imagesBitmap == null) {
            imageView.setImageResource(images[position]);
        } else {
            imageView.setImageBitmap(imagesBitmap[position]);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        }

        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);
    }

    public void setImage(Bitmap imgInicio, Bitmap imgFim){
        images = null;
        imagesBitmap = new Bitmap[]{imgInicio, imgFim};
    }
}
