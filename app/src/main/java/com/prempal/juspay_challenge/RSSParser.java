package com.prempal.juspay_challenge;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by prempal on 10/3/16.
 */
public class RSSParser {

    private static final String TAG = RSSParser.class.getSimpleName();

    public List parse(InputStream in) throws XmlPullParserException, IOException {
        try {

            boolean startParsing = false;
            String name = null, title = null, link;
            List<Article> articles = new ArrayList<>();

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(in, null);

            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_DOCUMENT) {
                    Log.d(TAG, "parse: Start Document");
                } else if (eventType == XmlPullParser.START_TAG) {
                    Log.d(TAG, "parse: Start tag " + xpp.getName());
                    if (xpp.getName().equals("item")) {
                        startParsing = true;
                    } else if (startParsing && xpp.getName().equals("title")) {
                        name = "title";
                    } else if (startParsing && xpp.getName().equals("link")) {
                        name = "link";
                    }
                } else if (eventType == XmlPullParser.END_TAG) {
                    Log.d(TAG, "parse: End tag " + xpp.getName());
                    name = null;
                } else if (eventType == XmlPullParser.TEXT) {
                    Log.d(TAG, "parse: Text " + xpp.getText());
                    if (name != null) {
                        if (name.equals("title")) {
                            title = xpp.getText();
                        } else if (name.equals("link")) {
                            link = xpp.getText();
                            articles.add(new Article(title, link));
                        }
                    }

                }
                eventType = xpp.next();
            }
            return articles;
        } finally {
            in.close();
        }
    }

}
