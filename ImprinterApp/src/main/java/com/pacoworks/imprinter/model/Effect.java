
package com.pacoworks.imprinter.model;

/**
 * Created by Paco on 25/01/2015. See LICENSE.md
 */
public class Effect extends Card {
    public String name;

    public String description;

    public Effect() {
    }

    @Override
    public String alternativeUnsanitizedFileName() {
        return name;
    }
}
