
package com.pacoworks.imprinter.model;

/**
 * Created by Paco on 25/01/2015. See LICENSE.md
 */
public class Skill extends Card {
    public int quantity;

    public String name;

    public String description;

    public String user;

    public Skill() {
    }

    @Override
    protected String alternativeUnsanitizedFileName() {
        return name;
    }
}
