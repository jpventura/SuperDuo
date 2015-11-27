package com.jpventura.footballscores.widget;

import android.graphics.drawable.PictureDrawable;

import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.SimpleResource;
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder;
import com.caverock.androidsvg.SVG;

public class SVGTranscoder implements ResourceTranscoder<SVG, PictureDrawable> {
    @Override
    public Resource<PictureDrawable> transcode(Resource<SVG> resource) {
        return new SimpleResource<>(new PictureDrawable(resource.get().renderToPicture()));
    }

    @Override
    public String getId() {
        return SVGTranscoder.class.getCanonicalName();
    }
}
