
package com.pacoworks.imprinter.model;

import android.text.TextUtils;

/**
 * Created by Paco on 25/01/2015. See LICENSE.md
 */
abstract class Card {
    protected String filename;

    public String getFilename() {
        if (TextUtils.isEmpty(filename) || "null".equals(filename)) {
            return alternativeUnsanitizedFileName().toLowerCase().replaceAll(" ", "_")
                    .replaceAll("[^\\w\\s]", "");
        }
        return filename;
    }

    protected abstract String alternativeUnsanitizedFileName();
}
