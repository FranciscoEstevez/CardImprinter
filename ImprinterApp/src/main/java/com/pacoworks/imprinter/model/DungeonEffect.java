package com.pacoworks.imprinter.model;

/**
 * Created by Paco on 25/01/2015.
 * See LICENSE.md
 */
public class DungeonEffect extends Card {
    public String name;

    public String description;

    @Override
    public String alternativeUnsanitizedFileName() {
        return name;
    }
}