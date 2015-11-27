package com.jpventura.footballscores.widget;

import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.SimpleResource;
import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;

import java.io.IOException;
import java.io.InputStream;

public class SVGDecoder implements ResourceDecoder<InputStream, SVG> {
    public Resource<SVG> decode(InputStream source, int width, int height) throws IOException {
        try {
            return new SimpleResource<>(SVG.getFromInputStream(source));
        } catch (SVGParseException e) {
            return null;
        }
    }

    @Override
    public String getId() {
        return SVGDecoder.class.getCanonicalName();
    }
}